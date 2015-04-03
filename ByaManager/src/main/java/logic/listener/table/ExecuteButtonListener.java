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

package logic.listener.table;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import javax.swing.AbstractAction;

import notification.Notification;

public class ExecuteButtonListener extends AbstractAction {
	private static final long serialVersionUID = -3232676091890281738L;

	/**
	 * @uml.property  name="fileToOpen"
	 */
	private File fileToOpen;

	public ExecuteButtonListener(Path pathToOpen) {
		this.fileToOpen = pathToOpen.toFile();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(!fileToOpen.exists()) {
			Notification.showNormalOptionPane("executeFileError");
			return;
		}
		
		if(System.getProperty("os.name").contains("Windows") || System.getProperty("os.name").contains("Mac")) {
			try {
				Desktop.getDesktop().open(fileToOpen);
			} catch (IOException e1) {
				Notification.showNormalOptionPane("executeFileError");
			}
		}
	}

}
