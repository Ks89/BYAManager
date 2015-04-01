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

import gui.MainFrame;

import java.io.IOException;
import java.net.Socket;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import logger.LoggerManager;
import notification.Notification;

import org.apache.log4j.Logger;

import preferences.Settings;

/**
 * Classe main
 */
public class BYAManager {

	private static int porta = 10378;
	private static final Logger LOGGER = Logger.getLogger(BYAManager.class);

	static {
		//carico la configurazione del logger che varra' per tutto il programma
		//senza doverlo fare ogni volta
		new LoggerManager();
	}

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
//					InputStream bya= new FileInputStream("/Users/Ks89/Desktop/BYA/cartella_senza_titolo/BYAManager.jar");
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

