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

import it.stefanocappa.gui.MainFrame;
import it.stefanocappa.gui.image.ImageLoader;
import it.stefanocappa.logic.LogicManager;
import it.stefanocappa.logic.listener.WindowClosingListener;
import it.stefanocappa.logic.listener.byam.DeviceComboBoxListener;
import it.stefanocappa.logic.listener.byam.OsComboBoxListener;
import it.stefanocappa.logic.listener.macosx.CommandQListener;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.apple.eawt.Application;

import it.stefanocappa.update.commercialdevicename.CommercialNameUpdater;
import it.stefanocappa.uuid.RegisteredUuid;

public final class DownloadManager implements Runnable {
	private static final Logger LOGGER = LogManager.getLogger(DownloadManager.class);

	private static DownloadManager instance = new DownloadManager();

	/**
	 * Costruttore privato della classe
	 */
	private DownloadManager() {
		SplashScreenManager.setText("loading");

		//creo graphicmanager e gli passo gli oggetti che voglio. Questo serve solo per poter chiamare i metodi NON STATICI.
		//per i metodi static non e' necessaria nessuna inizializzazione.
		//addirittura per i metodi static basta fare GraphicManager.metodoStatico
		LogicManager.getInstance();
	}

	/**
	 * Metodo che permette di ottenere l'istanza della classe.
	 * @return istanza della classe.
	 */
	public static DownloadManager getInstance() {
		return instance;
	}

	@Override
	public void run() {
		//inizializzo la tabella, statoPulsanti e tutti i pulsanti, le immagini
		//l'icona nella dock di mac e il menu del programma
		this.registraListenerCmdQ();

		SplashScreenManager.setText("checkingInternetConnection");
		
		SplashScreenManager.setText("initGui");
		//verifica dell'uuid ed eventualmente lo registrata
		(new RegisteredUuid()).checkUuidRegistration();
		
		
		SplashScreenManager.setText("initDownloadManager");
		this.avviaProgramma();
		
		LOGGER.info(System.getProperty("user.name") + " " + System.getProperty("os.name") + "__" + System.getProperty("os.version"));
	}

	private void registraListenerCmdQ() {
		//se il sistema operativo e' OSX
		//imposto l'icona nella Dock di macOSX
		//e impongo che la chiusura con cmd+q metta in pausa tutti i download
		if(System.getProperty("os.name").contains("Mac")) {
			Application.getApplication().setDockIconImage(ImageLoader.getInstance().getIconaBya());
			Application.getApplication().setQuitHandler(new CommandQListener());
		}
	}

	private void avviaProgramma() {
		LOGGER.info("avviaProgramma() - Avviato il metodo");

		SplashScreenManager.setText("checkingBYAMSoftwareUpdates");


		//--------------------------GESTIONE AGGIORNAMENTI SOFTWARE----------------------------
		UpdateManagerSoftware.getInstance().checkSoftwareUpdates();
		//---------------------------------------------------------------------------------------

		SplashScreenManager.setText("checkingDatabaseUpdates");

		//--------------------------GESTIONE AGGIORNAMENTI DATABASE----------------------------
		try {
			UpdateManagerDB.getInstance().updateDb();
		} catch (IOException e) {
			LOGGER.error("avviaProgramma() - Eccezione lanciata durante updateDb()", e);
		}
		
		//---------------------------------------------------------------------------------------

		SplashScreenManager.setText("startingadditionalOperations");

		//preparo i menu a tendina (JComboBox) con la lista dei firmware e di itunes
		CommercialNameUpdater.getInstance().loadDevices();
		LogicLoaderFirmware.getInstance().loadFirmware();
		LogicLoaderItunes.getInstance().loadiTunesVersion();
		
		//inserisco gli elementi nella combobox con i firmware e itunes
		LogicLoaderFirmware.getInstance().fillFirmwareComboBox();
		LogicLoaderItunes.getInstance().fillItunesComboBox();

		
		SplashScreenManager.setText("restoreUncompletedDownloads");

		//Ripristino i download non terminati e in caso cerco di recuperare queli .sha
		//sia che siano firmware, sia che siano versioni di itunes
		//prima quelli http
		(new Restorer()).ripristinaDownload();
		//dopo quelli https
		(new RestorerHttps()).ripristinaDownload();
		
		this.startDownloadCalculator();


		MainFrame.getInstance().addWindowListener(new WindowClosingListener());

		SplashScreenManager.setText("startingPleaseWait");
		//attivo timer di 8 secondi che inizia gia' a contare da quanto mostro la splash
		//qui dico solo quanto deve avere come limite massimo
		//se la splash e' aperta da 3 secondi questo non influenza nulla
		//se invece e meno, setta il limite a 8 sec, oltre il quale la splash sara' comunque chiusa
		SplashScreenManager.setTimer(8000); 

		//creo l'interfaccia grafica (istanziandola creo elementi, con createMainPanel() li posiziono tutti)
		//dopo registro i listener del menu in alto
		MainFrame.getInstance().createMainPanel();
		
		//metodo che registra i listener per ogni elemento della grafica inzializzato in avviaProgramma()
		this.registraListener();
		
//		ListenerRegister.getInstance().registerItemMenuListener();  era subito sotto a Mainframe ecc... e il registra listener non era qui ma piu' su

		//chiudo la splashScreen
		SplashScreenManager.chiudiSplash();

		MainFrame.getInstance().showMainFrame();
		LOGGER.info("avviaProgramma() - Metodo terminato");
	}


	private void startDownloadCalculator() {
		DownloadCalculator cs = new DownloadCalculator();
		cs.abilitaCalcoloScaricato(true);
		cs.start();
	}
	
	private void registraListener() {
		ListenerRegister.getInstance().registerItemMenuListener();
		ListenerRegister.getInstance().registerListener();
		ListenerRegister.getInstance().registerListerenSystemTray();
		LogicManager.getInstance().registerListenerStateButton();
		MainFrame.getListaDispositivi().addActionListener(new DeviceComboBoxListener());
		MainFrame.getOsList().addActionListener(new OsComboBoxListener());
	}
}