//package logica;

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
//import java.io.IOException;
//import java.net.URISyntaxException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.ArrayList;
//import java.util.List;
//
//import model.User;
//
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//
//import update.LinkServer;
//import update.database.ItunesUpdates;
//
//import exception.UpdateException;
//import fileutility.FileList;
//import fileutility.HttpFileDownload;
//
//public class ItunesUpdatesTest {
//	private static final String ITUNESLISTA = "iTunesLista.txt";
//	private static final String ITUNESLISTADOWNLOADED = "iTunesLista_downloaded.txt";
//	private static final String NEWITUNESTXT = "newItunes.txt";
//	/**
//	 * @uml.property  name="dataPath"
//	 */
//	private Path dataPath;
//	/**
//	 * @uml.property  name="newitunestxt"
//	 */
//	private Path newitunestxt;
//	/**
//	 * @uml.property  name="itunesLista"
//	 */
//	private Path itunesLista;
//	/**
//	 * @uml.property  name="itunesListaDownloaded"
//	 */
//	private Path itunesListaDownloaded;
//	/**
//	 * @uml.property  name="itunesUpdates"
//	 * @uml.associationEnd  
//	 */
//	private ItunesUpdates itunesUpdates;
//
//	@Before
//	public void setUp() throws Exception {
//		dataPath = User.getInstance().getDataPath();
//		newitunestxt = dataPath.resolve(NEWITUNESTXT);
//		itunesListaDownloaded = dataPath.resolve(ITUNESLISTADOWNLOADED);
//		itunesLista = dataPath.resolve(ITUNESLISTA);
//		itunesUpdates = new ItunesUpdates(dataPath);
//	}
//
//	/**
//	 * Test method for {@link update.database.ItunesUpdates#parseHtmlResponseOfItunesVersionFromApple()}.
//	 */
//	@Test
//	public final void testParseHtmlResponseOfItunesVersionFromApple1() {
//		//caso 1: tutto ok, scarico file dal server e testo metodo
//		try {
//			Files.deleteIfExists(newitunestxt);
//			HttpFileDownload.httpFileDownload(newitunestxt, LinkServer.LINKPHPITUNES);
//
//			List<String> result = itunesUpdates.parseHtmlResponseOfItunesVersionFromApple();
//
//			assertTrue(result.size()>0);
//			this.testResultArray(result);
//
//			assertFalse(Files.exists(newitunestxt)); //perche' a fine metodo questo file viene cancellato
//		} catch (UpdateException | URISyntaxException | IOException e) {
//			fail("Eccezione = " + e);
//		}
//	}
//
//	/**
//	 * Test method for {@link update.database.ItunesUpdates#parseHtmlResponseOfItunesVersionFromApple()}.
//	 */
//	@Test
//	public final void testParseHtmlResponseOfItunesVersionFromApple2() {
//		//caso 2: non scarico il file dal php, ma ne metto uno io dalla cartella resources dei test
//		//in questo caso metto il itunes_senzaBR.html e viene lanciata UpdateException
//		try {
//			Files.deleteIfExists(newitunestxt);
//			Files.copy(Paths.get("", "src", "test", "resources", "itunes_senzaBR.html"),newitunestxt);
//			itunesUpdates.parseHtmlResponseOfItunesVersionFromApple();
//			fail("non e' stata lanciata eccezione");
//		} catch (URISyntaxException | IOException e) {
//			fail("Rilevata eccezione diversa da UpdateException");
//		} catch (UpdateException e) {
//			assertTrue(true); //cioe' l'eccezione rilevata e quindi e' tutto ok
//		}
//	}
//
//
//	/**
//	 * Test method for {@link update.database.ItunesUpdates#parseHtmlResponseOfItunesVersionFromApple()}.
//	 */
//	@Test
//	public final void testParseHtmlResponseOfItunesVersionFromApple3() {
//		//caso 3: non scarico il file dal php, ma ne metto uno io dalla cartella resources dei test
//		//in questo caso metto il itunes_solounariga.html e il result e' un array di 1 elemento
//		try {
//			Files.deleteIfExists(newitunestxt);
//			Files.copy(Paths.get("", "src", "test", "resources", "itunes_solounariga.html"),newitunestxt);
//			List<String> result = itunesUpdates.parseHtmlResponseOfItunesVersionFromApple();
//
//			assertTrue(result.size()==1);
//			this.testResultArray(result);
//
//			assertFalse(Files.exists(newitunestxt)); //perche' a fine metodo questo file viene cancellato
//		} catch (UpdateException | URISyntaxException | IOException e) {
//			fail("Eccezione = " + e);
//		}
//	}
//
//
//	/**
//	 * Test method for {@link update.database.ItunesUpdates#parseHtmlResponseOfItunesVersionFromApple()}.
//	 */
//	@Test
//	public final void testParseHtmlResponseOfItunesVersionFromApple4() {
//		//caso 4: non scarico il file dal php, ma ne metto uno io dalla cartella resources dei test
//		//in questo caso metto il itunes_nonpresente.html e il result e' un array di 3 elemento con dentro anche le scritte NONPRESENTE
//		try {
//			Files.deleteIfExists(newitunestxt);
//			Files.copy(Paths.get("", "src", "test", "resources", "itunes_nonpresente.html"),newitunestxt);
//			List<String> result = itunesUpdates.parseHtmlResponseOfItunesVersionFromApple();
//
//			assertTrue(result.size()==3);
//			this.testResultArrayNotAvailableWithHash(result);
//
//			assertFalse(Files.exists(newitunestxt)); //perche' a fine metodo questo file viene cancellato
//		} catch (UpdateException | URISyntaxException | IOException e) {
//			fail("Eccezione = " + e);
//		}
//	}
//
//
//	/**
//	 * Test method for {@link update.database.ItunesUpdates#parseHtmlResponseOfItunesVersionFromApple()}.
//	 */
//	@Test
//	public final void testParseHtmlResponseOfItunesVersionFromApple5() {
//		//caso 5: non scarico il file dal php, ma ne metto uno io dalla cartella resources dei test
//		//in questo caso metto il itunes_senzaHash.html e il result e' un array di 3 elemento con dentro anche le scritte NONPRESENTE (solo sull'hash)
//		try {
//			Files.deleteIfExists(newitunestxt);
//			Files.copy(Paths.get("", "src", "test", "resources", "itunes_senzaHash.html"),newitunestxt);
//			List<String> result = itunesUpdates.parseHtmlResponseOfItunesVersionFromApple();
//
//			assertTrue(result.size()==3);
//			this.testResultArrayNotAvailableOnlyHash(result);
//
//			assertFalse(Files.exists(newitunestxt)); //perche' a fine metodo questo file viene cancellato
//		} catch (UpdateException | URISyntaxException | IOException e) {
//			fail("Eccezione = " + e);
//		}
//	}
//
//
//	/**
//	 * Test method for {@link update.database.ItunesUpdates#parseHtmlResponseOfItunesVersionFromApple()}.
//	 */
//	@Test
//	public final void testParseHtmlResponseOfItunesVersionFromApple6() {
//		//caso 6: non gli do in ingresso nessun file newitunestxt, e vedo se il metodo e' robusto
//		try {
//			Files.deleteIfExists(newitunestxt);
//			itunesUpdates.parseHtmlResponseOfItunesVersionFromApple();
//			fail("non e' stata lanciata eccezione");
//		} catch (URISyntaxException | IOException e) {
//			fail("Rilevata eccezione diversa da UpdateException " + e);
//		} catch (UpdateException e) {
//			assertTrue(true); //cioe' l'eccezione rilevata e quindi e' tutto ok
//		}
//	}
//
//	/**
//	 * Test method for {@link update.database.ItunesUpdates#parseHtmlResponseOfItunesVersionFromApple()}.
//	 */
//	@Test
//	public final void testParseHtmlResponseOfItunesVersionFromApple7() {
//		//caso 7: do itunes_piattaformainventata (primo e seconda piattaforma danneggiate, solo l'ultimo e' valido,
//		//perche' i primi sono 5 e 66 che non esistono, dovrebbero essere 0 e 32)
//		try {
//			Files.deleteIfExists(newitunestxt);
//			Files.copy(Paths.get("", "src", "test", "resources", "itunes_piattaformainventata.html"),newitunestxt);
//			List<String> result = itunesUpdates.parseHtmlResponseOfItunesVersionFromApple();
//			assertTrue(result.size()==1); //cioe' una riga non e' stata inserita perche' non ha superato la verifica
//		} catch (UpdateException | URISyntaxException | IOException e) {
//			fail("Eccezione = " + e);
//		}
//	}
//
//	/**
//	 * Test method for {@link update.database.ItunesUpdates#parseHtmlResponseOfItunesVersionFromApple()}.
//	 */
//	@Test
//	public final void testParseHtmlResponseOfItunesVersionFromApple8() {
//		//caso 8: do itunes_senza4underscore (prima riga ha un _ in meno prima dell'hash e l'ultima non li ha propio
//		try {
//			Files.deleteIfExists(newitunestxt);
//			Files.copy(Paths.get("", "src", "test", "resources", "itunes_senza4underscore.html"),newitunestxt);
//			List<String> result = itunesUpdates.parseHtmlResponseOfItunesVersionFromApple();
//			assertTrue(result.size()==1); //cioe' due righe non inserite perche' non ha superato la verifica
//		} catch (UpdateException | URISyntaxException | IOException e) {
//			fail("Eccezione = " + e);
//		}
//	}
//
//
//	/**
//	 * Test method for {@link update.database.ItunesUpdates#parseHtmlResponseOfItunesVersionFromApple()}.
//	 */
//	@Test
//	public final void testParseHtmlResponseOfItunesVersionFromApple9() {
//		//caso 9: do itunes_vuoto
//		try {
//			Files.deleteIfExists(newitunestxt);
//			Files.copy(Paths.get("", "src", "test", "resources", "itunes_vuoto.html"),newitunestxt);
//			itunesUpdates.parseHtmlResponseOfItunesVersionFromApple();
//		} catch (URISyntaxException | IOException e) {
//			fail("Rilevata eccezione diversa da UpdateException " + e);
//		} catch (UpdateException e) {
//			assertTrue(true);
//		}
//	}
//
//
//	/**
//	 * Test method for {@link update.database.ItunesUpdates#parseHtmlResponseOfItunesVersionFromApple()}.
//	 */
//	@Test
//	public final void testParseHtmlResponseOfItunesVersionFromApple10() {
//		//caso 10: do itunes_linknonvalido (primo e secondo link danneggiati, solo l'ultimo e' valido,
//		try {
//			Files.deleteIfExists(newitunestxt);
//			Files.copy(Paths.get("", "src", "test", "resources", "itunes_linknonvalido.html"),newitunestxt);
//			List<String> result = itunesUpdates.parseHtmlResponseOfItunesVersionFromApple();
//			assertTrue(result.size()==1); //cioe' una riga non e' stata inserita perche' non ha superato la verifica
//		} catch (UpdateException | URISyntaxException | IOException e) {
//			fail("Eccezione = " + e);
//		}
//	}
//
//	/**
//	 * Test method for {@link update.database.ItunesUpdates#prepareItunesVersionToWrite()}.
//	 */
//	@Test
//	public final void testPrepareItunesVersionToWrite1_3_4_5_8_10() {
//		//caso 1_3_4_5_7_8_10 :  (1, 3, 4, 5, 7, 8 o 10 non cambia nulla) io faccio con 1 perc omodita'
//		try {
//			FileList.deleteFilesInList(dataPath); //pulisco tutto per simulare primo avvio cosi' sono certo non faccia casini
//			UpdateManagerDB.getInstance().checkDbUpdates(); //al primo avvios carica ed estrae i database
//			assertTrue(Files.exists(itunesLista));
//
//			//come testParseHtmlResponseOfItunesVersionFromApple1 
//			Files.deleteIfExists(newitunestxt);
//			HttpFileDownload.httpFileDownload(newitunestxt, LinkServer.LINKPHPITUNES);
//			List<String> result = itunesUpdates.parseHtmlResponseOfItunesVersionFromApple();
//			assertTrue(result.size()>0);
//			this.testResultArray(result);
//			assertFalse(Files.exists(newitunestxt)); //perche' a fine metodo questo file viene cancellato
//			//fine test ricopiato
//
//
//			//Il metodo che segue richiede che ituneslist sia presente (per questo l'ho scaricaot prima)
//			boolean resultBoolean = itunesUpdates.prepareItunesVersionToWrite(result);
//			assertFalse(resultBoolean); //non ci sono aggiornamenti disponibili, tutto OK!!!
//		} catch (UpdateException | URISyntaxException | IOException e) {
//			fail("Eccezione = " + e);
//		} 
//	}
//
//
//	/**
//	 * Test method for {@link update.database.ItunesUpdates#prepareItunesVersionToWrite()}.
//	 */
//	@Test
//	public final void testPrepareItunesVersionToWrite2_6() {
//		//caso 2_6 : usando il 2 cioe' non c'erano i br nel file html ottenuto dal php
//		//			ma volendo anche il 6
//
//		//faccio 2 casi uno passando null e l'altor un array vuoto, ma non canbia a nulla
//		//perche' tanto si entra nello stesso if e da li lancia l'eccezione #UpdateException
//		//per testarlo passo null a prepareItunesVersionToWrite()
//		try {
//			itunesUpdates.prepareItunesVersionToWrite(null);
//			fail("non e' stata lanciata eccezione");
//		} catch (IOException e) {
//			fail("Rilevata eccezione diversa da UpdateException " + e);
//		} catch (UpdateException e) {
//			assertTrue(true); //cioe' l'eccezione rilevata e quindi e' tutto ok
//		}
//
//		//per testarlo passo un arrayList<String> senza elementi a prepareItunesVersionToWrite()
//		try {
//			itunesUpdates.prepareItunesVersionToWrite(new ArrayList<String>());
//			fail("non e' stata lanciata eccezione");
//		} catch (IOException e) {
//			fail("Rilevata eccezione diversa da UpdateException " + e);
//		} catch (UpdateException e) {
//			assertTrue(true); //cioe' l'eccezione rilevata e quindi e' tutto ok
//		}
//	}
//
//
//	/**
//	 * Test method for {@link update.database.ItunesUpdates#updateItunesDb()}.
//	 */
//	@Test
//	public final void testUpdateItunesDb() {
//		//do itunes_prime2sonoversioninuove (prima riga e' 19.6.1 (nuovo), secondo ha
//		//versione uguale, anche piattaforma, ma link e hash nuovi, terzo e' la 10.6.1x64 (quindi vecchia))
//		try {
//			FileList.deleteFilesInList(dataPath); //pulisco tutto per simulare primo avvio cosi' sono certo non faccia casini
//			UpdateManagerDB.getInstance().checkDbUpdates(); //al primo avvio scarica ed estrae i database
//			assertTrue(Files.exists(itunesLista));
//			
//			Files.deleteIfExists(newitunestxt);
//			Files.copy(Paths.get("", "src", "test", "resources", "itunes_prime2sonoversioninuove.html"),newitunestxt);
//
//			//chiamo metodo che sto testando
//			itunesUpdates.updateItunesDb();
//			assertTrue(itunesUpdates.isAggiorna()); //agg rilevato
//		} catch (UpdateException | URISyntaxException | IOException e) {
//			fail("Eccezione = " + e);
//		}
//
//	}
//
//
//	private void testResultArray(List<String> result) {
//		String platform, hash;
//		for(String version : result) {
//			//verifico se l'os compatibile e' valido (cioe' tra i 3 previsti)
//			platform = version.split("___")[1];
//			assertTrue(platform.equals("NONPRESENTE") || platform.equals("0") || platform.equals("32") || platform.equals("64")); 
//
//			//verifico se i link sono validi
//			assertTrue(version.split("___")[2].equals("NONPRESENTE") || itunesUpdates.linkToFileIsValid(version.split("___")[2]));
//
//			//verifico che l'hash SHA1 sia di 41 caratteri o sia "non presente"
//			hash = version.split("___")[3];
//			assertTrue(hash.length()==40 || hash.toUpperCase().equals("NONPRESENTE")); 
//		}
//	}
//
//	private void testResultArrayNotAvailableWithHash(List<String> result) {
//		String platform, hash;
//		for(String version : result) {
//			//verifico se l'os compatibile e' valido (cioe' tra i 3 previsti)
//			platform = version.split("___")[1];
//			assertTrue(platform.equals("NONPRESENTE") || platform.equals("0") || platform.equals("32") || platform.equals("64")); 
//
//			//verifico se i link sono validi (NB: il link ci deve essere sempre...se c'e' non presente il file non sara' 
//			//scaricabile e nei prossimi metodi verra' cancellata l'intera versione di itunes ottenuta)
//			assertTrue(itunesUpdates.linkToFileIsValid(version.split("___")[2]));
//
//			//verifico che l'hash SHA1 sia di 41 caratteri o sia "non presente"
//			hash = version.split("___")[3];
//			assertTrue(hash.length()==40); 
//		}
//	}
//
//	private void testResultArrayNotAvailableOnlyHash(List<String> result) {
//		String platform, hash;
//		for(String version : result) {
//			//verifico se l'os compatibile e' valido (cioe' tra i 3 previsti)
//			platform = version.split("___")[1];
//			assertTrue(platform.equals("0") || platform.equals("32") || platform.equals("64")); 
//
//			//verifico se i link sono validi
//			assertTrue(itunesUpdates.linkToFileIsValid(version.split("___")[2]));
//
//			//verifico che l'hash SHA1 sia di 41 caratteri o sia "non presente"
//			hash = version.split("___")[3];
//			assertTrue(hash.length()==40 || hash.toUpperCase().equals("NONPRESENTE")); 
//		}
//	}
//
//
//	/**
//	 * Test method for {@link update.database.ItunesUpdates#run()}.
//	 */
//	@Test
//	public final void testRun1() {
//		//caso 1 : non ci sono aggiornamenti disponibili
//		itunesUpdates.run();
//		assertFalse(itunesUpdates.isAggiorna());
//	}
//
//	@After
//	public void tearDown() throws Exception {
//		FileList.deleteFilesInList(dataPath); //pulisco tutto
//	}
//}
