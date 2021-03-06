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

import it.stefanocappa.fileutility.FileList;
import it.stefanocappa.gui.state.StateButton;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import it.stefanocappa.model.BufferSize;
import it.stefanocappa.model.FileWeb;
import it.stefanocappa.model.Firmware;
import it.stefanocappa.model.User;
import it.stefanocappa.notification.Notification;

import org.apache.http.client.ClientProtocolException;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import it.stefanocappa.connection.ConnectionManager;
import it.stefanocappa.connection.TestConnection;
import it.stefanocappa.logic.listener.SettingsListener;
import it.stefanocappa.logic.listener.toolbarbutton.PauseAllListener;
import it.stefanocappa.logic.listener.toolbarbutton.PauseListener;
import it.stefanocappa.logic.listener.toolbarbutton.RemoveAllListener;
import it.stefanocappa.logic.listener.toolbarbutton.RemoveListener;
import it.stefanocappa.logic.listener.toolbarbutton.ResumeAllListener;
import it.stefanocappa.logic.listener.toolbarbutton.ResumeListener;
import it.stefanocappa.preferences.Settings;

public final class LogicManager {
	private static final Logger LOGGER = LogManager.getLogger(LogicManager.class);

	private Settings impostazioni;
	private Path percorsoDownload;
	private Path percorsoDati;
	private Path percorsoDownloadTemp;
	private static LogicManager instance = new LogicManager();

	public static LogicManager getInstance() {
		return instance;
	}

	private LogicManager() {
		this.initializeConnectionResources();
	}

	private void initializeConnectionResources() {
		try {
			//se questa riga sotto rilancia eccezione mostra errori, altrimenti procede con
			//l'avvio del programma
			//qui leggo le preferenze e imposto il connection manager (con eventuale proxy) piu' altre operazioni
			this.preparaAvvio();
		} catch (ClientProtocolException e) {
			LOGGER.error("run() - ClientProtocolException _ Errore connessione=", e);
			this.removeProxy();
			Notification.showErrorOptionPane("downloadManagerConnectionException", "ClientProtocolException");
			System.exit(0);
		} catch (IOException e) {
			LOGGER.error("run() - IOException _ Errore connessione=", e);
			this.removeProxy();
			Notification.showErrorOptionPane("downloadManagerConnectionException", "IOException");
			System.exit(0);
		}
	}

	public void registerListenerStateButton() {
		StateButton.getPreferenzeButton().addActionListener(new SettingsListener());
		StateButton.getPauseButton().addActionListener(new PauseListener());
		StateButton.getResumeButton().addActionListener(new ResumeListener());
		StateButton.getRemoveButton().addActionListener(new RemoveListener());
		StateButton.getPauseButtonAll().addActionListener(new PauseAllListener());
		StateButton.getResumeButtonAll().addActionListener(new ResumeAllListener());
		StateButton.getRemoveButtonAll().addActionListener(new RemoveAllListener());
	}

	public void initPreferencesInLogicManager() {
		this.impostazioni = Settings.getInstance();

		LOGGER.info("initPreferencesInLogicManager() - versione=" + impostazioni.getVersione());

		this.percorsoDownload = Paths.get(impostazioni.getPercorsoDownload());
		this.percorsoDownloadTemp = Paths.get(impostazioni.getPercorsoDownloadTemp());
		this.percorsoDati = Paths.get(impostazioni.getPercorsoDati());
		BufferSize.setBufferSize(impostazioni.getDimensioneBuffer());
	}


	/**
	 * Metodo che rimuove l'impostazione del proxy, in seguito ad un errore di connessione del programma.
	 */
	private void removeProxy() {
		impostazioni.setProxyActivated(false);
		this.impostazioni.saveSettings();
		LOGGER.info("removeProxy() - Al prossimo avvio il proxy sara' disattivato");
	}



	// Add a new download.
	public void actionAdd(FileWeb fileWeb) {
		Download download;
		if(fileWeb instanceof Firmware) {
			download = new DownloadFirmware(percorsoDownload, fileWeb);
		} else { //se e' un itunesversion o un softwarejailbreak creo un DownloadSoftware
			download = new DownloadSoftware(percorsoDownload, fileWeb);
		}

		this.avviaThreadInsiemeDownload(download);
	}

	protected void avviaThreadInsiemeDownload(Download download) {
		(new Thread(download)).start();
	}


	/**
	 * Metodo per mettere in pausa tutti i download prima di 
	 * eseguire operazioni di chiusura, aggiornamento ecc...
	 */
	public void mettiPausaPreChiusura() {
		for(Download insiemeDownload : DownloadList.getInstance().getDownloadList()) {
			if(insiemeDownload!=null && insiemeDownload.getStatus()==0) {
				insiemeDownload.pause();
			}
		}
	}


	public void removeDownloadTemp() {
		LOGGER.info("resetDownloadTemp() - Avviato");
		this.deleteFileList(percorsoDownloadTemp,"percorsodownloadtemp");
		LOGGER.info("resetDownloadTemp() - Terminato");
	}

	public void removeDb() {
		LOGGER.info("resetDb() - Avviato");
		this.deleteFileList(percorsoDati,"percorsodati");
		LOGGER.info("resetDb() - Terminato");
	}

	public void reinizializzaProgramma() {
		LOGGER.info("reinizializzaProgramma()");

		this.removeDb();
		this.mettiPausaPreChiusura();

		this.deleteFileList(percorsoDownload,"percorsoDownload");
		this.removeDownloadTemp(); //metodo usato anche da terminale e quindi lo separo e lo richiamo qui
		this.deleteFileList(User.getInstance().getDownloadPath(),"percorsoDownloadPredefinito");
		this.deleteFileList(User.getInstance().getDownloadTempPath(),"percorsodownloadtempPredefinito");

		System.exit(1); //termina l'intera virtual machine
	}

	/**
	 * Metodo che rimuove tutti i file di una lista, dato un percorso e il nome dell'operazione.
	 * Per nome dell'operazione si intende un nome semplcei e breve che spieghi cosa deve essere cancellato.
	 * @param operationName
	 */
	private void deleteFileList(Path directoryPath, String operationName) {
		for(Path path : FileList.getFileList(directoryPath)) {
			try {
				Files.deleteIfExists(path);
			} catch (IOException e) {
				LOGGER.info("deleteFileList() - Nome operazione: " + operationName + 
						". Eccezione durante cancellazione di " + path.toString() + ". Eccezione = " + e);
			}
		}
	}

	private void preparaAvvio() throws IOException {
		//preparo PercorsiOS
		User.getInstance();

		//leggo le impostazioni e carico nella variabili globali di questa classe
		//i valori letti per poi essere usati anche da tutte le altre classi, poiche' usa singleton
		this.initPreferencesInLogicManager();

		//come prima cosa creo il connectionaManager inizializzandolo e passando come paramentri i dati del proxy
		ConnectionManager.getInstance().initConnectionManager(impostazioni.isProxyActivated(), impostazioni.getProxyServer(), impostazioni.getProxyPort());

		//verifico la connetivita' internet tramite richiesta GET a google
		(new TestConnection()).testConnection();
		LOGGER.info("Run - Test connessione: TUTTO OK!");
	}

	/**
	 * @return
	 * @uml.property  name="impostazioni"
	 */
	public Settings getImpostazioni() {
		return impostazioni;
	}

}
