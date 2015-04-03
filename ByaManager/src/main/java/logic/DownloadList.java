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

package logic;

import gui.state.StateActionTableButton;
import gui.table.ByaTableModel;

import java.util.ArrayList;
import java.util.List;


import notification.Notification;


public final class DownloadList {

	/**
	 * @uml.property  name="downloadList"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="logica.Download"
	 */
	private List<Download> downloadList;
	private static DownloadList instance = new DownloadList();

	private DownloadList() {
		downloadList = new ArrayList<Download>();
	}

	public static DownloadList getInstance() {
		return instance;
	}

	/**
	 * Metodo per aggingere un Download
	 * @param download Download da aggiungere.
	 */
	public void addDownload(Download download) {
		// Registra per ricevere la notifica
		download.addObserver(ByaTableModel.getInstance());
		downloadList.add(download);
		StateActionTableButton.getInstance().addButton(download.getUri().toString());

		// Fire table row insertion notification to table.
		ByaTableModel.getInstance().fireTableRowsInserted(downloadList.size() - 1, downloadList.size() - 1);
	}

	/**
	 * @return La lista dei download (processi).
	 */
	public List<Download> getDownloadList() {
		return downloadList;
	}

	/**
	 * @param i int che rappresenta l'iesemo download nella lista (o l'iesima riga).
	 * @return Il download identificato dalla riga.
	 */
	public Download getDownload(int i) {
		if(i==-1) {
			return null;
		}
		return downloadList.get(i);
	}
	
	
	public Download getSelectedDownload() {
		for(Download download : this.downloadList) {
			if(download.isSelectedDownload()) {
				return download;
			}
		}
		return null;
	}
	
	public void deselectAllDownloads() {
		for(Download download : this.downloadList) {
			download.setSelectedDownload(false);
		}
	}
	
	/**
	 * @param i int che rappresenta l'iesemo download nella lista (o l'iesima riga).
	 * @return Il download identificato dalla riga.
	 */
	public int getSelectedDownloadStatus() {
		Download download = this.getSelectedDownload();
		if(download==null) {
			return -1;
		}
		return download.getStatus();
	}

	// Remove a download from the list.
	/**
	 * Metodo per cancellare il download alla riga specificata.
	 * @param i int che rappresenta l'iesimo download (o riga della tabella).
	 */
	public void clearDownload(int i) {
		if(i!=-1) { 
			//cancello l'observer dal download selezionato nella tabella
			downloadList.get(i).deleteObserver(ByaTableModel.getInstance());

			//rimuovo anche dalla map dei pulsanti nella colonna action della tabella.
			StateActionTableButton.getInstance().removeButton(downloadList.get(i).getUri().toString());

			downloadList.remove(i);

			// Fire table i deletion notification to table.
			ByaTableModel.getInstance().fireTableRowsDeleted(i, i);
		} else {
			Notification.showNormalOptionPane("downloadListRemoveError");
		}
	}
	
	public void pause() {
		this.getSelectedDownload().pause();
	}
	
	public void resume() {
		this.getSelectedDownload().resume();
	}

	public void cancel() {
		this.getSelectedDownload().cancel();
	}
	
	public void pauseAll() {
		for(Download download : this.downloadList) {
			if(download.getStatus()==0) {
				download.pause();
			}
		}
	}
	
	public void resumeAll() {
		for(Download download : this.downloadList) {
			if(download.getStatus()==1) {
				download.resume();
			}
		}
	}
	
	public void cancelAll() {
		for(Download download : this.downloadList) {
			if(download.getStatus()==0 || download.getStatus()==1) {
				download.cancel();
			}
		}
		while(this.downloadList.size()>0) {
			this.clearDownload(0);
		}
	}

	// Ottiene il numero di elementi della lista
	public int getDownloadCount() {
		return downloadList.size();
	}
}
