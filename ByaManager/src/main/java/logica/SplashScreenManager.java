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

package logica;

import localization.Translator;
import gui.splashscreen.SplashScreen;

public final class SplashScreenManager {
	private static SplashScreen splash;
	private static SplashScreenManager instance = new SplashScreenManager();

	private SplashScreenManager() {
		splash = new SplashScreen(this.getClass().getResource("/splashbeta1.png"));
	}

	/**
	 * Metodo che permette di ottenere l'istanza della classe.
	 * @return istanza della classe GestioneSplashScreen.
	 */
	public static SplashScreenManager getInstance() {
		return instance;
	}
	
	public static void mostraSplash() {
		splash.splash(); //visualizza la splashScreen
	}

	public static void chiudiSplash() {
		splash.dispose(); //chiude la splashScreen
	}

	public static void setText(String key) {
		splash.showStatus(Translator.getSplashText(key));
	}
	
	public static void setTimer(int millisecondi) {
		splash.splashFor(millisecondi);
	}

}
