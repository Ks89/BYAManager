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

package it.stefanocappa.gui.kcomponent;



import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.JTabbedPane;
import javax.swing.plaf.metal.MetalTabbedPaneUI;

/**
 *	Classe che estende JTabbedPane personalizzandone la UI. Cioe', si occupa di 
 *	rimuovere i bordi e cambiare i pulsanti.
 */
public class KTabbedPane extends JTabbedPane {
	private static final long serialVersionUID = -900930986719784027L;

	/**
	 * Costruttore che imposta l'UI personalizzato.
	 */
	public KTabbedPane() {
		setOpaque(false);
		setFocusable(false);
		setUI(new UITab());
	}
}


/**
 *	Classe interna che estende MetalTabbedPaneUI per rimuovere i bordi e 
 *	cambiare i pulsanti dei tab.
 */
class UITab extends MetalTabbedPaneUI {
	@Override
	protected Insets getContentBorderInsets(int tabPlacement) {
		return new Insets(0, 0, 0, 0);
	}
	@Override
	protected void paintBottomTabBorder(int arg0, Graphics arg1, int arg2,
			int arg3, int arg4, int arg5, int arg6, int arg7, boolean arg8) {
	}
	@Override
	protected void paintContentBorderBottomEdge(Graphics arg0, int arg1,
			int arg2, int arg3, int arg4, int arg5, int arg6) {
	}
	@Override
	protected void paintContentBorderLeftEdge(Graphics arg0, int arg1,
			int arg2, int arg3, int arg4, int arg5, int arg6) {
	}
	@Override
	protected void paintContentBorderRightEdge(Graphics arg0, int arg1,
			int arg2, int arg3, int arg4, int arg5, int arg6) {
	}
	@Override
	protected void paintContentBorderTopEdge(Graphics arg0, int arg1, int arg2,
			int arg3, int arg4, int arg5, int arg6) {
	}
	@Override
	protected void paintFocusIndicator(Graphics arg0, int arg1,
			Rectangle[] arg2, int arg3, Rectangle arg4, Rectangle arg5,
			boolean arg6) {
	}
	@Override
	protected void paintHighlightBelowTab() {
	}
	@Override
	protected void paintLeftTabBorder(int arg0, Graphics arg1, int arg2,
			int arg3, int arg4, int arg5, int arg6, int arg7, boolean arg8) {
	}
	@Override
	protected void paintRightTabBorder(int arg0, Graphics arg1, int arg2,
			int arg3, int arg4, int arg5, int arg6, int arg7, boolean arg8) {
	}
	@Override
	protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex,
			int x, int y, int w, int h, boolean isSelected) {
		if(isSelected) {
			g.setColor(KColors.getVerdeChiaro());
			g.fillRect(x, y, w-1, h);
		} else {
			g.setColor(KColors.getVerde());
			g.fillRect(x, y, w-1, h);
		}
	}
	@Override
	protected void paintTabBorder(Graphics arg0, int arg1, int arg2, int arg3,
			int arg4, int arg5, int arg6, boolean arg7) {
	}
	@Override
	protected void paintTopTabBorder(int arg0, Graphics arg1, int arg2,
			int arg3, int arg4, int arg5, int arg6, int arg7, boolean arg8) {
	}
}
