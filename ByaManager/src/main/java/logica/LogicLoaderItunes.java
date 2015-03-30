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

package logica;

import gui.MainFrame;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.ItunesVersion;
import model.MacOsx;
import model.OperativeSystem;
import model.Windows;

import org.apache.log4j.Logger;

public class LogicLoaderItunes extends LogicLoader {
	private static final Logger LOGGER = Logger.getLogger(LogicLoaderItunes.class);
	private static final String ITUNESLISTA = "iTunesLista.txt";
	private static LogicLoaderItunes instance = new LogicLoaderItunes();

	/**
	 * @uml.property  name="iTunesMapNomeFile"
	 * @uml.associationEnd  qualifier="replace:java.lang.String model.ItunesVersion"
	 */
	private Map<String,ItunesVersion> iTunesMapNomeFile;
	/**
	 * @uml.property  name="iTunesListaNomeFile"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="model.ItunesVersion"
	 */
	private List<ItunesVersion> iTunesListaNomeFile;

	public LogicLoaderItunes() {
		super();
		this.iTunesMapNomeFile = new HashMap<String,ItunesVersion>();
		this.iTunesListaNomeFile = new ArrayList<ItunesVersion>();
	}

	public static LogicLoaderItunes getInstance() {
		return instance;
	}

	public List<Path> removeRenamedItunesVersion(List<Path> pathList) {
		List<Path> notRenamedFile = new ArrayList<Path>();
		for(Path path : pathList) {
			if(path.getFileName().toString().contains("iTunes_")) {
				if(iTunesMapNomeFile.get(path.getFileName().toString().split(".part")[0])!=null) {
					notRenamedFile.add(path);
				} else {
					try {
						Files.delete(path);
					} catch (IOException e) {
						LOGGER.error("removeRenamedItunesVersion() - Eccezione durante rimozione path: " + path.getFileName().toString() + ". Eccezione= " + e);
					}
				}
			}
		}
		return notRenamedFile;
	}

	private OperativeSystem getCorrectInstanceOfOperativeSystem(String platform) {
		if(platform.equals("0")) {
			return new MacOsx(platform);
		} else {
			return new Windows(platform);
		}
	}

	public void loadiTunesVersion() {
		LOGGER.info("loadiTunesVersion() - Avviato in percorso=" + super.getPercorsoDati().toString());
		try (
				InputStream inputStream = new FileInputStream(super.getPercorsoDati().resolve(ITUNESLISTA).toFile());
				InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
				BufferedReader br = new BufferedReader(inputStreamReader)
				) {
			String rigaFile = br.readLine();
			String[] letto;
			while(rigaFile!=null) {
				letto = rigaFile.split("___");
				if(letto.length>0) {
					ItunesVersion itunesVersion = new ItunesVersion();
					itunesVersion.setVersion(letto[0]);
					itunesVersion.addOperativeSystem(this.getCorrectInstanceOfOperativeSystem(letto[1]));
					itunesVersion.setPercorso(letto[2]);
					itunesVersion.setHash(letto[3].toUpperCase());
					itunesVersion.setDimension(0);
					iTunesMapNomeFile.put("iTunes" + "_" + itunesVersion.getOperativeSystemList().get(0).getShortName() + "_" + 
											itunesVersion.getVersion() + itunesVersion.getOperativeSystemList().get(0).getExecExtension(), itunesVersion);
					iTunesListaNomeFile.add(itunesVersion);
				}
				rigaFile = br.readLine();
			}
			LOGGER.info("loadiTunesVersion() - Riuscito");
		} catch (IOException e2) {
			LOGGER.error("loadiTunesVersion() - Eccezione=", e2);
		}
	}

	public ItunesVersion ottieniItunesVersionDaLista() {
		String platformName = (String)(MainFrame.getInstance().getOsList().getSelectedItem());
		String execExtension;
		if(platformName.contains("Mac")) {
			platformName = "Mac";
			execExtension = "dmg";
		} else {
			execExtension = "exe";
			if(platformName.contains("32bit")) {
				platformName = "Win32";
			} else {
				platformName = "Win64";
			}
		}
		String version = (String)(MainFrame.getInstance().getItunesList().getSelectedItem());
		return iTunesMapNomeFile.get("iTunes" + "_" + platformName + "_" + version + "." + execExtension);
	}

	public void fillItunesComboBox() {
		//inverto la lista dei firmware in modo tale che i piu' recenti appaiano all'inizio
		List<ItunesVersion> listaiTunesReverse = this.getiTunesListaNomeFile();
		Collections.reverse(listaiTunesReverse);

		for(ItunesVersion iTunesVersion : listaiTunesReverse) {
			if(iTunesVersion.getOperativeSystemList().get(0).getCompleteName().equals((String)(MainFrame.getInstance().getOsList().getSelectedItem()))) {
				MainFrame.getInstance().getItunesList().addItem(iTunesVersion.getVersion()); 
			}
		}
	}
	
	public Map<String, ItunesVersion> getiTunesMapNomeFile() {
		return iTunesMapNomeFile;
	}

	public List<ItunesVersion> getiTunesListaNomeFile() {
		return iTunesListaNomeFile;
	}

}