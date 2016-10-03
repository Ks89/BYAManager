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
import it.stefanocappa.gui.image.ImageResizer;

import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;

/**
 *	Classe che estende JToolBar per personalizzare il Layout, il bordo, i pulsanti che contiene,
 *	e l'interno aspetto dello sfondo (cioe' la toolbar nel vero senso della parola).
 */
public class KToolBar extends JToolBar{
	private static final long serialVersionUID = 8935959755604968585L;
	/**
	 * @uml.property  name="sx"
	 */
	private transient BufferedImage sx;
	/**
	 * @uml.property  name="dx"
	 */
	private transient BufferedImage dx;
	/**
	 * @uml.property  name="centro"
	 */
	private transient BufferedImage centro;
	/**
	 * @uml.property  name="altezza"
	 */
	private int altezza;
	/**
	 * @uml.property  name="lunghezza"
	 */
	private int lunghezza;

	/**
	 * Costruttore che carica le immagini per comporre lo sfondo con paintComponent, 
	 * rimuove i vordi e cambia il Layout in un GridLayout().
	 */
	public KToolBar() {
		super();
		BufferedImage toolBar = ImageLoader.getInstance().getToolBar();
		setFocusable(false);
		setFloatable(false);
		setBorder(BorderFactory.createEmptyBorder());
		setOpaque(false);
		setDoubleBuffered(true);
		setLayout(new GridLayout());
		
		UIManager.put("ToolTip.foreground", new ColorUIResource(KColors.getNero()));
	    UIManager.put("ToolTip.background", new ColorUIResource(KColors.getVerde()));
	    UIManager.put("ToolTip.border", BorderFactory.createEmptyBorder());	    
		
		if(!ImageLoader.getInstance().isErrore()) {
			sx = toolBar.getSubimage(0, 0, 1, toolBar.getHeight());
			centro = toolBar.getSubimage(1, 0, 1, toolBar.getHeight());
			dx = toolBar.getSubimage(2, 0, 2, toolBar.getHeight());
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		if(!ImageLoader.getInstance().isErrore()) {
			lunghezza = getWidth();
			altezza = getHeight();
			g.drawImage(sx, 0, 0, this);
			g.drawImage(ImageResizer.ridimensionaImmagine(centro, lunghezza-sx.getWidth()-dx.getWidth(), altezza), sx.getWidth(), 0, this);
			g.drawImage(dx, lunghezza - dx.getWidth(), 0, this);
		} else {
			super.paintComponent(g);
		}
	}
}