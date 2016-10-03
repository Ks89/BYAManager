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

package it.stefanocappa.gui.table.renderer;

import it.stefanocappa.gui.image.BufferedImageToIcon;
import it.stefanocappa.gui.image.ImageLoader;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JTable;

public class RendererColonna4 extends RendererColonnaComune {
	private static final long serialVersionUID = -3957055289437000647L;
	private Icon complete;
	private Icon pause;
	private Icon validation;
	private Icon merging;
	private Icon downloading;
	private Icon error;
	
	public RendererColonna4() {
		complete = new BufferedImageToIcon(ImageLoader.getInstance().getTableComplete());
		pause = new BufferedImageToIcon(ImageLoader.getInstance().getTablePause());
		validation = new BufferedImageToIcon(ImageLoader.getInstance().getTableValidation());
		merging = new BufferedImageToIcon(ImageLoader.getInstance().getTableMerging());
		downloading = new BufferedImageToIcon(ImageLoader.getInstance().getTableDownloading());
		error = new BufferedImageToIcon(ImageLoader.getInstance().getTableError());
	}
	
	/**
	 * Ottiene l'immagine dato lo status.
	 * @param status int che rappresenta lo status del download.
	 * @return L'icon associata allo stato.
	 */
	public Icon getImmagineStatus(int status) {
		switch(status) {
		case 0:  return downloading;
		case 1:  return pause;
		case 2:	 return complete;
		case 4:  return error;
		case 5:  return validation;
		case 6:  return merging;
		default: return null;
		}
	}
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		super.impostaGrafica(1, null, this.getImmagineStatus((Integer)value), isSelected); //1 = imposta SOLO icona
		return this;
	} 
}
