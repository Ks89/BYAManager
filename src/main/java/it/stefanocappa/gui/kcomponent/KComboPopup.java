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

import it.stefanocappa.gui.image.ImageLoader;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.metal.MetalScrollBarUI;
import javax.swing.plaf.metal.MetalScrollButton;

public class KComboPopup extends BasicComboPopup {
	private static final long serialVersionUID = -3019046469978236473L;
	/**
	 * @uml.property  name="scrollBar"
	 * @uml.associationEnd  
	 */
	private JScrollBar scrollBar;

	public KComboPopup(JComboBox<String> comboBox) {
		super(comboBox);
	}
	
	@Override
	//crea lo scrollpane verticale del popupmenu impostando una scrollbar personalizzata.
	protected JScrollPane createScroller() {
		JScrollPane scrollPane =  new JScrollPane(list, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollBar = new ScrollBar(); //se spostato nel costruttore crasha
		scrollPane.setVerticalScrollBar(scrollBar);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		return scrollPane;
	}

	@Override
	//personalizza il popup cambiandone i bordi.
	protected void paintBorder(Graphics g) {
		g.setColor(KColors.getVerde());
		g.drawLine(0, 0, 0, this.getHeight());
		g.drawLine(0, this.getHeight()-1, this.getWidth(), this.getHeight()-1);
		g.drawLine(this.getWidth()-1, 0, this.getWidth()-1, this.getHeight()-1);
		g.drawLine(this.getWidth() - scrollBar.getWidth(), 0, this.getWidth() - scrollBar.getWidth(), 0);
	}
}


/**
 *	Classe che estende la JScrollBar per impostarne la UI personalizzata, 
 *	rimuovere i bordi e creare 2 linee che la separano dal resto del popup, 
 *	una verde in modo da fare un po' di bordo e l'altra nera per evitare 
 *	che affiori lo sfondo grigio predefinito (non e' obbligatorio, ma e' l'unica
 *	soluzione che ho trovato per evitarlo).
 */
class ScrollBar extends JScrollBar {
	private static final long serialVersionUID = 2944264293409485999L;

	/**
	 * Costruttore che rimuove i bordi e imposta la UI.
	 */
	public ScrollBar() {
		setBorder(BorderFactory.createEmptyBorder());
		setOpaque(false);
		setFocusable(false);
		setUI(new ScrollBarUIPersonalizzata());
	}

	@Override
	protected void paintChildren(Graphics g) {
		g.setColor(KColors.getVerde());
		g.drawLine(1, 0, 1, getHeight() + 1);
		g.setColor(KColors.getNero());
		g.drawLine(0, 0, 0, getHeight() + 1);
		super.paintChildren(g);
	}
}


/**
 *	Classe che estende MetalScrollBarUI per personalizzare la UI della scrollbar caricando 
 *	freccie personalizzare e cambiando i colori.
 */
class ScrollBarUIPersonalizzata extends MetalScrollBarUI {
	@Override 
	//metodo sovrascritto nello stesso modo con cui e' fatto openjdk, ho dovuto cercare il sorgente del metodo e riusarlo
	//lo scopo e' quello di cambiare il colore della scrollbar vera e propria, cioe' la parte scorrevole (thumb).
	protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
		if(thumbBounds.isEmpty() || !scrollbar.isEnabled())     {
			return;
		}
		int w = thumbBounds.width;
		int h = thumbBounds.height;
		g.translate(thumbBounds.x, thumbBounds.y); //trasla nel piano 2 punti

		g.setColor(KColors.getVerde());
		g.fillRect(2, 0, w, h);
		g.translate(-thumbBounds.x, -thumbBounds.y); //non so perche' ma c'e' su openjdk
	}

	@Override
	//metodo sovrascritto nello stesso modo con cui e' fatto openjdk, ho dovuto cercare il sorgente del metodo e riusarlo
	//lo scopo e' quello di cambiare il colore in nero dello sfondo della scrollbar (track).
	protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
		g.setColor(KColors.getNero());
		g.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
	}

	@Override
	//imposta la freccia in alto, creata con FrecceScrollBarButton
	protected JButton createIncreaseButton(int orientation) {
		increaseButton =  new FrecceScrollBarButton( orientation, scrollBarWidth, isFreeStanding );
		return increaseButton;
	}

	@Override
	//imposta la freccia in basso, creata con FrecceScrollBarButton
	protected JButton createDecreaseButton(int orientation)	{
		increaseButton =  new FrecceScrollBarButton( orientation, scrollBarWidth, isFreeStanding );
		return increaseButton;
	}
}


/**
 *	Classe che estende MetalScrollButton per personalizzare le frecce della scrollbar.
 */
class FrecceScrollBarButton extends MetalScrollButton {
	private static final long serialVersionUID = 420148877104794931L;
	/**
	 * @uml.property  name="frecciaSu"
	 */
	private BufferedImage frecciaSu;
	/**
	 * @uml.property  name="frecciaGiu"
	 */
	private BufferedImage frecciaGiu;

	/**
	 * Costruttore della classe che carica le 2 immagini.
	 * @param direction
	 * @param width
	 * @param freeStanding
	 */
	public FrecceScrollBarButton(int direction, int width, boolean freeStanding) {
		super(direction, width, freeStanding);
		frecciaSu = ImageLoader.getInstance().getFrecciaComboBoxSu();
		frecciaGiu = ImageLoader.getInstance().getFrecciaComboBoxGiu(); 
	}

	@Override
	//metodo che pittura le 2 freccie nella scrollbar.
	public void paint(Graphics g) {
		if(direction == NORTH) {
			g.drawImage(frecciaSu, 2, 0, null);
		} else {
			g.drawImage(frecciaGiu, 2, 0, null);
		}
	}
}

//tecnica interessantissima per ottenere il pulsante della cambobox, 
//quello che appare nella combo e non nel popup che si apre
//Component[] comp = this.getComponents();
//for (int i = 0; i < comp.length; i++) {
//  if (comp[i] instanceof MetalComboBoxButton) {
//      MetalComboBoxButton coloredArrowsButton = (MetalComboBoxButton) comp[i];
//      coloredArrowsButton.getHeight()
//      coloredArrowsButton.getWidth()              
//      coloredArrowsButton.setComboIcon(new ImageIcon(freccia));
//      break;
//  }
//}


// ormai inutile ma e' meglio tenerlo che potrebbe servire in futuro per altre cose ed era stato difficile da trovare
//		BasicComboPopup bcp = (BasicComboPopup) super.createPopup();
//		bcp.setBorder(BorderFactory.createLineBorder(new Color(186,194,44)));
//		// non cancellare perche' utile ed e' difficile trovare questa info su internet
//		// c'e' un bordo interno attorno alla lista, dentro lo sroller
//		// JList list = bcp.getList();
//		// list.setBorder(BorderFactory.createLineBorder(Color.green, 2));
//		return bcp;