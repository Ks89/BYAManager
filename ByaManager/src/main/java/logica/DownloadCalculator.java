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


import java.util.List;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;


/**
 * Classe CalcoloScaricato che si occupa di calcolare velocita',
 * tempo rimanente e data di fine download per ogni download in tutta la tabella.
 */
public class DownloadCalculator extends Thread {
	private static final Logger LOGGER = LogManager.getLogger(DownloadCalculator.class);
	private static final String VUOTO = "   ";
	/**
	 * @uml.property  name="abilitazione"
	 */
	private boolean abilitazione = true;
	/**
	 * @uml.property  name="velocitaGlobale"
	 */
	private long velocitaGlobale = 0;


	/**
	 * Metodo per abilitare o disabilitare il calcoloScaricato
	 * in base allo stato.
	 * @param stato boolean per impostare l'abilitazione o la disabilitazione.
	 */
	public void abilitaCalcoloScaricato(boolean stato) {
		abilitazione = stato;
	}

	/**
	 * @return un boolean con lo stato dell'abilitazione.
	 */
	public boolean getAbilitazione() {
		return this.abilitazione;
	}

	public void run() {
		long scaricato, tempoRimanente, velocita;

		while(abilitazione) {
			//inserisco ritardo di un secondo ad ogni scansione
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				LOGGER.error("run() - InterruptedException= " + e);
			}
			List<Download> downloadList = DownloadList.getInstance().getDownloadList();
			for(Download download : downloadList) {
				if(download.getStatus()==0) { //se il download e' in corso 

					//chiamo il calcolo della velocita in InsiemeDownload che si occupa 
					//anche di calcolare il valore di scaricato
					download.calcolaVelocita();

					//ottengo il valore di scaricato dall'insiemeDownload dopo averlo calcolato
					scaricato = download.getDownloadedBytes();

					//ottengo la velocita' in byte al secondo dall'insiemedownload (visto il ritardo di 1000)
					velocita = download.getVelocita();

					if(velocita>0) {
						velocitaGlobale += velocita;
						tempoRimanente = (download.getDimensioneTotale() - scaricato) / (velocita); //espresso in secondi
						//imposto la dataTermine nell'insieme download
						//imposto il tempoRimanente nell'insieme download
						download.setRemainingTime(this.getTempoRimanente(tempoRimanente));
					} else {
						//imposto velocita, data termine, tempo rimanente vuoti e a zero nel caso in cui la velocita<=0
						download.setSpeed(0);
						download.setRemainingTime(VUOTO);
					}

				} else {
					download.setSpeed(0);
					download.setRemainingTime(VUOTO);
				}
				download.stateChanged(false);
			}

			TableLogic.setVelocitaGlobale(velocitaGlobale);
			velocitaGlobale=0;
		}

		//se il thread non e' abilitato vuol dire che devo azzerare tutti i tempi e la velocita' nella tabella per ogni riga
		for(Download insiemeDownload : DownloadList.getInstance().getDownloadList()) {
			//imposto velocita, datatermine, temporimanente vuoti e a zero nel caso in cui la velocita<=0
			insiemeDownload.setSpeed(0);
			insiemeDownload.setRemainingTime(VUOTO);
			TableLogic.setVelocitaGlobale(0);
		}
	}

	/**
	 * Metodo per ottenere il tempo rimanente esprimendolo in ore, minuti e secondi,
	 * @param remaining long che rappresenta il tempo restante in secondi.
	 * @return String che rappresenta il tempo rimanente espresso in h + m + s su una stessa stringa.
	 */
	public String getTempoRimanente(long remaining) {
		long seconds=0, minutes=0, hours=0;
		long restante = remaining;
		
		while(restante > 0) {
			if(restante<=60) {
				seconds += restante;
				restante = 0; //cioe' ho finito perche' ho contato i secondi
			} else {
				if(restante<=60*60) {
					minutes += restante/(60);
					restante -= minutes*60;
				} else { //allora resta solo da contare le ore
					hours += restante/(60*60);
					restante -= hours*60*60;
				}
			}
		}
		return hours + "h " + minutes + "m " + seconds + "s";
	}
}
