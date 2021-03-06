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

package it.stefanocappa.logic.listener.popupitem;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JMenuItem;

import it.stefanocappa.localization.Translator;
import it.stefanocappa.logic.Download;
import it.stefanocappa.logic.DownloadList;


public class DisableShaCheckListener extends AbstractAction {
	private static final long serialVersionUID = 2120190523393065248L;

	@Override
	public void actionPerformed(ActionEvent a) {
		Download download = DownloadList.getInstance().getSelectedDownload();
		if(download!=null) { 
			download.setNeedToValidate(!download.isNeedToValidate()); //permette di invertire lo stato di needToValidate ad ogni clic sull'item
			
			JMenuItem item = (JMenuItem)(a.getSource());
			if(item.getText().equals(Translator.getText("disableShaCheck"))) {
				item.setText(Translator.getText("activateShaCheck"));
			} else {
				item.setText(Translator.getText("disableShaCheck"));	
			}
		}
	}
}
