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

import model.CommercialDevice;
import model.Firmware;
import notification.Notification;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;

import fileutility.HttpFileDownload;

import time.TimeManager;
import update.LinkServer;


/**
 * Classe che riga su un altro thread per verificare la presenza di nuovi aggiornamenti dei firmware
 * Si occupa di chiamare il download di version.xml.
 * Dopo scansiona il file con la lista dei firmware e verifica se ci sono nuove versioni.
 * In ogni caso crea un nuovo file _new.txt con dentro tutti i firmware.
 * Se il file e' nuovo allora viene notificata a schermo la presenza di un database firmware aggiornato,
 * altrimenti il file _new.txt viene cancellato.
 * Il controllo avviene tramite SHA1 dei file. Se uguali il file e' lo stesso, altriementi e' cambiato.
 */
public final class FirmwareUpdates extends Observable implements Runnable {
	private static final String UNDERSCORE3 = "___";
	private static final String IPSWLIST = "ipswLista.txt";
	private static final String IPSWLISTNEW = "ipswLista_new.txt";
	private static final String VERSIONXMLNAME = "version.xml";

	private static final Logger LOGGER = Logger.getLogger(FirmwareUpdates.class);

	/**
	 * @uml.property  name="firmwareListNew"
	 */
	private List<Firmware> firmwareListNew;
	/**
	 * @uml.property  name="firmwareListToWrite"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="model.Firmware"
	 */
	private List<Firmware> firmwareListToWrite;

	/**
	 * @uml.property  name="dataPath"
	 */
	private Path dataPath;
	/**
	 * @uml.property  name="aggiornatoDatabase"
	 */
	private boolean aggiornatoDatabase = false;

	/**
	 * Costruttore della classe che si occupa di inizializzare l'istanza di downloadmanager,
	 * percorsiOs, la lista di firmware da scrivere ed il logger.
	 */
	public FirmwareUpdates (Path dataPath) {
		this.firmwareListToWrite = new ArrayList<Firmware>();
		this.dataPath = dataPath;
	}


	/**
	 * Leggo il file ipswLista.txt riga per riga e aggiungo in coda
	 * alla lista dei firmwareDaScrivere.
	 * @throws IOException
	 */
	private void leggiTxt() throws IOException {
		LOGGER.info("leggiTxt() - Avviato metodo");
		try (
				BufferedReader br = Files.newBufferedReader(dataPath.resolve(IPSWLIST), Charset.defaultCharset())
				) {	
			String rigaAttuale = br.readLine();
			String[] letto;
			Firmware firmware;
			while(rigaAttuale!=null) {
				if(!rigaAttuale.startsWith(":")) { //ignoro le righe con i ":"
					letto = rigaAttuale.split(UNDERSCORE3);
					String[] parziale1 = letto[0].split(",");
					//se la dimensione della splittata e' 2 e' ok, altrimenti e' l'url particolare dell'iphone 3,1 con 4.0
					//quindi il programma si bloccherebbe allora uso il length che corregge questo problema in questa situazione
					String[] parziale2 = parziale1[parziale1.length-1].split("_");
					String[] parziale3 = parziale1[parziale1.length-2].split("/");

					firmware = new Firmware();
					firmware.setDevice(new CommercialDevice(parziale3[parziale3.length-1] + "," + parziale2[0]));
					firmware.setVersion(parziale2[1]);
					firmware.setBuild(parziale2[parziale2.length-2]);
					firmware.setPercorso(letto[0]);
					firmware.setHash(letto[1].toUpperCase());
					firmware.setDimension(0);
					firmwareListToWrite.add(firmware);
				}
				rigaAttuale = br.readLine();
			}
		}
		LOGGER.info("leggiTxt() - Terminato metodo");
	}

	/**
	 * Metodo per caricare il file di testo e caricare nella lista dei firmware 
	 * da scrivere eventuali nuovi firmware usciti.
	 * @throws IOException
	 */
	private void caricaTxt() throws IOException{
		LOGGER.info("caricaTxt() - Avviato metodo");

		boolean trovato;
		//listaNuoviFirmware = lista dei firmare presenti nell'xml
		//listaFirmwareDaScrivere = lista con tutti i firmware attuali gia' caricati
		//se in listaNuoviFirmware ci sono versioni che non sono gia' in listaFirmwareDaScrivere
		//allora vengono aggiunti alla listaFirmwareDaScrivere.
		for(Firmware firmwareN : firmwareListNew) {	
			trovato=false;
			for(Firmware firmwareScrivere : firmwareListToWrite) {			
				if(firmwareN.getUri().equals(firmwareScrivere.getUri())) {
					trovato=true;
					break;
				}
			}
			//controllo se non ho trovato il firmware nella lista dei firmware da scrivere
			//vuol dire che e' nuovo e quindi lo aggiungo a questa lista
			if(!trovato) {
				firmwareListToWrite.add(firmwareN);
			}
		}

		LOGGER.info("caricaTxt() - Terminato metodo");
	}

	/**
	 * Metodo per salvere la lista dei firmware aggiornata su file.
	 * @throws IOException
	 */
	private void salvaTxt() throws IOException{
		LOGGER.info("salvaTxt() - Avviato metodo");
		//ora scrivo tutte la lista di firmware (sotto forma di righe del file) sul nuovo file _new.txt

		try (
				BufferedWriter out = Files.newBufferedWriter(dataPath.resolve(IPSWLISTNEW), Charset.defaultCharset())
				) {
			for(int i=0;i<firmwareListToWrite.size();i++) {
				//ora leggo il file appena salvato fino alla fine.
				if(i==firmwareListToWrite.size()-1) {
					out.write(firmwareListToWrite.get(i).toString());
				} else {
					out.write(firmwareListToWrite.get(i).toString()+"\n");
				}
			}
			LOGGER.info("salvaTxt() - Terminato metodo");
		}
	}

	

	/**
	 * Verifica lo SHA1 del nuovo database appena scaricato con quello 
	 * precedente gia' sul pc. 
	 * @return boolean 'true' indica che non ci sono aggiornamenti, 'false' invece ce ne sono.
	 * @throws IOException 
	 */
	private boolean verificaSHA1() throws IOException {
		LOGGER.info("verificaSHA1() - Avviato metodo");

		InputStream newStream = Files.newInputStream(dataPath.resolve(IPSWLISTNEW));
		InputStream originaleStream = Files.newInputStream(dataPath.resolve(IPSWLIST));

		String newSha1 = DigestUtils.sha1Hex(newStream);
		String originaleSha1 = DigestUtils.sha1Hex(originaleStream);

		newStream.close();
		originaleStream.close();

		LOGGER.info("verificaSHA1() - Terminato metodo");
		return !newSha1.equals(originaleSha1);
	}


	/**
	 * Aggiorna il database richiamando i metodi per scaricare il version.xml,
	 * analizza l'xml caricando i dati nella lista
	 * dei nuovi firmware, legge il database sul pc, carica nuovi firmware leggi nella lista,
	 * salva il nuovo database, lo confronta con quello precedente tramite SHA1 e cancella i vari file
	 * temporanei usati durante il processo.
	 * @return boolean
	 * @throws Exception
	 */
	private boolean aggiornaDatabaseDaXml() throws Exception {
		LOGGER.info("aggiornaDatabase() - Avviato metodo");

		//scarico ed eseguo il parsing del file version.xml e carico tutti i nuovi firmware nell'arrayList di Firmware.
		ParserPlist parser = new ParserPlist();
		LOGGER.info("aggiornaDatabaseDaXml() - Download version.xml avviato"); 
		HttpFileDownload.httpFileDownload(dataPath.resolve(VERSIONXMLNAME), LinkServer.LINKXMLAPPLE);

		LOGGER.info("aggiornaDatabaseDaXml() - Download version.xml completato"); 
		firmwareListNew = parser.effettuaParsing();

		//carico e leggo il txt mettendo tutti i firmware nell'arraylist "listaFirmwareDaScrivere"
		this.leggiTxt();
		this.caricaTxt();

		//salvo riga per riga il contenuto di listaFirmwareDaScrivere su file di testo
		this.salvaTxt();

		//verifico lo SHA1 dei 2 file IPSW aggiornato dal server e IPSW con i nuovi firmware estratti dall'xml
		boolean aggiornamenti = this.verificaSHA1(); //NB: non centra a nulla ipswlista_download, qui si parla di ipswLista_new!!!

		LOGGER.info("aggiornaDatabase() - Terminato metodo");
		return aggiornamenti;
	}

	private void removeXmlAndPlist() {
		try {
			Files.deleteIfExists(dataPath.resolve("version_new.plist"));
			Files.deleteIfExists(dataPath.resolve(VERSIONXMLNAME));
		} catch (IOException e) {
			LOGGER.error("removeXmlAndPlist() - IOException = " + e);
		}
	}


	private void updateFirmwareDb() throws Exception {
		TimeManager dataSistema = TimeManager.getInstance();

		//ottengo la data di ultima modifica del database
		Path path = dataPath.resolve(IPSWLIST);
		Date dataFile = new Date(Files.getLastModifiedTime(path).toMillis());
		LOGGER.info("run() - DataFile : " + dataFile.toString());

		//verifico se la data del sistema e' maggiore della data del db di almeno 24 ore
		if(dataSistema.isMaggioreDiOre(dataSistema.getDataSistema(), dataFile, 24)) {
			LOGGER.info("run() - Da aggiornare database perche' : dataSistema>dataFile+1Giorno");

			//scarico e converto il file version.xml in version.plist con struttura standard
			//se aggiorna=true eseguo aggiornamento
			boolean aggiorna = this.aggiornaDatabaseDaXml();
			if(aggiorna) { 
				//CI SONO AGGIORNAMENTI!!!!!!

				//avviso l'utente di riavviare il prgoramma
				//per far si che avvenga lo scambio dei file (non fatto in questa classe,
				//ma al prossimo avvio del downloadManager nella gestioneAggiornamento)
				Notification.showNormalOptionPane("checkDBUpdatesRestartToUpdate");
				aggiornatoDatabase = true;
			} else {
				//se sono uguali vuol dire che il file _new.txt (cioe' il database attuale con aggiunti i nuovi firmware estratti dall'xml 
				//e' inutile, cosi' lo cancello
				Files.delete(dataPath.resolve(IPSWLISTNEW));
				LOGGER.info("run() - Rimosso ipswLista_new.txt con successo");
			}			

			//cancello i file version.xml e version_new.plist
			this.removeXmlAndPlist();

			//IMPORTANTISSIMO
			//per far si che al prossimo avvio il programma non vada a controllare ancora gli aggiornamenti,
			//dato che fino a che non si sostituisce ipswLista con il _downloaded la data resta indietro a quella di sistema di almeno 24 ore e quindi per forza
			//piu' vecchia di 1 giorno di qualunque altra data sensata, modifico la data di ultima modifica del file a mano con quella di sistema
			Files.setLastModifiedTime(path, FileTime.fromMillis(dataSistema.getTempoSistema()));
		} else {
			LOGGER.info("run() - Non aggiornato database perche' : dataSistema<dataFile+1Giorno");
			//database non aggiornato, visto che e' piu' recente di 24 ore
		}
	}

	@Override
	public void run() {
		LOGGER.info("run() - Avviato metodo");
		try {
			this.updateFirmwareDb();  //aggiorno firmware db (il try e' individuale perche' non deve crashare anche l'update itunes in caso di problemi)
		} catch (Exception e) {
			//se entro qui e' perche' non e' stato possibile aggiornare tramite il database xml
			//eccezione lanciata da aggiornaDatabaseDaXml()
			LOGGER.error("run() - Exception=" , e);
			Notification.showErrorOptionPane("checkDBUpdatesGenericError", "CheckDBUpdates Error");
		}
		stateChanged();
		LOGGER.info("run() - Terminato metodo");
	}

	public boolean isAggiorna() {
		return aggiornatoDatabase;
	}

	/**
	 * Metodo per notificare un cambiamento all'observer, cioe' a #UpdateManagerDB.
	 */
	public void stateChanged() {
		setChanged();
		notifyObservers();
	}
}