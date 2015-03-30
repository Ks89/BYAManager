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

package logica.listener.table;

import gui.state.StateActionTableButton;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.AbstractAction;

import notification.Notification;

public class MoveToItunesFolderListener extends AbstractAction {
	private static final long serialVersionUID = -1973301498954950956L;
	/**
	 * @uml.property  name="moveFile"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private transient MoveFile moveFile;

	public MoveToItunesFolderListener(URI uri, Path sourcePath, String destinationPath) {
		this.moveFile = new MoveFile(uri, sourcePath,destinationPath);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(Notification.clickedYesInConfirmDialog("moveFileConfirmDialog")) {
			moveFile.start();
		}
	}
}

class MoveFile extends Thread {
	/**
	 * @uml.property  name="sourcePath"
	 */
	private Path sourcePath;
	/**
	 * @uml.property  name="destinationPath"
	 */
	private Path destinationPath;
	/**
	 * @uml.property  name="uri"
	 */
	private URI uri;

	public MoveFile(URI uri, Path sourcePath, String destinationPath) {
		this.uri = uri;
		this.sourcePath = sourcePath;
		this.destinationPath = Paths.get(destinationPath);
	}

	@Override
	public void run() {
		//ora accendo alla map dei pulsanti action della tabella e disattivo
		//quello identificato dalla chiave uguale al link del download
		//questo impedisce qualunque interazione dell'utente durante la procedura di spostamento
		StateActionTableButton.getInstance().setButtonEnable(uri.toString(), false);
		try {
			//sposta il file
			Files.move(sourcePath, destinationPath);
			Notification.showNormalOptionPane("movingFileCompleted");
			//ho gia' disattivato il pulsante e quindi lascio cosi'
		} catch (IOException e) {
			//in caso di problemi nello spostamento entra qui
			//come prima cosa riattivo il pulsante e poi notifico l'errore nello spostamento
			StateActionTableButton.getInstance().setButtonEnable(uri.toString(), true);
			Notification.showErrorOptionPane("movingFileError", "movingFileErrorTitle");
		}
	}
}
