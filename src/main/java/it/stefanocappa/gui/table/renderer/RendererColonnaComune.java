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

package it.stefanocappa.gui.table.renderer;

import it.stefanocappa.gui.kcomponent.KColors;

import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.table.DefaultTableCellRenderer;

public class RendererColonnaComune extends DefaultTableCellRenderer {
	private static final long serialVersionUID = 5689945361897950307L;
	private static final int TUTTO=0;
	private static final int SOLOICONA=1;
	private static final int SOLOTESTO=2;
	private boolean isSelected;
	
	protected void impostaGrafica(int impostazExtra, Object value, Icon icon, boolean isSelected) {
		this.isSelected = isSelected;

		switch(impostazExtra) {
		case TUTTO:
			setText((String)value);
			setIcon(icon);
			break;
		case SOLOICONA:
			setIcon(icon);
			break;
		case SOLOTESTO: //sia con questo, sia con default fa sempre setText(...)
		default: 
			setText((String)value);
			break;
		}
		
	
		if (isSelected) {
			setBackground(KColors.getVerde());
			setForeground(KColors.getNero());
		} else {
			setForeground(KColors.getVerde());
			setBackground(KColors.getNero());
		}
		setOpaque(false);
		setFocusable(false);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		if (isSelected) {
			g.setColor(KColors.getVerde());
			g.fillRect(0, 0, getWidth(), getHeight());
		}
		super.paintComponent(g);
	}
	
}
