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

import javax.swing.BorderFactory;
import javax.swing.JPopupMenu;

/**
 *	Classe che estende JPopupMenu per rimuovere i bordi e cambiarne il colore.
 */
public class KPopupMenu extends JPopupMenu {
	private static final long serialVersionUID = 2357101686781091107L;

	/**
	 * Costruttore che rimuove i bordi. Il cambio di colore e' eseguito
	 * dal metodo paintComponent.
	 */
	public KPopupMenu() {
		setOpaque(false);
		setBorder(BorderFactory.createEmptyBorder());
		setFocusable(false);
		setDoubleBuffered(true);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		g.setColor(KColors.getNero());
		g.fillRect(0, 0, getWidth(), getHeight());
	}
}
