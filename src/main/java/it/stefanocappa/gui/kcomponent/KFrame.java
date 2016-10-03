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

import javax.swing.JFrame;

import it.stefanocappa.update.Version;

/**
 *	Classe che crea un JFrame personalizzato con icona, dimensione preferita, titolo,
 *	posizione e soprattutto lo sfondo, richiamando il PanelPersonalizzato.
 */
public class KFrame extends JFrame{
	private static final long serialVersionUID = 6729366260424934194L;
	private static final String NOME = "BYAManager - " +  Version.getGuiFrameVersion() + " - by Stefano Cappa";

	public KFrame() {
		setTitle(NOME);
		setIconImage(ImageLoader.getInstance().getIconaBya());
		getLayeredPane().setOpaque(false);
		getRootPane().setOpaque(false);
		this.setContentPane(new KPanel());
	}
}