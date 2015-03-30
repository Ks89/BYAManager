///*
//Copyright 2011-2015 Stefano Cappa
//
//Licensed under the Apache License, Version 2.0 (the "License");
//you may not use this file except in compliance with the License.
//You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
//Unless required by applicable law or agreed to in writing, software
//distributed under the License is distributed on an "AS IS" BASIS,
//WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//See the License for the specific language governing permissions and
//limitations under the License.
//*/
//
//package update.database;
//
//import java.io.BufferedReader;
//import java.io.BufferedWriter;
//import java.io.IOException;
//import java.net.URISyntaxException;
//import java.nio.charset.Charset;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Observable;
//
//import org.apache.log4j.Logger;
//
//import update.LinkServer;
//import exception.UpdateException;
//import fileutility.HttpFileDownload;
//
//public class ItunesUpdates extends Observable implements Runnable {
//	private static final Logger LOGGER = Logger.getLogger(ItunesUpdates.class);
//	private static final String UNDERSCORE3 = "___";
//	private static final String ITUNESLIST = "iTunesLista.txt";
//	private static final String ITUNESLISTNEW = "iTunesLista_new.txt";
//	private static final String NEWITUNESTXT = "newItunes.txt";
//
//	/**
//	 * @uml.property  name="iTunesListFromLocalDbToWrite"
//	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="java.lang.String"
//	 */
//	private List<String> iTunesListFromLocalDbToWrite = null;
//
//	/**
//	 * @uml.property  name="dataPath"
//	 */
//	private Path dataPath;
//	/**
//	 * @uml.property  name="aggiornatoDatabase"
//	 */
//	private boolean aggiornatoDatabase = false;
//
//	public ItunesUpdates(Path dataPath) {
//		this.dataPath = dataPath;
//	}
//
//	public void updateItunesDb() throws UpdateException, IOException, URISyntaxException {
//		//leggo il file scaricato e carico le versioni rilevate in un ArrayList di string validando ogni elemento
//		//passo l'array al metodo che prepara le versioni alla scrittura, cioe' mette in lista solo le versioni nuove
//		//inoltre il metodo prepareItunesVersionToWrite() si occupa anche di aggiungere a iTunesListFromLocalDbToWrite (variabile globale)
//		//eventuali nuove versioni di itunes ottenute col metodo parseHtmlResponseOfItunesVersionFromApple()
//		//restituisce un boolean per dire se ci sono nuove versioni o no
//		//messo su 2 righe per migliorare la leggiblita'
//		List<String> resultArrayList = this.parseHtmlResponseOfItunesVersionFromApple();
//		boolean thereAreNewItunesVersion = this.prepareItunesVersionToWrite(resultArrayList); 
//
//
//		//se i metodi sopra hanno avuto problemi non devo nemmeno eseguire questo, quindi controllo
//		//che la lista delle versioni di iTunes da scrivere sia !=null per salvare, in alternativa non
//		//faccio nulla se non avvisare del problema nel logger. Ovviamente devo ancore assicurarmi che ci siano nuove
//		//versioni rilevate, altrimenti non ha senso notificare l'aggiornamento e richiedere un riavvio.
//		if(iTunesListFromLocalDbToWrite!=null && thereAreNewItunesVersion) {
//			this.salvaTxtItunesVersion();
//			aggiornatoDatabase = true;
//			LOGGER.info("run() - Aggiornamento DB eseguito, necessario riavvio per installare le nuove versioni");
//		} else {
//			LOGGER.info("run() - Nessun aggiornamento DB disponibile, il tuo Db itunes e' gia' aggiornato");
//		}
//	}
//
//	@Override
//	public void run() {
//		try {
//			//scarico il file con le nuovi versioni di iTunes richiamando il file .php sul server ks89
//			HttpFileDownload.httpFileDownload(dataPath.resolve(NEWITUNESTXT), LinkServer.LINKPHPITUNES);
//
//			this.updateItunesDb(); //aggiorno itunes db (il try e' individuale perche' non deve crashare anche l'update firmware in caso di problemi)
//		} catch(IOException | URISyntaxException e) {
//			LOGGER.error("run() - IOException|URISyntaxException",e);
//		} catch (UpdateException e) {
//			LOGGER.error("run() - UpdateExcetpion", e);
//		}
//		stateChanged();
//		LOGGER.info("run() - Terminato metodo");
//	}
//
//	/**
//	 * Metodo che ottiene le versioni di iTunes da Apple passando per il file .php.
//	 * @return String[] di numero righe della pagina html letta (dovrebbe essere sempre 3)
//	 * @throws URISyntaxException
//	 * @throws IOException
//	 */
//	public List<String> parseHtmlResponseOfItunesVersionFromApple() throws URISyntaxException, IOException, UpdateException {
//		LOGGER.info("parseHtmlResponseOfItunesVersionFromApple() - Metodo avviato");
//		String[] result = null;
//
//		if(!Files.isReadable(dataPath.resolve(NEWITUNESTXT))) {
//			LOGGER.warn("parseHtmlResponseOfItunesVersionFromApple() - Impossibile eseguire il metodo poiche' non e' stato possibile leggere " +
//					"il file con le versioni di iTunes sul server Apple"); 
//			throw new UpdateException(UpdateException.Causa.IMPOSSIBLETOREADNEWITUNESVERSIONFROMAPPLE);
//		}
//
//		try (
//				BufferedReader br = Files.newBufferedReader(dataPath.resolve(NEWITUNESTXT), Charset.defaultCharset());
//				) {
//
//			String entireHtmlFile = br.readLine(); //leggo tutto il file e lo metto in entireHtmlFile
//
//			if(entireHtmlFile!=null && entireHtmlFile.contains("<br />")) { 
//				//col trim rimuovo eventuali spazi bianchi (visto che succede in questo caso)
//				//se nel file non fosse corretto
//				result = entireHtmlFile.trim().split("<br />"); 
//			} else {
//				Files.delete(dataPath.resolve(NEWITUNESTXT));
//				throw new UpdateException(UpdateException.Causa.IMPOSSIBLETOREADNEWITUNESVERSIONFROMAPPLE);
//			}
//			Files.delete(dataPath.resolve(NEWITUNESTXT));
//			LOGGER.info("parseHtmlResponseOfItunesVersionFromApple() - Nessun problema durante la cancellazione di newItunes.txt");
//		}
//
//		if(result==null) {
//			throw new UpdateException(UpdateException.Causa.IMPOSSIBLETOREADNEWITUNESVERSIONFROMAPPLE);
//		} else {
//			if(result.length <= 0) {
//				throw new UpdateException(UpdateException.Causa.IMPOSSIBLETOREADNEWITUNESVERSIONFROMAPPLE);
//			}
//		}
//
//		//sono certo che result sia !=null e >0
//		return this.validateSingleItunesVersionFromApple(result);
//
//	}
//
//
//	/**
//	 * Metodo che riceve una riga del file html ottenuto dal php e verifica se rispetta la struttura del db di itunes.
//	 * @param itunesVersionFromHtmlFile
//	 * @throws UpdateException
//	 */
//	private List<String> validateSingleItunesVersionFromApple(String[] result) {
//		List<String> resultArrayList = new ArrayList<String>();
//		String[] splitted;
//
//		for(String newRow : result) {
//			try {
//				if(newRow==null || newRow.split(UNDERSCORE3).length!=4) {
//					throw new UpdateException(UpdateException.Causa.ITUNESROWPROBLEM);
//				} 
//
//				splitted = newRow.split(UNDERSCORE3);
//				if(!(splitted[1].toUpperCase().equals("NONPRESENTE") || splitted[1].equals("0") || splitted[1].equals("32") || splitted[1].equals("64")) || !linkToFileIsValid(splitted[2]) /* || !sha1ItunesIsValid(splitted[3])*/) {
//					throw new UpdateException(UpdateException.Causa.ITUNESROWFORMAT);
//				}			
//
//				resultArrayList.add(newRow);
//			} catch (UpdateException e) {
//				LOGGER.error("parseHtmlResponseOfItunesVersionFromApple() - Errore in riga '" + newRow +
//						"' del file con le nuove versioni di itunes. Non sara' aggiunta al database in locale" , e); 
//			}
//		}
//
//		return resultArrayList;
//	}
//
//	/**
//	 * Metodo che legge il db locale installato sul pc/mac al momento dell'avvio e carica ogni riga 
//	 * nella lista 'new' delle versioni di iTunes.
//	 * @return La lista "iTunesListFromLocalDbNew" con solo le versioni di iTunes lette all'avvio (locale).
//	 * @throws IOException
//	 */
//	private List<String> readiTunesLocalDb() throws IOException {
//		List<String> iTunesListFromLocalDbNew = new ArrayList<String>();
//
//		if(!Files.isReadable(dataPath.resolve(ITUNESLIST))) {
//			LOGGER.warn("readiTunesLocalDb() - Impossibile eseguire il metodo poiche' non e' stato possibile " +
//					"leggere il file con TUTTE le versioni di iTunes in LOCALE"); 
//			return iTunesListFromLocalDbNew;
//		}
//
//		try (	
//				BufferedReader br = Files.newBufferedReader(dataPath.resolve(ITUNESLIST), Charset.defaultCharset());
//				) {
//			String rigaAttuale = br.readLine();
//			while(rigaAttuale != null) {
//				iTunesListFromLocalDbNew.add(rigaAttuale);
//				rigaAttuale = br.readLine();
//			}
//		}
//		return iTunesListFromLocalDbNew;
//	}
//
//
//	/** 
//	 * Metodo che aggiunge alla lista da scrivere sul file col 
//	 * db definitivo e aggiornato le ventuali nuove versioni di iTunes.
//	 * @param iTunesVersionFromApple String con le versioni di iTunes scaricate direttamente da Apple attraverso il .php.
//	 * @return 
//	 * @throws IOException
//	 */
//	public boolean prepareItunesVersionToWrite(List<String> iTunesVersionFromApple) throws UpdateException, IOException {
//		int numOfItunesVersionBeforeUpdate;
//
//		LOGGER.info("prepareiTunesVersionToWrite() - metodo avviato");
//
//		if(iTunesVersionFromApple==null || iTunesVersionFromApple.size()==0) {
//			LOGGER.warn("prepareItunesVersionToWrite() - Impossibile eseguire il metodo poiche' non e' stato possibile scaricare " +
//					"leggere il file con le versioni di iTunes sul server Apple"); 
//			throw new UpdateException(UpdateException.Causa.IMPOSSIBLETOREADNEWITUNESVERSIONFROMAPPLE);
//		}
//
//		//leggo il database in locale delle versioni di itunes (quello gia' scaricato) e lo carico nell'arraylist
//		this.iTunesListFromLocalDbToWrite = this.readiTunesLocalDb();
//
//		numOfItunesVersionBeforeUpdate = iTunesListFromLocalDbToWrite.size();		
//
//		//se non e' riuscito a leggerlo la dimensione dell'arratlist e' 0 allora lancia l'eccezione di aggiornamento
//		if(numOfItunesVersionBeforeUpdate==0) {
//			throw new UpdateException(UpdateException.Causa.IMPOSSIBLETOREADITUNESLOCALDB);
//		}
//
//		//aggiongo a iTunesListFromLocalDbToWrite eventuali nuove versioni di Itunes prelevate da iTunesVersionFromApple
//		//nel caso in cui non siano presenti gia' nel database locale
//		this.addNewVersionInListToWrite(iTunesVersionFromApple);
//
//		//ora controllo se ho aggiunto delle versioni nuove alla lista semplicemente contando gli elementi
//		//in questo caso controllo se il numero di elementi e' aumentato rispetto a quello prima dell'update
//		//faccio un return con il risulato in modo che possa stabilire se serve risalvare il file di itunes oppure no
//		//il fatto di rilasvarlo impone poi di riavviare il rpogramma per poter caricare quello nuovo
//		return (iTunesListFromLocalDbToWrite.size() > numOfItunesVersionBeforeUpdate);
//	}
//
//
//	private void addNewVersionInListToWrite(List<String> iTunesVersionFromApple) {
//		boolean trovato = false;
//		//scansione le nuove versioni del php (so gia' che sono tutte valide)
//		for(String lastiTunesVersion : iTunesVersionFromApple) { 
//
//			//scansione le versioni gia' presenti nel database locale
//			for(String fileRow : iTunesListFromLocalDbToWrite) {
//
//				//faccio if solo su piattaforma e link, non su versione e hash perche' sul serve apple capita spesso che cambino piu' volte
//				//e nel peggiore dei casi potrebbero essere entrambi NONPRESENTE
//				if(fileRow.contains(lastiTunesVersion.split(UNDERSCORE3)[1]) && fileRow.contains(lastiTunesVersion.split(UNDERSCORE3)[2])) {
//					trovato = true;
//					LOGGER.info("prepareiTunesVersionToWrite() - Trovata nuova versione iTunes: " + lastiTunesVersion);
//				}
//			}
//
//			if(!trovato) {
//				iTunesListFromLocalDbToWrite.add(lastiTunesVersion);
//			}
//			trovato = false;
//		}
//	}
//
//	public boolean linkToFileIsValid(String link) {
//		return (link.startsWith("http://") && (link.endsWith(".dmg") || link.endsWith(".exe")));
//	}
//
//	/**
//	 * Metodo che salva il file finale con le versioni di iTunes unite anche agli eventuali aggiornamenti
//	 * fatti da Apple e ancora non inseriti nello zip coi database.
//	 * @throws IOException
//	 */
//	private void salvaTxtItunesVersion() throws IOException{
//		LOGGER.info("salvaTxtItunesVersion() - Avviato metodo");
//		//ora scrivo tutte la lista di firmware (sotto forma di righe del file) sul nuovo file _new.txt
//
//		try (
//				BufferedWriter out = Files.newBufferedWriter(dataPath.resolve(ITUNESLISTNEW), Charset.defaultCharset());
//				) {
//			for(int i=0;i<iTunesListFromLocalDbToWrite.size();i++) {
//				//ora leggo il file appena salvato fino alla fine.
//				if(i==iTunesListFromLocalDbToWrite.size()-1) {
//					out.write(iTunesListFromLocalDbToWrite.get(i));
//				} else {
//					out.write(iTunesListFromLocalDbToWrite.get(i) + "\n");
//				}
//			}
//			LOGGER.info("salvaTxtItunesVersion() - Terminato metodo");
//		}
//	}
//
//	public boolean isAggiorna() {
//		return aggiornatoDatabase;
//	}
//
//	/**
//	 * Metodo per notificare un cambiamento all'observer, cioe' a #UpdateManagerDB.
//	 */
//	public void stateChanged() {
//		setChanged();
//		notifyObservers();
//	}
//}
