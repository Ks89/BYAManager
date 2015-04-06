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

import gui.MainFrame;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import notification.Notification;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import preferences.Settings;

/**
 * Classe main
 */
public class BYAManager {

	private static int porta = 10378;
	
	private static final Logger LOGGER = LogManager.getLogger(BYAManager.class);

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
	
	
	/**
	 * Metodo main per eseguire il programma.
	 * @param args
	 */
	public static void main(String[] args) {	
		//deve essere sempre la prima riga, in modo che questa propieta' venga 
		//impostata nel thread principale e non in quello di swing.
		System.setProperty("apple.laf.useScreenMenuBar", "true");
		if(System.getProperty("os.name").contains("Mac")) {
			System.setProperty("com.apple.mrj.application.apple.menu.about.name", "BYAManager");
		}

		//inizializzo la gestione della splashscreen (usa singleton)
		SplashScreenManager.getInstance();

		try {
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
			LOGGER.error("main() - Eccezione nell'impostare il look and feel = " + e);
		}

		//carico subito le preferenze per impostare la lingua
		Settings.getInstance().initSettings();
		//dopo crea gli elementi della gui delle preferenze assegnandogli il nome nella lingua giusta
		Settings.getInstance().createAndAssignGuiElements();

		//ora vi accedo in modo statico per tutti il programma (riottenendo eventualmente l'istanza)
		SplashScreenManager.mostraSplash();
		SplashScreenManager.setText("starting");

		//per impostare manualmente la porta
		if(args.length>0) {
			porta = (Integer) (new CommandLineParser(args[0])).interpretaComando();
			System.out.println("Porta ricevuta: " + porta);
		}
		
		
//				try {
//					InputStream bya= new FileInputStream("/Users/Ks89/Desktop/BYA/BYAManager.jar");
//					System.out.println(" sha512 bya " + DigestUtils.sha512Hex(bya));
//										InputStream bya2= new FileInputStream("/Users/Ks89/Desktop/BYA/BYAUpdater.jar");
//										System.out.println(" sha512 updater " + DigestUtils.sha512Hex(bya2));
//				} catch (FileNotFoundException e1) {
//					e1.printStackTrace();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}

		ServerSingleInstance ssi = null;
		try {
			System.out.println("Porta impostata: " + porta);
			new Socket("localhost", porta);
			Notification.showNormalOptionPane("mainNoMultipleInstance");
			System.exit(1);
		} catch (IOException e) {
			ssi = new ServerSingleInstance(porta);
			ssi.start();
		} finally {
			SwingUtilities.invokeLater(MainFrame.getInstance()); //inizializzo la grafica
			DownloadManager.getInstance().run(); //inizializzo la logica
		}
	}

	/**
	 * Metodo per avviare questo file jar dall'updater. Non c'e' differenza
	 * ma mi server per tracciare con il loggere il diverso metodo di avvio
	 */
	public void avviaDaUpdater() {
		LOGGER.info("Richiesto avvio da Updater");
		Notification.showNormalOptionPane("mainThanksForUpdate");
		BYAManager.main(new String[1]);
	}

}

