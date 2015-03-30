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

import java.io.*;
import java.net.*;


import org.apache.log4j.Logger;

import uuid.RegisteredUuid;


/**
 * Classe che si occupa di controllare se ci sono altri processi attivi di questo programma. 
 * Per impedire piu' esecuzioni del programma viene creata una connessione con una certa porta, 
 * tramite socket.
 */
public class ServerSingleInstance extends Thread {
	private static final Logger LOGGER = Logger.getLogger(RegisteredUuid.class);
	/**
	 * @uml.property  name="porta"
	 */
	private int porta; 

	public ServerSingleInstance(int porta) {
		this.porta = porta;
	}
	
	public void run() {
		try {
			System.out.println("Porta impostata ssi: " + porta);
			ServerSocket serverSocket = new ServerSocket(porta, 1);
			Socket clientSocket;
			while (true) {
				// In attesa di una connessione
				clientSocket = serverSocket.accept();
				clientSocket.close();
			}
		}
		catch (IOException e) {
			LOGGER.error("ServerSingolaIstanza - run() - IOException= " + e);
		}
	}
}
