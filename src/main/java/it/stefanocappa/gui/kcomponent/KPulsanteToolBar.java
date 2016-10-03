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
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JToolTip;

/**
 *	Classe che estende JButton per creare essere usata nella creazione dei JButton della
 *	JToolbar. Infatti, vengono impostate le icone dei pulsanti, i tooltip (personalizzando i colori), 
 *	registrato con GestioneMouse un MouseListener per gestire l'illuminazione dello sfondo dei pulsanti 
 *	e per finire viene anche gestita la differenza tra pulsanti attivi e disabilitati.
 */
public class KPulsanteToolBar extends JButton {
	private static final long serialVersionUID = -3796052033082975729L;
	/**
	 * @uml.property  name="immagineAttivo"
	 */
	private transient BufferedImage immagineAttivo;
	/**
	 * @uml.property  name="immagineDisattivato"
	 */
	private transient BufferedImage immagineDisattivato;
	private static final int OVER = 1;
	/**
	 * @uml.property  name="gestoreMouse"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private transient MouseManagerForKComponent gestoreMouse;

	/**
	 * Costruttore che riceve le immagini dei pulsanti attivo e disattivo, rimuove il bordo, imposta 
	 * una dimensione prestabilita e minima, registra con GestoreMouse un MouseListener per occuparsi 
	 * dell'illuminazione dello sfondo del pulsante al passaggio del mouse. Nota bene: per disegnare 
	 * l'illuminazione viene usato un rettangolo ad angoli arrotondati semi-trasparente. Se volessi aggiornarne lo 
	 * stato dovrei prima cancellare quello presente, sovrascrivendo l'immagine con quello che vi era sotto in precedeza, 
	 * e poi ridisegnare la trasparenza. Questo evita che si sovrappongano 2 rettangoli semi-trasparenti riducendone
	 * cosi' la trasparenza.
	 * @param immagine BufferedImage che rappresenta il pulsante attivato.
	 * @param immagineDisattivato BufferedImage che rappresenta il pulsante disattivato.
	 */
	public KPulsanteToolBar(BufferedImage immagine, BufferedImage immagineDisattivato) {
		setBorder(BorderFactory.createEmptyBorder());
		setMinimumSize(new Dimension(20,36));
		setBounds(0, 0, 20, 36);
		setOpaque(false);
		this.immagineAttivo = immagine;
		this.immagineDisattivato = immagineDisattivato;

		gestoreMouse = new MouseManagerForKComponent(this);
		this.addMouseListener(gestoreMouse);
	}

	@Override
	public JToolTip createToolTip() {
		JToolTip toolTip = super.createToolTip();
		if(isEnabled()) {
			toolTip.setBackground(KColors.getVerde());
			toolTip.setForeground(KColors.getNero());
			toolTip.setBorder(BorderFactory.createLineBorder(KColors.getVerde()));
		} else {
			toolTip.setBackground(KColors.getNero());
			toolTip.setForeground(Color.GRAY);
			toolTip.setBorder(BorderFactory.createLineBorder(KColors.getNero()));
		}
		return toolTip;
	}

		@Override
		protected void paintComponent(Graphics g) {
			if(isEnabled()) {
				if(gestoreMouse.getTipo()==OVER) { 
					g.setColor(KColors.getBiancoTrasparente());
					g.fillRoundRect(3, 3, getWidth() - 6, getHeight() - 6 , 3, 3);
					g.drawImage(this.immagineAttivo, getWidth()/2 - immagineAttivo.getWidth()/2, getHeight()/2 - immagineAttivo.getHeight()/2, null);
				} else {
					g.drawImage(this.immagineAttivo, getWidth()/2 - immagineAttivo.getWidth()/2, getHeight()/2 - immagineAttivo.getHeight()/2, null);
				}
			} else {
				g.drawImage(this.immagineDisattivato, getWidth()/2 - immagineDisattivato.getWidth()/2, getHeight()/2 - immagineDisattivato.getHeight()/2, null);
			}
		}
}