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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import model.FileWeb;
import model.Firmware;
import notification.Notification;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import fileutility.CheckSha1;
import fileutility.FileList;
import gui.state.StateButton;
import gui.table.TableGui;

public class Restorer extends LogicLoader{
	private static final Logger LOGGER = LogManager.getLogger(Restorer.class);
	private static final String PART = ".part";
	private static final String SHA = ".sha";

	/**
	 * @uml.property  name="inizio1"
	 */
	private long inizio1;
	/**
	 * @uml.property  name="inizio2"
	 */
	private long inizio2;
	/**
	 * @uml.property  name="inizio3"
	 */
	private long inizio3;
	/**
	 * @uml.property  name="inizio4"
	 */
	private long inizio4;
	/**
	 * @uml.property  name="trovati"
	 */
	private List<String> trovati;

	private void moveShaFileFromTemp() {
		for(Path path : FileList.getFileList(super.getPercorsoDownloadTemp())) {
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
		LOGGER.info("ripristinaDownload() - Avviato metodo");

		//Sposta eventuali file sha contenuti nella cartella temp insieme ai download per poterli controllare dopo
		this.moveShaFileFromTemp();

		//Prima ottengo la lista dei file validi tenendo solo i firmware
		List<Path> validFileList = super.getValidFileList(super.getPercorsoDownloadTemp());

		//dopo cancello dal disco e dalla lista i file che non sono presenti nel database dei firmware, o per difetti o perche' rinominati manualmente
		List<Path> fileListNotRenamed = LogicLoaderFirmware.getInstance().removeRenamedFirmare(validFileList);

		//ora faccio lo stesso con le versioni di itunes e metto la lista dei file risultante alla fine di quella dei firmware, in questo
		//modo nella list fileListNotRenamed ho all'inizio i firmware e dopo le versioni di iTunes ripulite da eventuale rinominazioni o file non associabili a voci del database
		fileListNotRenamed.addAll(LogicLoaderItunes.getInstance().removeRenamedItunesVersion(validFileList));


		//chiamo il metodo per verificare la presenza tutti e 4 i file part passandogli come parametro
		//la lista dei file validi e senza file rinominati, ottenuta con gli appositi metodi. Se anche solo una part non e' valida,
		//vengono rimosse anche tutte le altre part associata a quel download.
		//Metodo universale che non dipende dal tipo di download.
		this.trovati = this.verificaPresenzaParti(fileListNotRenamed);

		//una volta verificata la presenza delle parti so che tutti i part sono validi.
		//Cosi controllo se vi sono file .sha e contemporaneamente anche i suoi .part. Se si, rimuovo lo sha in modo
		//che al riavvio il programma cotninui dai part (che potrebbero gia' essere terminati) avviando l'unione a la verifica sha,
		//altrimenti il metodo seguente notifica nel logger la presenza di un file .sha non gestibile
		this.rimuoviFileShaRicomponibili(this.getShaList(super.getPercorsoDownload()), validFileList);

		//avvio l'insieme download
		this.avviaDownloadRipresi();

		StateButton.getPreferenzeButton().setEnabled(true);

		if(trovati.size()>=1) {
			StateButton.attivaPulsantiAllAlRiavvio();
		}

		LOGGER.info("ripristinaDownload() - Terminato");
	}

	/**
	 * Metodo che rimuove i file .sha che possono essere facilmente ricreati ripartendo dai .part. Questo metodo non si
	 * assicura che ci siano tutte e 4, semplicemente un metodo prima rimuove dalla listaFileTemp quelli che non hanno
	 * 4 parti e poi viene passato a questo metodo solo quelli validi.
	 * @param shaPathList List<File> di tutti quelli con il nome che contiene ".sha" presenti nella cartella percorsoDownload.
	 * @param tempPathList List<File> di tutti i .part con tutte e 4 le parti presenti nella cartella percorsoDownloadTemp.
	 */
	private void rimuoviFileShaRicomponibili(List<Path> shaPathList, List<Path> tempPathList) {
		List<String> nomiFilePartiString = new ArrayList<String>();
		for(Path tempPath : tempPathList) {
			nomiFilePartiString.add(tempPath.getFileName().toString().split(".part")[0]);
		}

		//non serve verificare se il file ha gia' .sha nel percorso perche' la lista
		//che riceve (listaFileSha) e' gia costituita dai soli file .sha
		for(Path path : shaPathList) {
			if(Files.exists(path) && nomiFilePartiString.contains(path.getFileName().toString().replace(SHA, ""))) {
				//vuol dire che i file part sono presenti e che il .sha e' inutile
				try {
					Files.delete(path);
				} catch (IOException e) {
					LOGGER.info("rimuoviFileShaRicomponibili() - Eccezione durante rimozione di " + path.toAbsolutePath().toString() + ". Eccezione= " + e);
				}
			} else {
				LOGGER.info("ATTENZIONE! - Rilevato file SHA senza le parti per poterlo ricomporre");
				LOGGER.info("rimuoviFileShaRicomponibili() - SHA senza parti rilevato=" + path.toAbsolutePath().toString());
				if(Notification.clickedYesInConfirmDialog("restorerShaCheckConfirm")) {
					try {
						Files.delete(path);
					} catch (IOException e) {
						LOGGER.info("rimuoviFileShaRicomponibili() - Eccezione durante rimozione di " + path.toAbsolutePath().toString() + ". Eccezione= " + e);
					}
				} else {
					this.ripristinaFileWebValidation(path);
				}
			}
		}
	}


	private void ripristinaFileWebValidation(Path path) {
		FileWeb fileAssociato = null;
		try {
			String hashFileScaricato = CheckSha1.getSha1(path);

			if(path.getFileName().toString().contains("Restore")) {
				fileAssociato = LogicLoaderFirmware.getInstance().getFirmwareMapNomeFile().get(path.getFileName().toString().replace(SHA, ""));
			} else {
				if(path.getFileName().toString().startsWith("iTunes")) {
					fileAssociato = LogicLoaderItunes.getInstance().getiTunesMapNomeFile().get(path.getFileName().toString().replace(SHA, ""));
				} 
				//				else {
				//					fileAssociato = LogicLoaderJailbreak.getInstance().getMapFileName().get(file.getName().replace(".sha", ""));
				//				}
			}

			if(fileAssociato==null || hashFileScaricato==null) {
				Notification.showNormalOptionPane("restorerInternalError");
				return; //se non trovo il fileAssociato o hash nullo termino subito senza fare altri controlli
			}

			if(CheckSha1.isCorrect(hashFileScaricato, fileAssociato.getHash())) {
				//rinomino usando il metodo move perche' non posso fare altrimenti :)
				Files.move(path, super.getPercorsoDownload().resolve(path.getFileName().toString().replace(SHA, "")));
				Notification.showNormalOptionPane("restorerSameSha");
			} else {
				try {
					Files.delete(path);
				} catch (IOException e) {
					LOGGER.info("ripristinaFileWebValidation() - Eccezione durante rimozione di " + path.toAbsolutePath().toString() + ". Eccezione= " + e);
				}
				Notification.showNormalOptionPane("restorerDifferentSha");
			}
		} catch (IOException e) {
			Notification.showNormalOptionPane("restorerIOException");
			try {
				Files.delete(path);
			} catch (IOException e1) {
				LOGGER.info("ripristinaFileWebValidation() - Eccezione durante rimozione di " + path.toAbsolutePath().toString() + ". Eccezione= " + e1);
			}
		}
	}

	private List<String> verificaPresenzaParti(List<Path> pathFile) {
		int cont = 0;
		List<String> trovatiDaVerifica = new ArrayList<String>();
		for(Path path1 : pathFile) { //uso file1 e file2 per confrontare i file part
			for(Path path2 : pathFile) {
				if(path2.getFileName().toString().split(PART)[0].equals(path1.getFileName().toString().split(PART)[0])) {
					cont++; //se per il primo file1 ho trovato altri 3 (piu' se stesso) legati ad esso diventa == 4
				}
			}
			if(cont==4 && !trovatiDaVerifica.contains(path1.getFileName().toString().split(PART)[0])) {
				trovatiDaVerifica.add(path1.getFileName().toString().split(PART)[0]);
				LOGGER.info("verificaPresenzaParti() - String trovati=" + path1.toAbsolutePath().toString().split(PART)[0]);
			}
			cont=0;
		}
		this.removeUnusableParts(pathFile, trovatiDaVerifica);
		return trovatiDaVerifica;
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
					inizio1 = Files.size(super.getPercorsoDownloadTemp().resolve(trovato + ".part1"));
					inizio2 = Files.size(super.getPercorsoDownloadTemp().resolve(trovato + ".part2"));
					inizio3 = Files.size(super.getPercorsoDownloadTemp().resolve(trovato + ".part3"));
					inizio4 = Files.size(super.getPercorsoDownloadTemp().resolve(trovato + ".part4"));

					Download download;
					if(fileWeb instanceof Firmware) {
						download = new DownloadFirmware(super.getPercorsoDownload(), fileWeb);
					} else { //se e' un itunesversion o un softwarejailbreak creo un DownloadSoftware
						download = new DownloadSoftware(super.getPercorsoDownload(), fileWeb);
					}
					download.continueDownload(inizio1, inizio2, inizio3, inizio4);
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
