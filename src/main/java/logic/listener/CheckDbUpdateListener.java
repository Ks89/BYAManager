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

package logic.listener;

import gui.kcomponent.KColors;
import gui.state.StateButton;
import gui.state.StateLabel;

import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.AbstractAction;

import notification.Notification;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import localization.Translator;
import logic.UpdateManagerDB;

public class CheckDbUpdateListener extends AbstractAction {
	private static final long serialVersionUID = -3084400695437339690L;
	private static final Logger LOGGER = LogManager.getLogger(CheckDbUpdateListener.class);


	@Override
	public void actionPerformed(ActionEvent e) {
		StateButton.activateAfterUpdateDb(false);
		StateLabel.getAggDatabaseLabel().setForeground(KColors.getVerdeChiaro());
		StateLabel.getAggDatabaseLabel().setText("  " + Translator.getText("databaseLabelVerifica"));

		try {
			UpdateManagerDB.getInstance().prepareManualDbUpdate();
		} catch (IOException e2) {
			LOGGER.error("Error prepareManualDbUpdate", e2);
		}
		
		//i must use a new thread, because i don't want to freeze the GUI during updateDb().
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					
					UpdateManagerDB.getInstance().updateDb();
				} catch (IOException e1) {
					LOGGER.error("Errore durante l'aggiornamento manuale del database, cioe' " +
							"su richiesta dell'utente premendo il pulsante della toolbar", e1);
					Notification.showNormalOptionPane("updateManagerDBIOException");
				}
			}
		});
		thread.start();
	}
}
