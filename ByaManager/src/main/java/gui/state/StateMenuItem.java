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

package gui.state;

import gui.kcomponent.KMenu;
import gui.kcomponent.KMenuBar;
import gui.kcomponent.KMenuItem;

import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import localization.Translator;

public final class StateMenuItem {
	private static JMenu fileMenu;
	private static JMenu eseguiMenu;
	private static JMenu helpMenu;
	private static JMenuItem checkDBUpdateMenuItem;
	private static JMenuItem checkByamUpdateMenuItem;
	private static JMenuItem fileExitMenuItem;
	private static JMenuItem fileRiduciMenuItem;
	private static JMenuItem reinizializzaMenuItem;
	private static JMenuItem settingsMenuItem;
	private static JMenuItem aboutMenuItem;
	private static JMenuItem donazioneMenuItem;
	private static JMenuItem ksBlogMenuItem;
	private static JMenuItem byaMenuItem;
	
	private static JMenuBar menuBar;

	private StateMenuItem() {}
	
	static {
		// Set up file menu.
		menuBar = new KMenuBar();
		fileMenu = new KMenu(Translator.getText("fileItem"));
		eseguiMenu = new KMenu(Translator.getText("editItem"));
		helpMenu = new KMenu(Translator.getText("helpItem"));

		//primo dei 2 boolean se e' true: vedi commento a fianco epr capire a cosa si riferisce
		
		fileRiduciMenuItem = new KMenuItem(Translator.getText("riduciProgrammaItem"),KeyEvent.VK_R,false, false);
		fileExitMenuItem = new KMenuItem(Translator.getText("chiudiProgrammaItem"),KeyEvent.VK_X,true, false); //fine tendina
		
		reinizializzaMenuItem = new KMenuItem(Translator.getText("reinizializza"),KeyEvent.VK_I,true, false); //separazione
		settingsMenuItem = new KMenuItem(Translator.getText("settings"),KeyEvent.VK_S,true, false); //fine tendina
		
		aboutMenuItem = new KMenuItem(Translator.getText("aboutItem"),KeyEvent.VK_A,true, false); //separazione
		checkDBUpdateMenuItem = new KMenuItem(Translator.getText("dbItem"),KeyEvent.VK_U,false, false);
		checkByamUpdateMenuItem = new KMenuItem(Translator.getText("byamItem"),KeyEvent.VK_B,true, false); //separazione
		ksBlogMenuItem = new KMenuItem(Translator.getText("ks89Site"),KeyEvent.VK_8,false, false);
		byaMenuItem = new KMenuItem(Translator.getText("byaSite"),KeyEvent.VK_Y,false, false);
		donazioneMenuItem = new KMenuItem(Translator.getText("donaItem"),KeyEvent.VK_D,true, false); //fine tendina

		eseguiMenu.setMnemonic(KeyEvent.VK_E);
		helpMenu.setMnemonic(KeyEvent.VK_H);
		fileMenu.setMnemonic(KeyEvent.VK_F);

//		inviaMailMenuItem = new KMenuItem(Traduttore.getText("inviaEmail"),KeyEvent.VK_M);
//		inviaMailMenuItem.setEnabled(false);
	}
	
	public static void createMenu() {
		fileMenu.add(fileRiduciMenuItem);
		fileMenu.add(fileExitMenuItem);
		
		eseguiMenu.add(reinizializzaMenuItem);
		eseguiMenu.add(settingsMenuItem);
		
		helpMenu.add(aboutMenuItem);
		helpMenu.add(checkDBUpdateMenuItem);
		helpMenu.add(checkByamUpdateMenuItem);
		helpMenu.add(ksBlogMenuItem);
		helpMenu.add(byaMenuItem);
		helpMenu.add(donazioneMenuItem);
		
		menuBar.add(fileMenu);
		menuBar.add(eseguiMenu);
		menuBar.add(helpMenu);
	}

	public static JMenuItem getCheckDBUpdateMenuItem() {
		return checkDBUpdateMenuItem;
	}

	public static JMenuItem getCheckByamUpdateMenuItem() {
		return checkByamUpdateMenuItem;
	}

	public static JMenuItem getFileRiduciMenuItem() {
		return fileRiduciMenuItem;
	}

	public static JMenu getFileMenu() {
		return fileMenu;
	}

	public static JMenu getEseguiMenu() {
		return eseguiMenu;
	}

	public static JMenu getHelpMenu() {
		return helpMenu;
	}

	public static JMenuItem getFileExitMenuItem() {
		return fileExitMenuItem;
	}

	public static JMenuItem getReinizializzaMenuItem() {
		return reinizializzaMenuItem;
	}

	public static JMenuItem getSettingsMenuItem() {
		return settingsMenuItem;
	}

	public static JMenuItem getAboutMenuItem() {
		return aboutMenuItem;
	}

	public static JMenuItem getDonazioneMenuItem() {
		return donazioneMenuItem;
	}

	public static JMenuItem getKsBlogMenuItem() {
		return ksBlogMenuItem;
	}

	public static JMenuItem getByaMenuItem() {
		return byaMenuItem;
	}

	public static JMenuBar getMenuBar() {
		return menuBar;
	}
}
