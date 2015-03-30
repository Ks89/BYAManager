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

import gui.kcomponent.KColors;
import gui.kcomponent.KButton;
import gui.kcomponent.KCheckBox;
import gui.kcomponent.KComboBox;
import gui.kcomponent.KComboBoxRenderer;
import gui.kcomponent.KFrame;
import gui.kcomponent.KLabel;
import gui.kcomponent.KPanel;
import gui.kcomponent.KTabbedPane;
import gui.kcomponent.KTextField;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;


import localization.Translator;
import model.User;

public class SettingsMainFrame extends JFrame implements Runnable{
	private static final long serialVersionUID = -2926809765756452854L;
	private JButton salvaPreferenze;
	private JButton scegliCartella;
	private JButton loginTwitterButton;
	private JButton loginFacebookButton;
	private JLabel twitterLogged;
	private JLabel facebookLogged;
	private JPanel panelTotale;
	private JPanel panelBase;
	private JPanel panelAdvanced;
	private JPanel panelNotification;
	private JPanel panelProxy /*,panelSocial*/;
	private JTextField percorsoTextField;
	private JComboBox<String> tipoDim;
	private JComboBox<String> bufferComboBox;
	private JComboBox<String> languageComboBox;
	private JCheckBox systemTrayNotificationState;
	private JCheckBox autoUpdateState;
	private JTextField proxyServer;
	private JTextField proxyPort;
	private JCheckBox activateProxy;


	public SettingsMainFrame() {
		percorsoTextField = new KTextField(User.getInstance().getDownloadPath().toString());

		tipoDim = new KComboBox();
		tipoDim.setRenderer(new KComboBoxRenderer());
				
		bufferComboBox = new KComboBox();
		bufferComboBox.setRenderer(new KComboBoxRenderer());
		bufferComboBox.addItem("2048"); //predefinito perche' piu' comodo
		bufferComboBox.addItem("4096");
		bufferComboBox.addItem("8192");
		bufferComboBox.addItem("16384");
		
		languageComboBox = new KComboBox();
		languageComboBox.setRenderer(new KComboBoxRenderer());
		languageComboBox.addItem("English (EN)");
		languageComboBox.addItem("Italian (IT)");
		languageComboBox.addItem("Czech (CZ)");
		
		systemTrayNotificationState = new KCheckBox("", true);
		systemTrayNotificationState.setBackground(KColors.getNero());
		autoUpdateState = new KCheckBox("", false);
		autoUpdateState.setEnabled(false); //ancora non implementata la funzionalita'
		autoUpdateState.setBackground(KColors.getNero());
		
		twitterLogged = new KLabel("NOT LOGGED");
		facebookLogged = new KLabel("NOT LOGGED");
		
		activateProxy = new KCheckBox("", false);
		proxyServer = new KTextField("999.999.999.999");
		proxyPort = new KTextField("8080");
		proxyServer.setEnabled(false);
		proxyPort.setEditable(false);
		proxyPort.setEnabled(false);
	}
	
	public void applytTranslations() {
		scegliCartella = new KButton(Translator.getPrefMessage("scegliCartella"));
		tipoDim.addItem(Translator.getPrefMessage("byteItem"));
		tipoDim.addItem(Translator.getPrefMessage("multipliByteItem")); //predefinito perche' piu' comodo
		tipoDim.addItem(Translator.getPrefMessage("multipliBinaryByteItem")); 
		salvaPreferenze = new KButton(Translator.getPrefMessage("salvaPreferenze"));
		loginTwitterButton = new KButton(Translator.getPrefMessage("login"));
		loginFacebookButton = new KButton(Translator.getPrefMessage("login"));
	}
	
	private void insGridBagLayout(GridBagLayout layout, GridBagConstraints lim, int x, int y, int lx, int ly, double wx, double wy, int anchor, int fill, JComponent elemento, JPanel panel) {
		lim.insets = new Insets(5,5,5,5);
		lim.gridx = x;
		lim.gridy = y;
		lim.gridwidth = lx;
		lim.gridheight = ly;
		lim.weightx = wx;
		lim.weighty = wy;
		lim.anchor = anchor;
		lim.fill = fill;
		layout.setConstraints(elemento, lim);
		panel.add(elemento);
	}

	private void riempiPanelTotale() {
		GridBagConstraints lim = new GridBagConstraints();
		GridBagLayout layout = new GridBagLayout();
		
		panelTotale = new KPanel();
		panelTotale.setLayout(layout);
		
		insGridBagLayout(layout,lim, 0, 0, 2, 1, 0, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, getTabbedPane(), panelTotale);
		insGridBagLayout(layout,lim, 0, 1, 1, 1, 0.5, 0.5, GridBagConstraints.CENTER, GridBagConstraints.NONE, salvaPreferenze, panelTotale);
	
	}

	private JTabbedPane getTabbedPane() {
		JTabbedPane tabPane = new KTabbedPane();
		tabPane.addTab(Translator.getPrefMessage("tabStandard"), null, getPanelStandard(),Translator.getPrefMessage("toolTipTabStandard"));
		tabPane.addTab(Translator.getPrefMessage("tabAdvanced"), null, getPanelAdvanced(), Translator.getPrefMessage("toolTipTabAdvanced"));
		tabPane.addTab(Translator.getPrefMessage("tabNotification"), null, getPanelNotification(),Translator.getPrefMessage("toolTipTabNotification"));
//		tabPane.addTab(Traduttore.getPrefMessage("tabSocial"), null, getPanelSocial(),Traduttore.getPrefMessage("toolTipTabSocial"));
		tabPane.addTab(Translator.getPrefMessage("tabProxy"), null, getPanelProxy(),Translator.getPrefMessage("toolTipTabProxy"));
		return tabPane;
	}

	private JPanel getPanelStandard() {
		GridBagConstraints lim = new GridBagConstraints();
		GridBagLayout layout = new GridBagLayout();
		panelBase = new KPanel();
		panelBase.setLayout(layout);
		JLabel tipoDimensioneLabel = new KLabel(Translator.getPrefMessage("tipoDimensione"));
		
		layout.setConstraints(tipoDimensioneLabel, lim);
		
		insGridBagLayout(layout,lim, 0, 0, 1, 1, 0.5, 0.1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, scegliCartella, panelBase);
		insGridBagLayout(layout,lim, 0, 1, 2, 1, 0, 0.1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, percorsoTextField, panelBase);
		insGridBagLayout(layout,lim, 0, 2, 1, 1, 0, 0.1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, tipoDimensioneLabel, panelBase);
		insGridBagLayout(layout,lim, 1, 2, 1, 1, 0.5, 0.1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, tipoDim, panelBase);

		return panelBase;
	}

	/**
	 * @return
	 * @uml.property  name="panelAdvanced"
	 */
	private JPanel getPanelAdvanced() {
		GridBagConstraints lim = new GridBagConstraints();
		GridBagLayout layout = new GridBagLayout();
		panelAdvanced = new KPanel();
		panelAdvanced.setLayout(layout);
		JLabel dimensioneLabel = new KLabel(Translator.getPrefMessage("dimensioneBuffer"));
		JLabel languageLabel = new KLabel(Translator.getPrefMessage("languageLabel"));

		insGridBagLayout(layout,lim, 0, 0, 1, 1, 0.5, 0.1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, dimensioneLabel, panelAdvanced);
		insGridBagLayout(layout,lim, 1, 0, 1, 1, 0.5, 0.1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, bufferComboBox, panelAdvanced);
		insGridBagLayout(layout,lim, 0, 1, 1, 1, 0.5, 0.1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, languageLabel, panelAdvanced);
		insGridBagLayout(layout,lim, 1, 1, 1, 1, 0.5, 0.1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, languageComboBox, panelAdvanced);
		return panelAdvanced;
	}

	/**
	 * @return
	 * @uml.property  name="panelNotification"
	 */
	private JPanel getPanelNotification() {
		GridBagConstraints lim = new GridBagConstraints();
		GridBagLayout layout = new GridBagLayout();
		panelNotification = new KPanel();
		panelNotification.setLayout(layout);
		JLabel systemTrayNotificationLabel = new KLabel(Translator.getPrefMessage("systemTrayNotificationState"));
		JLabel autoUpdateLabel = new KLabel(Translator.getPrefMessage("autoUpdateState"));
		
		insGridBagLayout(layout,lim, 0, 0, 1, 1, 0.5, 0.1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, systemTrayNotificationLabel, panelNotification);
		insGridBagLayout(layout,lim, 1, 0, 1, 1, 0.5, 0.1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, systemTrayNotificationState, panelNotification);
		insGridBagLayout(layout,lim, 0, 1, 1, 1, 0.5, 0.1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, autoUpdateLabel, panelNotification);
		insGridBagLayout(layout,lim, 1, 1, 1, 1, 0.5, 0.1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, autoUpdateState, panelNotification);
		return panelNotification;
	}

//	private static JPanel getPanelSocial() {
//		GridBagConstraints lim = new GridBagConstraints();
//		GridBagLayout layout = new GridBagLayout();
//		panelSocial = new KPanel();
//		panelSocial.setLayout(layout);
//		JLabel twitter = new KLabel(Traduttore.getPrefMessage("twitter"));
//		JLabel facebook = new KLabel(Traduttore.getPrefMessage("facebook"));
//
//		insGridBagLayout(layout,lim, 0, 0, 1, 1, 0.5, 0.1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, twitter, panelSocial);
//		insGridBagLayout(layout,lim, 1, 0, 1, 1, 0.5, 0.1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, twitterLogged, panelSocial);
//		insGridBagLayout(layout,lim, 2, 0, 1, 1, 0.5, 0.1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, loginTwitterButton, panelSocial);
//		insGridBagLayout(layout,lim, 0, 1, 1, 1, 0.5, 0.1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, facebook, panelSocial);
//		insGridBagLayout(layout,lim, 1, 1, 1, 1, 0.5, 0.1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, facebookLogged, panelSocial);
//		insGridBagLayout(layout,lim, 2, 1, 1, 1, 0.5, 0.1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, loginFacebookButton, panelSocial);
//		return panelSocial;
//	}
	
	private JPanel getPanelProxy() {
		GridBagConstraints lim = new GridBagConstraints();
		GridBagLayout layout = new GridBagLayout();
		panelProxy = new KPanel();
		panelProxy.setLayout(layout);
		JLabel proxyActivationLabel = new KLabel(Translator.getPrefMessage("proxyActivation"));
		JLabel proxyServerLabel = new KLabel(Translator.getPrefMessage("proxyServer"));
		JLabel proxyPortLabel = new KLabel(Translator.getPrefMessage("proxyPort"));
		
		insGridBagLayout(layout,lim, 0, 0, 1, 1, 0.5, 0.1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, proxyActivationLabel, panelProxy);
		insGridBagLayout(layout,lim, 1, 0, 1, 1, 0.5, 0.1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, activateProxy, panelProxy);
		insGridBagLayout(layout,lim, 0, 1, 1, 1, 0.5, 0.1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, proxyServerLabel, panelProxy);
		insGridBagLayout(layout,lim, 1, 1, 1, 1, 0.5, 0.1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, proxyServer, panelProxy);
		insGridBagLayout(layout,lim, 0, 2, 1, 1, 0.5, 0.1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, proxyPortLabel, panelProxy);
		insGridBagLayout(layout,lim, 1, 2, 1, 1, 0.5, 0.1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, proxyPort, panelProxy);	

		return panelProxy;
	}
	
	
	public void run() {
		setTitle(Translator.getPrefMessage("preferencesFrameTitle"));
		setIconImage(Toolkit.getDefaultToolkit().getImage(KFrame.class.getResource("/byamanager_icona.png")));

		setMinimumSize(new Dimension(FrameSize.getWidthsettings(),FrameSize.getHeightsettings()));
		setSize(new Dimension(FrameSize.getWidthsettings(),FrameSize.getHeightsettings()));
		setPreferredSize(new Dimension(FrameSize.getWidthsettings(),FrameSize.getHeightsettings()));

		// metto la finestra a centro schermo
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((d.width/2) - FrameSize.getWidthsettings()/2,(d.height/2) - FrameSize.getHeightsettings()/2);
		
		this.riempiPanelTotale();
		setContentPane(this.panelTotale);

		pack();
		setVisible(true);
	}
	
	public JPanel getPanelTotale() {
		return panelTotale;
	}

	public JButton getSalvaPreferenze() {
		return salvaPreferenze;
	}

	public JButton getScegliCartella() {
		return scegliCartella;
	}

	public JTextField getPercorsoTextField() {
		return percorsoTextField;
	}

	public JComboBox<String> getTipoDim() {
		return tipoDim;
	}

	public JComboBox<String> getBufferComboBox() {
		return bufferComboBox;
	}
	
	public JComboBox<String> getLanguageComboBox() {
		return languageComboBox;
	}
	
	public JCheckBox getSystemTrayNotificationState() {
		return systemTrayNotificationState;
	}

	public JCheckBox getAutoUpdateState() {
		return autoUpdateState;
	}

	public JButton getLoginTwitterButton() {
		return loginTwitterButton;
	}

	public JButton getLoginFacebookButton() {
		return loginFacebookButton;
	}

	public JLabel getTwitterLogged() {
		return twitterLogged;
	}

	public JLabel getFacebookLogged() {
		return facebookLogged;
	}

	public JTextField getProxyServer() {
		return proxyServer;
	}

	public JTextField getProxyPort() {
		return proxyPort;
	}

	public JCheckBox getActivateProxy() {
		return activateProxy;
	}

	public void setActivateProxy(JCheckBox activateProxy) {
		this.activateProxy = activateProxy;
	}
}