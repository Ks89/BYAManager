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

import gui.image.ImageLoader;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.ComboPopup;

/**
 *	Classe che estende JCombooBox per personalizzare ogni aspetto estetico.
 */
public class KComboBox extends JComboBox<String> {
	private static final long serialVersionUID = 3128869030962368706L;
	/**
	 * @uml.property  name="freccia"
	 */
	private transient BufferedImage freccia;

	/**
	 * Costruttore che carica la freccia da mostrare quando la combobox
	 * e' chiusa (senza menu aperto) ed imposta l'UI personalizzato.
	 */
	public KComboBox() {
		setDoubleBuffered(true);
		freccia = ImageLoader.getInstance().getFrecciaComboBox();
		setFocusable(false);
		setUI(new UIComboBox());
		setRenderer(new KComboBoxRenderer());
	}

	@Override
	public void paintComponent(Graphics g){
		//pitturo la combobox a riposo (menu chiuso)
		super.paintComponent(g);
		g.setColor(KColors.getVerde());
		g.drawRect(0, 0, getWidth() - freccia.getWidth() - 1, getHeight() - 1);
	}
}


/**
 *	Classe che estende BasicComboBoxUI per personalizzare il popup e le freccie.
 */
class UIComboBox extends BasicComboBoxUI {
	@Override
	protected ComboPopup createPopup() {
		return new KComboPopup(comboBox);
	}

	@Override
	protected JButton createArrowButton() {
		//personalizza la freccia che deve apparire quando la combobox e' a riposo.
		return new PulsanteFreccia();
	}
}


/**
 *	Classe che estende JButton per personalizzare il pulsante che appare
 *	quando la combobox e' a riposo.
 */
class PulsanteFreccia extends JButton {
	private static final long serialVersionUID = -1926415597630465475L;
	/**
	 * @uml.property  name="freccia"
	 */
	private BufferedImage freccia;

	/**
	 * Costruttore che imposta la freccia.
	 */
	public PulsanteFreccia() {
		freccia = ImageLoader.getInstance().getFrecciaComboBox();
		setBorder(BorderFactory.createEmptyBorder());
		setOpaque(false);
		setFocusable(false);
	}

	@Override
	public void paintComponent(Graphics g) {
		g.drawImage(freccia,0,0,this);
		g.setColor(KColors.getVerde());
		g.drawRect(0, 0, getWidth()-1, getHeight()-1);
	}
}


