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

package it.stefanocappa.gui.state;

import it.stefanocappa.gui.kcomponent.KColors;
import it.stefanocappa.gui.kcomponent.KFont;

import javax.swing.JLabel;

import it.stefanocappa.localization.Translator;

/**
 * Classe che si occupa di creare le JLabel dell'interfaccia grafica.
 * Attenzione: istanziata una sola volta (DownloadManager), inserita con il set in GraphicManager e poi per usarla
 * altrove si usa direttamente il get dalla classe GraphicManager
 */
public final class StateLabel {
	private static JLabel aggDatabaseLabel;
	private static JLabel aggProgrammaLabel;
	private static JLabel ultimoAggDatabaseLabel;

	private StateLabel() {}
	
	/**
	 * Costruttore che inizializza le label e ne imposta alcune caratteristiche.
	 */
	static {
		aggDatabaseLabel = new JLabel("  " + Translator.getText("databaseLabelVerifica"));
		aggDatabaseLabel.setFont(KFont.getInstance().getFont());
		aggDatabaseLabel.setForeground(KColors.getVerdeChiaro());
		aggProgrammaLabel = new JLabel("  " + Translator.getText("byamLabelVerifica"));
		aggProgrammaLabel.setFont(KFont.getInstance().getFont());
		aggProgrammaLabel.setForeground(KColors.getVerdeChiaro());
		ultimoAggDatabaseLabel = new JLabel("");
	}

	/**
	 * @return JLabel associata all'aggiornamento del database.
	 */
	public static JLabel getAggDatabaseLabel() {
		return aggDatabaseLabel;
	}
	
	/**
	 * @return JLabel associata all'aggiornamento del programma.
	 */
	public static JLabel getAggProgrammaLabel() {
		return aggProgrammaLabel;
	}

	public static JLabel getUltimoAggDatabaseLabel() {
		return ultimoAggDatabaseLabel;
	}
	
}
