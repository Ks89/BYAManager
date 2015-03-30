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

package logica.listener;

import gui.MainFrame;
import gui.systemtray.SystemTrayManager;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JCheckBox;

import localization.Translator;
import notification.Notification;
import preferences.Settings;

public class WindowClosingListener extends WindowAdapter{

	@Override
	public void windowClosing(WindowEvent e) {
		//controllo se mostrare l'avviso sulla riduzione in system tray leggendo un boolean dalle impostazioni
		if(Settings.getInstance().getSystemTrayNotificationState()) { //true mostro l'avviso
			JCheckBox checkbox = new JCheckBox(Translator.getText("nonMostrareMessaggioRiduzioneIcona"), false);  
			Object[] params = {Translator.getMessage("messaggioRiduzioneIcona"), checkbox};  
			Notification.showDefaultConfirmDialog("downloadManagerSystrayMessageTitle", params);
			Settings.getInstance().setSystemTrayNotificationState(!checkbox.isSelected());
			Settings.getInstance().saveSettings();
		}
		MainFrame.getInstance().setVisible(false);
		SystemTrayManager.getInstance().creaSystemTray();
	}
	
}
