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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import model.BufferSize;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import fileutility.FileList;

import preferences.Settings;

public abstract class LogicLoader{
	private static final Logger LOGGER = LogManager.getLogger(LogicLoader.class);
	private static final String PART = ".part";

	/**
	 * @uml.property  name="percorsoDati"
	 */
	private Path percorsoDati;
	/**
	 * @uml.property  name="percorsoDownload"
	 */
	private Path percorsoDownload;
	/**
	 * @uml.property  name="percorsoDownloadTemp"
	 */
	private Path percorsoDownloadTemp; 

	/**
	 * Costruttore della classe
	 */
	public LogicLoader() {
		Settings impostazioni = Settings.getInstance();

		this.percorsoDownload = Paths.get(impostazioni.getPercorsoDownload());
		this.percorsoDownloadTemp = Paths.get(impostazioni.getPercorsoDownloadTemp());
		this.percorsoDati = Paths.get(impostazioni.getPercorsoDati());
		BufferSize.setBufferSize(Settings.getInstance().getDimensioneBuffer());
	}

	/**
	 * Metodo che rimuove i file part inutilizzabili per 2 ragioni: -il file e' piu' piccolo della dimensione del buffer,
	 * oppure la lista dei trovati non contiene uno dei file. Questa ultima situazione si puo' verificare solo
	 * se mancano dei file part associati a tale firmware.
	 * @param pathList
	 * @param found
	 */
	public void removeUnusableParts(List<Path> pathList, List<String> found) {
		for(Path path : pathList) {
			try {
				if(!found.contains(path.getFileName().toString().split(PART)[0]) 
						|| Files.size(path) < BufferSize.getBufferSize()) {
					Files.deleteIfExists(path);
				}
			} catch (IOException e) {
				LOGGER.error("removeUnusableParts() - Eccezione lanciata durante il calcolo della dimensione di " + path.toString() + ". Eccezione = " + e);
			}
		}
	}


	public List<Path> getShaList(Path path) {
		List<Path> shaListFile = new ArrayList<Path>();
		for(Path file : FileList.getFileList(path)) {
			if(file.getFileName().toString().contains(".sha")) {
				shaListFile.add(file);
				LOGGER.info("getShaList() - Rilevato file .sha =" + file.toAbsolutePath());
			}
		}
		return shaListFile;
	}


	protected List<Path> getValidFileList(Path directoryPath) {
		if(!Files.isDirectory(directoryPath)) {
			return null;
		}
		
		List<Path> arrayListOfValidPath = new ArrayList<Path>();

		for(Path path : FileList.getFileList(directoryPath)) {
			try {
				if((path.getFileName().toString().contains("Restore") || path.getFileName().toString().contains("iTunes_"))
						&& Files.size(path) >= BufferSize.getBufferSize()) {
					arrayListOfValidPath.add(path);
					LOGGER.info("getValidFileList() - List<Path>=" + path.toString());
				}
			} catch (IOException e) {
				LOGGER.error("getValidFileList() - Eccezione lanciata durante il calcolo della dimensione di " + path.toString() + ". Eccezione = " + e);
			}
		}
		return arrayListOfValidPath;
	}


	//************************************************************************************************************************
	//*************************************************GETTER AND SETTER******************************************************
	//************************************************************************************************************************
	public List<Path> getListaFile() {
		return this.getValidFileList(this.percorsoDownload);
	}
	/**
	 * @return
	 * @uml.property  name="percorsoDati"
	 */
	public Path getPercorsoDati() {
		return percorsoDati;
	}
	/**
	 * @return
	 * @uml.property  name="percorsoDownloadTemp"
	 */
	protected Path getPercorsoDownloadTemp() {
		return percorsoDownloadTemp;
	}
	/**
	 * @return
	 * @uml.property  name="percorsoDownload"
	 */
	protected Path getPercorsoDownload() {
		return percorsoDownload;
	}
}
