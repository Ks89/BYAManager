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

package logica.listener.byam;

import gui.MainFrame;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import logica.LogicLoaderFirmware;
import model.Firmware;

public class DeviceComboBoxListener extends AbstractAction {
	private static final long serialVersionUID = 1764514206828803076L;

	@Override
	public void actionPerformed(ActionEvent e) {
		//rimuovo tutti gli elementi dalla combobox coi firmware
		MainFrame.getInstance().getListaFirmware().removeAllItems();

		//scansiono la lista dei firmware e dove trovo un Firmware del dispositivo selezionato nel 
		//menu a tendina lo aggiungo alla combobox dei firmware.
		for(Firmware firmware : LogicLoaderFirmware.getInstance().getFirmwareListaNomeFile()) {
			if(firmware.getDevice().getNomeDispositivo().equals(LogicLoaderFirmware.getInstance().cercaDispositivo((String)MainFrame.getInstance().getListaDispositivi().getSelectedItem()))) {
				MainFrame.getInstance().getListaFirmware().addItem(firmware.getVersion() + " (" + firmware.getBuild() + ")"); 
			}
		}
	}
}
