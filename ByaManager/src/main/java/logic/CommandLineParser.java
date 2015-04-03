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

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import preferences.Settings;

public class CommandLineParser {
	/**
	 * @uml.property  name="comando"
	 */
	private String comando;
	private static final int DEFAULTPORT = 10378;
	private static final Logger LOGGER = LogManager.getLogger(CommandLineParser.class);

	public CommandLineParser(String comando) {
		this.comando = comando;
	}

	public Object interpretaComando() {
		LOGGER.info("Comando ricevuto: "+ comando);

		if(comando.contains("port-")) {
			return this.changePort();
		} else {
			//carico subito le preferenze per impostare la lingua
			Settings.getInstance().initSettings();
			//dopo crea gli elementi della gui delle preferenze assegnandogli il nome nella lingua giusta
			Settings.getInstance().createAndAssignGuiElements();
			//per impostare i percorsi nella classe
			(LogicManager.getInstance()).initPreferencesInLogicManager(); 
		}

		try {
			switch(comando) {
			case "reset-all":
				this.resetAll();
			case "delete-download-temp":
				this.deleteDownloadTemp();
			case "update-db":
				this.updateDb();
			case "update-software":
				this.updateSoftware();
			case "help":
				this.showHelp();
			default :
				LOGGER.info("Non e' stato rilevato un comando aggiuntivo, quindi avvio il programma con i parametri predefiniti");
				return DEFAULTPORT;
			}
		} catch (IOException e) {
			LOGGER.error("Errore durante l'esecuzione di una delle procedure richieste: " + e);
			LOGGER.info("Quindi, vvio il programma con i parametri predefiniti");
			return DEFAULTPORT;
		}
	}

	private Integer changePort() {
		LOGGER.info("Operazione eseguita, avvio in corso con porta personalizzata");
		int porta = Integer.parseInt(comando.replace("port-", ""));
		if(porta>=10000 && porta<20000) {
			return Integer.parseInt(comando.replace("port-", "")); //cambio la porta
		} else {
			LOGGER.error("Errore! La porta deve essere >= 10000 e <20000");
			LOGGER.error("Come conseguenza dell'errore e' stata impostata la porta predefinita");
			return DEFAULTPORT;
		}
	}

	private void resetAll() {
		//chiama il reset completo del programma e termina
		(LogicManager.getInstance()).reinizializzaProgramma();
		LOGGER.info("Operazione eseguita, chiususa in corso");
		LOGGER.info("Riavvia normalmente il programma eseguendo il file .jar");
	}

	private void deleteDownloadTemp() {
		//chiama il reset solo dei downloadTemporanei del programma e termina
		(LogicManager.getInstance()).removeDownloadTemp();
		LOGGER.info("Operazione eseguita, chiususa in corso");
		LOGGER.info("Riavvia normalmente il programma eseguendo il file .jar");
	}

	private void updateDb() throws IOException {
		//chiama l'aggiornamento manuale del database prima ancora di avviare e termina
		//		DownloadManager.getInstance().run(); nn so se serve, non ricordo se prima delle modifiche c'era

		//fermo eventuali download in corso
		(LogicManager.getInstance()).mettiPausaPreChiusura();

		//rimuovo i database
		(LogicManager.getInstance()).removeDb();

		//scarico i database
		UpdateManagerDB.getInstance().updateDb();
	}

	private void updateSoftware() {
		//chiama l'aggiornamento manuale del programma prima ancora di avviare e termina
		DownloadManager.getInstance().run();

		//fermo eventuali download in corso
		(LogicManager.getInstance()).mettiPausaPreChiusura();

		UpdateManagerSoftware.getInstance().checkSoftwareUpdates();
	}

	private void showHelp() {
		LOGGER.info("...................:::Help:::...................\n\n");
		LOGGER.info("   'port-xxxxx' cambia la porta usata dal programma. Al posto di xxxxx usare un numero intero >=10000 e <20000");
		LOGGER.info("   'reset-all' esegue una completa reinizializzazione del programma, cioe' cancella " +
				"il database dei firmware, i file temporanei usati e i download nelle cartelle: predefinita e personalizzata (completati e non)");
		LOGGER.info("   'delete-download-temp' esegue la cancellazione dei soli download temporanei, " +
				"sia nella cartella predefinita sia in quella personalizzata (se presente)");
		LOGGER.info("   'update-db' Cancella (per evitare problemi) e installa i nuovi database presenti sul server");
		LOGGER.info("   'update-software' Verifica e installa l'ultima versione del programma (solo se piu' recente di quella in uso)");
		LOGGER.info("   'help' apre questa guida :)");
	}

}
