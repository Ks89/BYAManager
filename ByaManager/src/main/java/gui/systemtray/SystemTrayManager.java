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

package gui.systemtray;

import gui.MainFrame;
import gui.image.ImageLoader;

import java.awt.AWTException;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import localization.Translator;
import notification.Notification;

public final class SystemTrayManager {
	/**
	 * @uml.property  name="esciTotalmenteItem"
	 */
	private MenuItem esciTotalmenteItem;
	/**
	 * @uml.property  name="ripristinaItem"
	 */
	private MenuItem ripristinaItem;
	/**
	 * @uml.property  name="tray"
	 */
	private SystemTray tray;
	/**
	 * @uml.property  name="trayIcon"
	 */
	private TrayIcon trayIcon;
	private static SystemTrayManager instance = new SystemTrayManager();
	
	private SystemTrayManager() {
		trayIcon = new TrayIcon(ImageLoader.getInstance().getIconaBya(), "BYA Manager", this.popupMenuTray());
	}
	
	/**
	 * Metodo che permette di ottenere l'istanza della classe.
	 * @return istanza della classe.
	 */
	public static SystemTrayManager getInstance() {
		return instance;
	}

	private PopupMenu popupMenuTray() {
		PopupMenu popup = new PopupMenu();
		esciTotalmenteItem = new MenuItem(Translator.getText("chiudiProgrammaItem"));
		ripristinaItem = new MenuItem(Translator.getText("mostraItem"));
		popup.add(ripristinaItem);
		popup.add(esciTotalmenteItem);
		return popup;
	}

	/**
	 * Metodo per creare la systemtray, se supportata dall'os, 
	 * e per stabilirne le dimensioni, aggiungere i listener,
	 * gestire i clic del mouse inerendo il riferimento nella variabile globale.
	 */
	public void creaSystemTray() {
		try {
			if(SystemTray.isSupported()) {
				tray = SystemTray.getSystemTray();
				trayIcon.setImageAutoSize(true);
				trayIcon.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						if(e.getClickCount()==2) {
							tray.remove(trayIcon);
							MainFrame.getInstance().setVisible(true);
						}
					}
				});

				tray.add(trayIcon);
			} else {
				Notification.showNormalOptionPane("graphicManagerSysTrayNotSupported");
			}
		} catch (AWTException e) {
			Notification.showNormalOptionPane("graphicManagerSysTrayError");
		}
	}
	
	public void removeSystemTray() {
		tray.remove(trayIcon);
	}

	/**
	 * @return
	 * @uml.property  name="esciTotalmenteItem"
	 */
	public MenuItem getEsciTotalmenteItem() {
		return esciTotalmenteItem;
	}

	/**
	 * @return
	 * @uml.property  name="ripristinaItem"
	 */
	public MenuItem getRipristinaItem() {
		return ripristinaItem;
	}
	
}
