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
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.JButton;

/**
 *	Classe che crea un pulsante personalizzato ridimensionabile
 */
public class KButton extends JButton {
	private static final long serialVersionUID = -8477547745718384583L;
	private static final int OVER = 1;
	private static final int CLICCATO = 2;
	private transient BufferedImage[] pezziImg;
	private transient BufferedImage[] pezziNormale;
	private transient BufferedImage[] pezziOver;
	private transient BufferedImage[] pezziCliccato;
	private transient MouseManagerForKComponent gestoreMouse;
	private int lunghezza;
	private int altezza;
	private int lungRidim;
	private int lungRidimSx;
	private int lungRidimDx;
	private int altezRidim;
	private int altezRidimBasso;
	private int altezRidimAlto;

	/**
	 * Costruttore che imposta il testo e aggiunge il listener del mouse
	 * per far cambiare le immagini con cui e' fatto il pulsante.
	 * @param testo String che rappresenta il testo da inserire nel pulsante.
	 */
	public KButton(String testo) {
		super(testo);
		setFont(KFont.getInstance().getFont());
		setText(testo);
		setOpaque(false);
		setFocusable(false); //metterlo true e gestire bene anche interazione utenti disabili
		setBorderPainted(false);
		setFocusPainted(false);
		setContentAreaFilled(false);
		gestoreMouse = new MouseManagerForKComponent(this);
		this.addMouseListener(gestoreMouse);

		pezziNormale = this.spezzaImmagine(ImageLoader.getInstance().getPulsanteNormale());
		pezziOver = this.spezzaImmagine(ImageLoader.getInstance().getPulsanteOver());
		pezziCliccato = this.spezzaImmagine(ImageLoader.getInstance().getPulsanteCliccato());
	}

	/**
	 * Metodo per dividere l'immagine originale del pulsante in piu' parti e caricarle in 
	 * un mettore di BufferedImage.
	 * @param immagine BufferedImage che rappresenta l'immagine del pulsante originale.
	 * @return array di BufferedImage contenente tutti i pezzi di quella data in ingresso.
	 */
	private BufferedImage[] spezzaImmagine(BufferedImage immagine) {
		BufferedImage[] pezzi = new BufferedImage[15];
		pezzi[0] = immagine.getSubimage(0, 0, 8, 7); //angolo alto sx
		pezzi[1] = immagine.getSubimage(0, 9, 8, 1); //striscia orizz lato sx alto
		pezzi[2] = immagine.getSubimage(0, 10, 8, 9); //buco sx
		pezzi[12] = immagine.getSubimage(0, 20, 8, 1); //striscia orizz lato sx basso
		pezzi[3] = immagine.getSubimage(0, immagine.getHeight() - 7, 8, 7); //angolo basso sx

		pezzi[4] = immagine.getSubimage(immagine.getWidth() - 8, 0, 8, 7); //angolo alto dx
		pezzi[5] = immagine.getSubimage(immagine.getWidth() - 8, 9, 8, 1); //striscia orizz lato dx alto
		pezzi[6] = immagine.getSubimage(immagine.getWidth() - 8, 10, 8, 9); //buco dx
		pezzi[13] = immagine.getSubimage(immagine.getWidth() - 8, 20, 8, 1); //striscia orizz lato dx basso
		pezzi[7] = immagine.getSubimage(immagine.getWidth() - 8, immagine.getHeight() - 7, 8, 7); //angolo basso dx

		pezzi[8] = immagine.getSubimage(23, 6, 1, immagine.getHeight() - 6 - 6); //prendo striscia verticale

		pezzi[9] = immagine.getSubimage(56, 0, 11, 6); //prendo buco alto centrale
		pezzi[10] = immagine.getSubimage(56, immagine.getHeight() - 6, 11, 6); //prendo buco basso centrale
		pezzi[11] = immagine.getSubimage(23, 0, 2, 6); //prendo bordo superiore
		pezzi[14] = immagine.getSubimage(23, immagine.getHeight() - 6, 2, 6); //prendo bordo inferiore
		return pezzi;
	}


	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		if(!ImageLoader.getInstance().isErrore()) {
			lunghezza = getWidth();
			altezza = getHeight();
			
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			switch(gestoreMouse.getTipo()) {
			case OVER:
				pezziImg = pezziOver;
				break;
			case CLICCATO:
				pezziImg = pezziCliccato;
				break;
			default:
				pezziImg = pezziNormale;
				break;
			}

			//pitturo elementi fissi
			g2d.drawImage(pezziImg[0], 0, 0, this); //angolo alto sx
			g2d.drawImage(pezziImg[2], 0, getHeight()/2 - pezziImg[2].getHeight()/2, this); //buco sx
			g2d.drawImage(pezziImg[3], 0, getHeight() - pezziImg[3].getHeight(), this); //angolo basso sx

			g2d.drawImage(pezziImg[9], getWidth()/2 - pezziImg[9].getWidth()/2, 0, this); //buco alto centro
			g2d.drawImage(pezziImg[10], getWidth()/2 - pezziImg[10].getWidth()/2, getHeight() - pezziImg[3].getHeight() + 1, this); //buco basso centro

			g2d.drawImage(pezziImg[4], getWidth() - pezziImg[4].getWidth(), 0, this); //angolo alto dx
			g2d.drawImage(pezziImg[6], getWidth() - pezziImg[6].getWidth(), getHeight()/2 - pezziImg[6].getHeight()/2, this); //buco dx
			g2d.drawImage(pezziImg[7], getWidth() - pezziImg[7].getWidth(), getHeight() - pezziImg[7].getHeight(), this); //angolo basso dx


			//pitturo elementi da estendere, calcolandone prima le dimensioni
			lungRidim = lunghezza - pezziImg[0].getWidth(null) - pezziImg[4].getWidth(null);
			altezRidim = altezza - pezziImg[9].getHeight(null) - pezziImg[10].getHeight(null);
			g2d.drawImage(ImageResizer.ridimensionaImmagine(pezziImg[8], lungRidim, altezRidim), pezziImg[0].getWidth(), pezziImg[9].getHeight(null), this); //striscia verticale estesa


			altezRidimAlto = altezza/2 - pezziImg[0].getHeight(null) - (pezziImg[2].getHeight(null) / 2);
			if(altezza != altezRidimAlto + altezRidimAlto) {
				altezRidimBasso = altezRidimAlto + 1;
			} else {
				altezRidimBasso = altezRidimAlto;
			}
			g2d.drawImage(ImageResizer.ridimensionaImmagine(pezziImg[1], pezziImg[1].getWidth(), altezRidimAlto), 0, pezziImg[0].getHeight(), this); //striscia orizz lato sx estesa alta
			g2d.drawImage(ImageResizer.ridimensionaImmagine(pezziImg[12], pezziImg[12].getWidth(), altezRidimBasso), 0, getHeight()/2 + pezziImg[2].getHeight()/2, this); //striscia orizz lato sx estesa bassa
			g2d.drawImage(ImageResizer.ridimensionaImmagine(pezziImg[5], pezziImg[5].getWidth(), altezRidimAlto), lunghezza - pezziImg[5].getWidth(), pezziImg[4].getHeight(), this); //striscia orizz lato dx estesa alta
			g2d.drawImage(ImageResizer.ridimensionaImmagine(pezziImg[13], pezziImg[13].getWidth(), altezRidimBasso), lunghezza - pezziImg[13].getWidth(), getHeight()/2 + pezziImg[6].getHeight()/2, this); //striscia orizz lato dx estesa bassa

			//bordo superiore a sx e dx
			lungRidimSx = lunghezza/2 - pezziImg[0].getWidth() - pezziImg[9].getWidth()/2;
			if(lunghezza != lungRidimSx +lungRidimSx) {
				lungRidimDx = lungRidimSx + 1; 
			} else {
				lungRidimDx = lungRidimSx;
			}
			g2d.drawImage(ImageResizer.ridimensionaImmagine(pezziImg[11], lungRidimSx, pezziImg[11].getHeight()), pezziImg[0].getWidth(), 0, this);
			g2d.drawImage(ImageResizer.ridimensionaImmagine(pezziImg[11], lungRidimDx, pezziImg[11].getHeight()), lunghezza/2 + pezziImg[9].getWidth()/2, 0, this);
			g2d.drawImage(ImageResizer.ridimensionaImmagine(pezziImg[14], lungRidimSx, pezziImg[14].getHeight()), pezziImg[0].getWidth(), altezza - pezziImg[14].getHeight(), this);
			g2d.drawImage(ImageResizer.ridimensionaImmagine(pezziImg[14], lungRidimDx, pezziImg[14].getHeight()), lunghezza/2 + pezziImg[9].getWidth()/2, altezza - pezziImg[14].getHeight(), this);
		} 
		super.paintComponent(g2d);
	}
}
