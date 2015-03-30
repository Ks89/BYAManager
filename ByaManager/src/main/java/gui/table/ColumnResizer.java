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

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.*;

public final class ColumnResizer {

	private ColumnResizer() {}

	private static int maxWidth;
	private static int headerWidth;

	public static void adjustColumnPreferredWidths(JTable table)  {
		TableCellRenderer rend, rendHeader;
		Component compHeader, comp;
		// strategy - get max width for cells in column and
		// make that the preferred width
		TableColumnModel columnModel = table.getColumnModel();
		for (int col = 0; col < table.getColumnCount(); col++) {
			maxWidth = 0;
			headerWidth = 0;

			for (int row = 0; row < table.getRowCount(); row++) {
				rend = table.getCellRenderer(row, col);
				comp = table.prepareRenderer(rend, row, col);
//				comp = rend.getTableCellRendererComponent(table, table.getValueAt(row, col), false, false, row, col);
				if(comp!=null) {
					maxWidth = Math.max(comp.getPreferredSize().width, maxWidth);					
				} else {
					maxWidth = 0;
				}

				rendHeader = columnModel.getColumn(col).getHeaderRenderer();
				compHeader = rendHeader.getTableCellRendererComponent(table, table.getColumnName(col), false, false, row, col);
				headerWidth = Math.max(compHeader.getPreferredSize().width, headerWidth);
			} 
			if(col!=3) { //per la colonna della progressbar non attiva la funzione, tanto non importa la sua dimensione
				columnModel.getColumn(col).setPreferredWidth(Math.max(maxWidth, headerWidth));
			}
		} 
	}
}