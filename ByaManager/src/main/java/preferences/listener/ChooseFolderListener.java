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


package preferences.listener;

import gui.SettingsMainFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Observable;

import javax.swing.JFileChooser;

import notification.Notification;

import model.User;



/**
 * Classe che contiere l'ActionListener per scegliere la cartella 
 * in cui salvare i download.
 */
public class ChooseFolderListener extends Observable implements ActionListener {

	private String percorsoDownloadTemp;
	private String percorsoDownload;
	private SettingsMainFrame settingsGuiElements;

	/**
	 * Costruttore per impostare le variabili.
	 * @param panel1 Riferimento al JComponent.
	 * @param pannello Riferimento al JFrame.
	 * @param labelPercorso Riferimento alla label che contiene il percorso di download.
	 */
	public ChooseFolderListener(SettingsMainFrame settingsGuiElements) {
		this.percorsoDownload = User.getInstance().getDownloadPath().toString();
		this.settingsGuiElements = settingsGuiElements;
	}

	@Override
	public void actionPerformed(ActionEvent a) {
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		if (fc.showOpenDialog(settingsGuiElements.getPanelTotale()) == JFileChooser.APPROVE_OPTION) {
			
			File file = fc.getSelectedFile(); //in questo caso una cartella
			percorsoDownload = file.getAbsolutePath() + System.getProperty("file.separator") + "ByaManager_Downloads";
			percorsoDownloadTemp = percorsoDownload + System.getProperty("file.separator") + "temp";

			//ora creo il percorso scelto
			try {
				Files.createDirectories(Paths.get(percorsoDownload));
			} catch (IOException e) {
				Notification.showNormalOptionPane("settingsProblemWithPath");
			}
			stateChanged();
		}
	}

	/**
	 * @return  String rappresentante il percorso di download temp.
	 * @uml.property  name="percorsoDownloadTemp"
	 */
	public String getPercorsoDownloadTemp() {
		return percorsoDownloadTemp;
	}

	/**
	 * @return  String rappresentante il percorso di download.
	 * @uml.property  name="percorsoDownload"
	 */
	public String getPercorsoDownload() {
		return percorsoDownload;
	}

	/**
	 * Metodo per notificare un cambiamento all'observers.
	 */
	public void stateChanged() {
		setChanged();
		notifyObservers();
	}
}
