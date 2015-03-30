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
//import java.nio.file.Path;
//
//import model.User;
//
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//
//import fileutility.FileList;
//
//public class FirmwareUpdatesTest {
//	private static final String ZIPNAME = "lists.zip";
//	private static final String IPSWLISTA = "ipswLista.txt";
//	private static final String IPSWLISTANEW = "ipswLista_new.txt";
//	private static final String IPSWLISTADOWNLOADED = "ipswLista_downloaded.txt";
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
//	
//	@Before
//	public void setUp() throws Exception {
//		dataPath = User.getInstance().getDataPath();
//		zipPath = dataPath.resolve(ZIPNAME);
//		ipswListaNew = dataPath.resolve(IPSWLISTANEW);
//		ipswListaDownloaded = dataPath.resolve(IPSWLISTADOWNLOADED);
//		ipswLista = dataPath.resolve(IPSWLISTA);
//	}
//
//	/**
//	 * Test method for {@link update.database.FirmwareUpdates#updateFirmwareDb()}.
//	 */
//	@Test
//	public final void testUpdateFirmwareDb() {
//	}
//
//	/**
//	 * Test method for {@link update.database.FirmwareUpdates#run()}.
//	 */
//	@Test
//	public final void testRun() {
//	}
//	
//	@After
//	public void tearDown() throws Exception {
//		FileList.deleteFilesInList(dataPath); //pulisco tutto
//	}
//}
