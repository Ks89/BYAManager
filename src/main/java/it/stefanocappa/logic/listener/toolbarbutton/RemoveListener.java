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

package it.stefanocappa.logic.listener.toolbarbutton;

import it.stefanocappa.gui.state.StateButton;
import it.stefanocappa.gui.table.TableGui;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;


import it.stefanocappa.logic.DownloadList;

public class RemoveListener extends AbstractAction {
	private static final long serialVersionUID = 6258701377588707153L;

	@Override
	public void actionPerformed(ActionEvent e) {
		TableGui.setClearing(true);
		DownloadList.getInstance().cancel(); 
		DownloadList.getInstance().clearDownload(TableGui.getInstance().getSelectedRow());
		TableGui.setClearing(false);
		StateButton.disattivaPulsanti();
		DownloadList.getInstance().deselectAllDownloads();
//		TableGui.setSelectedDownload(null);
	}
}