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

import gui.kcomponent.KColors;

import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.BevelBorder;
import javax.swing.table.TableCellRenderer;

public class RendererHeader extends JLabel implements TableCellRenderer {
	private static final long serialVersionUID = -2599771017712940346L;

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int rowIndex, int vColIndex) {
		setText(value.toString());
		setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, KColors.getNeroScuroTabella(), KColors.getNero()));
		setOpaque(false);
		return this;
	}

	@Override
	public void paintComponent(Graphics g) {
		if (!(g instanceof Graphics2D)) {
			return;
		}
		
		Graphics2D g2d = (Graphics2D)g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setColor(KColors.getVerde());
		g2d.setFont(new Font("Lucida Sans Serif",Font.BOLD,11));
		g2d.drawString(getText(), 3, getHeight()/2 + 4);
	}
}
