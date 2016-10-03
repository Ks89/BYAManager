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

package it.stefanocappa.logic.listener;

import it.stefanocappa.gui.kcomponent.KColors;
import it.stefanocappa.gui.state.StateButton;
import it.stefanocappa.gui.state.StateLabel;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import it.stefanocappa.localization.Translator;
import it.stefanocappa.logic.UpdateManagerSoftware;

public class CheckByamUpdateListener extends AbstractAction {
	private static final long serialVersionUID = 3784935326006900485L;

	@Override
	public void actionPerformed(ActionEvent e) {
		StateButton.activateAfterUpdateSoftware(false);
		StateLabel.getAggProgrammaLabel().setForeground(KColors.getVerdeChiaro());
		StateLabel.getAggProgrammaLabel().setText("  " + Translator.getText("byamLabelVerifica"));

		//i must use a new thread, because i don't want to freeze the GUI during checkSoftwareUpdates().
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				UpdateManagerSoftware.getInstance().checkSoftwareUpdates();
			}
		});
		thread.start();

	}

}
