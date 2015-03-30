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

package logica.listener.popupitem;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.AbstractAction;

import model.User;

import org.apache.log4j.Logger;


public class OpenDownloadFolderListener extends AbstractAction {
	private static final long serialVersionUID = 501165480852358688L;
	private static final Logger LOGGER = Logger.getLogger(OpenDownloadFolderListener.class);

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			Desktop.getDesktop().open(new File(User.getInstance().getDownloadPath().toString()));
		} catch (IOException e1) {
			LOGGER.error("avviaProgramma() - mostraItem - IOException= " + e1);
		}
	}

}
