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


package connection;

import notification.Notification;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;

/**
 *	Classe che si occupa di testare la connessione internet 
 *	prima di avviare il programma.
 */
public class TestConnection {
	private static final Logger LOGGER = Logger.getLogger(TestConnection.class);

	/**
	 * Verifica la connettivita tramite un get http.
	 * Non lo fa con il ping perche' si puo' anche disabilitare.
	 * @throws IOException
	 */
	public void testConnection() throws IOException {
		LOGGER.info("verificaRete() - Avviata verifica rete");

		URI uri = URI.create("http://www.google.com/");
		HttpURLConnection connection = (HttpURLConnection)uri.toURL().openConnection();
		connection.connect();

		if (connection.getResponseCode() / 100 != 2) {
			Notification.showErrorOptionPane("download201", "Server error 201");
			return;
		}

		connection.getContent();
		LOGGER.info("verificaRete() - Eseguita richiesta verifica rete " + uri);
	}
}
