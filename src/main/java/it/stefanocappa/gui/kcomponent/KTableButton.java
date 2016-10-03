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


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JButton;

public class KTableButton extends JButton {
	private static final long serialVersionUID = 4635225530592148376L;

	public static final int EXECUTE = 0;
	public static final int MOVE = 1;
	public static final int EXTRACT = 2;

	/**
	 * @uml.property  name="buttonType"
	 */
	private int buttonType; //puo' essere execute, move o extract (vedi le costanti)

	/**
	 * Costruttore che imposta il testo e aggiunge il listener del mouse
	 * per far cambiare il colore del pulsante.
	 * @param text String che rappresenta il testo da inserire nel pulsante.
	 */
	public KTableButton(String text, int buttonType) {
		super(text);
		this.buttonType = buttonType;

		setFont(KFont.getInstance().getFont());
		setText(text);
		setOpaque(false);
		setFocusable(false); //metterlo true e gestire bene anche interazione utenti disabili
		setBorderPainted(false);
		setFocusPainted(false);
		setContentAreaFilled(false);
		this.addMouseListener(new MouseManagerForKComponent(this));
	}

	@Override
	protected void paintComponent(Graphics g) {
		Color colorForeground, colorButton;
		Graphics2D g2d = (Graphics2D)g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		if(isEnabled())	{
			if(isSelected()) {
				//disegno il background
				g2d.setColor(KColors.getVerde());
				g2d.fillRect(0, 0, getWidth(), getHeight());

				//ottengo colore e lo metto in colorForeground per disegnare piu' avanti
				colorForeground = KColors.getVerde();
				colorButton = KColors.getNero();
			} else {
				colorForeground = KColors.getNero();
				colorButton = KColors.getVerde();
			}
		} else {
			if(isSelected()) {
				//disegno il background
				g2d.setColor(KColors.getVerde());
				g2d.fillRect(0, 0, getWidth(), getHeight());

				colorForeground = KColors.getVerdeChiaro();
				colorButton = KColors.getGrigietto();

			} else {
				colorForeground = KColors.getVerdeChiaro();
				colorButton = KColors.getGrigietto();
			}
		}

		setForeground(colorForeground);
		g2d.setColor(colorButton);
		
		if(!getText().contains("no")) {
			g2d.fillRoundRect(4, 1, getWidth() - 8, getHeight() - 2, 18, 18);
			super.paintComponent(g2d);
		}
	}

	/**
	 * @return
	 * @uml.property  name="buttonType"
	 */
	public int getButtonType() {
		return buttonType;
	}
}
