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

package gui.state;

import gui.kcomponent.KMenuItem;
import gui.kcomponent.KPopupMenu;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import localization.Translator;

/**
 *	Classe che si occupa di creare il PopupMenu cliccando col tasto destro su un download.
 */
public class StateTablePopupItem {

	private JMenuItem pausaItem;
	private JMenuItem riprendiItem;
	private JMenuItem rimuoviItem;
	private JMenuItem mostraItem;
	private JMenuItem urlItem;
	private JMenuItem disableShaCheck;

	private static final int DOWNLOADING = 0;
	private static final int PAUSED = 1;
	private static final int VALIDATION = 5;

	public StateTablePopupItem() {
		pausaItem = new KMenuItem(Translator.getText("pausaItem"),0,false, true);
		riprendiItem = new KMenuItem(Translator.getText("riprendiItem"),0,false, false);
		rimuoviItem = new KMenuItem(Translator.getText("rimuoviItem"),0,true, false);
		disableShaCheck = new KMenuItem(Translator.getText("disableShaCheck"),0,true, false);
		mostraItem = new KMenuItem(Translator.getText("mostraItem"),0,false, false);
		urlItem = new KMenuItem(Translator.getText("scaricaItem"),0,true, false); //ultimo del popup e' a TRUE
	}

	/**
	 * Metodo per impostare i popup del Download selezionato, in base al suo stato.
	 * @param status int che rappresenta lo stato del Download selezionato.
	 */
	public void setPopupState(int status) {
		switch(status) {
		case DOWNLOADING:
			pausaItem.setEnabled(true);
			riprendiItem.setEnabled(false);
			rimuoviItem.setEnabled(true);
			break;
		case PAUSED:
			pausaItem.setEnabled(false);
			riprendiItem.setEnabled(true);
			rimuoviItem.setEnabled(true);
			break;
		case VALIDATION: //se validation o altri status fa sempre quello in default (perche' non c'e' il break)
		default:
			pausaItem.setEnabled(false);
			riprendiItem.setEnabled(false);
			rimuoviItem.setEnabled(false);
			break;
		}
	}

	/**
	 * Metodo per creare/ottenere il PopupMenu associato al Download selezionato.
	 * @return
	 */
	public JPopupMenu getPopUp() {
		JPopupMenu popup = new KPopupMenu();
		popup.add(pausaItem);
		popup.add(riprendiItem);
		popup.add(rimuoviItem);
		popup.add(disableShaCheck);
		popup.add(mostraItem);
		popup.add(urlItem);
		return popup;
	}

	public JMenuItem getPausaItem() {
		return pausaItem;
	}
	public JMenuItem getRiprendiItem() {
		return riprendiItem;
	}
	public JMenuItem getRimuoviItem() {
		return rimuoviItem;
	}
	public JMenuItem getDisableShaCheck() {
		return disableShaCheck;
	}
	public JMenuItem getMostraItem() {
		return mostraItem;
	}
	public JMenuItem getUrlItem() {
		return urlItem;
	}
}
