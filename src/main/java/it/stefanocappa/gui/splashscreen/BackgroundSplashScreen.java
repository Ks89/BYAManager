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

package it.stefanocappa.gui.splashscreen;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

/**
 * Classe che si occupa di impostare lo sfondo della SplashScreen.
 */
public class BackgroundSplashScreen extends JPanel {

	private static final long serialVersionUID = 1L;
	/**
	 * @uml.property  name="img"
	 */
	private transient BufferedImage img;

	/**
	 * Costruttore che carica l'immagine e la setta nel JPanel.
	 * @param immagine
	 */
	public BackgroundSplashScreen(BufferedImage immagine) {
		super(true); //crea un JPanel con doubleBuffered true
		this.img = immagine;
	}

	// sovrascrivi il metodo paintComponent passandogli l'immagine partendo dalle coordinate 0,0 senza usare un ImageObserver (null)
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(img, 0, 0, null);
	}
}
