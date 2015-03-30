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
package gui.image;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.Icon;

public class BufferedImageToIcon implements Icon {
	/**
	 * @uml.property  name="image"
	 */
	private BufferedImage image;
	/**
	 * @uml.property  name="height"
	 */
	private int height;
	/**
	 * @uml.property  name="width"
	 */
	private int width;

	public BufferedImageToIcon(BufferedImage image) {
		this.image = image;
		this.height = image.getHeight();
		this.width = image.getWidth();
	}

	@Override
	public int getIconHeight() {
		return this.height;
	}

	@Override
	public int getIconWidth() {
		return this.width;
	}

	@Override
	public void paintIcon(Component arg0, Graphics g, int arg2, int arg3) {
		g.drawImage(this.image, 0, 0, null);
	}


}
