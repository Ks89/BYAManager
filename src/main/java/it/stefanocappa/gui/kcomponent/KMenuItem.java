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
import javax.swing.JMenuItem;

/**
 *	Classe che estende JMenuItem per cambiarne il colore, font e per
 *	gestire gli eventi MouseListener tramite la classe GestoreMouse.
 */
public class KMenuItem extends JMenuItem {
	private static final long serialVersionUID = -2555513566872593249L;
	private static final int OVER = 1;
	private static final int CLICCATO = 2;
	/**
	 * @uml.property  name="gestoreMouse"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private transient MouseManagerForKComponent gestoreMouse;

	//serve per mostrare il separatore dei menu, oppure per finire la tendina se l'item e' l'ultimo elemento di essa.
	/**
	 * @uml.property  name="separatorDownThisItem"
	 */
	private boolean separatorDownThisItem; 
	/**
	 * @uml.property  name="separatorUpThisItem"
	 */
	private boolean separatorUpThisItem; 

	/**
	 * Costruttore che imposta il testo, il mnemonic, il bordo ed infine
	 * registra il listener del mous tramite GestoreMouse.
	 * @param testo String che rappresenta il testo da mostrare.
	 * @param keyEvent int che rappresenta il mnemonic.
	 */
	public KMenuItem(String testo, int keyEvent, boolean separatorDownThisItem, boolean separatorUpThisItem) {
		setText(testo);
		setFont(KFont.getInstance().getFont());
		this.separatorUpThisItem = separatorUpThisItem;
		this.separatorDownThisItem = separatorDownThisItem;
		setOpaque(false);
		setFocusable(false);
		setDoubleBuffered(true);
		setBorder(BorderFactory.createEmptyBorder());
		setMnemonic(keyEvent);
		gestoreMouse = new MouseManagerForKComponent(this);
		this.addMouseListener(gestoreMouse);
	}

	@Override
	public void paintComponent(Graphics g) {
		if(g instanceof Graphics2D) {
			Graphics2D g2d = (Graphics2D) g;
			//imposto l'antialising per essere certo che i testi vengano disegnati bene
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			if(!isEnabled()) {
				g2d.setColor(KColors.getNero());
				g2d.fillRect(0, 0, getWidth(), getHeight());
				g2d.setColor(KColors.getGrigietto());
				g2d.drawString(getText(), 3, getHeight()/2 + 5);
			} else {
				switch(gestoreMouse.getTipo()) {
				case OVER:
					g2d.setColor(KColors.getVerde());
					g2d.fillRect(0, 0, getWidth(), getHeight());	
					g2d.setColor(KColors.getNero());
					g2d.drawString(getText(), 3, getHeight()/2 + 5);
					break;
				case CLICCATO:
					g2d.setColor(KColors.getVerdeChiaro());
					g2d.fillRect(0, 0, getWidth(), getHeight());
					g2d.setColor(KColors.getNero());
					g2d.drawString(getText(), 3, getHeight()/2 + 5);
					break;
				default:
					g2d.setColor(KColors.getNero());
					g2d.fillRect(0, 0, getWidth(), getHeight());	
					g2d.setColor(KColors.getVerde());
					g2d.drawString(getText(), 3, getHeight()/2 + 5);
					break;
				}
			}

			//disegno i bordi dell'item
			g2d.setColor(KColors.getVerde());
			g2d.drawLine(0, 0, 0, getHeight());
			g2d.drawLine(getWidth()-1, 0, getWidth()-1, getHeight());
			if(separatorUpThisItem) { 
				g2d.drawLine(0, 0, getWidth()-1, 0);
			}
			if(separatorDownThisItem) { 
				g2d.drawLine(0, getHeight()-1, getWidth()-1, getHeight()-1);
			}
		}
	}
}
