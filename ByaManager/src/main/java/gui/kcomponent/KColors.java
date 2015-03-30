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
package gui.kcomponent;

import java.awt.Color;

/**
 *	Classe final che implementa singleton e che contiene
 *	i colori da ottenere in modo statico.
 */
public final class KColors {
	
	private static Color verde;
	private static Color verdeChiaro;
	private static Color nero;
	private static Color neroScuroTabella;
	private static Color grigietto;
	private static Color biancoTrasparente;
	private static KColors instance = new KColors();

	/**
	 * Costruttore privato che imposta i colori. Nota che la classe Colori viene creata nel DownloadManager
	 * semplicemente chiamando l'istanza e senza usare l'oggetto, perche' serve solo per crearla.
	 * Una volta fatto tutte le altre classi la potranno usare.
	 */
	private KColors() {
		verde = new Color(186,194,44);
		verdeChiaro = new Color(214,222,71);
		nero = new Color(45,45,45);
		neroScuroTabella = new Color(30,30,30);
		grigietto = new Color(100,100,100);
		biancoTrasparente = new Color(255,255,255,38);
	}
	
	
	/**
	 * Metodo che permette di ottenere l'istanza della classe.
	 * @return istanza della classe Colori.
	 */
	public static KColors getInstance() {
		return instance;
	}

	/**
	 * @return Color verde.
	 */
	public static Color getVerde() {
		return verde;
	}

	/**
	 * @return Color verde chiaro.
	 */
	public static Color getVerdeChiaro() {
		return verdeChiaro;
	}

	/**
	 * @return Color nero.
	 */
	public static Color getNero() {
		return nero;
	}
	
	/**
	 * @return Color nero scuro della tabella.
	 */
	public static Color getNeroScuroTabella() {
		return neroScuroTabella;
	}
	
	/**
	 * @return Color grigio.
	 */
	public static Color getGrigietto() {
		return grigietto;
	}
	
	/**
	 * @return Color bianco trasparente.
	 */
	public static Color getBiancoTrasparente() {
		return biancoTrasparente;
	}
}
