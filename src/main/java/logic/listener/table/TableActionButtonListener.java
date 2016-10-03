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

import gui.kcomponent.KTableButton;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JTable;

import notification.Notification;
import logic.Download;
import logic.DownloadFirmware;
import logic.DownloadList;
import model.DownloadCompatibilityChecker;
import model.User;

public class TableActionButtonListener extends MouseAdapter {

	private final JTable table;

	public TableActionButtonListener(JTable table) {
		this.table = table;
	}

	@Override public void mouseClicked(MouseEvent e) {
		int row = table.rowAtPoint(e.getPoint());
		int column = table.columnAtPoint(e.getPoint());
		
		if (column==7 && row < table.getRowCount() && row >= 0) {
			Object value = table.getValueAt(row, column);
			Download downloadSelected = DownloadList.getInstance().getDownload(row);

			if (value instanceof JButton && ((KTableButton)value).isEnabled() &&
					DownloadCompatibilityChecker.isCompatibileWithMyOs(downloadSelected.getFileWeb().getOperativeSystemList(),User.getInstance().getOperativeSystemInstance())) {
				KTableButton button = (KTableButton)value;
				AbstractAction action = null;

				switch(button.getButtonType()) {
				case KTableButton.EXECUTE:
					action = new ExecuteButtonListener(downloadSelected.getRenamedFilePathWithoutParts());
					button.addActionListener(action);
					break;
				case KTableButton.MOVE:
					DownloadFirmware downloadFirmware = (DownloadFirmware)downloadSelected;
					try {
						String destinationPath = User.getInstance().getItunesIpswPath(downloadFirmware.getCommercialDevice().getProduct()) + "/" + downloadFirmware.getFileName();
						action = new MoveToItunesFolderListener(downloadSelected.getUri(), downloadSelected.getRenamedFilePathWithoutParts(),destinationPath);
						button.addActionListener(action);
					} catch (IOException e1) {
						Notification.showErrorOptionPane("movingFileError2", "movingFileError2Title");
					}
					break;
					//case  KTableButton.EXTRACT:
				default: //se e' extract (messo temporanemanete)
					//per ora inutilizzato, servira' per i software jailbreal zippati/rarrati
					break;
				}

				((JButton)value).doClick();

				if(action!=null) {
					button.removeActionListener(action);
				}
			}
		}
	}
}