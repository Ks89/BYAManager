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

package it.stefanocappa.gui.kcomponent;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;

/**
 *	Classe che rappresenta una font personalizzata caricata
 *	dalla cartella resources e personalizzata in stile, dimensione e colore.
 */
public final class KFont {
	private static KFont instance = new KFont();
	/**
	 * @uml.property  name="font"
	 */
	private Font font;

	/**
	 * Costruttore privato che carica la font e la imposta.
	 * Le eccezioni sono gestite internamente, perche' ne basta una
	 * che la font non viene personalizzata e impostata Arial bold a 13.
	 */
	private KFont() {
		try (
				InputStream inputStream = this.getClass().getResourceAsStream("/lg.ttf")
				) {
			Font caricata = Font.createFont(Font.TRUETYPE_FONT,inputStream);
			font = new Font(caricata.getFontName(), Font.BOLD, 13);
		} catch (FontFormatException e) {
			font = new Font("Arial", Font.BOLD, 13);
		} catch (IOException e) {
			font = new Font("Arial", Font.BOLD, 13);
		}
	}

	/**
	 * Metodo che permette di ottenere l'istanza della classe.
	 * @return istanza della classe FontPersonalizzata.
	 */
	public static KFont getInstance() {
		return instance;
	}

	/**
	 * @return  Font personalizzata oppure quella predefinita nel caso  non sia stato possibile caricare quella esterna.
	 * @uml.property  name="font"
	 */
	public Font getFont() {
		return font;
	}
}
