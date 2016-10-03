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

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;

public class KCheckBox extends JCheckBox{

	private static final long serialVersionUID = 7669204471521062925L;
	
	public KCheckBox(String text, boolean selected) {
		super(text,selected);
		setBorder(BorderFactory.createEmptyBorder());
		setOpaque(false);
		MouseManagerForKComponent gestoreMouse = new MouseManagerForKComponent(this);
		this.addMouseListener(gestoreMouse);
	}

	@Override
	protected void paintComponent(Graphics g) {
		
//		switch(gestoreMouse.getTipo()) {
//		case OVER:
//			pezziImg = pezziOver;
//			break;
//		case CLICCATO:
//			pezziImg = pezziCliccato;
//			break;
//		default:
//			pezziImg = pezziNormale;
//			break;
//		}
		
		if(isSelected()) {
			g.setColor(KColors.getVerde());
			g.fillRect(0, 0, getHeight(), getHeight());
		} else {
			g.setColor(KColors.getVerde());
			g.fillRect(0, 0, getHeight(), getHeight());
			g.setColor(KColors.getNero());
			g.fillRect(getHeight()/5, getHeight()/5, getHeight() - 2*getHeight()/5 + 1, getHeight() - 2*getHeight()/5 + 1);
		}
	}

}
