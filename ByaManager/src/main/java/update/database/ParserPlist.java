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
 *	Classe che si occupa di fare il parsing del file version.plist creato da "VersionXml".
 */
public final class ParserPlist {
	/**
	 * @uml.property  name="listaDispositivi" multiplicity="(0 -1)" dimension="1"
	 */
	private String[] listaDispositivi;
	/**
	 * @uml.property  name="verificoPresenzaSha1" multiplicity="(0 -1)" dimension="1"
	 */
	private String[] verificoPresenzaSha1;
	/**
	 * @uml.property  name="listaFirmware"
	 */
	private List<Firmware> listaFirmware;
	/**
	 * @uml.property  name="dictionary"
	 * @uml.associationEnd  
	 */
	private NSDictionary dictionary;
	/**
	 * @uml.property  name="dictionaryFinale"
	 * @uml.associationEnd  
	 */
	private NSDictionary dictionaryFinale;

	/**
	 * Costruttore della classe ParserPlist che inizializza la listaFirmware come un ArrayList.
	 */
	public ParserPlist() {
		listaFirmware = new ArrayList<Firmware>();
		//i seguenti vengono poi reinizializzati in seguito
		dictionary = null;
		dictionaryFinale = null;
		listaDispositivi = null;
	}
	
	/**
	 * Esegue il parsing del plist e restituisce la lista dei Firmware.
	 * @return Una List dei Firmware contenuti nel plist.
	 * @throws Exception Eccezione generica.
	 */
	public List<Firmware> effettuaParsing() throws Exception {
		Path path = User.getInstance().getDataPath().resolve("version.xml");
		Firmware firmware = null;
		//ottendo la root
		dictionary = (NSDictionary)PropertyListParser.parse(path.toFile());

		//ottengo l'oggetto con chiave specificata per avere gli aggiornamento
		dictionary = (NSDictionary)dictionary.objectForKey("MobileDeviceSoftwareVersionsByVersion");

		//ottengo la lista con dentro numeri (String) che identificano il numero dei dispositivi rilasciati
		String[] numeroDispositivi = dictionary.allKeys();

		//vado a prendere sempre l'ultimo numero che identifica la situazione piu' recente con tutti i dispositivi usciti
		dictionary = (NSDictionary)dictionary.objectForKey(numeroDispositivi[numeroDispositivi.length-1]); //scelta tipo firmare

		//ora devo scegliere tra MobileDeviceSoftwareVersions o RecoverySoftwareVersions
		//tengo il riferimento per usarlo nel metodo scansionaBuild senza perdere quello iniziale
		dictionaryFinale = (NSDictionary)dictionary.objectForKey("MobileDeviceSoftwareVersions");

		//ora ottengo la lista dei dispositivi
		listaDispositivi = dictionaryFinale.allKeys();

		//scansiono tutti i dispositivi andando a prendere i dati del loro firmware piu' recente e lo aggiunto
		//nella lista da restituire in uscita
		for(int i=0; i<listaDispositivi.length;i++) {
			this.entraInRestore(listaDispositivi[i]); //accede al campo Restore del plist mettendo in dictionary l'NSDictionary associato
			firmware = this.ottieniFirmware(i);
			if(firmware!=null) {
				listaFirmware.add(firmware); //aggiunge firmware alla lista (invocando anche il metodo per crearlo)
			}
		}
		return listaFirmware;
	}

	
	/**
	 * Metodo che entra nel dictionary "restore" del file plist, dato il dispositivo.
	 * @param dispositivo String che rappresenta il dispositivo.
	 */
	private void entraInRestore(String dispositivo) {
		//riciclo il dictionary usato sopra ad ogni ciclo mantenendo il riferimento a dictionaryFinale
		dictionary = (NSDictionary)dictionaryFinale.objectForKey(dispositivo);

		//scelgo la build Unknown che e' sempre l'ultima
		dictionary = (NSDictionary)dictionary.objectForKey("Unknown");

		//accedo a Universal
		dictionary = (NSDictionary)dictionary.objectForKey("Universal");

		//accedo a Restore
		dictionary = (NSDictionary)dictionary.objectForKey("Restore");
	}


	/**
	 * Metodo per ottenere un firmware, dato l'indice.
	 * @param indice int che rappresenta l'indice.
	 * @return Il Firmware richiesto.
	 */
	private Firmware ottieniFirmware(int indice) {
		boolean trovato = false;
		Firmware firmware = null;
		if(!(dictionary.objectForKey("FirmwareURL").toString().contains("protected"))) {
			
			//vuol dire che il link NON e' proibito da apple e quindi  puo' essere usato
			verificoPresenzaSha1 = dictionary.allKeys();
			for(String firmwareInPlist : verificoPresenzaSha1) {
				if(firmwareInPlist.equals("FirmwareSHA1")) {
					//se il firmware ha la voce SHA1 quando creo il firmware la inserisco, altrimenti no
					//vedi metodo sotto (creaFirmware)
					trovato = true; 
				}
			}	 
			
			//ora ritorno il firmware che creo col metodo creaFirmware alla lista
			firmware = this.creaFirmware(indice, trovato);
		}
		return firmware;
	}

	
	/**
	 * Metodo per creare un firmware.
	 * @param indice int che rappresenta l'indice da cui prelevare il dispositivo.
	 * @param trovato se e' true indica che il firmware ha lo SHA1, altrimenti no.
	 * @return
	 */
	private Firmware creaFirmware(int indice, boolean trovato) {
		//creo il Firmware
		Firmware firmware = new Firmware();
		firmware.setDevice(new CommercialDevice(listaDispositivi[indice]));
		firmware.setVersion(dictionary.objectForKey("ProductVersion").toString());
		firmware.setBuild(dictionary.objectForKey("BuildVersion").toString());
		firmware.setPercorso(dictionary.objectForKey("FirmwareURL").toString());
		//se nel plist non c'e' lo SHA1 non lo inserisco e ci metto "non presente"
		if(trovato) {
			firmware.setHash(dictionary.objectForKey("FirmwareSHA1").toString().toUpperCase());
		} else {
			firmware.setHash("non presente");
		}
		//non posso conoscere la dimensione dal file version.xml, quindi la metto sempre a 0
		firmware.setDimension(0); 
		return firmware;
	}
}