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

import java.awt.Component;

import javax.swing.JTable;

public class RendererGeneric extends RendererColonnaComune {
	private static final long serialVersionUID = -2581537749293515250L;

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,boolean isSelected, boolean hasFocus, int row, int column) {
		super.impostaGrafica(2, (String)value, null, isSelected); //2 = imposta SOLO testo
		return this;
	}
}
