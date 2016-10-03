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



import javax.swing.JLabel;

public class KLabel extends JLabel {

	private static final long serialVersionUID = 2245604890661420052L;

	public KLabel(String testo, int i) {
		super(testo, i);
		super.setFont(KFont.getInstance().getFont());
		super.setForeground(KColors.getVerde());
	}
	
	public KLabel(String testo) {
		super(testo);
		super.setFont(KFont.getInstance().getFont());
		super.setForeground(KColors.getVerde());
	}
	
}
