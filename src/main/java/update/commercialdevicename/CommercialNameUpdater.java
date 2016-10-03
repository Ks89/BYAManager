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

package update.commercialdevicename;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import update.LinkServer;

/**
 * Class to load devices from primary/secondary server in {@link LinkServer}
 */
public final class CommercialNameUpdater {
	private static final Logger LOGGER = LogManager.getLogger(CommercialNameUpdater.class);

	private static CommercialNameUpdater instance = new CommercialNameUpdater();

	private Map<String,String> codeNameCommecialDeviceMap;

	private CommercialNameUpdater() {
		this.codeNameCommecialDeviceMap = new HashMap<String,String>();
	}

	/**
	 * Method to get the instance of this class.
	 * @return instance of this class.
	 */
	public static CommercialNameUpdater getInstance() {
		return instance;
	}


	/**
	 * This method parses line by line a web page (a txt file) identified by the link parameter
	 * (without writing data on the hard disk).
	 */
	public void loadDevices() {
		LOGGER.info("loadDevices() - Method started");		
		try (
				InputStream inputStream = new URL(LinkServer.LINKDEVICE_KS).openStream();
				InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
				BufferedReader br = new BufferedReader(inputStreamReader)
				) {
			String rigaFile = br.readLine();
			String[] letto;
			while(rigaFile!=null) {
				if(rigaFile.contains("___")) {
					letto = rigaFile.split("___");
					if(letto.length>0) {
						codeNameCommecialDeviceMap.put(letto[0],letto[1]);
					}
				}
				rigaFile = br.readLine();
			}
			LOGGER.info("loadDevices() - Method completed");
		} catch (IOException e2) {
			LOGGER.error("loadDevices() - Error. Primary server probably offline.", e2);

			try (
					InputStream inputStream = new URL(LinkServer.LINKDEVICE).openStream();
					InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
					BufferedReader br = new BufferedReader(inputStreamReader)
					) {
				String rigaFile = br.readLine();
				String[] letto;
				while(rigaFile!=null) {
					letto = rigaFile.split("___");
					if(letto.length>0) {
						codeNameCommecialDeviceMap.put(letto[0],letto[1]);
					}
					rigaFile = br.readLine();
				}
				LOGGER.info("loadDevices() - Method completed");
			} catch (IOException e3) {
				LOGGER.error("loadDevices() - Error. Secondary server probably offline.", e3);
			}

		}
	}

	public Map<String, String> getCodeNameCommecialDeviceMap() {
		return codeNameCommecialDeviceMap;
	}
}
