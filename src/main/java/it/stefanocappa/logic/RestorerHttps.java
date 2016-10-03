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

package it.stefanocappa.logic;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import it.stefanocappa.model.FileWeb;
import it.stefanocappa.model.Firmware;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import it.stefanocappa.fileutility.FileList;
import it.stefanocappa.gui.state.StateButton;
import it.stefanocappa.gui.table.TableGui;

public class RestorerHttps extends LogicLoader{
	private static final Logger LOGGER = LogManager.getLogger(RestorerHttps.class);
	private static final String PART = ".part";
	private static final String SHA = ".sha";

	private long inizio1;
	private List<String> trovati;

	private void moveShaFileFromTemp() {
		for(Path path : FileList.getFileList(super.getPercorsoDownloadHttpsTemp())) {
			if(path.endsWith(SHA)) {
				try {
					Files.move(path, super.getPercorsoDownload().resolve(path.getFileName()));
				} catch (IOException e) {
					LOGGER.error("moveShaFileFromTemp() - Errore durante la rinominazione del file = " + e);
				}
			}
		}
	}

	public void ripristinaDownload() {
		LOGGER.info("ripristinaDownload() Https - Avviato metodo");

		//Sposta eventuali file sha contenuti nella cartella temp insieme ai download per poterli controllare dopo
		this.moveShaFileFromTemp();

		//Prima ottengo la lista dei file validi tenendo solo i firmware
		List<Path> validFileList = super.getValidFileList(super.getPercorsoDownloadHttpsTemp());

		//dopo cancello dal disco e dalla lista i file che non sono presenti nel database dei firmware, o per difetti o perche' rinominati manualmente
		List<Path> fileListNotRenamed = LogicLoaderFirmware.getInstance().removeRenamedFirmare(validFileList);

		//ora faccio lo stesso con le versioni di itunes e metto la lista dei file risultante alla fine di quella dei firmware, in questo
		//modo nella list fileListNotRenamed ho all'inizio i firmware e dopo le versioni di iTunes ripulite da eventuale rinominazioni o file non associabili a voci del database
		fileListNotRenamed.addAll(LogicLoaderItunes.getInstance().removeRenamedItunesVersion(validFileList));

		this.trovati = new ArrayList<>();
		
		for(Path path1 : fileListNotRenamed) { 
			this.trovati.add(path1.getFileName().toString().split(PART)[0]);
			LOGGER.info("verificaPresenzaParti() - String trovati=" + path1.toAbsolutePath().toString().split(PART)[0]);
		}
		
		//avvio l'insieme download
		this.avviaDownloadRipresi();

		StateButton.getPreferenzeButton().setEnabled(true);

		if(trovati!=null && trovati.size()>=1) {
			StateButton.attivaPulsantiAllAlRiavvio();
		}

		LOGGER.info("ripristinaDownload() - Terminato");
	}


	private void avviaDownloadRipresi() {
		for(String trovato : this.trovati) {

			FileWeb fileWeb = null;
			if(trovato.contains("Restore")) {
				fileWeb = LogicLoaderFirmware.getInstance().getFirmwareMapNomeFile().get(trovato);
			} else {
				if(trovato.startsWith("iTunes")) {
					fileWeb = LogicLoaderItunes.getInstance().getiTunesMapNomeFile().get(trovato);
				} 
				//				else {
				//e' di certo un JailbreakSoftware
				//					fileWeb = LogicLoaderJailbreak.getInstance().getMapFileName().get(trovato);
				//				}
			}

			if(fileWeb!=null) {
				AddingDownload.getInstance().putFileWeb(fileWeb);
				try {
					inizio1 = Files.size(super.getPercorsoDownloadHttpsTemp().resolve(trovato + ".part1"));

					Download download;
					if(fileWeb instanceof Firmware) {
						download = new DownloadFirmware(super.getPercorsoDownload(), fileWeb);
					} else { //se e' un itunesversion o un softwarejailbreak creo un DownloadSoftware
						download = new DownloadSoftware(super.getPercorsoDownload(), fileWeb);
					}
					
					//qui passo inizio1 al primo process, mentre gli altri 0, tanto non saranna avviati nel caso di
					//download https
					download.continueDownload(inizio1, 0, 0, 0);
					LogicManager.getInstance().avviaThreadInsiemeDownload(download);
				} catch (IOException e) {
					LOGGER.error("avviaDownloadRipresi() - Eccezione nel calsolo della dimensione di un .part= " + e); 
				}
			}
		}
		this.gestisciSelezioneRighe();
	}

	private void gestisciSelezioneRighe() {
		//finito il caricamento dei download, se c'e' una riga selezionata
		//aggiorno subito i suoi pulsanti
		if(TableGui.getInstance().getSelectedRow()!=-1) {
			Download selectedDownload = DownloadList.getInstance().getDownload(TableGui.getInstance().getSelectedRow());
			if(selectedDownload!=null) {
				StateButton.aggiornaPulsanti(selectedDownload.getStatus());
			}
		}
	}
}
