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

import gui.MainFrame;
import gui.state.StateButton;

import java.awt.event.ActionEvent;
import java.nio.file.Path;
import java.util.List;

import javax.swing.AbstractAction;

import logic.AddingDownload;
import logic.Download;
import logic.DownloadList;
import logic.LogicLoaderFirmware;
import logic.LogicLoaderItunes;
import logic.LogicManager;
import logic.Process;
import model.FileWeb;
import model.Firmware;
import model.ItunesVersion;
import notification.Notification;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * Classe che contiere l'ActionListener per aggiungere i firmware
 * dalla lista.
 */
public class AddFromListListener extends AbstractAction {
	private static final long serialVersionUID = 4718167301597935681L;
	private static final Logger LOGGER = LogManager.getLogger(Process.class);
	
	//e' transient perche' la classe FileWeb non e' serializzabile, mentre questa si e quindi bisogna segnalare che questo non verra' serializzato
	private transient FileWeb fileWeb; 
	
	private void setGlobalVariables() {
		switch(MainFrame.getInstance().getTabbedPaneIndex()) {
		case 0:
			fileWeb = LogicLoaderFirmware.getInstance().ottieniFirmwareDaLista();
			break;
		case 1:
			fileWeb = LogicLoaderItunes.getInstance().ottieniItunesVersionDaLista();
			break;
		default: 
			//TODO aggiungere parte dei downlaod jailbreak software
			break;
		}
	}
	
	private List<Path> getFileList() {
		List<Path> pathList = null;
		if(fileWeb instanceof Firmware) {
			pathList = LogicLoaderFirmware.getInstance().getListaFile();
		} 
		if(fileWeb instanceof ItunesVersion) {
			pathList = LogicLoaderItunes.getInstance().getListaFile();
		} 
		//			else { //se e' un JailbreakSoftware
		//			}
		return pathList;
	}

	@Override
	public void actionPerformed(ActionEvent a) {
		LogicManager lm = LogicManager.getInstance();

		this.setGlobalVariables();
		
		if(fileWeb!=null && !AddingDownload.getInstance().containsUri(fileWeb.getUri())) {
			
			int statoVerifica = this.verificaPresenzaFileWeb(fileWeb,this.getFileList());
			
			switch(statoVerifica) {
			case 0:
				//aggiunto il firmware nella hashmap che contiene i fileweb in corso d'aggiunta.
				//appena aggiunto il Download, un metodo di quella classe si occupera' di cancellare il valore
				//dalla tabella.
				AddingDownload.getInstance().putFileWeb(fileWeb);
				LOGGER.debug("downloadDaInserire :"  + fileWeb.getUri().toString());
				lm.actionAdd(fileWeb);
				break;
			case 1:
				Notification.showNormalOptionPane("addFromListListenerDownloadInList");
				break;
			case 2:
			default:
				Notification.showNormalOptionPane("addFromListListenerFileDownloaded");
				break;
			}
			
			StateButton.attivaTuttiPulsantiAll();
		} else {
			Notification.showNormalOptionPane("addFromListListenerAddingFile");
		}
	}
	
	/**
	 * Metodo per verificare se un download e' gia' in lista o se e' gia' scaricato e nella
	 * cartella dei download.
	 * In questo caso il download non sarebbe permesso.
	 * @param fileWeb riferimento al Firmware che voglio scaricare
	 * @return Un int: "0" -> e' possibile inserire il nuovo download
	 * 					"1" - > download gia' in lista
	 * 					"2" -> download gia' scaricato nella cartella dei download
	 */
	private int verificaPresenzaFileWeb(FileWeb fileWeb,List<Path> pathListDownload) {
		for(Download download : DownloadList.getInstance().getDownloadList()) {
			//se trovo il download vuol dire che e' gia' presente e quindi lo impedisco
			if(download.getProcessList().get(0).getURIString().equals(fileWeb.getUri().toString())) {
				LOGGER.debug("trovato : " + fileWeb.getUri().toString());
				return 1;
			}
		}

		//ora cerco se c'e' un download che e' gia' scaricato impedisco il download
		for(Path path : pathListDownload) {
			if(fileWeb.getFileName().contains(path.getFileName().toString())) {
				return 2;
			}
		}
		//se non ha trovato nulla ritorna 0
		return 0;			
	}
}
