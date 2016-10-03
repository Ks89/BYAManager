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

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 *	Classe che gestisce ol mouse.
 */
public class MouseManagerForKComponent implements MouseListener {
	private Component component;
	private static final int NORMALE = 0;
	private static final int OVER = 1;
	private static final int CLICCATO = 2;
	private int tipo = 0;
	
	/**
	 * Costruttore che imposta il component per permettere di richiamare il
	 * metodo repaint di component ad ogni cambiamento di stato del mouse.
	 * @param component
	 */
	public MouseManagerForKComponent(Component component) {
		this.component = component;
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		//roll-over
		tipo = OVER;
		component.repaint();	
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		tipo = NORMALE;
		component.repaint();	
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		//quando premo il pulsante
		tipo = CLICCATO;
		component.repaint();	
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		tipo = NORMALE;
		component.repaint();	
	}
	
	/**
	 * @return  int che rappresenta il tipo di evento che e' stato  richiamato.
	 * @uml.property  name="tipo"
	 */
	public int getTipo() {
		return this.tipo;
	}

}
