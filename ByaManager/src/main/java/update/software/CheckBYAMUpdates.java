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

package update.software;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import model.User;
import notification.Notification;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;

import update.LinkServer;


import exception.UpdateException;
import fileutility.FileList;
import fileutility.HttpFileDownload;

/**
 *	Classe che si occupa di verificare aggiornamenti dell'interno programma
 *	inteso come file .JAR.
 */
public final class CheckBYAMUpdates extends Observable implements Runnable{
	private static final Logger LOGGER = Logger.getLogger(CheckBYAMUpdates.class);
	private static final String BYAMANAGERNEW = "BYAManager-new.j_a_r";
	private static final String BYAUPDATER = "BYAUpdater.jar";
	private String shaBya;
	private String shaUpdater;
	private String versioneDaFile;
	private Path jarPath;

	public CheckBYAMUpdates() {
		jarPath = Paths.get(User.getInstance().getJarPath());
	}

	/**
	 * Metodo per ottenere i jar del programma e dell'updater dal server
	 * verificarne gli sha512 e in caso coincidano con quelli nel file non solleva eccezioni,
	 * altrimenti solleva AggiornamentoException.
	 * @throws IOException
	 * @throws UpdateException 
	 */
	public void ottieniJarDaServer() throws IOException, UpdateException {
		this.scaricaJAR(); //scarico updater e lo sha
		//controllo gli SHA512 per capire se i file sono corretti
		this.verificaSha();
	}

	/**
	 * Metodo per scaricare i jar contenente l'ultima
	 * versione di ByaMagaer e dell'updater.
	 */
	public void scaricaJAR() {
		try {
			LOGGER.info("scaricaJAR() - Avviato metodo");
			HttpFileDownload.httpFileDownload(jarPath.resolve(BYAMANAGERNEW), LinkServer.LINKJAR_KS);
			HttpFileDownload.httpFileDownload(jarPath.resolve(BYAUPDATER), LinkServer.LINKUPDATER_KS);
		} catch (IOException | URISyntaxException e) {
			try {
				LOGGER.info("scaricaJAR() - Link KS non funzionante, riprovo");
				//nel caso il primo try abbia scaricato solo un pezzo di questi file, li cancello per evitare problemi
				Files.deleteIfExists(jarPath.resolve(BYAMANAGERNEW));
				Files.deleteIfExists(jarPath.resolve(BYAUPDATER));
				HttpFileDownload.httpFileDownload(jarPath.resolve(BYAMANAGERNEW), LinkServer.LINKJAR);
				HttpFileDownload.httpFileDownload(jarPath.resolve(BYAUPDATER), LinkServer.LINKUPDATER);
			} catch (IOException | URISyntaxException e1) {
				LOGGER.error("scaricaJAR() - IOException oppure URISyntaxException=" , e1);
			}
		}
		LOGGER.info("scaricaJAR() - Terminato metodo");
	}

	/**
	 * Metodo per verificare gli SHA dei file scaricati usati per l'operazione
	 * di aggiornamento.
	 * @throws IOException
	 * @throws UpdateException
	 */
	public void verificaSha() throws IOException,UpdateException {
		InputStream bya = new FileInputStream(jarPath.resolve(BYAMANAGERNEW).toFile());
		InputStream updater = new FileInputStream(jarPath.resolve(BYAUPDATER).toFile());
		String shaByaScaricato = DigestUtils.sha512Hex(bya);
		String shaUpdaterScaricato = DigestUtils.sha512Hex(updater);
		if(!shaByaScaricato.equals(this.shaBya) || !shaUpdaterScaricato.equals(this.shaUpdater)) {
			throw new UpdateException(UpdateException.Causa.CORRUPTED_JAR);
		}
		bya.close();
		updater.close();
		this.shaBya = null;
		this.shaUpdater = null;
		shaByaScaricato = null;
		shaUpdaterScaricato = null;
	}

	/**
	 * Metodo chiamato da aggiornaProgramma per rimuovere l'updater 
	 * dalla cartella in cui e' in esecuzione il programma.
	 */
	public void removeUpdater() {	
		for(Path path : FileList.getFileList(jarPath)) {
			if(path.endsWith(BYAUPDATER)) {
				try {
					Files.delete(path);
				} catch (IOException e) {
					LOGGER.error("removeUpdater() - IOException = " + e);		
				}
			}
		}
	}

	/**
	 * Metodo che dato un link ottiene il file/pagina internet in quella posizione e la parsa riga per riga
	 * inserendo ogni riga come elemento di una lista di stringhe. Questo senza nessuna interazione con l'hd
	 * poiche' non usa i file, ma solo l'inputstreamreader.
	 * @param link
	 * @return
	 * @throws IOException
	 */
	private List<String> serverFileReader(String link) throws IOException {
		LOGGER.info("serverFileReader() - Avviato metodo");
		List<String> rowFileList = new ArrayList<String>();

		try (
				InputStream inputStream = new URL(link).openStream();
				InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
				BufferedReader br = new BufferedReader(inputStreamReader);
				) {
			String rigaAttuale = br.readLine();
			while(rigaAttuale != null) {
				rowFileList.add(rigaAttuale);
				rigaAttuale = br.readLine();
			}
		} catch(IOException e) {
			throw e;
		}
		LOGGER.info("serverFileReader() - Terminato metodo");
		return rowFileList;
	}

	private void update(String linkSha, String linkVersion) throws IOException, URISyntaxException {
		//inserisco nelle variabili globali gli sha si updater e programma, letti dal file di controllo (ottenuto senza scaricarlo)
		List<String> check = this.serverFileReader(linkSha);
		this.shaBya = check.get(0);
		this.shaUpdater = check.get(1);

		//ottengo la versione dal file version leggendo solo la prima riga del file
		this.versioneDaFile = this.serverFileReader(linkVersion).get(0);

		if(versioneDaFile!=null) {
			stateChanged();
		}
	}

	//in questo metodo non ottengo il 2 jar veri e propri ma eseguo solo i controlli.
	//il download e' chiamato da DownloadManager e delegato al metodo ottieniJarDaServer(), mentre la cancellazione dell'updater, alriavvio
	//e' delegata a rimuoviUpdater().
	public void run() {
		LOGGER.info("run() - Avviato metodo");
		try { //Inizia a provare il server
			this.update(LinkServer.LINKSHA_KS,LinkServer.LINKVERSION_KS);
		} catch (Exception e) {
			LOGGER.error("run() - Exception=" , e);
			try {
				this.update(LinkServer.LINKSHA,LinkServer.LINKVERSION);
			} catch(IOException e1) {
				LOGGER.error("run() - IOException=" , e1);
				Notification.showNormalOptionPane("checkBYAMUpdatesError");
				System.exit(1);
			} catch(URISyntaxException e1) {
				LOGGER.error("run() - URISyntaxException= " + e1);
				Notification.showNormalOptionPane("checkBYAMUpdatesError");
			}
		} 
		LOGGER.info("run() - Terminato metodo");
	}	

	/**
	 * @return
	 */
	public String getVersioneDaFile() {
		return versioneDaFile;
	}

	/**
	 * Metodo che notifica all'observers che lo status di questo download e' cambiato a UpdateManagerSoftware
	 */
	public void stateChanged() {
		setChanged();
		notifyObservers();
	}
}
