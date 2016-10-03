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
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.JMenu;

/**
 *	Classe che estende JMenu per cambiare il colore, la font ecc... e registra anche GestoreMouse.
 */
public class KMenu extends JMenu{
	private static final long serialVersionUID = -4694870199132752633L;
	private static final int OVER = 1;
	private static final int CLICCATO = 2;
	/**
	 * @uml.property  name="gestoreMouse"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private transient MouseManagerForKComponent gestoreMouse;

	/**
	 * Costruttore che imposta il testo, registra il MouseListener, gestisce i bordi 
	 * ed imposta la Font personalizzata.
	 * @param testo
	 */
	public KMenu(String testo) {
		super(testo);
		setFont(KFont.getInstance().getFont());
		setOpaque(false);
		setFocusable(false);
		setBorder(BorderFactory.createEmptyBorder());
		setDoubleBuffered(true);

		gestoreMouse = new MouseManagerForKComponent(this);
		this.addMouseListener(gestoreMouse);

		//ottengo la tendina (senza elementi dentro) che si apre, cioe' un popUpMenu e tolgo i bordi
		getPopupMenu().setBorder(BorderFactory.createEmptyBorder());
	}


	@Override
	public void paintComponent(Graphics g) {
		if(g instanceof Graphics2D) {
			Graphics2D g2d = (Graphics2D) g;
			//imposto l'antialising per essere certo che i testi vengano disegnati bene
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			switch(gestoreMouse.getTipo()) {
			case OVER:
				g2d.setColor(KColors.getVerdeChiaro());
				g2d.fillRect(0, 0, getWidth(), getHeight());	
				g2d.setColor(KColors.getNero());
				g2d.drawString(getText(), 1, getHeight()/2 + 5);
				break;
			case CLICCATO:
				g2d.setColor(KColors.getNero());
				g2d.fillRect(0, 0, getWidth(), getHeight());	
				g2d.setColor(KColors.getVerde());
				g2d.drawString(getText(), 1, getHeight()/2 + 5);
				break;
			default:
				g2d.setColor(KColors.getVerde());
				g2d.fillRect(0, 0, getWidth(), getHeight());	
				g2d.setColor(KColors.getNero());
				g2d.drawString(getText(), 1, getHeight()/2 + 5);
				break;
			}
		}
	}
}
