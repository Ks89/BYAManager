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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import model.User;

/**
 *	Class that sorts the ipswLista.txt.
 */
public class CommandLineDbSorter {

	private static final String UNDERSCORE3 = "___";
	private static final String IPSWSORTEDLIST = "ipswListSorted.txt";
	private static final String IPSWLIST = "ipswLista.txt";

	//the db (ipswLista.txt) loaded in this arraylist, where the value is a List of lines
	//(like in the db) in this format "HTTPURL___SHA1" with the device's code name ad attribute
	private List<CodenameDeviceType> deviceListInDb;

	public CommandLineDbSorter () {
		deviceListInDb = new ArrayList<>();
	}

	public CodenameDeviceType getCodenameDeviceTypeByCodename(String codename) {
		for(CodenameDeviceType element : deviceListInDb) {
			if(element.getCodename().equals(codename)) {
				return element;
			}
		}
		return null;
	}


	/**
	 * Method to load the db in the hasmap called {@link #deviceListInDb}.
	 * @throws IOException Exception if there are problems.
	 */
	public void loadFirmwareDbInHashmap() throws IOException {
		try (
				BufferedReader br = Files.newBufferedReader(
						User.getInstance().getDataPath().resolve(IPSWLIST), Charset.defaultCharset())
				) {
			String currentLine = br.readLine();
			String deviceCodeName, dbTxtLine;
			String[] read,partial1,partial2,partial3;
			while(currentLine!=null) {
				read = currentLine.split(UNDERSCORE3);
				partial1 = read[0].split(",");
				//if the size is splitted in 2 is ok, otherwise is a strange url, like for iphone 3,1 with iOS 4.0.
				//i use length to fix this strange situation without throw an Exception.
				partial2 = partial1[partial1.length-1].split("_");
				partial3 = partial1[partial1.length-2].split("/");
				deviceCodeName = partial3[partial3.length-1] + "," + partial2[0];

				if(this.getCodenameDeviceTypeByCodename(deviceCodeName)==null) {
					CodenameDeviceType deviceType = new CodenameDeviceType(deviceCodeName);
					deviceListInDb.add(deviceType);
				}

				dbTxtLine = read[0] + UNDERSCORE3 + read[1].toUpperCase();
				this.getCodenameDeviceTypeByCodename(deviceCodeName).getLineFirmwareVersions().add(dbTxtLine);

				currentLine = br.readLine();
			}
		}
	}


	public void writeSortedDbOnDisk() throws IOException {
		Files.deleteIfExists(User.getInstance().getDataPath().resolve(IPSWSORTEDLIST));
		try (
				BufferedWriter br = Files.newBufferedWriter(User.getInstance().getDataPath().resolve(IPSWSORTEDLIST), 
						Charset.defaultCharset(), StandardOpenOption.CREATE_NEW, StandardOpenOption.APPEND);
				) {

			for(int j=0; j<deviceListInDb.size(); j++) {
				for(int i=0; i<deviceListInDb.get(j).getLineFirmwareVersions().size(); i++) 
					if(j==deviceListInDb.size()-1 && i==deviceListInDb.get(j).getLineFirmwareVersions().size()-1) {
						br.write(deviceListInDb.get(j).getLineFirmwareVersions().get(i));
					} else {
						br.write(deviceListInDb.get(j).getLineFirmwareVersions().get(i)+"\n");
					}
			}
		}
	}
}

class CodenameDeviceType {

	@Getter private String codename;
	@Getter private List<String> lineFirmwareVersions;

	public CodenameDeviceType (String codename) {
		lineFirmwareVersions = new ArrayList<>();
		this.codename = codename;
	}
}