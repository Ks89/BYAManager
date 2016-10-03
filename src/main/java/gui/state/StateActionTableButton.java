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

import gui.kcomponent.KTableButton;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;

import localization.Translator;
import model.User;

public final class StateActionTableButton {
	private static StateActionTableButton instance = new StateActionTableButton();
	/**
	 * @uml.property  name="buttonMap"
	 * @uml.associationEnd  qualifier="url:java.lang.String javax.swing.JButton"
	 */
	private Map<String,JButton> buttonMap;

	private StateActionTableButton() {
		buttonMap = new HashMap<String,JButton>();
	}

	public static StateActionTableButton getInstance() {
		return instance;
	}

	public JButton getButton(String url) {
		return buttonMap.get(url);
	}

	public Map<String, JButton> getButtonMap() {
		return buttonMap;
	}

	public void addButton(String url) {
		JButton button = null;

		 //non cambiare le scritte "no linux", "no mac/linux" ecc...perche' sono usate nella classe KTableButton
		
		if(url.endsWith(".ipsw")) {
			if(User.getInstance().isItunesCompatible()) {
				button = new KTableButton(Translator.getText("moveFirmware"),KTableButton.MOVE);
			} else {
				button = new KTableButton("no linux",KTableButton.MOVE);	
			}
		}
		if(url.endsWith(".exe")) {
			if(User.getInstance().isWindows()) {
				button = new KTableButton(Translator.getText("executeFile"),KTableButton.EXECUTE);
			} else {
				button = new KTableButton("no mac/linux",KTableButton.EXECUTE);	
			}
		}
		if(url.endsWith(".dmg")) {
			if(User.getInstance().isMac()) {
				button = new KTableButton(Translator.getText("executeFile"),KTableButton.EXECUTE);
			} else {
				button = new KTableButton("no win/linux",KTableButton.EXECUTE);	
			}

		}

		if(button!=null) {
			button.setEnabled(false); //all'avvio devono sempre essere false. Bisogna abilitarli solo quando sono COMPLETED
			buttonMap.put(url, button);
		} else {
			button = new KTableButton("no win/mac/linux",KTableButton.EXECUTE);
			button.setEnabled(false);
			buttonMap.put(url, button);
		}
	}


	public void removeButton(String url) {
		buttonMap.remove(url);
	}

	//abilita o disabilita un pulsante
	public void setButtonEnable(String url, boolean state) {
		if(buttonMap.get(url)!=null) {
			buttonMap.get(url).setEnabled(state);
		}
	}
}
