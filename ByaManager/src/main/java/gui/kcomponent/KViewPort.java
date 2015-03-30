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

import javax.swing.JViewport;

/**
 *	Classe che estende JViewport per impostare lo sfondo alla JTable.
 */
public class KViewPort extends JViewport	{
	private static final long serialVersionUID = -8742194335791019526L;
	/**
	 * @uml.property  name="sfondo"
	 */
	private transient BufferedImage sfondo;

	/**
	 * Costruttore che carica lo sfondo solo nel caso in cui la classe Immagini riesca 
	 * a ottenere l'immagine sfondo. In caso non vi riesca, il metodo paintChildren lo pittura
	 * nel modo predefinito.
	 */
	public KViewPort() {
		if(!ImageLoader.getInstance().isErrore()) {
			sfondo = ImageLoader.getInstance().getSfondo();
		}
	}

	@Override
	public void paintChildren(Graphics g) {
		int maxHeight = getHeight() + (sfondo.getHeight()*2);
		int maxWidth = getWidth() + (sfondo.getWidth()*2);

		if(!ImageLoader.getInstance().isErrore()) {
			for(int i=0; i<maxHeight; i = i + sfondo.getHeight()) {
				for(int j=0; j<maxWidth; j = j + sfondo.getWidth()) {
					g.drawImage(sfondo, j, i, null);
				}
			}
		}
		super.paintChildren(g);
	}
};
