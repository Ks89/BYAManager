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

package update.software;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import lombok.Getter;
import model.User;
import notification.Notification;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import update.LinkServer;
import exception.UpdateException;
import fileutility.FileList;
import fileutility.HttpFileDownload;

/**
 *	Class that check if there is a new version of BYAManager.jar and compare the sha512.
 */
public final class CheckBYAMUpdates extends Observable implements Runnable{
	private static final Logger LOGGER = LogManager.getLogger(CheckBYAMUpdates.class);
	
	private static final String BYAMANAGERNEW = "BYAManager-new.j_a_r";
	private static final String BYAUPDATER = "BYAUpdater.jar";
	private String shaBya;
	private String shaUpdater;
	@Getter private String versionFromFile;
	private Path jarPath;

	/**
	 * Constructor the initialize jarPath.
	 */
	public CheckBYAMUpdates() {
		jarPath = Paths.get(User.getInstance().getJarPath());
	}

	/**
	 * Method to get BYAManager.jar and BYUpdater.jar from the server.<br></br>
	 * After that, this method check the sha512 of these files. 
	 * @throws IOException Exception that represents a problem.
	 * @throws UpdateException Exception that represents a sha512 mismatch error.
	 */
	public void downloadJarFromServer() throws IOException, UpdateException {
		//download updater, byamanager and also a file with sha512
		this.downloadJARs(); 
	
		//check SHA512 to know if these files are correct
		this.checkSha();
	}

	/**
	 * Method to download the new version of BYAManager and BYAUpdater
	 */
	public void downloadJARs() {
		try {
			LOGGER.info("downloadJARs() - Method started");
			//download jars from primary server
			HttpFileDownload.httpFileDownload(jarPath.resolve(BYAMANAGERNEW), LinkServer.LINKJAR_KS);
			HttpFileDownload.httpFileDownload(jarPath.resolve(BYAUPDATER), LinkServer.LINKUPDATER_KS);
		} catch (IOException | URISyntaxException e) {
			try {
				LOGGER.info("downloadJARs() - Link primary server is broken, retrying with the secondary server...");
				//remove partial jars
				Files.deleteIfExists(jarPath.resolve(BYAMANAGERNEW));
				Files.deleteIfExists(jarPath.resolve(BYAUPDATER));
				//download jars from secondary server
				HttpFileDownload.httpFileDownload(jarPath.resolve(BYAMANAGERNEW), LinkServer.LINKJAR);
				HttpFileDownload.httpFileDownload(jarPath.resolve(BYAUPDATER), LinkServer.LINKUPDATER);
			} catch (IOException | URISyntaxException e1) {
				LOGGER.error("downloadJARs() - IOException or URISyntaxException", e1);
			}
		}
		LOGGER.info("downloadJARs() - Method completed");
	}

	/**
	 * Method to compare sha512 of the downloaded jars with the sha512 in the txt file in my server.
	 * @throws IOException Exception that represents a problem.
	 * @throws UpdateException Exception that represents a sha512 mismatch error.
	 */
	public void checkSha() throws IOException, UpdateException {
		InputStream bya = new FileInputStream(jarPath.resolve(BYAMANAGERNEW).toFile());
		InputStream updater = new FileInputStream(jarPath.resolve(BYAUPDATER).toFile());
		String shaByaManagerDownloaded = DigestUtils.sha512Hex(bya);
		String shaUpdaterDownloaded = DigestUtils.sha512Hex(updater);
		
		if(!shaByaManagerDownloaded.equals(this.shaBya) || !shaUpdaterDownloaded.equals(this.shaUpdater)) {
			throw new UpdateException(UpdateException.Reason.CORRUPTED_JAR);
		}
		bya.close();
		updater.close();
		this.shaBya = null;
		this.shaUpdater = null;
		shaByaManagerDownloaded = null;
		shaUpdaterDownloaded = null;
	}

	/**
	 * Method called by updateSoftware in logic.UpdateManagerSoftware to remove BYAUpdater.
	 */
	public void removeUpdater() {	
		for(Path path : FileList.getFileList(jarPath)) {
			if(path.endsWith(BYAUPDATER)) {
				try {
					Files.delete(path);
				} catch (IOException e) {
					LOGGER.error("removeUpdater() - IOException", e);		
				}
			}
		}
	}

	/**
	 * This method parses line by line a web page identified by the link parameter
	 * (without writing data on the hard disk).
	 * @param link String that represents the http link of a webpage.
	 * @return A list of String. Every string is a line in the webpage.
	 * @throws IOException Exception thrown is there are problems.
	 */
	private List<String> serverFileReader(String link) throws IOException {
		LOGGER.info("serverFileReader() - Method started");
		List<String> rowFileList = new ArrayList<String>();

		try (
				InputStream inputStream = new URL(link).openStream();
				InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
				BufferedReader br = new BufferedReader(inputStreamReader);
				) {
			String row = br.readLine();
			while(row != null) {
				rowFileList.add(row);
				row = br.readLine();
			}
		} catch(IOException e) {
			throw e;
		}
		LOGGER.info("serverFileReader() - Method completed");
		return rowFileList;
	}

	/**
	 * This method gets the file that contains sha512 of BYAManager.jar and BYAUpdater.jar and another
	 * file that contains the latest version of BYAManager in a format like this "X,X,X,X".
	 * @param linkSha Http link to the sha file
	 * @param linkVersion Http link to the version file
	 * @throws IOException Exception thrown is there are problems.
	 */
	private void update(String linkSha, String linkVersion) throws IOException {
		List<String> check = this.serverFileReader(linkSha);
		//in this two global variables there are two sha512. 
		this.shaBya = check.get(0);
		this.shaUpdater = check.get(1);

		//now i get the lastest version code of BYAManager in a format like this "X,X,X,X"
		this.versionFromFile = this.serverFileReader(linkVersion).get(0);

		if(versionFromFile!=null) {
			stateChanged();
		}
	}

	public void run() {
		LOGGER.info("run() - Started");
		try {
			//primary server
			this.update(LinkServer.LINKSHA_KS,LinkServer.LINKVERSION_KS);
		} catch (Exception e) {
			LOGGER.error("run() - Error in primary server. Trying with the secondary server...", e);
			try {
				//secondary server
				this.update(LinkServer.LINKSHA,LinkServer.LINKVERSION);
			} catch(IOException e1) {
				LOGGER.error("run() - IOException" , e1);
				Notification.showNormalOptionPane("checkBYAMUpdatesError");
				Runtime.getRuntime().exit(0);
			}
		} 
		LOGGER.info("run() - Completed");
	}	

	/**
	 * Method that notifies to the observer ({@link logic.UpdateManagerSoftware}) that the status is changed.
	 */
	public void stateChanged() {
		setChanged();
		notifyObservers();
	}
}
