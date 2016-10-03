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

package it.stefanocappa.gui.table;

import it.stefanocappa.gui.kcomponent.KTableButton;
import it.stefanocappa.gui.state.StateActionTableButton;

import java.util.*;

import javax.swing.*;
import javax.swing.table.*;

import it.stefanocappa.localization.Translator;
import it.stefanocappa.logic.Download;
import it.stefanocappa.logic.DownloadFirmware;
import it.stefanocappa.logic.DownloadList;
import it.stefanocappa.logic.TableLogic;
import it.stefanocappa.model.Firmware;
import it.stefanocappa.model.ItunesVersion;


/**
 * E' il Table Model personalizzato.
 */
public final class ByaTableModel extends AbstractTableModel implements Observer {
	private static final long serialVersionUID = 1L;

	private static final String[] NOMICOLONNE = {Translator.getText("nome"), Translator.getText("versione"),
		Translator.getText("dimensione"),Translator.getText("progresso"),
		Translator.getText("stato"),Translator.getText("velocita"),Translator.getText("rimanente"),Translator.getText("actionTable")};

	private static final Class<?>[] CLASSICOLONNE = new Class<?>[] {String.class, String.class,String.class, JProgressBar.class, Integer.class, String.class, String.class, KTableButton.class};

	private static ByaTableModel instance = new ByaTableModel();

	/**
	 * Costruttore privato della classe.
	 */
	private ByaTableModel() {
	}

	/**
	 * Metodo che permette di ottenere l'istanza della classe
	 * @return La classe ByaTableModel.
	 */
	public static ByaTableModel getInstance() {
		return instance;
	}

	// Ottiene il numero di colonne
	@Override
	public int getColumnCount() {
		return NOMICOLONNE.length;
	}

	// Ottiene il nome della colonna dato l'indice
	@Override
	public String getColumnName(int col) {
		return NOMICOLONNE[col];
	}

	// Ottiene la classe della colonna dato l'indice
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Class getColumnClass(int col) {
		return CLASSICOLONNE[col];
	}

	// Ottiene il valore per una della identificata dalla riga e dalla colonna della tabella.
	public Object getValueAt(int row, int col) {
		Download download = DownloadList.getInstance().getDownload(row);
		switch (col) {
		case 0:
			if(download.getFileWeb() instanceof Firmware) {
				return ((DownloadFirmware)download).getCommercialDevice().getNomeCommerciale();
			} else {
				return "iTunes";
			}
		case 1: //versione (eventualmente anche la build)
			if(download.getFileWeb() instanceof Firmware) {
				return TableLogic.getVersione(download.getProcessList().get(0).getURIString());				
			} else {
				return ((ItunesVersion)download.getFileWeb()).getVersionAndOs();
			}
		case 2: // Size
			return download.getDimension();
		case 3: // Progress
			return new Float((((float) download.getDownloadedBytes()) / (download.getDimensioneTotale())) * 100);
		case 4: // Status
			return download.getStatus();
		case 5: // Veocita'
			return download.getSpeed();
		case 6: // Tempo rimamente
			return download.getRemainingTime(); 
		case 7: //azione
			return StateActionTableButton.getInstance().getButton(download.getUri().toString());
		}
		return "";
	}

	//metodo chiamato da Download per aggiornare la selezione della tabella.
	@Override
	public void update(Observable o, Object arg) {
		if(o instanceof Download) {
			int index = DownloadList.getInstance().getDownloadList().indexOf(o);
			fireTableRowsUpdated(index, index);
			
//			if(((Download)o).getStatus()==Download.COMPLETE) {
//				StateActionTableButton.getInstance().getButton(this.getUri().toString()).setEnabled(true);
//			}
		}

	}
	@Override
	public int getRowCount() {
		return DownloadList.getInstance().getDownloadCount();
	}

	@Override
	public void fireTableRowsInserted(int firstRow, int lastRow) {
		super.fireTableRowsInserted(firstRow, lastRow);
		TableGui.getInstance().resizeColumn();
	}
}