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
import java.io.FileNotFoundException;
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

import org.apache.log4j.Logger;

import model.CommercialDevice;
import model.Firmware;
import model.MacOsx;
import model.OperativeSystem;
import model.Windows;

public final class LogicLoaderFirmware extends LogicLoader{
	private static final Logger LOGGER = Logger.getLogger(LogicLoaderFirmware.class);
	private static final String IPSWLISTA = "ipswLista.txt";
	private static LogicLoaderFirmware instance = new LogicLoaderFirmware();

	/**
	 * @uml.property  name="firmwareMapNomeFile"
	 * @uml.associationEnd  qualifier="replace:java.lang.String model.Firmware"
	 */
	private Map<String,Firmware> firmwareMapNomeFile;
	public Map<String, Firmware> getFirmwareMapNomeFile() {
		return firmwareMapNomeFile;
	}
	/**
	 * @uml.property  name="firmwareListaNomeFile"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="model.Firmware"
	 */
	private List<Firmware> firmwareListaNomeFile;
	/**
	 * @uml.property  name="arrayListDispositivi"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="model.CommercialDevice"
	 */
	private List<CommercialDevice> arrayListDispositivi;

	private LogicLoaderFirmware() {
		super();
		this.firmwareMapNomeFile = new HashMap<String,Firmware>();
		this.firmwareListaNomeFile = new ArrayList<Firmware>();
		this.arrayListDispositivi = new ArrayList<CommercialDevice>();
	}

	/**
	 * Metodo che permette di ottenere l'istanza della classe.
	 * @return istanza della classe.
	 */
	public static LogicLoaderFirmware getInstance() {
		return instance;
	}

	public List<Path> removeRenamedFirmare(List<Path> pathList) {
		List<Path> notRenamedFile = new ArrayList<Path>();
		for(Path path : pathList) {
			if(path.getFileName().toString().contains("Restore")) {
				if(firmwareMapNomeFile.get(path.getFileName().toString().split(".part")[0])!=null) {
					notRenamedFile.add(path);
				} else {
					try {
						Files.delete(path);
					} catch (IOException e) {
						LOGGER.error("removeRenamedFirmare() - Eccezione durante rimozione path: " + path.getFileName().toString() + ". Eccezione= " + e);
					}
				}
			}
		}
		return notRenamedFile;
	}


	/**
	 * Metodo per verificare se un dispositivo (identificato dal suo nome commerciale) e' presente
	 * nella lista dei dispositivi
	 * @param dispCommerciale String che rappresenta il nome commerciale del dispositivo.
	 * @return Un boolean, true se presente, false se non presente.
	 */
	public boolean isPresente(String dispCommerciale) {
		for(CommercialDevice dispositivoCom : arrayListDispositivi) {
			if(dispositivoCom.getNomeDispositivo().equals(dispCommerciale)) {
				return true;
			}
		}
		return false;
	}

	public void fillFirmwareComboBox() {
		for(CommercialDevice dispositivoCom : this.getArrayListDispositivi()) {
			MainFrame.getInstance().getListaDispositivi().addItem(dispositivoCom.getNomeCommerciale());
		}

		//inverto la lista dei firmware in modo tale che i piu' recenti appaiano all'inizio
		List<Firmware> listaFirmwareReverse = this.getFirmwareListaNomeFile();
		Collections.reverse(listaFirmwareReverse);

		for(Firmware firmware : listaFirmwareReverse) {
			if(firmware.getDevice().getNomeDispositivo().equals(this.cercaDispositivo((String)MainFrame.getInstance().getListaDispositivi().getSelectedItem()))) {
				MainFrame.getInstance().getListaFirmware().addItem(firmware.getVersion() + " (" + firmware.getBuild() + ")"); 
			}
		}
	}

	/**
	 * Metodo che dato un nomeCommerciale restituisce il nome in codice del Device.
	 * @param nomeCommerciale
	 * @return
	 */
	public String cercaDispositivo(String nomeCommerciale) {
		for(CommercialDevice dispositivoCom : arrayListDispositivi) {
			if(dispositivoCom.getNomeCommerciale().equals(nomeCommerciale)) {
				return dispositivoCom.getNomeDispositivo();
			}
		}
		return null; //non trovato
	}

	private OperativeSystem getCorrectInstanceOfOperativeSystem(String platform) {
		if(platform.equals("0")) {
			return new MacOsx(platform);
		} else {
			return new Windows(platform);
		}
	}

	/**
	 * Metodo per caricare i firmware dal database in locale.
	 */
	public void loadFirmware() {
		LOGGER.info("loadFirmware() - Avviato in percorso=" + super.getPercorsoDati().toString());		
		try (
				InputStream inputStream = new FileInputStream(super.getPercorsoDati().resolve(IPSWLISTA).toFile());
				InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
				BufferedReader br = new BufferedReader(inputStreamReader)
				) {
			String rigaFile = br.readLine();
			String[] letto;
			while(rigaFile!=null) {
				letto = rigaFile.split("___");
				if(letto.length>0) {
					Firmware firmware = new Firmware();
					String[] parziale1 = letto[0].split(",");
					//se la dimensione della splittata e' 2 e' ok, altrimenti e' l'url particolare dell'iphone 3,1 con 4.0
					//quindi il programma si bloccherebbe allora uso il length che corregge questo problema in questa situazione
					String[] parziale2 = parziale1[parziale1.length-1].split("_");
					String[] parziale3 = parziale1[parziale1.length-2].split("/");
					firmware.addOperativeSystem(this.getCorrectInstanceOfOperativeSystem("0")); 
					firmware.addOperativeSystem(this.getCorrectInstanceOfOperativeSystem("32"));
					firmware.addOperativeSystem(this.getCorrectInstanceOfOperativeSystem("64"));
					firmware.setDevice(new CommercialDevice(parziale3[parziale3.length-1] + "," + parziale2[0]));
					firmware.setVersion(parziale2[1]);
					firmware.setBuild(parziale2[parziale2.length-2]);
					firmware.setPercorso(letto[0]);
					firmware.setHash(letto[1].toUpperCase());
					firmware.setDimension(0);
					firmwareMapNomeFile.put(firmware.getFileName(), firmware);
					firmwareListaNomeFile.add(firmware);

					String disp = parziale3[parziale3.length-1] + "," + parziale2[0];
					if(!isPresente(disp)) {
						arrayListDispositivi.add((new CommercialDevice(disp)));
					}
				}
				rigaFile = br.readLine();
			}
			LOGGER.info("loadFirmware() - Riuscito");
		} catch (FileNotFoundException e2) {
			LOGGER.error("loadFirmware() - FileNotFoundException=", e2);
		} catch (IOException e2) {
			LOGGER.error("loadFirmware() - IOException=", e2);
		}
	}

	public Firmware ottieniFirmwareDaLista() {
		String nomeDispositivo = this.cercaDispositivo((String)MainFrame.getInstance().getListaDispositivi().getSelectedItem());
		String build = ((String)MainFrame.getInstance().getListaFirmware().getSelectedItem()).split(" ")[1].replace("(", "");
		build = build.replace(")", "");
		Firmware firmware = firmwareMapNomeFile.get(nomeDispositivo + "_" +
				((String)MainFrame.getInstance().getListaFirmware().getSelectedItem()).split(" ")[0] + "_" + build + "_Restore.ipsw");
		return firmware;
	}

	public List<Firmware> getFirmwareListaNomeFile() { 
		return firmwareListaNomeFile;
	}
	public List<CommercialDevice> getArrayListDispositivi() {
		return arrayListDispositivi;
	}
}
