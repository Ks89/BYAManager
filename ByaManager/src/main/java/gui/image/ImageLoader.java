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

/**
 *	Classe che carica le immagini e le imposta in modo da ottenerle in modo statico.
 */
public final class ImageLoader {
	private static ImageLoader instance = new ImageLoader();
	/**
	 * @uml.property  name="errore"
	 */
	private boolean errore;
	/**
	 * @uml.property  name="sfondo"
	 */
	private BufferedImage sfondo;
	/**
	 * @uml.property  name="toolBar"
	 */
	private BufferedImage toolBar;
	/**
	 * @uml.property  name="pulsanteNormale"
	 */
	private BufferedImage pulsanteNormale;
	/**
	 * @uml.property  name="pulsanteOver"
	 */
	private BufferedImage pulsanteOver;
	/**
	 * @uml.property  name="pulsanteCliccato"
	 */
	private BufferedImage pulsanteCliccato;
	/**
	 * @uml.property  name="pulsanteDonazione"
	 */
	private BufferedImage pulsanteDonazione;
	/**
	 * @uml.property  name="iconaBya"
	 */
	private BufferedImage iconaBya;
	/**
	 * @uml.property  name="frecciaComboBox"
	 */
	private BufferedImage frecciaComboBox;
	/**
	 * @uml.property  name="frecciaComboBoxSu"
	 */
	private BufferedImage frecciaComboBoxSu;
	/**
	 * @uml.property  name="frecciaComboBoxGiu"
	 */
	private BufferedImage frecciaComboBoxGiu;
	/**
	 * @uml.property  name="logoGenericoVerde"
	 */
	private BufferedImage logoGenericoVerde;
	/**
	 * @uml.property  name="logoGenericoNero"
	 */
	private BufferedImage logoGenericoNero;
	/**
	 * @uml.property  name="refreshDb"
	 */
	private BufferedImage refreshDb;
	/**
	 * @uml.property  name="refreshByam"
	 */
	private BufferedImage refreshByam;
	/**
	 * @uml.property  name="preferenze"
	 */
	private BufferedImage preferenze;
	/**
	 * @uml.property  name="pause"
	 */
	private BufferedImage pause;
	/**
	 * @uml.property  name="play"
	 */
	private BufferedImage play;
	/**
	 * @uml.property  name="remove"
	 */
	private BufferedImage remove;
	/**
	 * @uml.property  name="pausaAll"
	 */
	private BufferedImage pausaAll;
	/**
	 * @uml.property  name="playAll"
	 */
	private BufferedImage playAll;
	/**
	 * @uml.property  name="removeAll"
	 */
	private BufferedImage removeAll;
	/**
	 * @uml.property  name="refreshDbD"
	 */
	private BufferedImage refreshDbD;
	/**
	 * @uml.property  name="refreshByamD"
	 */
	private BufferedImage refreshByamD;
	/**
	 * @uml.property  name="preferenzeD"
	 */
	private BufferedImage preferenzeD;
	/**
	 * @uml.property  name="pauseD"
	 */
	private BufferedImage pauseD;
	/**
	 * @uml.property  name="playD"
	 */
	private BufferedImage playD;
	/**
	 * @uml.property  name="removeD"
	 */
	private BufferedImage removeD;
	/**
	 * @uml.property  name="pausaAllD"
	 */
	private BufferedImage pausaAllD;
	/**
	 * @uml.property  name="playAllD"
	 */
	private BufferedImage playAllD;
	/**
	 * @uml.property  name="removeAllD"
	 */
	private BufferedImage removeAllD;
	/**
	 * @uml.property  name="logoItunes9"
	 */
	private BufferedImage logoItunes9;
	/**
	 * @uml.property  name="logoItunes10"
	 */
	private BufferedImage logoItunes10;
	/**
	 * @uml.property  name="logoFirmware"
	 */
	private BufferedImage logoFirmware;
	/**
	 * @uml.property  name="tableComplete"
	 */
	private BufferedImage tableComplete;
	/**
	 * @uml.property  name="tablePause"
	 */
	private BufferedImage tablePause;
	/**
	 * @uml.property  name="tableValidation"
	 */
	private BufferedImage tableValidation;
	/**
	 * @uml.property  name="tableMerging"
	 */
	private BufferedImage tableMerging;
	/**
	 * @uml.property  name="tableDownloading"
	 */
	private BufferedImage tableDownloading;
	/**
	 * @uml.property  name="tableError"
	 */
	private BufferedImage tableError;
		
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

	/**
	 * @return  BufferedImage contenente lo sfondo.
	 * @uml.property  name="sfondo"
	 */
	public BufferedImage getSfondo() {
		return sfondo;
	}
	
	/**
	 * @return  BufferedImage contenente la toolBar.
	 * @uml.property  name="toolBar"
	 */
	public BufferedImage getToolBar() {
		return toolBar;
	}
	
	/**
	 * @return  BufferedImage contenente il pulsante normale.
	 * @uml.property  name="pulsanteNormale"
	 */
	public BufferedImage getPulsanteNormale() {
		return pulsanteNormale;
	}
	
	/**
	 * @return  BufferedImage contenente il pulsante cliccato.
	 * @uml.property  name="pulsanteCliccato"
	 */
	public BufferedImage getPulsanteCliccato() {
		return pulsanteCliccato;
	}
	
	/**
	 * @return  BufferedImage contenente il pulsante over.
	 * @uml.property  name="pulsanteOver"
	 */
	public BufferedImage getPulsanteOver() {
		return pulsanteOver;
	}

	/**
	 * @return  BufferedImage contenente il pulsante donazione.
	 * @uml.property  name="pulsanteDonazione"
	 */
	public BufferedImage getPulsanteDonazione() {
		return pulsanteDonazione;
	}
	
	/**
	 * @return  boolean che rappresenta l'errore, se presente.
	 * @uml.property  name="errore"
	 */
	public boolean isErrore() {
		return errore;
	}
	
	/**
	 * @return  BufferedImage contenente l'icona di Bya.
	 * @uml.property  name="iconaBya"
	 */
	public BufferedImage getIconaBya() {
		return iconaBya;
	}

	/**
	 * @return  BufferedImage contenente il logo Apple (dispositivo generico in colonna 0 tabella) verde.
	 * @uml.property  name="logoGenericoVerde"
	 */
	public BufferedImage getLogoGenericoVerde() {
		return logoGenericoVerde;
	}

	/**
	 * @return  BufferedImage contenente il logo Apple (dispositivo generico in colonna 0 tabella) nero.
	 * @uml.property  name="logoGenericoNero"
	 */
	public BufferedImage getLogoGenericoNero() {
		return logoGenericoNero;
	}

	/**
	 * @return  BufferedImage contenente il pulsante refreshDb.
	 * @uml.property  name="refreshDb"
	 */
	public BufferedImage getRefreshDb() {
		return refreshDb;
	}

	/**
	 * @return  BufferedImage contenente il pulsante refreshBya.
	 * @uml.property  name="refreshByam"
	 */
	public BufferedImage getRefreshByam() {
		return refreshByam;
	}

	/**
	 * @return  BufferedImage contenente il pulsante preferenze.
	 * @uml.property  name="preferenze"
	 */
	public BufferedImage getPreferenze() {
		return preferenze;
	}

	/**
	 * @return  BufferedImage contenente il pulsante pause.
	 * @uml.property  name="pause"
	 */
	public BufferedImage getPause() {
		return pause;
	}

	/**
	 * @return  BufferedImage contenente il pulsante play.
	 * @uml.property  name="play"
	 */
	public BufferedImage getPlay() {
		return play;
	}

	/**
	 * @return BufferedImage contenente il pulsante delete.
	 */
	public BufferedImage getDelete() {
		return remove;
	}

	/**
	 * @return  BufferedImage contenente il pulsante pauseAll.
	 * @uml.property  name="pausaAll"
	 */
	public BufferedImage getPausaAll() {
		return pausaAll;
	}

	/**
	 * @return  BufferedImage contenente il pulsante playAll.
	 * @uml.property  name="playAll"
	 */
	public BufferedImage getPlayAll() {
		return playAll;
	}

	/**
	 * @return BufferedImage contenente il pulsante deleteAll.
	 */
	public BufferedImage getDeleteAll() {
		return removeAll;
	}

	/**
	 * @return  BufferedImage contenente il pulsante refreshDb disattivato.
	 * @uml.property  name="refreshDbD"
	 */
	public BufferedImage getRefreshDbD() {
		return refreshDbD;
	}

	/**
	 * @return  BufferedImage contenente il pulsante refreshBya disattivato.
	 * @uml.property  name="refreshByamD"
	 */
	public BufferedImage getRefreshByamD() {
		return refreshByamD;
	}

	/**
	 * @return  BufferedImage contenente il pulsante preferenze disattivato.
	 * @uml.property  name="preferenzeD"
	 */
	public BufferedImage getPreferenzeD() {
		return preferenzeD;
	}

	/**
	 * @return  BufferedImage contenente il pulsante pause disattivato.
	 * @uml.property  name="pauseD"
	 */
	public BufferedImage getPauseD() {
		return pauseD;
	}

	/**
	 * @return  BufferedImage contenente il pulsante play disattivato.
	 * @uml.property  name="playD"
	 */
	public BufferedImage getPlayD() {
		return playD;
	}

	/**
	 * @return BufferedImage contenente il pulsante delete disattivato.
	 */
	public BufferedImage getDeleteD() {
		return removeD;
	}

	/**
	 * @return  BufferedImage contenente il pulsante pauseAll disattivato.
	 * @uml.property  name="pausaAllD"
	 */
	public BufferedImage getPausaAllD() {
		return pausaAllD;
	}

	/**
	 * @return  BufferedImage contenente il pulsante playAll disattivato.
	 * @uml.property  name="playAllD"
	 */
	public BufferedImage getPlayAllD() {
		return playAllD;
	}

	/**
	 * @return BufferedImage contenente il pulsante deleteAll disattivato.
	 */
	public BufferedImage getDeleteAllD() {
		return removeAllD;
	}

	/**
	 * @return  BufferedImage contenente frecciaComboBox.
	 * @uml.property  name="frecciaComboBox"
	 */
	public BufferedImage getFrecciaComboBox() {
		return frecciaComboBox;
	}
	/**
	 * @return  BufferedImage contenente frecciaComboBox su.
	 * @uml.property  name="frecciaComboBoxSu"
	 */
	public BufferedImage getFrecciaComboBoxSu() {
		return frecciaComboBoxSu;
	}

	/**
	 * @return  BufferedImage contenente frecciaComboBox giu.
	 * @uml.property  name="frecciaComboBoxGiu"
	 */
	public BufferedImage getFrecciaComboBoxGiu() {
		return frecciaComboBoxGiu;
	}

	/**
	 * @return
	 * @uml.property  name="logoItunes9"
	 */
	public BufferedImage getLogoItunes9() {
		return logoItunes9;
	}

	/**
	 * @return
	 * @uml.property  name="logoItunes10"
	 */
	public BufferedImage getLogoItunes10() {
		return logoItunes10;
	}

	/**
	 * @return
	 * @uml.property  name="logoFirmware"
	 */
	public BufferedImage getLogoFirmware() {
		return logoFirmware;
	}

	/**
	 * @return
	 * @uml.property  name="tableComplete"
	 */
	public BufferedImage getTableComplete() {
		return tableComplete;
	}

	/**
	 * @return
	 * @uml.property  name="tablePause"
	 */
	public BufferedImage getTablePause() {
		return tablePause;
	}

	/**
	 * @return
	 * @uml.property  name="tableValidation"
	 */
	public BufferedImage getTableValidation() {
		return tableValidation;
	}

	/**
	 * @return
	 * @uml.property  name="tableMerging"
	 */
	public BufferedImage getTableMerging() {
		return tableMerging;
	}

	/**
	 * @return
	 * @uml.property  name="tableDownloading"
	 */
	public BufferedImage getTableDownloading() {
		return tableDownloading;
	}

	/**
	 * @return
	 * @uml.property  name="tableError"
	 */
	public BufferedImage getTableError() {
		return tableError;
	}
}