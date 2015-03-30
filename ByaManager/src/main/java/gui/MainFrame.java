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

package gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JViewport;
import javax.swing.SwingConstants;

import localization.Translator;

import gui.kcomponent.KColors;
import gui.kcomponent.KButton;
import gui.kcomponent.KComboBox;
import gui.kcomponent.KFont;
import gui.kcomponent.KFrame;
import gui.kcomponent.KLabel;
import gui.kcomponent.KPanel;
import gui.kcomponent.KTabbedPane;
import gui.kcomponent.KViewPort;
import gui.state.StateButton;
import gui.state.StateLabel;
import gui.state.StateMenuItem;
import gui.table.TableGui;

public final class MainFrame extends KFrame implements Runnable {
	private static final long serialVersionUID = 1865707130950307863L;

	private static MainFrame instance = new MainFrame();
	private static JButton addLista;
	private static JLabel velocitaGlobale;
	private static JComboBox<String> listaDispositivi;

	private static JComboBox<String> listaFirmware;

	private static JComboBox<String> osList;

	private static JComboBox<String> itunesList;
	private static JTabbedPane tabbedPane;

	public MainFrame() {

	}

	@Override
	public void run() {
		//Attenzione: la classe Colori viene creata nel DownloadManager semplicemente chiamando l'istanza e senza usare 
		//l'oggetto, perche' serve solo per crearla. Una volta fatto tutte le altre classi la potranno usare.
		KColors.getInstance(); //creo la classe per poi usarla dopo

		velocitaGlobale = new KLabel("", SwingConstants.RIGHT);

		addLista = new KButton(Translator.getText("aggiungiButton"));

		listaDispositivi = new KComboBox();
		listaFirmware = new KComboBox();

		osList = new KComboBox();
		itunesList = new KComboBox();

		osList.addItem("Windows 32bit");
		osList.addItem("Windows 64bit");
		osList.addItem("Mac OSX");

		tabbedPane = new KTabbedPane();
		tabbedPane.addTab("iOS", null, getAddFirmware(),"Firmware download");
		tabbedPane.addTab("iTunes", null, getAddiTunes(),"iTunes download");

		//imposto la barra dei menu in alto
		StateMenuItem.createMenu();
		setJMenuBar(StateMenuItem.getMenuBar()); //qui aggiungo la barra al frame del programma
	}

	public void createMainPanel() {
		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints lim = new GridBagConstraints();
		setLayout(layout);

		JViewport viewPort = new KViewPort();
		viewPort.setView(TableGui.getInstance());
		JScrollPane scroll = new JScrollPane();
		scroll.setViewport(viewPort);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scroll.setBorder(BorderFactory.createEmptyBorder());

		insGridBagLayout(layout, lim, 0, 0, 2, 1, 0, 0, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, StateButton.creaToolBar(), null);
		insGridBagLayout(layout, lim, 0, 1, 2, 2, 0, 0, 0, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, tabbedPane, null);
		insGridBagLayout(layout, lim, 0, 3, 2, 1, addLista.getSize().width, 0, 1, 0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, addLista, null);
		insGridBagLayout(layout, lim, 0, 4, 2, 1, 0, 0, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new JLabel("       "), null);
		insGridBagLayout(layout, lim, 0, 5, 1, 1, 0, 0, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, StateLabel.getAggProgrammaLabel(), null);
		insGridBagLayout(layout, lim, 0, 6, 1, 1, 0, 0, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, StateLabel.getAggDatabaseLabel(), null);
		insGridBagLayout(layout, lim, 1, 6, 1, 1, 0, 0, 0, 0, GridBagConstraints.EAST, GridBagConstraints.NONE, velocitaGlobale, null);

		insGridBagLayout(layout, lim, 0, 7, 2, 1, 0, 0, 1, 0.5, GridBagConstraints.CENTER, GridBagConstraints.BOTH, scroll, null);
	}

	private void insGridBagLayout(GridBagLayout layoutPanel, GridBagConstraints lim, int x, int y, int lx, int ly, int ipadx, int ipady, double wx, double wy, int anchor, int fill, JComponent elemento, JPanel panel) {
		lim.insets = new Insets(4,3,4,3);
		lim.gridx = x;
		lim.gridy = y;
		lim.gridwidth = lx;
		lim.gridheight = ly;
		lim.ipadx = ipadx;
		lim.ipady = ipady;
		lim.weightx = wx;
		lim.weighty = wy;
		lim.anchor = anchor;
		lim.fill = fill;
		layoutPanel.setConstraints(elemento, lim);
		if(panel!=null) {
			panel.add(elemento);
		} else {
			add(elemento);
		}
	}

	private JPanel getAddFirmware() {
		JPanel panel = new KPanel();
		GridBagLayout layoutPanel = new GridBagLayout();
		GridBagConstraints limPanel = new GridBagConstraints();

		limPanel.insets = new Insets(4, 3, 4, 3);
		panel.setLayout(layoutPanel);

		JLabel scegliDisp = new KLabel(" " + Translator.getText("scegliDispositivo"));
		JLabel scegliFirmware = new KLabel(" " + Translator.getText("scegliFirmware"));

		insGridBagLayout(layoutPanel, limPanel, 0, 0, 1, 1, 0, 0, 0, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, scegliDisp, panel);
		insGridBagLayout(layoutPanel, limPanel, 1, 0, 1, 1, 0, 0, 1, 0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, listaDispositivi, panel);
		insGridBagLayout(layoutPanel, limPanel, 0, 1, 1, 1, 0, 0, 0, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, scegliFirmware, panel);
		insGridBagLayout(layoutPanel, limPanel, 1, 1, 1, 1, 17, 0, 0, 0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, listaFirmware, panel);
		return panel;
	}

	private JPanel getAddiTunes() {
		JPanel panel = new KPanel();
		GridBagLayout layoutPanel = new GridBagLayout();
		GridBagConstraints limPanel = new GridBagConstraints();
		limPanel.insets = new Insets(4, 3, 4, 3);
		panel.setLayout(layoutPanel);

		JLabel scegliOS = new KLabel(" " + Translator.getText("scegliOS"));
		JLabel scegliVersione = new KLabel(" " + Translator.getText("scegliVersione"));

		insGridBagLayout(layoutPanel, limPanel, 0, 0, 1, 1, 0, 0, 0, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, scegliOS, panel);
		insGridBagLayout(layoutPanel, limPanel, 1, 0, 1, 1, 0, 0, 1, 0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, osList, panel);
		insGridBagLayout(layoutPanel, limPanel, 0, 1, 1, 1, 0, 0, 0, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, scegliVersione, panel);
		insGridBagLayout(layoutPanel, limPanel, 1, 1, 1, 1, 17, 0, 0, 0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, itunesList, panel);
		return panel;
	}

	public void setTextLabelVelocitaGlobale(String velocitaGlobaleText) {
		velocitaGlobale.setFont(KFont.getInstance().getFont());
		velocitaGlobale.setText(velocitaGlobaleText);
	}

	/**
	 * Metodo che permette di ottenere l'istanza della classe.
	 * @return istanza della classe.
	 */
	public static MainFrame getInstance() {
		return instance;
	}

	public JButton getAddLista() {
		return addLista;
	}

	public JComboBox<String> getListaDispositivi() {
		return listaDispositivi;
	}

	public JComboBox<String> getListaFirmware() {
		return listaFirmware;
	}

	public JComboBox<String> getOsList() {
		return osList;
	}

	public JComboBox<String> getItunesList() {
		return itunesList;
	}
	
	public int getTabbedPaneIndex() {
		return tabbedPane.getSelectedIndex();
	}

	public void showMainFrame() {
		// metto la finestra a centro schermo
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		setPreferredSize(new Dimension(FrameSize.getWidthframe(),FrameSize.getHeightframe()));
		setLocation((d.width/2) - FrameSize.getWidthframe()/2,(d.height/2) - FrameSize.getHeightframe()/2);
		pack();
		setVisible(true);
	}
}
