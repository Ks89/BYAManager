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

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


import model.CommercialDevice;
import model.Firmware;
import model.User;

import com.dd.plist.*;


/**
 *	This is the parser of "version.plist" created from {@link FirmwareUpdates}.
 */
public final class ParserPlist {
	private String[] devicesList;
	private String[] verificoPresenzaSha1;
	private List<Firmware> firmwaresList;
	private NSDictionary dictionary;
	private NSDictionary lastDictionary;

	/**
	 * Constructor of the class
	 */
	public ParserPlist() {
		this.firmwaresList = new ArrayList<Firmware>();
		this.dictionary = null;
		this.lastDictionary = null;
		this.devicesList = null;
	}
	
	/**
	 * Method that starts parsing and returns a firmwares list.
	 * @return A List of firmware available in the plist file.
	 * @throws Exception Generic Exception.
	 */
	public List<Firmware> startParsing() throws Exception {
		Path path = User.getInstance().getDataPath().resolve("version.xml");
		Firmware firmware = null;
		//get the root node
		dictionary = (NSDictionary)PropertyListParser.parse(path.toFile());

		//get the object with the specified key
		dictionary = (NSDictionary)dictionary.objectForKey("MobileDeviceSoftwareVersionsByVersion");

		//get the list with numbers (String) inside, that represents the number of released devices
		String[] numeroDispositivi = dictionary.allKeys();

		//i go to the last number, because this represent the present (now), with all available devices.
		dictionary = (NSDictionary)dictionary.objectForKey(numeroDispositivi[numeroDispositivi.length-1]); //choose firmware's type

		//now i need to choose between MobileDeviceSoftwareVersions and RecoverySoftwareVersions.
		//i choose MobileDeviceSoftwareVersions, and the reference is saved in lastDictionary
		lastDictionary = (NSDictionary)dictionary.objectForKey("MobileDeviceSoftwareVersions");

		//get the device list
		devicesList = lastDictionary.allKeys();

		//scan all devices, getting data about the latest version of their firmwares.
		for(int i=0; i<devicesList.length;i++) {
			//enter in Restore of the plist. In dictionary there is associated NSDictionary
			this.enterInRestore(devicesList[i]); 
			firmware = this.obtainFirmware(i);
			if(firmware!=null) {
				//add the firmware to the list
				firmwaresList.add(firmware); 
			}
		}
		return firmwaresList;
	}

	
	/**
	 * This method enters in dictionary "restore" of the plist's file, using the device.
	 * @param device String that represents the device.
	 */
	private void enterInRestore(String device) {
		//I reuse the dictionary used above every cycle, maintaining the reference to lastDictionary
		dictionary = (NSDictionary)lastDictionary.objectForKey(device);

		//i choose the build called "Unknown", that it's always the last one.
		dictionary = (NSDictionary)dictionary.objectForKey("Unknown");

		//enter in Universal
		dictionary = (NSDictionary)dictionary.objectForKey("Universal");

		//enter in Restore
		dictionary = (NSDictionary)dictionary.objectForKey("Restore");
	}


	/**
	 * Method to obtain a {@link Firmware}, using the index.
	 * @param index int that represents the index.
	 * @return The requested {@link Firmware}.
	 */
	private Firmware obtainFirmware(int index) {
		boolean trovato = false;
		Firmware firmware = null;
		if(!(dictionary.objectForKey("FirmwareURL").toString().contains("protected"))) {
			
			//if the link is not prohibited by apple, it's can be used.
			verificoPresenzaSha1 = dictionary.allKeys();
			for(String firmwareInPlist : verificoPresenzaSha1) {
				if(firmwareInPlist.equals("FirmwareSHA1")) {
					//se il firmware ha la voce SHA1 quando creo il firmware la inserisco, altrimenti no
					//vedi metodo sotto (creaFirmware)
					trovato = true; 
				}
			}	 
			
			//now i return the firmware, created tiwh the method creareFirmware.
			firmware = this.createFirmware(index, trovato);
		}
		return firmware;
	}

	
	/**
	 * Method to create a {@link Firmware}.
	 * @param index int that represents the index useful to obtain the device.
	 * @param found boolean that if it's true, the {@link Firmware} has SHA1, otherwise no.
	 * @return The created {@link Firmware}.
	 */
	private Firmware createFirmware(int index, boolean found) {
		//create firmware
		Firmware firmware = new Firmware();
		firmware.setDevice(new CommercialDevice(devicesList[index]));
		firmware.setVersion(dictionary.objectForKey("ProductVersion").toString());
		firmware.setBuild(dictionary.objectForKey("BuildVersion").toString());
		firmware.setPercorso(dictionary.objectForKey("FirmwareURL").toString());
		//if the SHA1 exists in the plist
		if(found) {
			firmware.setHash(dictionary.objectForKey("FirmwareSHA1").toString().toUpperCase());
		} else {
			//otherwise i set "non presente"
			firmware.setHash("non presente");
		}
		//i can't know the size of a firmware obtained from version.xml. For this reason i set this size to 0.
		firmware.setSize(0); 
		return firmware;
	}
}