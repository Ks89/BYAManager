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

package it.stefanocappa.gui.table.renderer;

import it.stefanocappa.gui.image.ImageLoader;

import java.awt.Component;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JTable;

/**
 * Classe che estende e sovrascrive il cell render della prima colonna della tabella.
 * Tramite questa classe viene aggiunta alla cella l'icona del dispositivo.
 */
public class RendererColonna0 extends RendererColonnaComune {
	private static final long serialVersionUID = 1L;

	private BufferedImage disegnaLogoGenerico(boolean isSelected) {
		if(isSelected) {
			return ImageLoader.getInstance().getLogoGenericoNero();
		} else {
			return ImageLoader.getInstance().getLogoGenericoVerde();
		}
	}

	private BufferedImage drawItunesLogo(boolean isSelected) {
		if(isSelected) {
			return ImageLoader.getInstance().getLogoItunes10();
		} else {
			return ImageLoader.getInstance().getLogoItunes10();
		}
	}
	
	private BufferedImage drawFirmwareLogo(boolean isSelected) {
		if(isSelected) {
			return ImageLoader.getInstance().getLogoFirmware();
		} else {
			return ImageLoader.getInstance().getLogoFirmware();
		}
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		String nomeFile = (String)value;

		if(nomeFile.contains("AppleTV") || nomeFile.contains("iP")) {
			super.impostaGrafica(0, nomeFile, new ImageIcon(this.drawFirmwareLogo(isSelected)), isSelected);
			return this;
		}
		if(nomeFile.contains("iTunes")) {
			super.impostaGrafica(0, nomeFile, new ImageIcon(this.drawItunesLogo(isSelected)), isSelected);
			return this;
		} else {
			super.impostaGrafica(0, nomeFile, new ImageIcon(this.disegnaLogoGenerico(isSelected)), isSelected); //0 = imposta tutto sia icona sia testo
			return this;
		}
	} 
}