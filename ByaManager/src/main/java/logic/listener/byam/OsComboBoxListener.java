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

package logic.listener.byam;

import gui.MainFrame;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import logic.LogicLoaderItunes;
import model.ItunesVersion;

public class OsComboBoxListener extends AbstractAction {
	private static final long serialVersionUID = -5794754578043198718L;

	@Override
	public void actionPerformed(ActionEvent e) {
		//rimuovo tutti gli elementi dalla combobox con itunes version
		MainFrame.getInstance().getItunesList().removeAllItems();

		//scansiono la lista di itunesVersion e dove trovo un itunesVersion dell'os selezionato nel 
		//menu a tendina lo aggiungo alla combobox degli itunesVersion.
		for(ItunesVersion iTunesVersion : LogicLoaderItunes.getInstance().getiTunesListaNomeFile()) {
			if(iTunesVersion.getOperativeSystemList().get(0).getCompleteName().equals((String)(MainFrame.getInstance().getOsList().getSelectedItem()))) {
				MainFrame.getInstance().getItunesList().addItem(iTunesVersion.getVersion()); 
			}
		}
	}
}