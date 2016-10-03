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

package it.stefanocappa.notification;

import javax.swing.JOptionPane;

import it.stefanocappa.localization.Translator;

/**
 * Class used to show notifications.
 */
public final class Notification {

	private Notification() {}
	
	public static void showNormalOptionPane(String key) {
		JOptionPane.showMessageDialog(null, Translator.getMessage(key));
	}

	public static void showErrorOptionPane(String key, String name) {
		JOptionPane.showMessageDialog(null, Translator.getMessage(key), name, JOptionPane.ERROR_MESSAGE);
	}
	
	public static void showDefaultConfirmDialog(String key, Object[] objects) {
		JOptionPane.showConfirmDialog(null, objects, Translator.getMessage(key), JOptionPane.DEFAULT_OPTION);  
	}

	public static void showInfoOptionPane(String key, String name) {
		JOptionPane.showMessageDialog(null, Translator.getMessage(key), name, JOptionPane.INFORMATION_MESSAGE);
	}
	
	public static boolean clickedYesInConfirmDialog(String key) {
		return JOptionPane.showConfirmDialog(null, Translator.getMessage(key))==JOptionPane.YES_OPTION;
	}
}
