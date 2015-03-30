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

package time;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Classe per gestire il tempo, cioe' l'ora del sistema, ottenere la data di ultima modifica
 * di un verto file, dire se una data e' maggiore di un'altra o incrementare una certa data di 
 * una certa quantita' di tempo
 */
public final class TimeManager {

	private static TimeManager instance = new TimeManager();
	
	/**
	 * Costruttore privato della classe.
	 */
	private TimeManager() {}
	
	/**
	 * Metodo che permette di ottenere l'istanza della classe
	 * @return La classe Tempo.
	 */
	public static TimeManager getInstance() {
		return instance;
	}

	/**
	 * Metodo per ottenere la data del sistema operativo
	 * @return Date che rappresenta la data del sistema operativo
	 */
	public Date getDataSistema() {
		return Calendar.getInstance().getTime();
	}

	/**
	 * Metodo per ottenere il long il numero di millisecondi
	 * rappresentanti la data del sistema operativo. Si occupa di richiamare 
	 * da solo il metodo getDataSistema().
	 * @return long rappresentante il numero di millisecondi della data del sistema operativo.
	 */
	public long getTempoSistema() {
		return this.getDataSistema().getTime();
	}

	/**
	 * Restituisce il numero di millisecondi di una data incrementata di una certa
	 * quantita' di ore.
	 * @param ore int che rappresenta il numero di ore di cui incrementare la data
	 * @return int che rappresenta la data in millisecondi risultate
	 */
	public int incrementaOre(int ore) {
		return ore * 60 * 60 * 1000; 
	}

	/**
	 * Restituisce il numero di millisecondi di una data incrementata di una certa
	 * quantita' di secondi.
	 * @param secondi int che rappresenta il numero di secondi di cui incrementare la data
	 * @return long che rappresenta la data in millisecondi risultate
	 */
	public long incrementaSecondi(int secondi) {
		return secondi * 1000L; //casta da solo in long grazie alla L alla fine di 1000 
	}


	/**
	 * Metodo che restituisce true se la data1 e' successiva alla data2 sommata ad un numero di secondi (offset).
	 * Se il terzo parametro e' 0 avviene un semplice controllo di date, altrimenti viene inserito
	 * un certo offset.
	 * @param data1 Data da confrontare
	 * @param data2 Data da confrontare
	 * @param ore int che rappresenta il numero di ore con cui incrementare la data2
	 * 			prima di confrontarla
	 * @return boolean: "true" se e' maggiore, "false" se minore
	 */
	public boolean isMaggioreDiOre (Date data1, Date data2, int ore) {
		return data1.after(new Date(data2.getTime() + this.incrementaOre(ore)));
	}


	/**
	 * Metodo per ottenere la data di fine download prevista
	 * @param restante long che rappresenta il tempo restante il millisecondi
	 * @return String formattata che rappresenta la data prevista per il completamento del download
	 */
	public String getDataFine(long restante){
		Date data = new Date(this.getDataSistema().getTime() + this.incrementaSecondi((int)restante));
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		return sdf.format(data.getTime());
	}
}
