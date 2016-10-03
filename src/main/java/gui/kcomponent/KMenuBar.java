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
package gui.kcomponent;



import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JMenuBar;

/**
 *	Classe che estende JMenuBar per cambiarne colore, font ecc...
 *	e registra anche GestoreMouse.
 */
public class KMenuBar extends JMenuBar{

	private static final long serialVersionUID = 8935959755604968585L;
	
	/**
	 * Costruttore che si occupa di impostare la font, rimuovere i bordi ecc.. per
	 * la classe menuBar personalizzato.
	 */
	public KMenuBar() {
		setOpaque(false);
		setDoubleBuffered(true);
		setFont(KFont.getInstance().getFont());
		setBorder(BorderFactory.createEmptyBorder());
		setFocusable(false);
	}

	@Override
	public void paintComponent(Graphics g) {
		g.setColor(KColors.getVerde());
		g.fillRect(0, 0, getWidth(), getHeight());
	}
}
