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

package gui.table;

import gui.kcomponent.KColors;
import gui.state.StateButton;
import gui.state.StateTablePopupItem;

import java.awt.Color;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.JTableHeader;

import gui.table.renderer.RendererColonna0;
import gui.table.renderer.RendererColonna4;
import gui.table.renderer.RendererColumn7;
import gui.table.renderer.RendererGeneric;
import gui.table.renderer.RendererHeader;
import gui.table.renderer.RendererProgressBar;
import localization.Translator;
import logic.Download;
import logic.DownloadList;
import logic.listener.popupitem.DisableShaCheckListener;
import logic.listener.popupitem.DownloadInBrowserListener;
import logic.listener.popupitem.OpenDownloadFolderListener;
import logic.listener.table.TableActionButtonListener;
import logic.listener.table.TablePopupListener;
import logic.listener.toolbarbutton.PauseListener;
import logic.listener.toolbarbutton.RemoveListener;
import logic.listener.toolbarbutton.ResumeListener;

public final class TableGui extends JTable implements Observer {
	private static final long serialVersionUID = -3798208288140176133L;

	// serve per verificare che non possa essere inserito nulla durante la cancellazione
	private static boolean clearing;
	private StateTablePopupItem popupItemState;
	private static ByaTableModel tableModel;
	private static TableGui instance = new TableGui();

	/**
	 * Metodo che permette di ottenere l'istanza della classe
	 * @return La classe TableGui.
	 */
	public static TableGui getInstance() {
		return instance;
	}

	private TableGui() {
		tableModel = ByaTableModel.getInstance();
		popupItemState = new StateTablePopupItem();
		setModel(tableModel);
		
		getColumnModel().getColumn(7).setCellRenderer(new RendererColumn7());
		getColumnModel().getColumn(4).setCellRenderer(new RendererColonna4());
		getColumnModel().getColumn(0).setCellRenderer(new RendererColonna0());
		
		RendererProgressBar renderer = new RendererProgressBar(0, 100);
		setDefaultRenderer(Object.class, new RendererGeneric());
		setDefaultRenderer(JProgressBar.class, renderer);
		
		setRowHeight((int) renderer.getPreferredSize().getHeight());
		setShowGrid(false);
		setBackground(Color.BLACK);
		setOpaque(false);
		setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
		
		JTableHeader tableHeader = getTableHeader();
		tableHeader.setBackground(KColors.getNero());
		tableHeader.setFocusable(false);
		tableHeader.setBorder(BorderFactory.createEmptyBorder());
		tableHeader.setReorderingAllowed(false);
		tableHeader.setResizingAllowed(true);
		for(int i=0; i<8 ;i++ ){
			getColumnModel().getColumn(i).setHeaderRenderer(new RendererHeader());
		}
		
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				tableSelectionChanged();
			}
		});
		
		addMouseListener(new TableActionButtonListener(this));

		TablePopupListener tpl = new TablePopupListener();
		addMouseListener(tpl);
		tpl.addObserver(this);
	}

	/**
	 * Metodo importantissimo che una volta chiamato si occupa di ridemensionare le colonne della tabella.
	 * Viene fatto tutto in automatico.
	 */
	public void resizeColumn() {
//		ColumnResizer.adjustColumnPreferredWidths(this);
//		this.revalidate();
	}

	// Called when table row selection changes.
	private void tableSelectionChanged() {
		//If not in the middle of clearing a download,
		//set the selected download
		if (!clearing && getSelectedRow()!=-1) {
			DownloadList.getInstance().deselectAllDownloads();
			
			Download selectedDownload = DownloadList.getInstance().getDownload(getSelectedRow()); //valore costantemente letto da GestioneLogica
			selectedDownload.setSelectedDownload(true);
			StateButton.aggiornaPulsanti(selectedDownload.getStatus());
		}
	}

	public static void setClearing(boolean clearing) {
		TableGui.clearing = clearing;
	}

	public static ByaTableModel getTableModel() {
		return tableModel;
	}

	public StateTablePopupItem getPopupItemState() {
		return popupItemState;
	}

	@Override
	public void update(Observable o, Object ogg) {
		if(o instanceof TablePopupListener) {
			TablePopupListener tpl = (TablePopupListener)o;
			Download download = DownloadList.getInstance().getDownload(tpl.getRow());
			//abilito le voci del popupMenu secondo lo stato del download su cui si ha cliccato
			popupItemState.setPopupState(download.getStatus());
			
			popupItemState.getPausaItem().addActionListener(new PauseListener());
			popupItemState.getRiprendiItem().addActionListener(new ResumeListener());
			popupItemState.getRimuoviItem().addActionListener(new RemoveListener());
			popupItemState.getMostraItem().addActionListener(new OpenDownloadFolderListener());
			popupItemState.getDisableShaCheck().addActionListener(new DisableShaCheckListener());
			popupItemState.getUrlItem().addActionListener(new DownloadInBrowserListener(download.getUri()));
			
			
			JPopupMenu popup = popupItemState.getPopUp();

			//ora devo impostare la voce per disabiliare lo sha
			JMenuItem menuItem = popupItemState.getDisableShaCheck();
			if(download.isNeedToValidate()) {
				menuItem.setText(Translator.getText("disableShaCheck"));
			} else {
				menuItem.setText(Translator.getText("activateShaCheck"));
			}
			
			//se il downlaod e' in corso o in pausa permetti di cambiare la verifica dello sha, altrimenti no
			menuItem.setEnabled(download.getStatus()==Download.DOWNLOADING || download.getStatus()==Download.PAUSED);

			popup.show(this, tpl.getxPos(), tpl.getyPos());
			popup.setVisible(true);

			popupItemState.getPausaItem().removeActionListener(new PauseListener());
			popupItemState.getRiprendiItem().removeActionListener(new ResumeListener());
			popupItemState.getRimuoviItem().removeActionListener(new RemoveListener());
			popupItemState.getMostraItem().removeActionListener(new OpenDownloadFolderListener());
			popupItemState.getDisableShaCheck().removeActionListener(new DisableShaCheckListener());
			popupItemState.getUrlItem().removeActionListener(new DownloadInBrowserListener(download.getUri()));
			
		}
	}
}
