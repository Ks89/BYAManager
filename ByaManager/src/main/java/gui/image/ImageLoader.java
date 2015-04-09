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

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import lombok.Getter;

/**
 *	Classe che carica le immagini e le imposta in modo da ottenerle in modo statico.
 */
public final class ImageLoader {
	private static ImageLoader instance = new ImageLoader();
	@Getter private boolean errore;
	@Getter private BufferedImage sfondo;
	@Getter private BufferedImage toolBar;
	@Getter private BufferedImage pulsanteNormale;
	@Getter private BufferedImage pulsanteOver;
	@Getter private BufferedImage pulsanteCliccato;
	@Getter private BufferedImage pulsanteDonazione;
	@Getter private BufferedImage iconaBya;
	@Getter private BufferedImage frecciaComboBox;
	@Getter private BufferedImage frecciaComboBoxSu;
	@Getter private BufferedImage frecciaComboBoxGiu;
	@Getter private BufferedImage logoGenericoVerde;
	@Getter private BufferedImage logoGenericoNero;
	@Getter private BufferedImage refreshDb;
	@Getter private BufferedImage refreshByam;
	@Getter private BufferedImage preferenze;
	@Getter private BufferedImage pause;
	@Getter private BufferedImage play;
	@Getter private BufferedImage remove;
	@Getter private BufferedImage pausaAll;
	@Getter private BufferedImage playAll;
	@Getter private BufferedImage removeAll;
	@Getter private BufferedImage refreshDbD;
	@Getter private BufferedImage refreshByamD;
	@Getter private BufferedImage preferenzeD;
	@Getter private BufferedImage pauseD;
	@Getter private BufferedImage playD;
	@Getter private BufferedImage removeD;
	@Getter private BufferedImage pausaAllD;
	@Getter private BufferedImage playAllD;
	@Getter private BufferedImage removeAllD;
	@Getter private BufferedImage logoItunes9;
	@Getter private BufferedImage logoItunes10;
	@Getter private BufferedImage logoFirmware;
	@Getter private BufferedImage tableComplete;
	@Getter private BufferedImage tablePause;
	@Getter private BufferedImage tableValidation;
	@Getter private BufferedImage tableMerging;
	@Getter private BufferedImage tableDownloading;
	@Getter private BufferedImage tableError;
		
	/**
	 * Costruttore privato che legge le immagini e in caso contrario
	 * segnala l'errore con una variabile boolean, ottenibile staticamente con
	 * isErrore().
	 */
	private ImageLoader() {
		errore = false;
		try {
			sfondo = ImageIO.read(this.getClass().getResource("/sfondoFrame.png"));
			toolBar = ImageIO.read(this.getClass().getResource("/toolBar.png"));
			pulsanteNormale = ImageIO.read(this.getClass().getResource("/buttonNormale.png"));
			pulsanteOver = ImageIO.read(this.getClass().getResource("/buttonOver.png"));
			pulsanteCliccato = ImageIO.read(this.getClass().getResource("/buttonPremuto.png"));
			pulsanteDonazione = ImageIO.read(this.getClass().getResource("/donazione.png"));
			iconaBya = ImageIO.read(this.getClass().getResource("/byamanager_icona.png"));
			logoGenericoVerde = ImageIO.read(this.getClass().getResource("/logo_generico.png")); 
			logoGenericoNero = ImageIO.read(this.getClass().getResource("/logo_generico_S.png")); 
			refreshDb = ImageIO.read(this.getClass().getResource("/refresh_db.png"));
			refreshByam = ImageIO.read(this.getClass().getResource("/refresh_byam.png"));
			preferenze = ImageIO.read(this.getClass().getResource("/preferenze.png"));
			pause = ImageIO.read(this.getClass().getResource("/pausa.png"));
			play = ImageIO.read(this.getClass().getResource("/play.png"));
			remove = ImageIO.read(this.getClass().getResource("/delete.png"));
			pausaAll = ImageIO.read(this.getClass().getResource("/pausa_all.png"));
			playAll = ImageIO.read(this.getClass().getResource("/play_all.png"));
			removeAll = ImageIO.read(this.getClass().getResource("/delete_all.png"));
			refreshDbD = ImageIO.read(this.getClass().getResource("/refresh_db_D.png"));
			refreshByamD = ImageIO.read(this.getClass().getResource("/refresh_byam_D.png"));
			preferenzeD = ImageIO.read(this.getClass().getResource("/preferenze_D.png"));
			pauseD = ImageIO.read(this.getClass().getResource("/pausa_D.png"));
			playD = ImageIO.read(this.getClass().getResource("/play_D.png"));
			removeD = ImageIO.read(this.getClass().getResource("/delete_D.png"));
			pausaAllD = ImageIO.read(this.getClass().getResource("/pausa_all_D.png"));
			playAllD = ImageIO.read(this.getClass().getResource("/play_all_D.png"));
			removeAllD = ImageIO.read(this.getClass().getResource("/delete_all_D.png"));
			frecciaComboBox = ImageIO.read(this.getClass().getResource("/frecciaComboBox.png"));
			frecciaComboBoxSu = ImageIO.read(this.getClass().getResource("/frecciaComboBox-UP.png"));
			frecciaComboBoxGiu = ImageIO.read(this.getClass().getResource("/frecciaComboBox-DOWN.png"));
			logoItunes9 = ImageIO.read(this.getClass().getResource("/itunes9.png"));
			logoItunes10 = ImageIO.read(this.getClass().getResource("/itunes10.png"));
			logoFirmware = ImageIO.read(this.getClass().getResource("/tabella_ipsw.png"));
			tableComplete = ImageIO.read(this.getClass().getResource("/tabella_complete.png"));
			tablePause = ImageIO.read(this.getClass().getResource("/tabella_pause.png"));
			tableValidation = ImageIO.read(this.getClass().getResource("/tabella_validation.png"));
			tableMerging = ImageIO.read(this.getClass().getResource("/tabella_merging.png"));
			tableDownloading = ImageIO.read(this.getClass().getResource("/tabella_download.png"));
			tableError = ImageIO.read(this.getClass().getResource("/tabella_error.png"));
		} catch (IOException e) {
			errore = true;
		}
	}
	
	/**
	 * Metodo che permette di ottenere l'istanza della classe.
	 * @return istanza della classe Immagini.
	 */
	public static ImageLoader getInstance() {
		return instance;
	}
}