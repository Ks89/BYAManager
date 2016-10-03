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


import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JList;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

/**
 *	Classe che estende BasicComboBoxRenderer per cambiare la font e i colori.
 *	Uso questa classe perche' un JComboBox e' gestito come se fosse una tabella a 
 *	una colonna e piu' righe.
 */
public class KComboBoxRenderer extends BasicComboBoxRenderer {
	private static final long serialVersionUID = -8377951721619538521L;
	/**
	 * @uml.property  name="selezionato"
	 */
	private boolean selezionato;

	/**
	 * Costruttore che imposta la font personalizzata.
	 */
	public KComboBoxRenderer() {  
		setOpaque(false);
		setFont(KFont.getInstance().getFont());
	}  

	@Override
	public Component getListCellRendererComponent(@SuppressWarnings("rawtypes") JList list,
							Object value,int index,boolean isSelected,boolean cellHasFocus) {  
		if(value!=null) {
			setText(value.toString());
		}
		this.selezionato = isSelected;
		return this;  
	}

	@Override
	protected void paintChildren(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		if(selezionato) {
			g2d.setColor(KColors.getVerde());
			g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
			g2d.setFont(KFont.getInstance().getFont());
			g2d.setColor(KColors.getNero());
		} else {
			g2d.setColor(KColors.getNero());
			g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
			g2d.setFont(KFont.getInstance().getFont());
			g2d.setColor(KColors.getVerde());
		}
		g2d.drawString(getText(), 4, this.getHeight()/2 + 5);
	}
}
