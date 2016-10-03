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

import java.awt.*;

import javax.swing.*;
import javax.swing.plaf.metal.MetalProgressBarUI;
import javax.swing.table.*;

/**
 *	Classe per aggiornare la barra di progresso del download nella tabella.
 */
public class RendererProgressBar extends JProgressBar implements TableCellRenderer {
	private static final long serialVersionUID = 1L;

	/**
	 * Costruttore per la classe che inizializza min e max
	 * @param min int che rappresenta il massimo.
	 * @param max int che rappresenta il minimo.
	 */
	public RendererProgressBar(int min, int max) {
		super(min, max);
		setBorder(BorderFactory.createEmptyBorder());
		setUI(new UIProgressBar());
		setStringPainted(true);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,boolean hasFocus, int row, int column) {
		// imposta la percentuale di completamento della JProgressBar
		setOpaque(false);
		setValue((int) ((Float) value).floatValue());
		return this;
	}
}


/**
 *	Classe che definisce la UI della ProgressBar estendendo l'UI metal e modificandolo.
 *	In particolare cambia il colore (anche della percentuale scritta)  e il bordo.
 */
class UIProgressBar extends MetalProgressBarUI{
	@Override
	public void paintDeterminate(Graphics g, JComponent c) {
		if (!(g instanceof Graphics2D)) {
			return;
		}

		Insets b = progressBar.getInsets(); // area for border
		int barRectWidth = progressBar.getWidth() - (b.right + b.left);
		int barRectHeight = progressBar.getHeight() - (b.top + b.bottom);

		if (barRectWidth <= 0 || barRectHeight <= 0) {
			return;
		}

		// amount of progress to draw
		int amountFull = getAmountFull(b, barRectWidth, barRectHeight);

		Graphics2D g2 = (Graphics2D)g;
		g2.setColor(KColors.getVerde());			

		if (progressBar.getOrientation() == JProgressBar.HORIZONTAL) {
			// draw the cells
			if (getCellSpacing() == 0 && amountFull > 0) {
				// draw one big Rect because there is no space between cells
				g2.setStroke(new BasicStroke((float)barRectHeight, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
			} else {
				// draw each individual cell
				g2.setStroke(new BasicStroke((float)barRectHeight, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 
						0.f, new float[] { getCellLength(), getCellSpacing() }, 0.f));
			}

			if (c.getComponentOrientation().isLeftToRight()) {
				g2.drawLine(b.left, (barRectHeight/2) + b.top, amountFull + b.left, (barRectHeight/2) + b.top);
			} else {
				g2.drawLine((barRectWidth + b.left), (barRectHeight/2) + b.top, barRectWidth + b.left - amountFull, (barRectHeight/2) + b.top);
			}
		}

		// Se la percentuale viene scritta la mostra
		if (progressBar.isStringPainted()) {
			paintString(g, b.left, b.top, barRectWidth, barRectHeight, amountFull, b);
		}
	}

	@Override
	protected Color getSelectionBackground() {
		return KColors.getVerde();
	}

	@Override
	protected Color getSelectionForeground() {
		return KColors.getNero();
	}
}