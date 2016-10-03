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



import java.awt.Graphics;

import javax.swing.JSeparator;

public class KSeparator extends JSeparator {
	private static final long serialVersionUID = -1075463452912601109L;

	public KSeparator() {
		setOpaque(true);
	}

	@Override
	protected void paintComponent(Graphics g) {
		g.setColor(KColors.getNero());
		g.fillRect(0,0,getWidth(),getHeight());

		g.setColor(KColors.getVerde());

		//bordi
		g.drawLine(0, 0, 0, getHeight());
		g.drawLine(getWidth()-1, 0, getWidth()-1, getHeight());

//		g.fillRect(0, (getHeight()/2)-1, getWidth(), 1);
		g.fillRect(0, getHeight()/2, getWidth(), 1);
	}
}
