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


package it.stefanocappa.gui.image;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 *	Classe che si occupa del ridimensionamento di immagini.
 */
public final class ImageResizer {
	private static AffineTransform affineTransform;
	
	/**
	 * Costruttore nascosto perche' inutile (detto da Sonar XD).
	 */
	private ImageResizer() {
	}
	
	/**
	 * Metodo statico che serve per ottenere un'immagine ridimensionata in base ai parametri forniti in ingresso.
	 * @param originale BufferedImage che rappresenta l'immagine da ridimensionare.
	 * @param lunghezzaRidim int che rappresenta la lunghezza in pixer desiderata.
	 * @param altezzaRidim int che rappresenta l'altezza in pixel desiderata.
	 * @return BufferedImahe che rappresenta l'immagine ridimensionata in base ai 3 parametri forniti. In particolare sara' 
	 * l'immagine "originale" con lunghezza "lunghezzaRidim" e altezza "altezzaRidim".
	 */
	public static BufferedImage ridimensionaImmagine(BufferedImage originale, int lunghezzaRidim, int altezzaRidim) {
		BufferedImage ridimensionata = new BufferedImage(lunghezzaRidim, altezzaRidim, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = ridimensionata.createGraphics();
		affineTransform = AffineTransform.getScaleInstance((double)(lunghezzaRidim)/
				originale.getWidth(),(double)(altezzaRidim)/originale.getHeight());
		g2d.drawRenderedImage(originale,affineTransform);
		g2d.dispose();
		return ridimensionata;
	}
	
}
