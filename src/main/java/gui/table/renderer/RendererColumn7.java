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

package gui.table.renderer;

import gui.kcomponent.KTableButton;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

public class RendererColumn7 extends JButton implements TableCellRenderer {

	private static final long serialVersionUID = 7046535873504692321L;

	public RendererColumn7() {
		setOpaque(true);
	}

	public Component getTableCellRendererComponent(JTable table, Object value,boolean isSelected, boolean hasFocus, int row, int column) {
		KTableButton button = (KTableButton)value;
		button.setSelected(isSelected);
		return button;	
	}
}