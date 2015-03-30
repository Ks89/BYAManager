//package logica;
//

/*
Copyright 2015 Stefano Cappa

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

//import static org.junit.Assert.*;
//
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.Date;
//
//import model.User;
//
//import org.apache.commons.codec.digest.DigestUtils;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//
//import fileutility.FileList;
//
//import time.TimeManager;
//
///**
// *	Classe di test che testa 2 metodi pubblici. Attenzione il metodo updateDb() non viene testato direttamente, tanto
// *	non serve, perche' il test della classe che si occupa di fare l'aggiornamento dal server Apple e' testata a parte.
// */
//public class UpdateManagerDBTest {
//
//	private static final String ZIPNAME = "lists.zip";
//	private static final String IPSWLISTA = "ipswLista.txt";
//	private static final String IPSWLISTANEW = "ipswLista_new.txt";
//	private static final String IPSWLISTADOWNLOADED = "ipswLista_downloaded.txt";
//	private static final String ITUNESLISTA = "iTunesLista.txt";
//	private static final String ITUNESLISTADOWNLOADED = "iTunesLista_downloaded.txt";
//	/**
//	 * @uml.property  name="dataPath"
//	 */
//	private Path dataPath;
//	/**
//	 * @uml.property  name="zipPath"
//	 */
//	private Path zipPath;
//	/**
//	 * @uml.property  name="ipswListaNew"
//	 */
//	private Path ipswListaNew;
//	/**
//	 * @uml.property  name="ipswListaDownloaded"
//	 */
//	private Path ipswListaDownloaded;
//	/**
//	 * @uml.property  name="ipswLista"
//	 */
//	private Path ipswLista;
//	/**
//	 * @uml.property  name="itunesLista"
//	 */
//	private Path itunesLista;
//	/**
//	 * @uml.property  name="itunesListaDownloaded"
//	 */
//	private Path itunesListaDownloaded;
//
//	/**
//	 * @throws java.lang.Exception
//	 */
//	@Before
//	public void setUp() throws Exception {
//		dataPath = User.getInstance().getDataPath();
//		zipPath = dataPath.resolve(ZIPNAME);
//		ipswListaNew = dataPath.resolve(IPSWLISTANEW);
//		ipswListaDownloaded = dataPath.resolve(IPSWLISTADOWNLOADED);
//		ipswLista = dataPath.resolve(IPSWLISTA);
//		itunesListaDownloaded = dataPath.resolve(ITUNESLISTADOWNLOADED);
//		itunesLista = dataPath.resolve(ITUNESLISTA);
//	}
//
//	/**
//	 * Test method for {@link logica.UpdateManagerDB#prepareManualDbUpdate()}.
//	 */
//	@Test
//	public void testPrepareManualDbUpdate1() {
//		//caso 1 e 2
//		try {
//			//caso 1: NON esiste IPSWLISTANEW e lo voglio cancellare
//			FileList.deleteFilesInList(dataPath); //pulisco tutto
//			Files.createFile(ipswLista); //creo il file vuoto ipswLista.txt perche' altrimenti crasha unpo dei metodi privati interni
//			UpdateManagerDB.getInstance().prepareManualDbUpdate();
//			Files.deleteIfExists(ipswListaNew);  // mi assicuro che il file non esista
//			assertFalse(Files.exists(ipswListaNew)); // il file esisteva e dopo la cancellazione deve non esistere piu'
//
//			//caso 2: esiste IPSWLISTANEW e lo voglio cancellare
//			FileList.deleteFilesInList(dataPath); //pulisco tutto
//			Files.createFile(ipswListaNew); //creo il file
//			Files.createFile(ipswLista); //creo il file vuoto ipswLista.txt perche' altrimenti crasha unpo dei metodi privati interni
//			UpdateManagerDB.getInstance().prepareManualDbUpdate();
//			assertFalse(Files.exists(ipswListaNew)); // il file NON esisteva e dopo la cancellazione deve non esistere ancora
//		} catch (IOException e) {
//			fail("Eccezione = " + e.toString());
//		}
//	}
//
//	/**
//	 * Test method for {@link logica.UpdateManagerDB#prepareManualDbUpdate()}.
//	 */
//	@Test
//	public void testPrepareManualDbUpdate2() {
//		//caso 3: NON esiste IPSWLISTA
//		FileList.deleteFilesInList(dataPath); //pulisco tutto
//		try {
//			UpdateManagerDB.getInstance().prepareManualDbUpdate();
//			fail("Il test e' fallito perche' non si puo' ottenere la data di un file che non esiste, l'istruzione sopra" +
//					"avrebbe dovuto lanciare una eccezione");
//		} catch (IOException e) {
//			assertTrue(true); //il test e riuscito perche' ha generato un eccezione
//		}
//	}
//
//	/**
//	 * Test method for {@link logica.UpdateManagerDB#prepareManualDbUpdate()}.
//	 */
//	@Test
//	public void testPrepareManualDbUpdate3() {
//		//caso 4: esiste IPSWLISTA e voglio vedere se la data di ultima modifica e' stata settata correttamente
//		try {
//			FileList.deleteFilesInList(dataPath); //pulisco tutto
//			Files.createFile(ipswLista); //creo il file
//			TimeManager dataSistema = TimeManager.getInstance();
//			UpdateManagerDB.getInstance().prepareManualDbUpdate();
//			//se la data di ultima modifica e' uguale a quella di sistema meno 24 ore
//			//lo scopo di cio' e che togliendo 24 ore il programma rileva il file come non aggiornato e chiama i metodi del caso
//			//in questo test io controllo solo che effettivamente la procedura funzioni
//			Date systemDateLess24Hours = new Date(dataSistema.getTempoSistema() - dataSistema.incrementaOre(24));
//			Date changedDate = new Date(Files.getLastModifiedTime(ipswLista).toMillis());
//			assertEquals(systemDateLess24Hours.toString(),changedDate.toString());
//		} catch (IOException e) {
//			fail("Eccezione = " + e.toString());
//		}
//	}
//
//
//	/**
//	 * Test method for {@link logica.UpdateManagerDB#checkDbUpdates()}.
//	 */
//	@Test
//	public void testCheckDbUpdates1() {
//		//caso 1: Primo avvio, il metodo scarica lo zip, lo estrae,
//		//lo rimuove e imposta con 24 ore in meno le date di ultima modifica dei file contenuti
//		try {
//			FileList.deleteFilesInList(dataPath); //pulisco tutto per simulare primo avvio
//			UpdateManagerDB.getInstance().checkDbUpdates();
//			TimeManager timeManager = TimeManager.getInstance();
//			Date systemDateLess24Hours = new Date(timeManager.getTempoSistema() - timeManager.incrementaOre(24));
//			Date changedDateIpswLista = new Date(Files.getLastModifiedTime(ipswLista).toMillis());
//			Date changedDateItunesLista = new Date(Files.getLastModifiedTime(itunesLista).toMillis());
//
//			assertEquals(systemDateLess24Hours.toString(),changedDateIpswLista.toString());
//			assertEquals(systemDateLess24Hours.toString(),changedDateItunesLista.toString());
//			assertFalse(Files.exists(zipPath));
//		} catch (IOException e) {
//			fail("Eccezione = " + e.toString());
//		}
//	}
//
//	/**
//	 * Test method for {@link logica.UpdateManagerDB#checkDbUpdates()}.
//	 */
//	@Test
//	public void testCheckDbUpdates2() {
//		//caso 2: Non e' piu' il primo avvio (e non devo aggiornare nulla di lasciato in sospeso) e i database sono aggiornati entrambi
//		//in questo test simulo questa situazione cosi':
//		//- Cancello tutto e chiamo metodo per primoavvio
//		//- Ora inizia il caso vero e proprio, richiamando ancora il metodo in modo che non sia piu' il primo avvio 
//		//il metodo scarica lo zip, lo estrae ma QUETA VOLTA i file hanno il nome DOWNLOADED
//		//lo rimuove e verifica se uno dei 2 database e' da aggiornare contando il numero di righe
//		//il caso che test e' quello in cui non devo aggiornare nulla (poiche' sul server tengo sempre i file piu' recenti)
//		//se questo test FALLISSE e' perche' sul server i file sono da aggiornare
//		FileList.deleteFilesInList(dataPath); //pulisco tutto per simulare primo avvio
//		try {
//			UpdateManagerDB.getInstance().checkDbUpdates(); //primo avvio
//			UpdateManagerDB.getInstance().checkDbUpdates(); //NON E' PIU' IL PRIMO AVVIO
//
//			assertFalse(Files.exists(ipswListaDownloaded));
//			assertFalse(Files.exists(itunesListaDownloaded));
//			assertTrue(Files.exists(ipswLista));
//			assertTrue(Files.exists(itunesLista));
//		} catch (IOException e) {
//			fail("Eccezione = " + e.toString());
//		}
//	}
//
//	/**
//	 * Test method for {@link logica.UpdateManagerDB#checkDbUpdates()}.
//	 */
//	@Test
//	public void testCheckDbUpdates3() {
//		//caso 3: Non e' piu' il primo avvio (e non devo aggiornare nulla di lasciato in sospeso) e uno dei database NON e' aggiornato
//		//in questo test simulo questa situazione cosi':
//		//- Cancello tutto e copio i database dalle risorse di test nella cartella dati
//		//- Ora inizia il caso vero e proprio, chiamo il metodo e lascio che si accorga di un aggiornamento dei database
//		//il caso che test e' quello in cui DEVO aggiornare (poiche' apposta prendo dei database vecchi dalla cartella dei test del progetto)
//
//		FileList.deleteFilesInList(dataPath); //pulisco tutto
//		//ora copio i file dalla cartella resources di test al dataPath
//		try {
//			Files.copy(Paths.get("", "src", "test", "resources", IPSWLISTA),ipswLista);
//			Files.copy(Paths.get("", "src", "test", "resources", ITUNESLISTA),itunesLista);
//
//			UpdateManagerDB.getInstance().checkDbUpdates(); //lancio metodo vero e proprio
//
//			TimeManager timeManager = TimeManager.getInstance();
//			Date systemDateLess24Hours = new Date(timeManager.getTempoSistema() - timeManager.incrementaOre(24));
//			Date changedDateIpswLista = new Date(Files.getLastModifiedTime(ipswLista).toMillis());
//
//			assertFalse(Files.exists(ipswListaDownloaded));
//			assertFalse(Files.exists(itunesListaDownloaded));
//			assertTrue(Files.exists(ipswLista));
//			assertTrue(Files.exists(itunesLista));
//			assertEquals(systemDateLess24Hours.toString(),changedDateIpswLista.toString()); //solo su ipsw, perche' itunes non aggiorna solo in base alla data ma verifica sempre gli update con php
//		} catch (IOException e) {
//			fail("Impossibile copiare in dataPath i database 'invecchiati' per fare il test. Eccezione = " + e);
//		} finally {
//			FileList.deleteFilesInList(dataPath); //pulisco tutto
//		}
//	}
//	
//	/**
//	 * Test method for {@link logica.UpdateManagerDB#checkDbUpdates()}.
//	 */
//	@Test
//	public void testCheckDbUpdates4() {
//		//caso 4: Non e' piu' il primo avvio (e non devo aggiornare nulla di lasciato in sospeso) e uno dei database e' piu' recente di quello scaricato dal server
//		//in questo test simulo questa situazione cosi':
//		//- Cancello tutto e copio i database dalle risorse di test nella cartella dati (ipswLista ha tantissime righe in piu' apposta)
//		//- Ora inizia il caso vero e proprio, chiamo il metodo e lascio che si accorga di un database locale piu' recente
//		//il caso che test e' quello in cui NON devo aggiornare (poiche' apposta prendo dei database molto piu' recenti con ipsw FAKE dalla cartella dei test del progetto)
//
//		FileList.deleteFilesInList(dataPath); //pulisco tutto
//		//ora copio i file dalla cartella resources di test al dataPath
//		try {
//			Files.copy(Paths.get("", "src", "test", "resources", "ipswLista_fake.txt"),ipswLista);
//			Files.copy(Paths.get("", "src", "test", "resources", ITUNESLISTA),itunesLista);
//
//			UpdateManagerDB.getInstance().checkDbUpdates(); //lancio metodo vero e proprio
//
//			assertFalse(Files.exists(ipswListaDownloaded));
//			assertFalse(Files.exists(itunesListaDownloaded));
//			assertTrue(Files.exists(ipswLista));
//			assertTrue(Files.exists(itunesLista));
//			
//			String shaTestFile = DigestUtils.sha512Hex(new FileInputStream(Paths.get("", "src", "test", "resources", "ipswLista_fake.txt").toString()));
//			String shaFileInDataPath = DigestUtils.sha512Hex(new FileInputStream(ipswLista.toString()));
//			
//			//mi assicuro anche gli il file ipswLista sia uguale (con sha1) a quello nella cartella delle risorse per i test, 
//			//poiche' in questo caso e' piu' recente di quelli su internet e non deve fare nulla
//			assertEquals(shaTestFile, shaFileInDataPath);
//		} catch (IOException e) {
//			fail("Impossibile copiare in dataPath i database 'invecchiati' per fare il test. Eccezione = " + e);
//		} finally {
//			FileList.deleteFilesInList(dataPath); //pulisco tutto
//		}
//	}
//	
//	/**
//	 * Test method for {@link logica.UpdateManagerDB#updateDb()}.
//	 */
//	@Test
//	public void testUpdateDb() {
//		try {
//			UpdateManagerDB.getInstance().checkDbUpdates();
//			UpdateManagerDB.getInstance().updateDb();
//			assertTrue(true);
//		} catch (IOException e) {
//			fail("test fallito perch'e lanciata eccezione");
//		}
//	}
//	
//	/**
//	 * @throws java.lang.Exception
//	 */
//	@After
//	public void tearDown() throws Exception {
//		FileList.deleteFilesInList(dataPath); //pulisco tutto
//	}
//}
