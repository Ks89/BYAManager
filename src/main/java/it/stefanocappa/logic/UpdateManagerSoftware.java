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

package it.stefanocappa.logic;

import java.awt.Color;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Observable;
import java.util.Observer;

import it.stefanocappa.model.User;
import it.stefanocappa.notification.Notification;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import it.stefanocappa.update.Version;
import it.stefanocappa.update.software.CheckBYAMUpdates;

import it.stefanocappa.localization.Translator;
import it.stefanocappa.exception.UpdateException;
import it.stefanocappa.gui.kcomponent.KColors;
import it.stefanocappa.gui.state.StateButton;
import it.stefanocappa.gui.state.StateLabel;

public final class UpdateManagerSoftware implements Observer{
	private static final Logger LOGGER = LogManager.getLogger(UpdateManagerSoftware.class);
	private static UpdateManagerSoftware instance = new UpdateManagerSoftware();
	/**
	 * @uml.property  name="checkByamUpdates"
	 * @uml.associationEnd  
	 */
	private CheckBYAMUpdates checkByamUpdates; 

	public static UpdateManagerSoftware getInstance() {
		return instance;
	}

	private UpdateManagerSoftware() {
		checkByamUpdates = new CheckBYAMUpdates();
	}

	/**
	 * Metodo per aggiornare il programma, scaricando i jar, verificando gli SHA dei jar scaricati e se
	 * combaciano con la lista dsul server nel file di controllo, modifica le label del programma
	 * ed esegue l'updater. Nel caso in cui l'aggiornamento sia stato eseguito correttamente e che
	 * quindi al riavvio successivo risulti c, cancella l'updater.
	 * @param versione String che rappresenta la versione del programma nel formato x.x.x.x.
	 */
	private void updateSoftware(String versione) {	
		//aggiornamentoDatabase false: il database e' aggiornato, altrimenti no
		if(Version.verificaEsistenzaVersionePiuRecente(versione)==1) { //se e' true scarico il JAR
			LOGGER.info("setAggiornamentoProgramma() - riavvia");
			try {
				StateLabel.getAggProgrammaLabel().setForeground(Color.WHITE);
				StateLabel.getAggProgrammaLabel().setText("  " + Translator.getText("byamLabelAggiornamento"));
				SplashScreenManager.setText("updateProgress");
				checkByamUpdates.downloadJarFromServer();
				this.eseguiUpdater();			
			} catch (IOException e) {
				LOGGER.error("setAggiornamentoProgramma() - IOException= " + e);
			} catch (UpdateException e) {
				StateLabel.getAggProgrammaLabel().setForeground(Color.RED);
				StateLabel.getAggProgrammaLabel().setText("  " + Translator.getText("byamLabelErrore"));
				Notification.showErrorOptionPane("updateManagerSoftwareUpdateException", "Software update error");
			}
		} else {
			checkByamUpdates.removeUpdater();
			LOGGER.info("setAggiornamentoProgramma() - aggiornato");
			StateLabel.getAggProgrammaLabel().setText("  " + Translator.getText("byamLabelAgg"));	
			StateLabel.getAggProgrammaLabel().setForeground(KColors.getVerde());
		}
		StateButton.activateAfterUpdateSoftware(true);
	}

	public void checkSoftwareUpdates() {
		checkByamUpdates.addObserver(this);
		checkByamUpdates.run();
	}

	/**
	 * Metodo per eseguire (in un'altra JVM) il file BYAUpdater.jar scaricato dal server. 
	 * Questo permette l'aggiornamento del programma.
	 */
	private void eseguiUpdater() {
		//avvio updater
		String updaterPathAndName = User.getInstance().getJarPath() + 
				System.getProperty("file.separator") + "BYAUpdater.jar";
		String managerJarName = User.getInstance().getJarName();
		String updaterPath = User.getInstance().getJarPath();

		LOGGER.info("eseguiUpdater() - percorsoUpdater=" + updaterPathAndName);
		LOGGER.info("eseguiUpdater() - User.getInstance().getJarName() : " + managerJarName);
		LOGGER.info("eseguiUpdater() - riga comando=" + "java -jar " + updaterPathAndName + " " + managerJarName + " " + updaterPath);


		if(updaterPathAndName!=null && managerJarName!=null && updaterPath!=null) {
			try {
				//creo la riga di comando inserendo alla fine il parametro che indica i
				String [] rigaComando = {"java","-jar", updaterPathAndName, managerJarName, updaterPath};
				ProcessBuilder pb = new ProcessBuilder(rigaComando);
				pb.start();

				//			this.mettiPausaPreChiusura(); TODO dovrei far si che durante l'aggiornamento tutto venga messo in pausa
				Runtime.getRuntime().exit(0);
			} catch (MalformedURLException e) {
				LOGGER.error("eseguiUpdater() - MalformedURLException=", e);
			} catch (IllegalArgumentException e) {
				LOGGER.error("eseguiUpdater() - IllegalArgumentException=", e);
			} catch (SecurityException e) {
				LOGGER.error("eseguiUpdater() - SecurityException=", e);
			}  catch (IOException e) {
				LOGGER.error("eseguiUpdater() - IOException=", e);
			}
		} else {
			LOGGER.debug("eseguiUpdater() - null arguments. updaterPathAndName=" +
					updaterPathAndName + ",managerJarName=" + managerJarName + ",updaterPath=" + updaterPath);
		}
	}


	@Override
	public void update(Observable o, Object obj) {
		if(o instanceof CheckBYAMUpdates) {
			CheckBYAMUpdates vabyam = (CheckBYAMUpdates)o;
			this.updateSoftware(vabyam.getVersionFromFile());
			vabyam.deleteObserver(this); //lo tolgo perche' tanto la verifica dell'aggiornamento avviene una sola volta
		}
	}
}
