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

package preferences;

import gui.SettingsMainFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import localization.Translator;
import model.User;
import notification.Notification;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import preferences.listener.ChooseFolderListener;
import update.Version;

import java.util.Locale;
import java.util.Observable;
import java.util.Observer;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javax.swing.SwingUtilities;


/**
 *	Classe impostazioni che gestisce il pannello preferenze del programma.
 */
public final class Settings implements Observer{
	private static final Logger LOGGER = LogManager.getLogger(Settings.class);

	private String versioneSoftware;
	private String versione;
	private String percorsoDati;
	private String percorsoDownload;
	private String percorsoDownloadTemp;
	private String percorsoDownloadHttpsTemp;
	private String proxyServer;
	private String proxyPort;
	private int bufferDimensionIndex;
	private int dimensionTypeIndex;
	private int languageComboIndex;
	private boolean systemTrayNotificationState;
	private boolean autoUpdateState;
	private boolean proxyActivationState;
	private SettingsMainFrame settingsMainFrame;
	private Preferences prefs;
	private ChooseFolderListener scegliCartellaListener;
	private static Settings instance = new Settings();
	
	public static Settings getInstance() {
		return instance;
	}
	
	/**
	 * Costruttore per inizializzare percorsiOS, quello delle impostazioni e creare il logger.
	 * @param versione String che rappresenta la versione del programma.
	 */
	private Settings() {
		prefs = Preferences.userNodeForPackage(getClass());
		versioneSoftware = Version.getVersion();
		this.systemTrayNotificationState = true;
		this.autoUpdateState = false;
		this.settingsMainFrame = new SettingsMainFrame();
	}
	
	public void createAndAssignGuiElements() {
		Translator.setLocale(languageComboIndex);
		this.settingsMainFrame.applytTranslations();
		this.registraListener();
	}

	/**
	 * Metodo per aprire le preferenze del programma.
	 * @param percorsoDati Istanza dell'oggetto percorsoDati.
	 */
	public void apriPreferenze() {		
		this.loadSettings();
		this.applySettings();
		SwingUtilities.invokeLater(this.settingsMainFrame);
	}
	
	
	public void loadSettings() {
		//leggo dati e li metto nelle variabili
		//il secondo valore in get e' il valore di default
		this.percorsoDati = prefs.get("percorsoDati", User.getInstance().getDataPath().toString());
		this.percorsoDownload = prefs.get("percorsoDownload", User.getInstance().getDownloadPath().toString());
		this.percorsoDownloadTemp = this.percorsoDownload + File.separatorChar + "temp";
		this.percorsoDownloadHttpsTemp = this.percorsoDownload + File.separatorChar + "temphttps";
		this.bufferDimensionIndex = prefs.getInt("bufferDownload", 0); // default e' con 2048, cioe' la posizione 0
		this.languageComboIndex = prefs.getInt("languageCombo", this.getDefaultOsLanguageIndex()); //default e' quelal predefinita del sistema operativo
		this.dimensionTypeIndex = prefs.getInt("tipoDimensione", 1); //di default e' coi Multipli dei Byte, posizione 1
		this.systemTrayNotificationState = prefs.getBoolean("systemTrayNotificationState", true);
		this.autoUpdateState = prefs.getBoolean("autoUpdateState", true);
		this.proxyServer = prefs.get("proxyServer", "127.0.0.1");
		this.proxyPort = prefs.get("proxyPort", "8080");
		this.proxyActivationState = prefs.getBoolean("proxyActivationState", false);
	}
	
	public void initSettings() {
		try {
			//da tenere fuori dal loasSettings perche' quel metodo e' chiamato piu' volte, queste righe sotto solo una, all'inizializzazione
			this.versione = prefs.get("versione", versioneSoftware);
			if(versione.contains("0,3,") || versione.contains("0,4,") || versione.equals("0,5,0,0") || versione.startsWith("0,5,1,") || versione.startsWith("0,5,2,")
					 || versione.startsWith("0,5,3,")  || versione.startsWith("0,5,4,") || versione.startsWith("0,5,5,") || versione.startsWith("0,5,6,")
					 || versione.startsWith("0,6,0,") || versione.startsWith("0,7,0,")) { 
				prefs.clear(); //cancella preferenze
			}

			this.loadSettings();
			
			//ora mi assicuro di aggiornare il campo che indentifica la versione delle preferenze,
			//sovrascrivendolo con la versione attuale, piu' aggiornata
			switch(Version.verificaEsistenzaVersionePiuRecente(versione)) {
			case 1: //vuol dire che ho fatto un downgrade per qualche motivo inspiegabile, ma per ora ho deciso comunque di permetterlo
				LOGGER.info("initSettings() - Perche' hai eseguito un DOWNGRADE da " + versione +  " a " + versioneSoftware + "??? Aggiornami subito!!!");
				break;
			case -1: //vuol dire che ho appenac aggiornato perche' le preferenze erano piu' vecchie della nuova versione software
				LOGGER.info("initSettings() - Grazie per avermi aggiornato da " + versione +  " a " + versioneSoftware);
				this.versione = Version.getVersion();
				this.saveSettings();
				break;	
			default: 
				LOGGER.info("initSettings() - Versione preferenze = versione software -> OK!");
				break;
			}

			this.logSettings();
		} catch(BackingStoreException e) {
			try {
				prefs.clear(); //cancella preferenze
				Notification.showNormalOptionPane("settingsErrorAndReset");
				LOGGER.error("initSettings() - preferenze resettate a causa di un errore, Exception=" + e);
			} catch (BackingStoreException e1) {
				LOGGER.error("initSettings() - BackingStoreException durante il reset, dopo alla precedente BackingStoreException= " + e1);
			}
		}
	}
	
	/**
	 * Metodo per caricare le preferenze.
	 */
	private void applySettings() {
			//modifico la gui in base ai dati delle variabili (precedentemten letti dalle preferenze)
			settingsMainFrame.getBufferComboBox().setSelectedIndex(bufferDimensionIndex);
			settingsMainFrame.getTipoDim().setSelectedIndex(this.dimensionTypeIndex);
			settingsMainFrame.getLanguageComboBox().setSelectedIndex(this.languageComboIndex);
			settingsMainFrame.getPercorsoTextField().setText(this.percorsoDownload);
			settingsMainFrame.getSystemTrayNotificationState().setSelected(this.systemTrayNotificationState);
			settingsMainFrame.getAutoUpdateState().setSelected(this.autoUpdateState);
			settingsMainFrame.getProxyServer().setText(this.proxyServer);
			settingsMainFrame.getProxyPort().setText("" + this.proxyPort);
			settingsMainFrame.getActivateProxy().setSelected(this.proxyActivationState);
			settingsMainFrame.getProxyServer().setEnabled(this.proxyActivationState);
			settingsMainFrame.getProxyPort().setEnabled(this.proxyActivationState);
	}

	
	private void logSettings() {
		LOGGER.info("versione: " + versione);
		LOGGER.info("percorso di dati: " + percorsoDati);
		LOGGER.info("percorso di download: " + percorsoDownload);
		LOGGER.info("percorso di downloadTemp: " + percorsoDownloadTemp);
		LOGGER.info("percorso di downloadHttpsTemp: " + percorsoDownloadHttpsTemp);
		LOGGER.info("dimensione buffer: " + bufferDimensionIndex);
		LOGGER.info("unita misura index: " + dimensionTypeIndex);
		LOGGER.info("lingua index: " + languageComboIndex);
		LOGGER.info("systemTrayNotificationState: " + systemTrayNotificationState);
		LOGGER.info("autoUpdateState: " + autoUpdateState);
		LOGGER.info("proxyActivationState: " + proxyActivationState);
		LOGGER.info("proxyServer: " + proxyServer);
		LOGGER.info("proxyPort: " + proxyPort);
	}

	/**
	 * Metodo per salvare le preferenze.
	 */
	public void saveSettings() {
		try {
			prefs.clear ();
			prefs.put("versione", this.versione);
			prefs.put("percorsoDati", this.percorsoDati);
			prefs.put("percorsoDownload", settingsMainFrame.getPercorsoTextField().getText());
			prefs.putInt("bufferDownload", bufferDimensionIndex);
			prefs.putInt("tipoDimensione", this.dimensionTypeIndex);
			prefs.putInt("languageCombo", this.languageComboIndex);
			prefs.putBoolean("systemTrayNotificationState", this.systemTrayNotificationState);
			prefs.putBoolean("autoUpdateState", this.autoUpdateState);
			prefs.put("proxyServer", settingsMainFrame.getProxyServer().getText());
			prefs.put("proxyPort", settingsMainFrame.getProxyPort().getText());
			prefs.putBoolean("proxyActivationState", settingsMainFrame.getActivateProxy().isSelected());
			prefs.exportNode(System.out);
		} catch (BackingStoreException | IOException e) {
			LOGGER.error("saveSettings()", e);
		}
	}

	
	private void registraListener() {
		settingsMainFrame.getSalvaPreferenze().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				salvaPreferenzeAction();
			}
		});
		settingsMainFrame.getActivateProxy().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(settingsMainFrame.getActivateProxy().isSelected()) {
					settingsMainFrame.getProxyServer().setEnabled(true);
					settingsMainFrame.getProxyPort().setEnabled(true); 
				} else {
					settingsMainFrame.getProxyServer().setEnabled(false);
					settingsMainFrame.getProxyPort().setEnabled(false); 
				}
			}
		});
		scegliCartellaListener = new ChooseFolderListener(settingsMainFrame);
		scegliCartellaListener.addObserver(this);
		settingsMainFrame.getScegliCartella().addActionListener(scegliCartellaListener);
//		ElementiGuiImpostazioni.getLoginTwitterButton().addActionListener(new LoginTwitterListener());
	}

	private void setDownloadPath() {
		//se l'ultimo carattere e' una barra lo rimuovo
		//TODO ha molti casi non considerati...creare metodo a parte che valida il percorso
		int ultimoCarattere = this.percorsoDownload.length() - 1;
		if(this.percorsoDownload.charAt(ultimoCarattere)=='/' ||
				this.percorsoDownload.charAt(ultimoCarattere)=='\\') { // la doppia barra viene interpretata come una sola
			this.percorsoDownload = this.percorsoDownload.substring(0, ultimoCarattere);
		}
		if(!percorsoDownload.endsWith("ByaManager_Downloads")) {
			this.percorsoDownload = this.percorsoDownload + System.getProperty("file.separator") + "ByaManager_Downloads";
		} 

	}
	
	private void salvaPreferenzeAction() {
		if(Notification.clickedYesInConfirmDialog("settingsChangedNeedRestart")) {

			//applico le modifiche alle variabili in base ai dati letti dai component swing
			bufferDimensionIndex = settingsMainFrame.getBufferComboBox().getSelectedIndex();
			dimensionTypeIndex = settingsMainFrame.getTipoDim().getSelectedIndex();
			languageComboIndex = settingsMainFrame.getLanguageComboBox().getSelectedIndex();
			
			this.percorsoDownload = settingsMainFrame.getPercorsoTextField().getText();

			this.systemTrayNotificationState = settingsMainFrame.getSystemTrayNotificationState().isSelected();
			this.autoUpdateState = settingsMainFrame.getAutoUpdateState().isSelected();

			this.setDownloadPath();
	
			this.percorsoDownloadTemp = percorsoDownload + System.getProperty("file.separator") + "temp";
			this.percorsoDownloadHttpsTemp = percorsoDownload + System.getProperty("file.separator") + "temp";

			settingsMainFrame.getPercorsoTextField().setText(percorsoDownload);

			
			//creo il percorso di downloadtemp
			//lo faccio anche quando seleziono un percorso con ChooseFolderListener, ma li e' con la gui
			//il caso ancora non preso in considerazione e' la scrittura manuale nella field del percorso
			//e quindi faccio quanto segue per creare il percorso con le relative cartelle.
			try {
				Path path = Paths.get(this.percorsoDownloadTemp);
				Files.createDirectories(path);
				if(Files.exists(path)) {
					this.logSettings();
					saveSettings();
					loadSettings();
					this.logSettings();
				} else {
					LOGGER.error("salvaPreferenzeAction - Percorso downloadtemp non valido");
					Notification.showNormalOptionPane("settingsProblemWithPath");
				}
			} catch (IOException e) {
				LOGGER.error("salvaPreferenzeAction - IOException= " + e);
				Notification.showNormalOptionPane("settingsProblemWithPath");
			}
			
			try {
				Path path2 = Paths.get(this.percorsoDownloadHttpsTemp);
				Files.createDirectories(path2);
				if(Files.exists(path2)) {
					this.logSettings();
					saveSettings();
					loadSettings();
					this.logSettings();
				} else {
					LOGGER.error("salvaPreferenzeAction - Percorso downloadHttpstemp non valido");
					Notification.showNormalOptionPane("settingsProblemWithPath");
				}
			} catch (IOException e) {
				LOGGER.error("salvaPreferenzeAction - IOException= " + e);
				Notification.showNormalOptionPane("settingsProblemWithPath");
			}
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		//o e' scegliCartellaListener
		this.percorsoDownloadTemp = this.scegliCartellaListener.getPercorsoDownloadTemp();
		this.percorsoDownload = this.scegliCartellaListener.getPercorsoDownload();
		settingsMainFrame.getPercorsoTextField().setText(percorsoDownload);
	}
	
	
	private int getDefaultOsLanguageIndex() {
		switch(Locale.getDefault().getLanguage()) {
		case "it":
			return 1;
		case "cs":
			return 2;
		default: //eng o altre
			return 0;
		}
	}
	
	/**
	 * @return  String rappresentante la versione.
	 */
	public String getVersione() {
		return versione;
	}
	/**
	 * @param versione  String per impostare la versione.
	 */
	public void setVersione(String versione) {
		this.versione = versione;
	}
	/**
	 * @return  String rappresentante il percorsoDati.
	 */
	public String getPercorsoDati() {
		return percorsoDati;
	}
	/**
	 * @return  String rappresentante il percorsoDownload.
	 */
	public String getPercorsoDownload() {
		return percorsoDownload;
	}
	/**
	 * @return  String rappresentante il percorsoDownloadTemp.
	 */
	public String getPercorsoDownloadTemp() {
		return percorsoDownloadTemp;
	}
	/**
	 * @return  String rappresentante il percorsoDownloadHttpsTemp.
	 */
	public String getPercorsoDownloadHttpsTemp() {
		return percorsoDownloadHttpsTemp;
	}
	
	/**
	 * @return int rappresentante la dimensione buffer.
	 */
	public int getDimensioneBuffer() {
		return Integer.parseInt(settingsMainFrame.getBufferComboBox().getSelectedItem().toString());
	}
	/**
	 * @return int rappresentante il tipo di dimensione da visualizzare.
	 */
	public int getTipoDimensione() {
		return dimensionTypeIndex;
	}

	public int getLanguageComboIndex() {
		return languageComboIndex;
	}
	/**
	 * @return boolean rappresentante lo stato di systemTrayNotification.
	 */
	public boolean getSystemTrayNotificationState() {
		return systemTrayNotificationState;
	}
	/**
	 * @return boolean rappresentante lo stato di autoUpdate.
	 */
	public boolean getAutoUpdateState() {
		return autoUpdateState;
	}

	public void setSystemTrayNotificationState(boolean systemTrayNotificationState) {
		this.systemTrayNotificationState = systemTrayNotificationState;
	}

	public void setAutoUpdateState(boolean autoUpdateState) {
		this.autoUpdateState = autoUpdateState;
	}

	public String getProxyServer() {
		return proxyServer;
	}

	public String getProxyPort() {
		return proxyPort;
	}

	public boolean isProxyActivated() {
		return proxyActivationState;
	}

	public void setProxyActivated(boolean proxyActivationState) {
		this.proxyActivationState = proxyActivationState;
	}
}
