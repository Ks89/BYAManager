/*
Copyright 2011-2015 Stefano Cappa

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package logica;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Observable;

import model.BufferSize;

import org.apache.log4j.Logger;


/**
 * Classe che rappresenta il singolo Processo associato ad un ed un solo Download.
 */
public class Process extends Observable implements Runnable {
	private static final Logger LOGGER = Logger.getLogger(Process.class);
	private static final String PART = ".part";

	public static final int DOWNLOADING = 0;
	public static final int PAUSED = 1;
	public static final int COMPLETE = 2;
	public static final int CANCELLED = 3;
	public static final int ERROR = 4;
	public static final int VALIDATION = 5;
	public static final int MERGING = 6;

	private int parte;
	private URI uri; // download URL
	private Path filePath;
	private long size; // size of download in bytes
	private long downloaded; // number of bytes downloaded
	private long partenza;
	private int status; // current status of download
	private String tempoRimanente;
	private String dataTermine;
	private Path downloadTempPath;
	private String renameIn;

	//usati per la rete
	private InputStream stream;

	private byte[] byteArray;
	private int limiteDownload;
	private int read;
	private FileChannel fileChannel;
	private ByteBuffer bb;
	private FileLock lock;

	/**
	 * Costruttore della classe Processo che inizializza le variabili tramite il firmware associato. 
	 * Inoltre imposta anche il numero della parte del Download, la dimensione del buffer, la dimensione da cui 
	 * iniziare a scaricare il file e quella a cui terminare.
	 * @param percorsoFirmware URI del firmware associato al Processo.
	 * @param parte int che rappresenta la parte del Processo del Download.
	 * @param downloaded long che rappresenta la quantita' di byte da cui inziare a scaricare.
	 * @param size long che indica il punto in cui finire il download.
	 */
	public Process(Path downloadTempPath, String renameIn, URI percorsoFirmware, int parte, long downloaded, long size) {
		this.downloadTempPath = downloadTempPath;
		this.renameIn = renameIn; //nome del file con cui salvare il .part (se null, il nome file concide con quello del metodo getFileName())
		this.uri = percorsoFirmware;
		this.size = size;
		this.downloaded = downloaded;
		this.partenza = downloaded; //questo se in download e' avviato/pausa ecc... 
		//ma il programma non e' mai stato chiuso se fosse stato chiuso

		status = DOWNLOADING;
		this.parte = parte;
	}

	/**
	 * Metodo per ottenere la dimensione da cui iniziare a scaricare il file, 
	 * in base alla parte del Processo e alla dimensione totale del Download.
	 * @return Un long rappresentante la quantita' di 
	 */
	public long getDownloaded() {
		long contentLength = (4* this.size) / this.parte;
		switch(this.parte) {
		case 1:
			return downloaded;
		case 2:
			return downloaded - contentLength / 4;
		case 3:
			return downloaded - contentLength / 4 - contentLength / 4;
		default ://cioe' 4
			return downloaded - contentLength / 4 - contentLength / 4 - contentLength / 4;
		}
	}

	/**
	 * Metodo per mettere in pausa il download, terminare la connessione httpGet e 
	 * rilasciare le risorse.
	 */
	public void pause() {
		status = PAUSED;
		try {
			stream.close();
			this.chiusuraFile();
		} catch (IOException e) {
			e.printStackTrace();
			LOGGER.error("pause() - IOException= " + e);
		}
	}

	/**
	 * Metodo per riprendere il download del processo.
	 */
	public void riprendi() {
		status = DOWNLOADING;
		avviaThreadDownload();
	}

	/**
	 * Metodo per terminare lo scaricamento, terminare il processo, cancellare il .part 
	 * e notificare la cancellazione al Download.
	 */
	public void cancel() {
		status = CANCELLED;
		try {
			stream.close();
			this.chiusuraFile();
		} catch (IOException e) {
			LOGGER.error("cancel() - IOException= " + e);
		}
		boolean cancellazione = false;
		File[] listaFile = downloadTempPath.toFile().listFiles();
		for(File fileCancellare : listaFile) {
			//ora controllo se il nome del file che sto scaricando (contenuto in renameIn) e' uguale al file scansionato nella cartella dal for.
			if(this.renameIn.equals(fileCancellare.getName().replace(PART + this.parte, ""))) {
				cancellazione = fileCancellare.delete();
				LOGGER.debug("Cancellazione: " + cancellazione);
				break;
			}
		}
	}

	/**
	 * Metodo per impostare lo stato di errore, terminare il processo, cancellare il .part 
	 * e notificare l'errore al Download.
	 */
	private void error() {
		status = ERROR;
		try {
			stream.close();
			this.chiusuraFile();
		} catch (IOException e) {
			LOGGER.error("error() - IOException= " + e);
		}
		boolean cancellazione = false;
		File d = downloadTempPath.toFile();
		File[] listaFile = d.listFiles();
		for(File fileCancellare : listaFile) {
			if(this.getURIString().contains(fileCancellare.getName().replace(PART, ""))) {
				cancellazione = fileCancellare.delete();
				break;
			}
		}
		(new Thread(this)).interrupt();
		LOGGER.debug("Cancellazione: " + cancellazione);
	}

	/**
	 * Metodo per avviare il thread.
	 */
	private void avviaThreadDownload() {
		Thread thread = new Thread(this);
		thread.start();
	}

	/**
	 * Metodo per ottenere il nome del file, dato il percorso.
	 * @param uri URI rappresentante il percorso del download.
	 * @return Una String rappresentante il nome del file.
	 */
	public String getFileName(URI uri) {
		Path path = Paths.get(uri);
		return path.getFileName().toString();
	}

	/**
	 * Metodo che fornisce il nome del file .part da scaricare. Nel caso di un Firmware o un jailbreakTool non ci sono problemi,
	 * ma per iTunes e' necessario correggere il nome del file aggiungendo al versione e l'os compatibile (dati ricevuti durante
	 * la creazione del Process dalla classe Download).
	 * @return
	 */
	private String getDownloadFilePartName() {
		if(this.renameIn==null) {
			return downloadTempPath + "/" + getFileName(uri) + PART + parte;
		} else {
			return downloadTempPath + "/" + this.renameIn + PART + parte;
		}
	}

	public void run() {
		LOGGER.debug("inizio = " + this.downloaded + ", fine= " + this.size + ", partenza= " + this.partenza + ", scaricato= " + (this.downloaded - this.partenza));
		try {	 
			HttpURLConnection connection = (HttpURLConnection)uri.toURL().openConnection();
			connection.setRequestProperty("Range","bytes=" + downloaded + "-");

			// Connect to server.
            connection.connect();
			
         // Make sure response code is in the 200 range.
            if (connection.getResponseCode() / 100 != 2) {
                error();
            }
            
			this.filePath = Paths.get(this.getDownloadFilePartName());
			
			fileChannel = FileChannel.open(filePath,StandardOpenOption.WRITE,StandardOpenOption.READ,StandardOpenOption.CREATE);
			if(lock!=null && !lock.isValid())  {
				lock = fileChannel.lock(downloaded-partenza, size, false); //imposto il lock solo sul tratto che sto scaricando
			}

//			httpGet = new HttpGet(uri);
//			httpGet.setHeader("Range","bytes=" + this.downloaded + "-" + this.size);
//			httpGet.setHeader("User-Agent","Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/535.7 (KHTML, like Gecko) Chrome/16.0.912.63 Safari/535.7");

			//timeout durante l'invio di pacchetti
			//			httpGet.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 1000);

			//timeout nell'apertura della connessione
			//			httpGet.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 1000);

//			HttpResponse response = GestoreConnessione.getInstance().getHttpClient().execute(httpGet);
//			if (response.getStatusLine().getStatusCode() / 100 != 2) {
//				error();
//			}

//			entity = response.getEntity();
//			stream = entity.getContent();

			stream = connection.getInputStream();
		
			byteArray = new byte[BufferSize.getBufferSize()];
			bb = ByteBuffer.allocate( BufferSize.getBufferSize() );


			//avvia downloadProcesso
			while (status == DOWNLOADING) {
				if (size - downloaded > BufferSize.getBufferSize()) {
					limiteDownload = BufferSize.getBufferSize();
				} else {
					limiteDownload = (int)(size - downloaded);
					byteArray = new byte[limiteDownload];
					bb = ByteBuffer.allocate(limiteDownload);
				}		

				// Read from server into buffer.
				read = stream.read(byteArray);
				//			byteArray = IOUtils.toByteArray(stream);

				if (read == -1) {
					break;
				}

				bb.clear();

				if(fileChannel.isOpen()) {
					bb.put(byteArray);
					bb.flip();
					fileChannel.write(bb, downloaded-partenza);

					if(lock!=null && lock.isValid()) {
						lock.release();
					}

					downloaded += read;
					lock = fileChannel.lock(downloaded-partenza, size, false);
				}

				if(downloaded == size) {
					// cioe' sono all'ultimo pezzo da scaricare
					status = MERGING;
				}
			}



			if (status == MERGING) {

//				if(!httpGet.isAborted()) {
//					LOGGER.info("Download - Httpget non e' chiuso -> OK");
//					LOGGER.info("Download - Consume in corso...");
//					EntityUtils.consume(this.entity);
//					LOGGER.info("Download - Consume eseguito");
//				} else {
//					httpGet.abort();
//					LOGGER.info("Download - Httpget chiuso -> OK");
//				}

				this.chiusuraFile();

				stateChanged();

				LOGGER.info("Download - File scaricato=" + this.getURIString());
			}
		} catch (IOException e) {
			error();
			e.printStackTrace();
			LOGGER.info("Download - Errore");
		}
	}

	/**
	 * Metodo per chiudere un file, rilasciando il lock, il fileChannel e il file.
	 * @throws IOException
	 */
	private void chiusuraFile() throws IOException {
		if(lock!=null && lock.isValid()) {
			lock.release();
			LOGGER.info("Download - Lock rilasciato");
		}
		if(fileChannel!=null && fileChannel.isOpen()) {
			fileChannel.close();
			LOGGER.info("Download - FileChannel chiuso");
		}
		read = 0;
	}

	/**
	 * Metodo per notificare un cambiamento all'observers.
	 */
	public void stateChanged() {
		setChanged();
		notifyObservers();
	}

	public void setDataTermine(String dataTermine){
		this.dataTermine = dataTermine;
	}
	public String getDataTermine(){
		return dataTermine;
	}
	public void setTempoRimanente(String tempoRimanente){
		this.tempoRimanente = tempoRimanente;
	}
	public String getTempoRimanente(){
		return tempoRimanente;
	}
	public void setPartenza(long partenza) {
		this.partenza = partenza;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getURIString() {
		return uri.toString();
	}
	public long getSize() {
		return size;
	}
	public int getStatus() {
		return status;
	}
	public int getParte() {
		return parte;
	}
}