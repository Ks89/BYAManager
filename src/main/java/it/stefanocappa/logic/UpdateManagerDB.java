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

import it.stefanocappa.fileutility.HttpFileDownload;
import it.stefanocappa.fileutility.Unzip;
import it.stefanocappa.gui.MainFrame;
import it.stefanocappa.gui.kcomponent.KColors;
import it.stefanocappa.gui.state.StateButton;
import it.stefanocappa.gui.state.StateLabel;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;

import it.stefanocappa.localization.Translator;
import it.stefanocappa.model.User;
import it.stefanocappa.notification.Notification;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import it.stefanocappa.time.TimeManager;
import it.stefanocappa.update.LinkServer;
import it.stefanocappa.update.database.FirmwareUpdates;

public final class UpdateManagerDB implements Observer{
	private static final Logger LOGGER = LogManager.getLogger(UpdateManagerDB.class);

	private static final String ZIPNAME = "lists.zip";
	private static final String IPSWLISTA = "ipswLista.txt";
	private static final String IPSWLISTANEW = "ipswLista_new.txt";
	private static final String IPSWLISTADOWNLOADED = "ipswLista_downloaded.txt";
	private static final String ITUNESLISTA = "iTunesLista.txt";
	private static final String ITUNESLISTANEW = "iTunesLista_new.txt";
	private static final String ITUNESLISTADOWNLOADED = "iTunesLista_downloaded.txt";

	private Path dataPath;
	private boolean updatedFirmwareList;
	private boolean updatedItunesList;

	private static UpdateManagerDB instance = new UpdateManagerDB();

	public static UpdateManagerDB getInstance() {
		return instance;
	}

	private UpdateManagerDB() {
		dataPath = User.getInstance().getDataPath();
		updatedFirmwareList = false;
		updatedItunesList = false;
	}

	public void prepareManualDbUpdate() throws IOException {
		if(!Files.exists(dataPath)) {
			throw new IOException("downloadPath non esistente");
		}

		//cancello ipswLista_new.txt per evitare problemi nelle fasi successive
		Files.deleteIfExists(dataPath.resolve(IPSWLISTANEW));

		// ora modifico la data di "ultima modifica" del database a 24 ore prima della data attuale, in questo
		// modo, per forza, il pogramma rileva un database vecchio e ne verifica nuove versioni
		this.impostaComeDaAggiornareDB(dataPath.resolve(IPSWLISTA));
	}

	private void extractZipAndClean(boolean isDownloaded) throws IOException {
		//estraggo il file zip contenente i vari txt del database nella stessa cartella dello zip
		Unzip.extract_with_zip4j(dataPath, ZIPNAME, dataPath.toString(), isDownloaded);
		
		//ora sposto i file estratti
		Unzip.moveFiles(Paths.get(dataPath.toString(),"lists"), dataPath, isDownloaded);
		
		//ora rimuovo tutte le cose temporanee e inutili
		Unzip.removeTempFilesAndListsFolder(dataPath, Paths.get(dataPath.toString(),"lists"), ZIPNAME);
	}

	/**
	 * Metodo che verifica gli aggiornamenti del database e se presenti si occupa anche di gestirne
	 * l'aggiornamento tramite metodi privati ausialiari.
	 * Attenzione: non serve notificare l'aggiornamento al programma perche' questo metodo e' eseguito
	 * prima di caricare la lista firmware, quindi ad ogni avvio viene verificato il database ed aggiornato
	 * questo garantisce che venga sempre caricato il txt piu' recente.
	 * L'unico caso in cui serve notificare e' quando il metodo cha scansiona l'xml in VerificaNuoviIpsw
	 * trova nuovi link. Questa situazione non e' risolvibile in automatico ma richiede il riavvio manuale del programma.
	 * Il metodo provvede a notificare all'utente che e' avvenuto l'aggiornamento del datbase, ma non avvia da solo
	 * la verifica di nuove versioni sul server Apple. Questo e' fatto dal metodo che chiama questo
	 */
	public void checkDbUpdates() throws IOException{
		LOGGER.info("checkDbUpdates() - Metodo avviato");

		//verifico se la cartella dati esiste
		if(!Files.isDirectory(dataPath)) {
			LOGGER.error("Errore, dataPath non e' una cartella");
			Notification.showNormalOptionPane("updateManagerDBDiskAccessError");
			LogicManager.getInstance().mettiPausaPreChiusura();
			System.exit(0);
		} 

		//sostituisco il database nuovo a quello vecchio, pero' solo
		//se l'esecuzione precedente avevo lasciato il programma in stato "riavvia per aggiornare".
		//maggiori spiegazioni sul metodo sono nella javadoc
		boolean updateFirmwareDbAfterRestartIsDone = this.replaceFirmwareDbAfterStart();
		boolean updateItunesDbAfterRestartIsDone = this.replaceItunesDbAfterStart();
		LOGGER.info("checkDbUpdates() - updateFirmwareDbAfterRestartIsDone= " + updateFirmwareDbAfterRestartIsDone);
		LOGGER.info("checkDbUpdates() - updateItunesDbAfterRestartIsDone= " + updateItunesDbAfterRestartIsDone);

		//ora verifico se e' il primo avvio del programma
		//se o il file firmware o itunes non esistono (vuol dire che prima non e' stata fatta neanche la
		//sostituzione, ma questo non importa, perche' e' il primo avvio)
		boolean primoAvvio = Files.notExists(dataPath.resolve(IPSWLISTA)) || Files.notExists(dataPath.resolve(ITUNESLISTA));
		LOGGER.info("checkDbUpdates() - primoAvvio= " + primoAvvio);

		if(primoAvvio) {
			//liste in zip ancora non presente nella cartella ByaManager
			//allora lo scarico dal server
			this.scaricaDb(dataPath.resolve(ZIPNAME), LinkServer.LINKZIP_KS, LinkServer.LINKZIP);

			//estraggo il file zip contenente i vari txt del database nella stessa cartella dello zip
			//e cancello dallo zip. Il parametro false indica che i file NON sono Downloaded, cioe' e' il primo avvio
			this.extractZipAndClean(false);

			//una volta scaricato il file ed estratti i db decremento le loro date di ultima modifica di 
			//2 giorni, in modo tale che siano sempre piu' vecchi
			//e che richieda obbligatoriamente un aggiornamento (subito dopo al suo download)
			//una volta aggiornato la data cambia e quindi non crea piu' problemi.
			//Questo serve per aggiornarlo con le ultime uscite del .xml, mentre per le versioni di iTunes non c'e' differenza.
			this.impostaComeDaAggiornareDB(dataPath.resolve(IPSWLISTA));
			this.impostaComeDaAggiornareDB(dataPath.resolve(ITUNESLISTA));
		} else {
			//se non e' il primo avvio scarico l'ipsw downloaded e l'itunes downlaoded dal server (estraendo lo zip)
			//scarico il database dal server, riciclando il metodo scaricaDB per ottenere lo zip
			this.scaricaDb(dataPath.resolve(ZIPNAME), LinkServer.LINKZIP_KS, LinkServer.LINKZIP);

			//estraggo e rimuovo lo zip
			//il parametro true indica che i file devono essere rinominati come _downloaded
			this.extractZipAndClean(true); 

			//e procedo con la verifica degli aggiornamenti in base al numero di righe
			//del file scaricato (se ne ha di piu' di quello attuale ha piu' file e quindi e' piu' recente)
			//questo metodo puo' lanciare un eccezione...se accedesse vedi il catch per gestire il problema ed avviare
			boolean deviAggiornare = (this.checkIfNeedUpdateDbBeforeFirmwareLoad() || this.checkIfNeedUpdateDbBeforeItunesLoad());
			LOGGER.info("checkDbUpdates() - deviaggiornare= " + deviAggiornare);

			//se devo aggiornare chiamo il metodo che cancella DB attuale e rinomina il downloaded in attuale
			if(deviAggiornare) {
				this.updateDbBeforeFirmwareLoad();
				this.updateDbBeforeItunesLoad();

				//qui il file downloaded e' diventato quello attuale ma con una data non corretta
				//cosi' la cambio in modo che la verifica dell'xml lo individui come da controllare
				//e verifichi anche l'aggiornamento
				this.impostaComeDaAggiornareDB(dataPath.resolve(IPSWLISTA));
				//						this.impostaComeDaAggiornareDB(PercorsiOS.getPercorsoDati() + "/" + ITUNESLISTA);

				//DA ORA SONO CERTO CHE IL CONTROLLO DELLA DATA+1GIORNO PORTERA' ALLA VERIFICA DELL'XML
				//quindi ora avviso l'utente che il rpgoramma andra' a verificare la presenza di altri
				//aggiornamenti dal server apple (version.xml)
				LOGGER.info("updateManagerDBNotificationAppleConnection");
				//Notification.showNormalOptionPane("updateManagerDBNotificationAppleConnection");
			} else {
				//altrimenti rimuovo i file downloaded scaricati poco fa perche' inutili
				Files.delete(dataPath.resolve(IPSWLISTADOWNLOADED));
				Files.delete(dataPath.resolve(ITUNESLISTADOWNLOADED));
				LOGGER.info("checkDbUpdates() - cancellazione downloaded inutili eseguita senza errori");
			}
		}
	}


	/**
	 * Chiama checkDbUpdates() per verifica l'esistenza di aggiornamenti a agire sui file del database salvati in locale
	 * Dopo esegue un procedura che richiedera' alcuni minuti per verificare gli aggiornamenti con apple e nel caso prepararli e 
	 * richiedere un riavvio per installarli.
	 * @throws IOException 
	 */
	public void updateDb() throws IOException {
//		ItunesUpdates itunesUpdates = new ItunesUpdates(dataPath);
		FirmwareUpdates firmwareUpdates = new FirmwareUpdates(dataPath);

		this.checkDbUpdates();
		
		//****** ARRIVATO QUI SONO SICURO DI AVERE L'ULTIMA VERSIONE DEL DATABASE .TXT
		//(SEMPRE CHE NON CI SIA L'ECCEZIONE, MA IN QUEL CASO SI FERMA TUTTO L'AGGIORNAMENTO)
		//NON E' INVECE CERTO CHE VERRA' CONTROLLATO L'XML. QUESTO VALE SOLO IN 2 CASI:
		// 1) IL DB E' PIU' VECCHIO DI UN GIORNO E ALLORA E' GIUSTO VERIFICARE GLI AGGIORNAMENTI DIRETTAMENTE DAL SERVER APPLE
		// 2) HO OTTENUTO UN DB AGGIORNATO DAL SERVER BYA O ALTERNATIVO QUINDI E' GIUSTO 
		//		VERIFICARE ULTERIORI AGGIORNAMENTI (DELL'ULTIMO MOMENTO) DIRETTAMENTE DAL SERVER APPLE

		//Avvio il thread che gestisce il download del xml e la verifica delle versioni di itunes 
		//ed eventualmente si occupa degli aggiornamenti
		LOGGER.info("updateDb() - Avvio i thread per l'agg dal server Apple");
//		itunesUpdates.addObserver(this);	
		firmwareUpdates.addObserver(this);
//		itunesUpdates.run();
		firmwareUpdates.run();
		LOGGER.info("updateDb() - Thread avviato");
	}

	private void updateDbBeforeItunesLoad() throws IOException {
		Path nowPath = dataPath.resolve(ITUNESLISTA);
		Path downloadedPath = dataPath.resolve(ITUNESLISTADOWNLOADED);

		LOGGER.info("updateDbBeforeFirmwareLoad() - File iTunesLista=" + nowPath.toString());
		LOGGER.info("updateDbBeforeFirmwareLoad() - File iTunesLista_downlaoded=" + downloadedPath.toString());

		if(Files.exists(nowPath) && Files.exists(downloadedPath)) {
			Files.delete(nowPath);
			Files.move(downloadedPath, dataPath.resolve(ITUNESLISTA)); //rinomina
			LOGGER.info("updateDbBeforeFirmwareLoad() - Database locale e 'downloaded' diversi, sostituzione eseguita");
		} else {
			LOGGER.error("updateDbBeforeFirmwareLoad() - Errore, i 2 database 'attuale' e 'downloaded' non sono entrambi presenti allo stesso momento per poter aggiornare");
		}
	}


	private void updateDbBeforeFirmwareLoad() throws IOException {
		Path nowPath = dataPath.resolve(IPSWLISTA);
		Path downloadedPath = dataPath.resolve(IPSWLISTADOWNLOADED);

		LOGGER.info("updateDbBeforeFirmwareLoad() - File ipswLista=" + nowPath.toString());
		LOGGER.info("updateDbBeforeFirmwareLoad() - File ipswLista_downlaoded=" + downloadedPath.toString());

		if(Files.exists(nowPath) && Files.exists(downloadedPath)) {
			Files.delete(nowPath);
			Files.move(downloadedPath, dataPath.resolve(IPSWLISTA)); //rinomina
			LOGGER.info("updateDbBeforeFirmwareLoad() - Database locale e 'downloaded' diversi, sostituzione eseguita");
		} else {
			LOGGER.error("updateDbBeforeFirmwareLoad() - Errore, i 2 database 'attuale' e 'downloaded' non sono entrambi presenti allo stesso momento per poter aggiornare");
		}
	}



	/*
	 * Metodo che verifica se aggiornare il database itunes in base al numero di righe di quello attuale e quello sul server.
	 */
	private boolean checkIfNeedUpdateDbBeforeItunesLoad() throws IOException {
		Path nowPath = dataPath.resolve(ITUNESLISTA);
		Path downloadedPath = dataPath.resolve(ITUNESLISTADOWNLOADED);

		LOGGER.info("verificaSeAggiornareDBPrimaCaricamentoFirmware() - File iTunesLista=" + nowPath.toString());
		LOGGER.info("verificaSeAggiornareDBPrimaCaricamentoFirmware() - File iTunesLista_downlaoded=" + downloadedPath.toString());

		//leggo i 2 database e conto il numero di righe
		int numRigheAttuale = this.contaNumeroRigheFile(dataPath.resolve(ITUNESLISTA));
		int numRigheScaricato = this.contaNumeroRigheFile(dataPath.resolve(ITUNESLISTADOWNLOADED));	

		//se quello scaricato ha piu' righe di quello attuale vuol dire che e' piu' aggiornato e quindi
		//devo aggiornare, altrimenti no.
		return numRigheScaricato > numRigheAttuale; 
	}


	/*
	 * Metodo che verifica se aggiornare il database firmware in base al numero di righe di quello attuale e quello sul server.
	 */
	private boolean checkIfNeedUpdateDbBeforeFirmwareLoad() throws IOException {
		Path nowPath = dataPath.resolve(IPSWLISTA);
		Path downloadedPath = dataPath.resolve(IPSWLISTADOWNLOADED);

		LOGGER.info("checkIfNeedUpdateDbBeforeFirmwareLoad() - File ipswLista=" + nowPath.toString());
		LOGGER.info("checkIfNeedUpdateDbBeforeFirmwareLoad() - File ipswLista_downlaoded=" + downloadedPath.toString());

		//leggo i 2 database e conto il numero di righe
		int numRigheAttuale = this.contaNumeroRigheFile(dataPath.resolve(IPSWLISTA));
		int numRigheScaricato = this.contaNumeroRigheFile(dataPath.resolve(IPSWLISTADOWNLOADED));	

		//se quello scaricato ha piu' righe di quello attuale vuol dire che e' piu' aggiornato e quindi
		//devo aggiornare, altrimenti no.
		return numRigheScaricato > numRigheAttuale; 
	}


	private int contaNumeroRigheFile(Path filePath) throws IOException{
		LOGGER.info("contaNumeroRigheFile() - Avviato metodo");
		int conteggioRighe = 0;

		try (
				BufferedReader br = Files.newBufferedReader(filePath, Charset.defaultCharset())
				) {
			String rigaAttuale = br.readLine();
			while(rigaAttuale!=null) {
				conteggioRighe++;
				rigaAttuale = br.readLine();
			}
		}
		LOGGER.info("contaNumeroRigheFile() - Terminato metodo");
		return conteggioRighe;
	}

	/**
	 * Metodo che non serve ad aggiornare nel vero senso della parola, ma serve ad applicare
	 * un aggiornamento datto nell'avvio precedente. Cioe' e' quel metodo che si occupa di
	 * rimuovere il vecchio database di itunes e rinominare quello nuovo, in modo che il programma lo legga
	 * e ci carichi i link.
	 * @return
	 * @throws IOException 
	 */
	private boolean replaceItunesDbAfterStart() throws IOException {
		Path oldPath = dataPath.resolve(ITUNESLISTA);
		Path newPath = dataPath.resolve(ITUNESLISTANEW);

		LOGGER.info("replaceItunesDbAfterStart() - File iTunesLista=" + oldPath.toString());
		LOGGER.info("replaceItunesDbAfterStart() - File iTunesLista_new=" + newPath.toString());

		if(Files.exists(oldPath) && Files.exists(newPath)) {
			Files.delete(oldPath);
			Files.move(newPath, oldPath); //rinomina
			LOGGER.info("updateDbBeforeFirmwareLoad() - Database locale e 'downloaded' diversi, sostituzione eseguita");
			return true;
		}
		LOGGER.info("replaceItunesDbAfterStart() - Non e' stato rilevato nessun database-updates di itunes da applicare");
		return false;
	}

	/**
	 * Metodo che non serve ad aggiornare nel vero senso della parola, ma serve ad applicare
	 * un aggiornamento datto nell'avvio precedente. Cioe' e' quel metodo che si occupa di
	 * rimuovere il vecchio database e rinominare quello nuovo, in modo che il programma lo legga
	 * e ci carichi i link.
	 * @return
	 * @throws IOException 
	 */
	private boolean replaceFirmwareDbAfterStart() throws IOException {
		Path oldPath = dataPath.resolve(IPSWLISTA);
		Path newPath = dataPath.resolve(IPSWLISTANEW);

		LOGGER.info("replaceFirmwareDbAfterStart() - File ipswLista=" + oldPath.toString());
		LOGGER.info("replaceFirmwareDbAfterStart() - File ipswLista_new=" + newPath.toString());

		if(Files.exists(oldPath) && Files.exists(newPath)) {
			Files.delete(oldPath);
			Files.move(newPath, oldPath); //rinomina
			LOGGER.info("updateDbBeforeFirmwareLoad() - Database locale e 'downloaded' diversi, sostituzione eseguita");
			return true;
		}
		LOGGER.info("replaceFirmwareDbAfterStart() - Non e' stato rilevato nessun database-updates degli ipsw da applicare");
		return false;
	}


	private void scaricaDb(Path pathFile, String linkFileServer, String linkAlternativoFileServer) {
		try {
			//scarica il database
			HttpFileDownload.httpFileDownload(pathFile, linkFileServer);
			LOGGER.info("sostituisciDatabase() - Avviato download database");
		} catch (IOException | URISyntaxException e) {
			LOGGER.error("sostituisciDatabase() - Eccezione, ma e' tutto ok, per ora=" , e);
			LOGGER.info("sostituisciDatabase() - fallito download da bya, provo sul server alternativo");
			try {
				//scarica il database dal server alternativo
				HttpFileDownload.httpFileDownload(pathFile, linkAlternativoFileServer);
			} catch (IOException | URISyntaxException e1) {
				LOGGER.error("sostituisciDatabase() - Eccezione=" , e1);
			}
		}
	}

	private void impostaComeDaAggiornareDB(Path path) throws IOException {
		TimeManager timeManager = TimeManager.getInstance() ;
		Files.setLastModifiedTime(path, FileTime.fromMillis(timeManager.getTempoSistema() - timeManager.incrementaOre(24)));
	}

	/**
	 * Metodo per aggiornare il database, scaricando i jar, verificando gli SHA dei jar scaricati e se
	 * combaciano con la lista dsul server nel file di controllo, modifica le label del programma
	 * ed esegue l'updater. Nel caso in cui l'aggiornamento sia stato eseguito correttamente e che
	 * quindi al riavvio successivo risulti aggiornato, cancella l'updater.
	 * @param aggiornamentoDatabase Un boolean, se true deve essere aggiornato, altrimenti no.
	 * @throws IOException 
	 */
	private void aggiornaDB(boolean aggiornamentoDatabase) throws IOException {
		//aggiornamentoDatabase false: il database e' aggiornato, altrimenti no
		if(!aggiornamentoDatabase) {
			LOGGER.info("setAggiornamentoDatabase() - aggiornato");
			StateLabel.getAggDatabaseLabel().setForeground(KColors.getVerde());
			StateLabel.getAggDatabaseLabel().setText("  " + Translator.getText("databaseLabelAgg"));

			StateButton.activateAfterUpdateDb(true);
		} else {
			//la sostituzione del db sara' fatta al prossimo avvio
			//per ora notifico solo che bisogna riavviare per aggiornare
			Notification.showNormalOptionPane("checkDBUpdatesRestartToUpdate");
			
			LOGGER.info("setAggiornamentoDatabase() - riavvia");
			StateLabel.getAggDatabaseLabel().setForeground(Color.WHITE);
			StateLabel.getAggDatabaseLabel().setText("  " + Translator.getText("databaseLabelRiavvia"));	
			MainFrame.getInstance().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			StateLabel.getAggDatabaseLabel().setForeground(Color.WHITE);
		}

		StateLabel.getUltimoAggDatabaseLabel().setText("   " + this.ottieniDataUltimoAggDB());
	}


	private String ottieniDataUltimoAggDB() throws IOException {
		return (new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")).format(new Date(Files.getLastModifiedTime(dataPath.resolve(IPSWLISTA)).toMillis()));
	}

	//posso toglierlo se metto l'update nel downloadmanager e la registrazione dell'observer sempre in downlaodmanager
	//passandogli come parametro il riferimento a questa classe qui.
	@Override
	public void update(Observable o, Object arg1) {
//		if(o instanceof ItunesUpdates) {
//			ItunesUpdates itunesUpdates = (ItunesUpdates)o;
//			updatedItunesList = itunesUpdates.isAggiorna();
//			itunesUpdates.deleteObserver(this);
//		}
		if(o instanceof FirmwareUpdates) {
			FirmwareUpdates firmwareUpdates = (FirmwareUpdates)o;
			updatedFirmwareList = firmwareUpdates.isDbUpdated();
			firmwareUpdates.deleteObserver(this);
		}
		
		try {
			this.aggiornaDB(updatedItunesList || updatedFirmwareList);
		} catch (IOException e) {
			LOGGER.error("update() - IOException= " + e);
		}
	}
}
