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
import javax.swing.JPanel;

/**
 *	Classe che estende JPanel per rimuovere i bordi e caricare lo sfondo tramite
 *  un'immagine ripetuta fino alla risoluzione di 1920xqualche cosa.
 */
public class KPanel extends JPanel{
	private static final long serialVersionUID = 8624047765321094743L;
	private static final int MAX = 1920;
	/**
	 * @uml.property  name="sfondo"
	 */
	private transient BufferedImage sfondo;
	/**
	 * @uml.property  name="errore"
	 */
	private boolean errore = false;

	/**
	 * Costruttore che ottiene lo sfondo dalla classe Immagini e 
	 * rimuove i bordi dal JPanel. In caso di problemi col caricamento lo sfondo
	 * non viene disegnato. La vera operazione di "pittura" dell'immagine avviene
	 * nel metodo paintComponent che, in caso di errori, si occupa solo di richiamare 
	 * il metodo nella super-classe senza disegnare lo sfondo.
	 */
	public KPanel() {
		setOpaque(false);
		setBorder(BorderFactory.createEmptyBorder());
		if(!ImageLoader.getInstance().isErrore()) {
			sfondo = ImageLoader.getInstance().getSfondo();
		} else {
			this.errore = true;
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {
		//TODO renderlo adattabile con prestazioni accettabili
		if(!this.errore) {
			for(int i=0; i<MAX; i = i + sfondo.getHeight()) {
				for(int j=0; j<MAX; j = j + sfondo.getWidth()) {
					g.drawImage(sfondo, j, i, null);
				}
			}
			
		}
	}
}
