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

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Observable;

import javax.swing.JTable;
import javax.swing.SwingUtilities;

import lombok.Getter;

public class TablePopupListener extends Observable implements MouseListener {

	@Getter private int xPos;
	@Getter private int yPos;
	@Getter private int row;

	@Override
	public void mouseClicked(MouseEvent e) {
		if (SwingUtilities.isRightMouseButton(e)) {
			JTable t = (JTable) e.getSource();
			row = t.rowAtPoint(e.getPoint());
			xPos = e.getX();
			yPos = e.getY();
			t.requestFocusInWindow();
			t.changeSelection(row, 0, false, false);
			stateChanged();
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}

	/**
	 * Metodo per notificare un cambiamento all'observers (TableGui o cmq la Tabella, cmq si chiami).
	 */
	public void stateChanged() {
		setChanged();
		notifyObservers();
	}
}
