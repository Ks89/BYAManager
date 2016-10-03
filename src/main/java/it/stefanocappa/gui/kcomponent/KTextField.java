
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

import javax.swing.JTextField;

/**
 *	Classe che estende JTextField per cambiare la font e i colori.
 */
public class KTextField extends JTextField {
	private static final long serialVersionUID = 2143102562782582070L;

	/**
	 * Costruttore che imposta il testo, la font e i colori di sfondo e testo.
	 * @param testo String rappresentante il testo da scrivere all'interno.
	 */
	public KTextField(String testo) {
		setText(testo);
		setFont(KFont.getInstance().getFont());
		setBackground(KColors.getNero());
		setForeground(KColors.getVerde());
		setDisabledTextColor(KColors.getGrigietto());
	}

	@Override
	protected void paintBorder(Graphics g) {
		//disegna il bordo creando un quadrato (vuoto all'interno)
		g.drawRect(0, 0, getWidth() - 1 , getHeight() - 1);
	}


}