// Generated by delombok at Sat Sep 17 15:11:19 CEST 2016
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

import gui.image.ImageLoader;
import gui.kcomponent.KPulsanteToolBar;
import gui.kcomponent.KToolBar;
import javax.swing.JButton;
import javax.swing.JToolBar;
import localization.Translator;

/**
 * Classe che si occupa della creazione e gestione dello stato dei pulsanti.
 */
public final class StateButton {
	private static JButton refreshDBButton;
	private static JButton refreshBYAMButton;
	private static JButton preferenzeButton;
	private static JButton pauseButton;
	private static JButton resumeButton;
	private static JButton removeButton;
	private static JButton removeButtonAll;
	private static JButton pauseButtonAll;
	private static JButton resumeButtonAll;
	public static final int DOWNLOADING = 0;
	public static final int PAUSED = 1;
	public static final int COMPLETE = 2;
	public static final int ERROR = 4;
	public static final int VALIDATION = 5;

	private StateButton() {
	}

	/**
	 * Costruttore che crea e disattiva tutti i pulsanti. Infine, 
	 * si occupa anche di impostare i tooltip. 
	 */
	static {
		refreshDBButton = new KPulsanteToolBar(ImageLoader.getInstance().getRefreshDb(), ImageLoader.getInstance().getRefreshDbD());
		refreshBYAMButton = new KPulsanteToolBar(ImageLoader.getInstance().getRefreshByam(), ImageLoader.getInstance().getRefreshByamD());
		preferenzeButton = new KPulsanteToolBar(ImageLoader.getInstance().getPreferenze(), ImageLoader.getInstance().getPreferenzeD());
		pauseButton = new KPulsanteToolBar(ImageLoader.getInstance().getPause(), ImageLoader.getInstance().getPauseD());
		resumeButton = new KPulsanteToolBar(ImageLoader.getInstance().getPlay(), ImageLoader.getInstance().getPlayD());
		removeButton = new KPulsanteToolBar(ImageLoader.getInstance().getRemove(), ImageLoader.getInstance().getRemoveD());
		pauseButtonAll = new KPulsanteToolBar(ImageLoader.getInstance().getPausaAll(), ImageLoader.getInstance().getPausaAllD());
		resumeButtonAll = new KPulsanteToolBar(ImageLoader.getInstance().getPlayAll(), ImageLoader.getInstance().getPlayAllD());
		removeButtonAll = new KPulsanteToolBar(ImageLoader.getInstance().getRemoveAll(), ImageLoader.getInstance().getRemoveAllD());
		refreshDBButton.setEnabled(false);
		refreshBYAMButton.setEnabled(false);
		pauseButton.setEnabled(false);
		resumeButton.setEnabled(false);
		preferenzeButton.setEnabled(false);
		removeButton.setEnabled(false);
		pauseButtonAll.setEnabled(false);
		resumeButtonAll.setEnabled(false);
		removeButtonAll.setEnabled(false);
		refreshDBButton.setToolTipText(Translator.getText("toolTipRefreshDb"));
		refreshBYAMButton.setToolTipText(Translator.getText("toolTipRefreshBya"));
		preferenzeButton.setToolTipText(Translator.getText("toolTipPref"));
		pauseButton.setToolTipText(Translator.getText("toolTipPause"));
		resumeButton.setToolTipText(Translator.getText("toolTipPlay"));
		removeButton.setToolTipText(Translator.getText("toolTipRemove"));
		pauseButtonAll.setToolTipText(Translator.getText("toolTipPauseAll"));
		resumeButtonAll.setToolTipText(Translator.getText("toolTipPlayAll"));
		removeButtonAll.setToolTipText(Translator.getText("toolTipRemoveAll"));
	}

	/**
	 * Metodo per creare la toolbar contenente i pulsanti creati dal costruttore della classe.
	 * @return JToolBar contenente i pulsanti del menu, gia' nell'ordine voluto.
	 */
	public static JToolBar creaToolBar() {
		JToolBar toolbar = new KToolBar();
		toolbar.add(refreshDBButton);
		toolbar.add(refreshBYAMButton);
		toolbar.add(preferenzeButton);
		toolbar.add(pauseButton);
		toolbar.add(resumeButton);
		toolbar.add(removeButton);
		toolbar.add(pauseButtonAll);
		toolbar.add(resumeButtonAll);
		toolbar.add(removeButtonAll);
		return toolbar;
	}

	/**
	 * Metodo per attivare/disattivare il pulsante e l'item per gestire gli aggiornamenti software
	 */
	public static void activateAfterUpdateSoftware(boolean state) {
		StateMenuItem.getCheckByamUpdateMenuItem().setEnabled(state);
		refreshBYAMButton.setEnabled(state);
	}

	/**
	 * Metodo per attivare/disattivare il pulsante e l'item per gestire gli aggiornamenti database
	 */
	public static void activateAfterUpdateDb(boolean state) {
		StateMenuItem.getCheckDBUpdateMenuItem().setEnabled(state);
		refreshDBButton.setEnabled(state);
	}

	/**
	 * Metodo per aggiornare i pulsanti in base allo stato del Download.
	 * @param status int rappresentante lo stato del Download selezionato.
	 */
	public static void aggiornaPulsanti(int status) {
		switch (status) {
		case DOWNLOADING: 
			//downloading
			pauseButton.setEnabled(true);
			resumeButton.setEnabled(false);
			removeButton.setEnabled(true);
			break;

		case PAUSED: 
			//pausa
			pauseButton.setEnabled(false);
			resumeButton.setEnabled(true);
			removeButton.setEnabled(true);
			break;

		case VALIDATION: 
			//validation
			pauseButton.setEnabled(false);
			resumeButton.setEnabled(false);
			removeButton.setEnabled(false);
			break;

		default: 
			//errore o COMPLETATO
			pauseButton.setEnabled(false);
			resumeButton.setEnabled(false);
			removeButton.setEnabled(true);
		}
	}

	/**
	 * Metodo che aggiorna i pulsanti in caso di download terminato.
	 */
	public static void completaDownloadPulsanti() {
		pauseButton.setEnabled(false);
		resumeButton.setEnabled(false);
		removeButton.setEnabled(true);
	}

	/**
	 * Metodo per disattivare i pulsanti.
	 */
	public static void disattivaPulsanti() {
		pauseButton.setEnabled(false);
		resumeButton.setEnabled(false);
		removeButton.setEnabled(false);
	}

	/**
	 * Metodo per attivare i pulsanti 
	 * quando e' in download.
	 */
	public static void attivaPulsantiDownloading() {
		pauseButton.setEnabled(true);
		resumeButton.setEnabled(false);
	}

	/**
	 * Metodo per attivare i pulsanti 
	 * quando e' in pausa.
	 */
	public static void attivaPulsantiPause() {
		pauseButton.setEnabled(false);
		resumeButton.setEnabled(true);
	}

	/**
	 * Metodo che inizializza i pulsanti All.
	 */
	public static void inizializzaPulsantiAll() {
		pauseButtonAll.setEnabled(false);
		resumeButtonAll.setEnabled(false);
		removeButtonAll.setEnabled(false);
	}

	/**
	 * Metodo che aggiorna lo stato dei pulsanti All (caso pause) e modifica anche lo stato
	 * dei pulsanti pausa e resume (normali).
	 */
	public static void pausaPulsantiAll() {
		pauseButtonAll.setEnabled(false);
		resumeButtonAll.setEnabled(true);
		removeButtonAll.setEnabled(true);
	}

	/**
	 * Metodo che aggiorna lo stato dei pulsanti All (caso resume) e modifica anche lo stato
	 * dei pulsanti pausa e resume (normali).
	 */
	public static void resumePulsantiAll() {
		pauseButtonAll.setEnabled(true);
		resumeButtonAll.setEnabled(false);
		removeButtonAll.setEnabled(true);
	}

	/**
	 * Metodo che aggiorna lo stato dei pulsanti All (caso remove) e modifica anche lo stato
	 * dei pulsanti pausa, resume, remove (normali).
	 */
	public static void removePulsantiAll() {
		pauseButtonAll.setEnabled(false);
		resumeButtonAll.setEnabled(false);
		removeButtonAll.setEnabled(false);
	}

	/**
	 * Metodo per attivare tutti i pulsanti All.
	 */
	public static void attivaTuttiPulsantiAll() {
		pauseButtonAll.setEnabled(true);
		resumeButtonAll.setEnabled(true);
		removeButtonAll.setEnabled(true);
	}

	/**
	 * Metodo per attivare alcuni pulsanti All.
	 */
	public static void attivaPulsantiAllAlRiavvio() {
		pauseButtonAll.setEnabled(true);
		resumeButtonAll.setEnabled(false);
		removeButtonAll.setEnabled(true);
	}

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	public static JButton getRefreshDBButton() {
		return StateButton.refreshDBButton;
	}

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	public static JButton getRefreshBYAMButton() {
		return StateButton.refreshBYAMButton;
	}

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	public static JButton getPreferenzeButton() {
		return StateButton.preferenzeButton;
	}

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	public static JButton getPauseButton() {
		return StateButton.pauseButton;
	}

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	public static JButton getResumeButton() {
		return StateButton.resumeButton;
	}

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	public static JButton getRemoveButton() {
		return StateButton.removeButton;
	}

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	public static JButton getRemoveButtonAll() {
		return StateButton.removeButtonAll;
	}

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	public static JButton getPauseButtonAll() {
		return StateButton.pauseButtonAll;
	}

	@java.lang.SuppressWarnings("all")
	@javax.annotation.Generated("lombok")
	public static JButton getResumeButtonAll() {
		return StateButton.resumeButtonAll;
	}
}
