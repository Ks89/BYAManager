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

package it.stefanocappa.logic;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import it.stefanocappa.model.BufferSize;
import it.stefanocappa.model.DownloadCompatibilityChecker;
import it.stefanocappa.model.FileWeb;
import it.stefanocappa.model.Firmware;
import it.stefanocappa.model.ItunesVersion;
import it.stefanocappa.notification.Notification;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import it.stefanocappa.connection.ConnectionManager;
import it.stefanocappa.preferences.Settings;


import it.stefanocappa.model.User;
import it.stefanocappa.fileutility.CheckSha1;
import it.stefanocappa.fileutility.FilesMerger;
import it.stefanocappa.gui.state.StateActionTableButton;
import it.stefanocappa.gui.state.StateButton;

/**
 *	Classe che rappresenta il download, costituito da Processi.
 */
public abstract class Download extends Observable implements Runnable,Observer {
	private static final Logger LOGGER = LogManager.getLogger(Process.class);

	public static final int DOWNLOADING = 0;
	public static final int PAUSED = 1;
	public static final int COMPLETE = 2;
	public static final int CANCELLED = 3;
	public static final int ERROR = 4;
	public static final int VALIDATION = 5;
	public static final int MERGING = 6;

	private boolean selectedDownload;
	private int status;
	private List<Process> process;
	private long totalSize;
	private long downloadedBytes;
	private long speed;
	private String remainingTime;
	private boolean needToValidate = true; //IMPORTANTE: se true validation e' attiva, se no non avviene nessuna fase di verifica sha1
	private FileWeb fileWeb;
	private FilesMerger filesMerger;
	private URI uri;
	private Path downloadPath;
	private Path shaFiledownloadPath;
	private Path renamedFilePathWithoutParts;
	private Path downloadTempPath, downloadTempHttpsPath;
	private int terminatedCount = 0;
	private boolean newDownload = true ; //indica se il download e' nuovo o era gia' stato avviato e ora deve essere ripristinato
	private long inizio1;
	private long fine1;
	private long inizio2;
	private long fine2;
	private long inizio3;
	private long fine3;
	private long inizio4;
	private long fine4;

	private HttpGet httpGet;
	private HttpEntity entity;
	private CloseableHttpResponse response;

	/**
	 * Costruttore che inizializza tutte le variabili globali, dato i firmware associato al download.
	 * @param downloadPath
	 * @param fileWeb
	 */
	public Download(Path downloadPath, FileWeb fileWeb) {
		this.uri = fileWeb.getUri();
		this.fileWeb = fileWeb;
		this.downloadPath = downloadPath;
		this.downloadTempPath = Paths.get(downloadPath.toString(), "temp");
		this.downloadTempHttpsPath = Paths.get(downloadPath.toString(), "temphttps");

		this.shaFiledownloadPath = Paths.get(downloadPath.toString(), this.getFileName() + ".sha");
		this.renamedFilePathWithoutParts = Paths.get(downloadPath.toString(), this.getFileName());
	}

	@Override
	public void run() {
		try {

			httpGet = new HttpGet(uri);

			response = ConnectionManager.getInstance().getHttpclient().execute(httpGet);

			if (response.getStatusLine().getStatusCode() / 100 != 2) {
				Notification.showErrorOptionPane("download201", "Server error 201");
				return;
			}

			entity = response.getEntity();

			long contentLength = entity.getContentLength();

			if (contentLength < 1) {
				Notification.showErrorOptionPane("download202", "Server error 202");
				return;
			}

			if((downloadTempPath.toFile().getFreeSpace()) < contentLength ) {
				Notification.showErrorOptionPane("download203", "Server error 203");
			}


			//dealloco scollegandomi
			response.close();

			process = new ArrayList<Process>();
			totalSize = contentLength;

			
			fine1 = contentLength / 4;
			fine2 = contentLength / 4 + contentLength / 4;
			fine3 = contentLength / 4 + contentLength / 4 + contentLength / 4;
			fine4 = contentLength;

			if(newDownload) {
				inizio1 = 0;
				inizio2 = fine1;
				inizio3 = fine2;
				inizio4 = fine3;
			} else { //gli inizi li ho gia' impostati dicendo di continuare il download cosi' ora imposto solo le fini
				if(inizio1>=BufferSize.getBufferSize()) { 
					inizio1 = inizio1 - BufferSize.getBufferSize() + 0;
				} else {
					inizio1 = 0;
				}
				inizio2 = inizio2 - BufferSize.getBufferSize() + fine1;
				inizio3 = inizio3 - BufferSize.getBufferSize() + fine2;
				inizio4 = inizio4 - BufferSize.getBufferSize() + fine3;
			}

			LOGGER.debug(inizio1 + "-" + fine1 +"\n" +  inizio2 +"-" + fine2 +"\n" + inizio3 +"-" + fine3 +"\n" + inizio4 +"-" + fine4);

			LOGGER.info("URI: " + uri.toString());
			
			if(uri.toString().startsWith("https://")) {
				this.startHttpsProcess();
			} else {
				this.avviaProcessi();
			}

		} catch (IOException e) {
			LOGGER.error("run() - IOException= " + e);
		}


		DownloadList.getInstance().addDownload(this);

		//		stateChanged();
		//		logicManager.rimuoviInAggiunta(this.fileWeb);

		//gli passo un valore a caso imporante che non sia null,
		//cosi' il table model individua questo caso e si comporta in modo diverso
		//in tutti gli altri casi del stateChanged gli passo null
		stateChanged(true);

		AddingDownload.getInstance().removeUri(this.getFileWeb().getUri()).getUri();

	}
	

	/**
	 * Metodo per inizializzare il processo per https
	 */
	private void startHttpsProcess() {
		Process d1 = new Process(downloadTempHttpsPath, getFileName(), fileWeb.getUri(), 1, inizio1, fine4);
		d1.addObserver(this);

		process.add(d1);

		if(!newDownload) {
			d1.setPartenza(0);
		}
		d1.riprendi();
	}

	

	/**
	 * Metodo per inizializzare i Processi che fanno parte del Download.
	 */
	private void avviaProcessi() {
		Process d1 = new Process(downloadTempPath, getFileName(), fileWeb.getUri(), 1, inizio1, fine1);
		Process d2 = new Process(downloadTempPath, getFileName(), fileWeb.getUri(), 2, inizio2, fine2);
		Process d3 = new Process(downloadTempPath, getFileName(), fileWeb.getUri(), 3, inizio3, fine3);
		Process d4 = new Process(downloadTempPath, getFileName(), fileWeb.getUri(), 4, inizio4, fine4);
		d1.addObserver(this);
		d2.addObserver(this);
		d3.addObserver(this);
		d4.addObserver(this);

		process.add(d1);
		process.add(d2);
		process.add(d3);
		process.add(d4);

		if(!newDownload) {
			d1.setPartenza(0);
			d2.setPartenza(fine1);
			d3.setPartenza(fine2);
			d4.setPartenza(fine3);
		}
		d1.riprendi();
		d2.riprendi();
		d3.riprendi();
		d4.riprendi();
	}

	/**
	 * Metodo per continuare un download indicando l'inizio di tutti i suoi processi
	 * impostando a "false" il nuovo download.
	 * @param inizio1 long che rappresenta l'inzio del Processo.
	 * @param inizio2 long che rappresenta l'inzio del Processo.
	 * @param inizio3 long che rappresenta l'inzio del Processo.
	 * @param inizio4 long che rappresenta l'inzio del Processo.
	 */
	public void continueDownload(long inizio1,long inizio2,long inizio3,long inizio4) {
		this.inizio1 = inizio1;
		this.inizio2 = inizio2;
		this.inizio3 = inizio3 ;
		this.inizio4 = inizio4;
		this.newDownload = false;
	}

	
	/**
	 * Metodo chiamato alla fine del download https, per unire i file e verificare lo SHA1.
	 * @param parte
	 */
	public void setMergingDownloadStateHttps(int parte) {
		if(process.get(parte-1).getStatus()==Process.MERGING) {
			this.terminatedCount = this.terminatedCount + 1;
		}

		LOGGER.info("setMergingDownloadState() - Download: " + this.getFileName() + " -> "+ terminatedCount);

		if(this.terminatedCount==1) {
			this.status = Process.MERGING;
			stateChanged(null);

			//quando il download e' in validation aggiorno lo stato dei pulsanti del download selezionato
			this.setMergingDownload();

			//unisco i file part in uno solo (.ipsw) e li cancello
			//non creo il riferimento all'oggetto tanto non serve a nulla
			this.filesMerger = new FilesMerger(downloadPath,this.getFileName());

			filesMerger.createCompleteIpsw();

			this.setValidateDownloadStateHttps();

			//quando il download e' completato aggiorno lo stato dei pulsanti del download selezionato
			this.completeDownload(this);
		}
	}
	
	
	/**
	 * Metodo chiamato alla fine del download, per unire i file e verificare lo SHA1.
	 * @param parte
	 */
	public void setMergingDownloadState(int parte) {
		if(process.get(parte-1).getStatus()==Process.MERGING) {
			this.terminatedCount = this.terminatedCount + 1;
		}

		LOGGER.info("setMergingDownloadState() - Download: " + this.getFileName() + " -> "+ terminatedCount);

		if(this.terminatedCount==4) {
			this.status = Process.MERGING;
			stateChanged(null);

			//quando il download e' in validation aggiorno lo stato dei pulsanti del download selezionato
			this.setMergingDownload();

			//unisco i file part in uno solo (.ipsw) e li cancello
			//non creo il riferimento all'oggetto tanto non serve a nulla
			this.filesMerger = new FilesMerger(downloadPath,this.getFileName());

			filesMerger.createCompleteIpsw();

			this.setValidateDownloadState();

			//quando il download e' completato aggiorno lo stato dei pulsanti del download selezionato
			this.completeDownload(this);
		}
	}

	/**
	 * Metodo che aggiorna i pulsanti in caso di download terminato.
	 * @param download InsiemeDownload selezionato per cui si vuole modificare lo stato dei pulsanti.
	 */
	public void completeDownload(Download download){
		if(DownloadList.getInstance().getSelectedDownload()!=null && DownloadList.getInstance().getSelectedDownload().equals(download)) {
			StateButton.completaDownloadPulsanti();
		}
	}

	/**
	 * Metodo che aggiorna i pulsanti in cado di download in merging.
	 * @param download Download selezionato per cui si vuole modificare lo stato dei pulsanti.
	 */
	public void setMergingDownload(){
		if(DownloadList.getInstance().getSelectedDownload()!=null && DownloadList.getInstance().getSelectedDownload().equals(this)) {
			StateButton.disattivaPulsanti();
		}
	}

	
	private void setValidateDownloadStateHttps() {
		try {
			//verifico lo SHA1 del file appena unito dai file part
			if(this.needToValidate) { 
				LOGGER.info("setValidateDownloadState() - Avvio la verifica SHA1");
				this.status = Process.VALIDATION;
				this.validationSHA1();
			} else {
				LOGGER.info("setValidateDownloadState() - Verifica SHA1 non necessaria, rinomino il file");
				Files.move(shaFiledownloadPath, renamedFilePathWithoutParts); //rinomina
				status = COMPLETE;
			}

			//ora rimuovo le parti perche' il download e' stato completato e verificato e ha dato risultato positivo
			filesMerger.removeParts(); 

		} catch (Exception e) {
			LOGGER.error("validationSHA1() - Errore controllo SHA1 _ Exception=", e);
			Notification.showNormalOptionPane("downloadDamagedFile");
		}

		this.downloadedBytes = this.totalSize;


		//il download e' completato, quindi ora posso attivare il pulsante nella colonna azione della tabella
		this.activateButtonActionCulomn();

		stateChanged(null);
	}
	
	
	private void setValidateDownloadState() {
		try {
			//verifico lo SHA1 del file appena unito dai file part
			if(this.needToValidate) { 
				LOGGER.info("setValidateDownloadState() - Avvio la verifica SHA1");
				this.status = Process.VALIDATION;
				this.validationSHA1();
			} else {
				LOGGER.info("setValidateDownloadState() - Verifica SHA1 non necessaria, rinomino il file");
				Files.move(shaFiledownloadPath, renamedFilePathWithoutParts); //rinomina
				status = COMPLETE;
			}

			//ora rimuovo le parti perche' il download e' stato completato e verificato e ha dato risultato positivo
			filesMerger.removeParts(); 

		} catch (Exception e) {
			LOGGER.error("validationSHA1() - Errore controllo SHA1 _ Exception=", e);
			Notification.showNormalOptionPane("downloadDamagedFile");
		}

		this.downloadedBytes = this.totalSize;


		//il download e' completato, quindi ora posso attivare il pulsante nella colonna azione della tabella
		this.activateButtonActionCulomn();

		stateChanged(null);
	}

	private void activateButtonActionCulomn() {
		if(DownloadCompatibilityChecker.isCompatibileWithMyOs(this.getFileWeb().getOperativeSystemList(), User.getInstance().getOperativeSystemInstance())) {
			StateActionTableButton.getInstance().setButtonEnable(this.getUri().toString(), true);
		}
	}

	/**
	 * Restiteusce solo il nome del file (non il percorso)
	 * @return Una String rappresentante il nome del file.
	 */
	public String getFileName() {
		if(this.fileWeb instanceof Firmware) {
			String fileName = uri.getPath();
			return fileName.substring(fileName.lastIndexOf('/') + 1);
			//NON POSSO USARE PATH PER COSE IN RETE
			//			Path path = Paths.get(uri);
			//			return path.getFileName().toString();
		} 
		if(this.fileWeb instanceof ItunesVersion) {
			return ((ItunesVersion)this.fileWeb).getFileName();
		}
		return ""; //da cancellare quando scommento le righe sopra
	}

	/**
	 * Metodo richiamato da setValidation per verificato lo SHA1 del file scaricato,
	 * confrontandolo con quello dello del firmware, ad esso associato.
	 * @throws IOException 
	 */
	private void validationSHA1() throws IOException {
		LOGGER.info("validationSHA1() - Avviato. Status download=" + status);
		LOGGER.info("validationSHA1() - Sha1 Firmware=" + fileWeb.getHash());

		String hashFileScaricato = CheckSha1.getSha1(shaFiledownloadPath);

		LOGGER.info("validationSHA1() - Sha1 file scaricato=" + hashFileScaricato);

		if(CheckSha1.isCorrect(hashFileScaricato, fileWeb.getHash())) {
			status = COMPLETE;
		} else {
			status = ERROR;
		}
		Files.move(shaFiledownloadPath, renamedFilePathWithoutParts); //rinomina
		LOGGER.info("validationSHA1() - Download completato con status=" + status);
	}

	/**
	 * Metodo che riprende il download, processo per processo.
	 */
	public void resume() {
		for(Process process : this.process) {
			process.riprendi();
		}
		this.status = Process.DOWNLOADING;
		this.terminatedCount = 0;
	}


	/**
	 * Metodo per mettere in pausa il download, processo per processo.
	 */
	public void pause() {
		for(Process process : this.process) {
			process.pause();
		}
		this.status = Process.PAUSED;
		this.terminatedCount = 0;
	}


	/**
	 * Metodo per mettere fermare il download, processo per processo.
	 */
	public void cancel() {
		for(Process process : this.process) {
			process.cancel();
		}
		this.status = Process.CANCELLED;
	}

	/**
	 * Metodo che notifica all'observers che lo status di questo download e' cambiato al TableModel della tabella
	 */
	public void stateChanged(Object notifyAddToAddingDownload) {
		setChanged();
		notifyObservers(notifyAddToAddingDownload);
	}

	@Override //ricevuta da #Process
	public void update(Observable o, Object arg1) {
		//chiamo validation facendo casting dell'observable a Processo e ottenendo la parte
		//il casting e' possibile perche' Processo estende Observable
		if(o instanceof Process) {
			if(uri.toString().startsWith("https://")) {
				this.setMergingDownloadStateHttps(((Process)o).getParte());
			} else {
				this.setMergingDownloadState(((Process)o).getParte());
			}
		}
	}

	/**
	 * Metodo per calcolare la quantita' di byte scaricate dal download,
	 * sommando i vari parziali, processo per processo.
	 * @return long che rappresenta il numero di byte scaricati.
	 */
	private long calculateDownloadedCorrently() {
		long downloadedNow = 0;
		//calcolo quanto e' stato scaricato il file
		for(Process process : this.getProcessList()) {
			downloadedNow += process.getDownloaded();
		}
		return downloadedNow;
	}

	/**
	 * Metodo per calcolare la velocita' attuale del download. 
	 * Esso imposta la variabile velocita' con il risultato.
	 */
	public void calcolaVelocita() {
		//calcolo scaricato per usarlo subito sotto per ottenere la velocita
		long scaricatoAttuale = this.calculateDownloadedCorrently();

		//setto la velocita' in byte al secondo (visto il ritardo di 1000)
		//in pratica sottraggo quanto ho scaricato ora (variabile locale) 
		//con quanto avevo scaricato prima (variabile globale)
		this.speed = scaricatoAttuale - this.downloadedBytes;

		if(this.downloadedBytes!=this.totalSize) {
			this.downloadedBytes = scaricatoAttuale;
		}
	}

	/**
	 * Metodo per ottenere la dimensione dell'insiemeDownload in base alle opzioni
	 * scelte nel pannello delle preferenze.
	 * @param dimensioneDaMostrare int che indica in che unita' di misura visualizzare la velocita'.
	 * @return Una String che rappresenta la dimensione in base alla dimensioneDaMostrare.
	 */
	public String getDimension() {
		int dimensioneDaMostrare = Settings.getInstance().getTipoDimensione();
		switch(dimensioneDaMostrare) {
		case 0://in byte
			return (totalSize == -1) ? "" : totalSize + "";
		case 1://in B,KB,MB,GB
			return (totalSize == -1) ? "" : this.convertDimensionToString(totalSize,1000);
		default://in B,KiB,MiB,GiB (se ==2)
			return (totalSize == -1) ? "" : this.convertDimensionToString(totalSize,1024);
		}
	}

	/**
	 * Metodo per convertire la dimensione del download in base al valore di conversione.
	 * @param dimensione float che rappresenta la dimensione.
	 * @param conversione int che indica come convertire la dimensione, cioe' in che unita' di misura.
	 * @return
	 */
	private String convertDimensionToString(float dimensione, int conversione) {
		//conversione puo' essere 1000 -> multipli dei Byte o 1024-> multipli di Binary Byte
		String kb,mb,gb;
		if(conversione==1000) {
			kb = " KB";
			mb = " MB";
			gb = " GB";
		} else {
			kb = " KiB";
			mb = " MiB";
			gb = " GiB";
		}
		//non server fare gli if-else tanto ci sono i return dentro che terminano il metodo
		if(dimensione<conversione) {
			return dimensione + " B"; 
		}
		if(dimensione<conversione*conversione) {
			return String.format("%.2f", dimensione/conversione) + kb;
		} 
		if(dimensione<conversione*conversione*conversione) {
			return String.format("%.2f", dimensione/(conversione*conversione)) + mb;
		} 
		return String.format("%.2f", dimensione/(conversione*conversione*conversione)) + gb;
	}

	/**
	 * Metodo per ottenere la velocita' di download formattata come String,
	 * con l'unita' di misura.
	 * @param Download Download di cui ottenere la velocita'
	 * @return Una String che contiene la velocita' e l'unita' di misura.
	 */
	public String getSpeed() {
		int dimensioneDaMostrare = Settings.getInstance().getTipoDimensione();
		if(speed==0) {
			return "";
		}

		if(dimensioneDaMostrare==2) {
			//in B,KiB,MiB,GiB
			return (speed)/1024 + " KiB/S";
		} else {
			//in B,KB,MB,GB
			return (speed)/1000 + " KB/S";
		}
	}

	public String getRemainingTime() {
		return remainingTime;
	}
	public void setRemainingTime(String remainingTime) {
		this.remainingTime = remainingTime;
	}
	public int getStatus() {
		return status;
	}
	public long getDimensioneTotale() {
		return totalSize;
	}
	public long getVelocita() {
		return speed;
	}
	public void setSpeed(long speed) {
		this.speed = speed;
	}
	public long getDownloadedBytes() {
		return downloadedBytes;
	}
	public List<Process> getProcessList() {
		return process;
	}
	public URI getUri() {
		return uri;
	}
	public FileWeb getFileWeb() {
		return fileWeb;
	}
	public Path getRenamedFilePathWithoutParts() {
		return renamedFilePathWithoutParts;
	}
	public boolean isNeedToValidate() {
		return needToValidate;
	}
	public void setNeedToValidate(boolean needToValidate) {
		this.needToValidate = needToValidate;
	}

	public boolean isSelectedDownload() {
		return selectedDownload;
	}

	public void setSelectedDownload(boolean selectedDownload) {
		this.selectedDownload = selectedDownload;
	}
}
