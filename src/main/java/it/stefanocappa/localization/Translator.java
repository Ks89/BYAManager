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

package it.stefanocappa.localization;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Classe che si occupa di tradurre i testi nella lingua di
 * default del sistema operativo.
 */
public final class Translator {

	private static Locale locale;
	private static ResourceBundle text;
	private static ResourceBundle message;
	private static ResourceBundle messagePreferences;
	private static ResourceBundle splash;

	private Translator(){}

	static {
		locale = Locale.getDefault();
		//		locale = new Locale("cs","CZ"); //ceco
		//		locale = new Locale("en","US");
		//		locale = new Locale("it","IT");
		text = ResourceBundle.getBundle("translations", locale);
		message = ResourceBundle.getBundle("translations-m", locale);
		messagePreferences = ResourceBundle.getBundle("translations-p", locale);
		splash = ResourceBundle.getBundle("translations-s", locale);
	}

	public static void setLocale(int languageComboIndex) {
		//ora accedo alla preferenze e imposto la lingua, che da ora non dipende piu' solo
		//dal sistema operativo. Su tutti gli OS parte come quella del sistema operativo
		switch(languageComboIndex) {
		case 1:
			locale = new Locale("it","IT");
			break;
		case 2:
			locale = new Locale("cs","CZ");
			break;
		default: //come 0, cioe' english
			locale = new Locale("en","US");
			break;
		}
		text = ResourceBundle.getBundle("translations", locale);
		message = ResourceBundle.getBundle("translations-m", locale);
		messagePreferences = ResourceBundle.getBundle("translations-p", locale);
		splash = ResourceBundle.getBundle("translations-s", locale);
	}
	/**
	 * Metodo statico per ottenere il testo dal file translations_xx_XX.properties associato
	 * alla lingua dell'os, in base alla chiave fornite in ingresso.
	 * @param chiave String che rappresenta la chiave del testo nel file translations_xx_XX.properties.
	 * @return Una String rappresentante il testo tradotto nella lingua desiderata.
	 */
	public static String getText(String chiave) {
		return text.getString(chiave);
	}
	/**
	 * Metodo statico per ottenere il testo dal file translations-m_xx_XX.properties associato
	 * alla lingua dell'os, in base alla chiave fornite in ingresso.
	 * @param chiave String che rappresenta la chiave del testo nel file translations_xx_XX.properties.
	 * @return Una String rappresentante il testo tradotto nella lingua desiderata.
	 */
	public static String getMessage(String chiave) {
		return message.getString(chiave);
	}
	/**
	 * Metodo statico per ottenere il testo dal file translations-p_xx_XX.properties associato
	 * alla lingua dell'os, in base alla chiave fornite in ingresso.
	 * @param chiave String che rappresenta la chiave del testo nel file translations_xx_XX.properties.
	 * @return Una String rappresentante il testo tradotto nella lingua desiderata.
	 */
	public static String getPrefMessage(String chiave) {
		return messagePreferences.getString(chiave);
	}
	/**
	 * Metodo statico per ottenere il testo dal file translations-s_xx_XX.properties associato
	 * alla lingua dell'os, in base alla chiave fornite in ingresso.
	 * @param chiave String che rappresenta la chiave del testo nel file translations_xx_XX.properties.
	 * @return Una String rappresentante il testo tradotto nella lingua desiderata.
	 */
	public static String getSplashText(String chiave) {
		return splash.getString(chiave);
	}

	private static String getLingua() {
		return Locale.getDefault().getCountry();
	}

	private static String getStato() {
		return Locale.getDefault().getLanguage();
	}

	public static String getTraduzione() {
		return getLingua() + getStato();
	}

}
