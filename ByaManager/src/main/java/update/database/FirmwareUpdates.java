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

package update.database;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Observable;

import lombok.Getter;
import model.CommercialDevice;
import model.Firmware;
import notification.Notification;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import fileutility.HttpFileDownload;
import time.TimeManager;
import update.LinkServer;


/**
 * Class the runs on a different Thread to check if there are new firmwares.<br></br>
 * This class downloads version.xml to parse and check if there are new versions available.<br></br>
 * Anyway, this class creates a new file "_new.txt" with every firmware inside.<br></br>
 * If exists a new firmware, the user will be advised with a notification, otherwise file _new.txt will be removed.<br></br>
 * To increase the performance, this class check if the sha1 of the _new.txt is different from the original file.
 */
public final class FirmwareUpdates extends Observable implements Runnable {
	private static final Logger LOGGER = LogManager.getLogger(FirmwareUpdates.class);

	private static final String UNDERSCORE3 = "___";
	private static final String IPSWLIST = "ipswLista.txt";
	private static final String IPSWLISTNEW = "ipswLista_new.txt";
	private static final String VERSIONXMLNAME = "version.xml";

	private List<Firmware> firmwareListNew;
	private List<Firmware> firmwareListToWrite;
	private Path dataPath;
	@Getter private boolean dbUpdated = false;

	/**
	 * Constructor of this class.
	 */
	public FirmwareUpdates (Path dataPath) {
		this.firmwareListToWrite = new ArrayList<Firmware>();
		this.dataPath = dataPath;
	}


	/**
	 * Method that reads ipswLista.txt line by line. Every line is added to the list of firmware.
	 * @throws IOException Exception thrown if there are problems.
	 */
	private void readTxt() throws IOException {
		LOGGER.info("readTxt() - Method started");
		try (
				BufferedReader br = Files.newBufferedReader(dataPath.resolve(IPSWLIST), Charset.defaultCharset())
				) {	
			String currentLine = br.readLine();
			String[] read;
			Firmware firmware;
			while(currentLine!=null) {
				 //ignore lines with ":"
				if(!currentLine.startsWith(":")) {
					read = currentLine.split(UNDERSCORE3);
					String[] partial1 = read[0].split(",");
					//if the size is splitted in 2 is ok, otherwise is a strange url, like for iphone 3,1 with iOS 4.0.
					//i use length to fix this strange situation without throw an Exception.
					String[] partial2 = partial1[partial1.length-1].split("_");
					String[] partial3 = partial1[partial1.length-2].split("/");

					firmware = new Firmware();
					firmware.setDevice(new CommercialDevice(partial3[partial3.length-1] + "," + partial2[0]));
					firmware.setVersion(partial2[1]);
					firmware.setBuild(partial2[partial2.length-2]);
					firmware.setPercorso(read[0]);
					firmware.setHash(read[1].toUpperCase());
					firmware.setDimension(0);
					firmwareListToWrite.add(firmware);
				}
				currentLine = br.readLine();
			}
		}
		LOGGER.info("readTxt() - Method completed");
	}

	/**
	 * Method to load a text file and to load new firmwares in the firwareList.
	 * @throws IOException Exception thrown if there are problems.
	 */
	private void loadTxt() throws IOException{
		LOGGER.info("loadTxt() - Method started");

		boolean found;
		//firmwareListNew = list of new firmwares from version.xml
		//firmwareToWrite = list with all current firmwares loaded
		//if in firmwareListNew there are versions not already in firmwareToWrite, this firmware will be added to firmwareToWrite.
		for(Firmware firmwareN : firmwareListNew) {	
			found=false;
			for(Firmware firmwareToWrite : firmwareListToWrite) {			
				if(firmwareN.getUri().equals(firmwareToWrite.getUri())) {
					found=true;
					break;
				}
			}
			//if not found, is a new firmware
			if(!found) {
				firmwareListToWrite.add(firmwareN);
			}
		}

		LOGGER.info("loadTxt() - Method completed");
	}

	/**
	 * Method to save the updated list on a file (_new.txt).
	 * @throws IOException Exception thrown if there are problems.
	 */
	private void saveTxt() throws IOException{
		LOGGER.info("saveTxt() - Method started");
		try (
				BufferedWriter out = Files.newBufferedWriter(dataPath.resolve(IPSWLISTNEW), Charset.defaultCharset())
				) {
			for(int i=0;i<firmwareListToWrite.size();i++) {
				//now i read the file from the beginning to the end
				//every line is terminated with ("\n") but not the last line.
				if(i==firmwareListToWrite.size()-1) {
					out.write(firmwareListToWrite.get(i).toString());
				} else {
					out.write(firmwareListToWrite.get(i).toString()+"\n");
				}
			}
			LOGGER.info("saveTxt() - Method completed");
		}
	}

	

	/**
	 * Method to compare the SHA1 of the new db with the previous one, already on the hd.
	 * @return boolean 'true' indicates that no updates are available, 'false' otherwise.
	 * @throws IOException Exception thrown if there are problems.
	 */
	private boolean checkSHA1() throws IOException {
		LOGGER.info("checkSHA1() - Method started");

		InputStream newStream = Files.newInputStream(dataPath.resolve(IPSWLISTNEW));
		InputStream originaleStream = Files.newInputStream(dataPath.resolve(IPSWLIST));

		String newSha1 = DigestUtils.sha1Hex(newStream);
		String originaleSha1 = DigestUtils.sha1Hex(originaleStream);

		newStream.close();
		originaleStream.close();

		LOGGER.info("checkSHA1() - Method completed");
		return !newSha1.equals(originaleSha1);
	}


	/**
	 * Method to update the db, download the new version.xml, analyzing this xml and loading data into the list.
	 * After that, this method reads the db on the hd, loads new firmwares obtained from the list, saves the new db,
	 * compares this news db with the previous one with a sha1 check and removes some temp files.
	 * @return boolean 'true' if the sha1 check is ok, 'false' otherwise.
	 * @throws Exception Exception thrown if there are problems.
	 */
	private boolean updateDbFromXml() throws Exception {
		LOGGER.info("updateDbFromXml() - Method started");

		//download and parse version.xml and load every new firmwares in the firmware's ArrayList.
		ParserPlist parser = new ParserPlist();
		LOGGER.info("aggiornaDatabaseDaXml() - Download version.xml started"); 
		HttpFileDownload.httpFileDownload(dataPath.resolve(VERSIONXMLNAME), LinkServer.LINKXMLAPPLE);
		LOGGER.info("aggiornaDatabaseDaXml() - Download version.xml completed"); 
		firmwareListNew = parser.startParsing();

		//load and read the txt adding every new firmwares into the ArrayList "firmwareListToWrite"
		this.readTxt();
		this.loadTxt();

		//save line by line "firmwareListToWrite" on the hd
		this.saveTxt();

		//compare ths SHA1 of the dbs (ipswLista_new and ipswLista)
		boolean update = this.checkSHA1(); 

		LOGGER.info("updateDbFromXml() - Method completed");
		return update;
	}

	/**
	 * Remove every xml and plist files. 
	 */
	private void removeXmlAndPlist() {
		try {
			Files.deleteIfExists(dataPath.resolve("version_new.plist"));
			Files.deleteIfExists(dataPath.resolve(VERSIONXMLNAME));
		} catch (IOException e) {
			LOGGER.error("removeXmlAndPlist() - IOException", e);
		}
	}


	private void updateFirmwareDb() throws Exception {
		TimeManager systemDate = TimeManager.getInstance();

		//get the last modified time of the db
		Path path = dataPath.resolve(IPSWLIST);
		Date lastModifiedDate = new Date(Files.getLastModifiedTime(path).toMillis());
		LOGGER.info("updateFirmwareDb() - LastModifiedTime : " + lastModifiedDate.toString());

		//check systemdate > db date + 24h 
		if(systemDate.isMaggioreDiOre(systemDate.getDataSistema(), lastModifiedDate, 24)) {
			LOGGER.info("updateFirmwareDb() - Update required, because: systemdate>dbDate+24h");

			//if update==true starts the update process
			boolean update = this.updateDbFromXml();
			if(update) { 
				//UPDATES AVAILABLE!!!
				
				//notifies the user that a reboot is required
				Notification.showNormalOptionPane("checkDBUpdatesRestartToUpdate");

				//to be sure that during the next restart, the updated db will be loaded
				//this function isn't completely implemented here
				dbUpdated = true;
			} else {
				//no updated -> remove the _new.txt file
				Files.delete(dataPath.resolve(IPSWLISTNEW));
				LOGGER.info("updateFirmwareDb() - Rimosso ipswLista_new.txt con successo");
			}			

			//remove version.xml and version_new.plist
			this.removeXmlAndPlist();

			//***********************IMPORTANT*********************
			//ENGLISH: 
			//to be sure that during the next restart, this method won't check updated another time, i change the Last Modified Time
			//ITALIAN EXTENDED EXPLANATION:
			//per far si che al prossimo avvio il programma non vada a controllare ancora gli aggiornamenti,
			//dato che fino a che non si sostituisce ipswLista con il _downloaded la data resta indietro a quella di sistema di almeno 24 ore e quindi per forza
			//piu' vecchia di 1 giorno di qualunque altra data sensata, modifico la data di ultima modifica del file a mano con quella di sistema
			Files.setLastModifiedTime(path, FileTime.fromMillis(systemDate.getTempoSistema()));
		} else {
			LOGGER.info("updateFirmwareDb() - Not udated because: systemdate<dbDate+24h");
			//database not updated, because systemdate<dbDate+24h
		}
	}

	@Override
	public void run() {
		LOGGER.info("run() - Method started");
		try {
			//update firmware db (if you want to add itunes update, create another try-catch.
			//don't add a method here, because if one of them crashes the other should also executed.
			this.updateFirmwareDb();  
		} catch (Exception e) {
			//if i am here, there was problems during the update process.
			LOGGER.error("run() - Exception" , e);
			Notification.showErrorOptionPane("checkDBUpdatesGenericError", "CheckDBUpdates Error");
		}
		stateChanged();
		LOGGER.info("run() - Method completed");
	}

	/**
	 * Method that notifies to the observer ({@link #UpdateManagerDB}) that the status is changed.
	 */
	public void stateChanged() {
		setChanged();
		notifyObservers();
	}
}