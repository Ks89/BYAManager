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

package it.stefanocappa.logic.listener.menuitem;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.swing.AbstractAction;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import it.stefanocappa.fileutility.FileList;
import it.stefanocappa.model.User;
import it.stefanocappa.notification.Notification;

public class RestoreToDefaultListener extends AbstractAction {
	private static final long serialVersionUID = 4770036607253381680L;
	private static final Logger LOGGER = LogManager.getLogger(RestoreToDefaultListener.class);

	@Override
	public void actionPerformed(ActionEvent e) {
		if(Notification.clickedYesInConfirmDialog("graphicManagerRestoreDefault")) {
			this.reinizializzaProgramma();
//			LogicManager.getInstance().reinizializzaProgramma(); //TODO difficile fare tutto senza dipendenze, perche'
//			//cmq devo poter mettere in pausa anche tutti i downlaod prima di eseguire operazione cosi' delicate
//			//e per falro ho bisogno del riferimento a LogicManager o cmq anche al Download (nel caso spostatti qui il metodo = FOLLIA)
		}
	}
	
	
	private void removeDownloadTemp() {
		LOGGER.info("resetDownloadTemp() - Avviato");
		this.deleteFileList(User.getInstance().getDownloadTempPath(),"percorsodownloadtemp");
		LOGGER.info("resetDownloadTemp() - Terminato");
	}

	private void removeDb() {
		LOGGER.info("resetDb() - Avviato");
		
		this.deleteFileList(User.getInstance().getDataPath(),"percorsodati");
		LOGGER.info("resetDb() - Terminato");
	}

	public void reinizializzaProgramma() {
		LOGGER.info("reinizializzaProgramma()");

		this.removeDb();
//		this.mettiPausaPreChiusura();

		this.deleteFileList(User.getInstance().getDownloadPath(),"percorsoDownload");
		this.removeDownloadTemp(); //metodo usato anche da terminale e quindi lo separo e lo richiamo qui
		this.deleteFileList(User.getInstance().getDownloadPath(),"percorsoDownloadPredefinito");
		this.deleteFileList(User.getInstance().getDownloadTempPath(),"percorsodownloadtempPredefinito");

		Runtime.getRuntime().exit(0); //termina l'intera virtual machine
	}

	/**
	 * Metodo che rimuove tutti i file di una lista, dato un percorso e il nome dell'operazione.
	 * Per nome dell'operazione si intende un nome semplcei e breve che spieghi cosa deve essere cancellato.
	 * @param operationName
	 */
	private void deleteFileList(Path directoryPath, String operationName) {
		for(Path path : FileList.getFileList(directoryPath)) {
			try {
				Files.deleteIfExists(path);
			} catch (IOException e) {
				LOGGER.info("deleteFileList() - Nome operazione: " + operationName + 
						". Eccezione durante cancellazione di " + path.toString() + ". Eccezione = " + e);
			}
		}
	}
}
