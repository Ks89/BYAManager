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

package logic;

import java.io.IOException;

import gui.MainFrame;
import gui.image.ImageLoader;
import logic.LogicManager;
import logic.listener.WindowClosingListener;
import logic.listener.byam.DeviceComboBoxListener;
import logic.listener.byam.OsComboBoxListener;
import logic.listener.macosx.CommandQListener;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.apple.eawt.Application;

import uuid.RegisteredUuid;

/*
 *
 * Da fare con priorita' alta:  (????? non ricordo perche' sono cosi' a priorita' alta)
 * - rimettere proxy usando le api di java e non piu' il connectionmanager di apache
 * - rimuovere del tutto il connection manager 
 * - eliminare cartella temp e far si che le parti vengano salvati in percorsoDownload come file nascosti
 * 		quando devo unirle usare il metodo concatenaParti2 (commentato) della classe UnioneFile
 */

/*
 * Da fare:
 * - Mettere controllo della sha1 se e' esadeciamale tramite un pattern. Per ora c'e' il codice commentato in CheckDBUpdate
 * - Controllo per far si che sia possibile scaricare un file solo se c'e' lo spazio disponibile nel disco (fatto, ma da testare)
 * - Aggiungere grafica personalizzata al popup nella systemtray
 * - Separare Gui dal download manager per poter avviare il DM dal main e quando si avvia la GUi farlo dal DM con invokelater
 * - Rimuovere la ncessita' di riavviare il programma dopo un aggiornamento dal file xml
 * - creare metodo a parte che valida il percorso inserito nella textfield del pannello preferenze
 * - prevedere 4 stati completed per l'insieme download, in modo da avvisare nella tabella quanti sono i thread in corso (magari solo se abilitata voce in preferenze)
 * - Riscrivere sistema di upload file usando crittografia
 * - Riscrivere sistema di controllo SHA512 criptando il link verso altervista (magari passando per un proxy)
 * - Scrivere la funzione di invio email
 * - Eliminare il bordo nel jwindow
 * - Migliorare aspetto spostando meglio componenti
 * - Creare icone del programma a diverse dimensioni e trasparenti e non.
 * - Rimuovere menu donazioni ed inserire il pulsante nella grafica
 * - Aggiungere pulsanti twitter, facebook, youtube, link al sito ecc.. per ks89 e BYA
 * - Creare in alto a dx un qualche cosa, cliccandoci si apre con animazione nel jframe (magari usando il GlassPane) una finstre arrotondata che non copre tutot il frame
 * -	e mostra i vari link ai siti ecc... (sarebbe una figata farlo, magari anche con animazioni).
 */

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
		LogicLoaderFirmware.getInstance().loadFirmware();
		LogicLoaderItunes.getInstance().loadiTunesVersion();
		
		//inserisco gli elementi nella combobox con i firmware e itunes
		LogicLoaderFirmware.getInstance().fillFirmwareComboBox();
		LogicLoaderItunes.getInstance().fillItunesComboBox();

		
		SplashScreenManager.setText("restoreUncompletedDownloads");

		//Ripristino i download non terminati e in caso cerco di recuperare queli .sha
		//sia che siano firmware, sia che siano versioni di itunes
		(new Restorer()).ripristinaDownload();
		
		this.startDownloadCalculator();

//		//metodo che registra i listener per ogni elemento della grafica inzializzato in avviaProgramma()
//		this.registraListener();

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
		MainFrame.getInstance().getListaDispositivi().addActionListener(new DeviceComboBoxListener());
		MainFrame.getInstance().getOsList().addActionListener(new OsComboBoxListener());
	}
}