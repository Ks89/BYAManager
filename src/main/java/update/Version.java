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

package update;

import notification.Notification;

public final class Version {

	public static final String VERSION = "1,0,1,0";
	
	private static int majorSw;
	private static int minorSw;
	private static int bugfixSw;
	private static int revisionSw; 
	
	private static int majorServer;
	private static int minorServer;
	private static int bugfixServer;
	private static int revisionServer;
	
	private Version(){}
	  
	public static String getVersion() {
		return VERSION;
	}

	/**
	 * Imposta la versione maggiore da controllare, che nella maggior parte dei casi assume il significato della
	 * versione rilevata dal Server.
	 * @param versionMajorOrServer
	 */
	private static void setMajorOrServerVersion(String versionMajorOrServer) {
		//la versione del programma deve essere sempre indicata con X,X,X,X
		String[] versioneArray = versionMajorOrServer.split(",");
		majorServer = Integer.parseInt(versioneArray[0]);
		minorServer = Integer.parseInt(versioneArray[1]);
		bugfixServer = Integer.parseInt(versioneArray[2]);
		revisionServer = Integer.parseInt(versioneArray[3]);
	}
	
	private static void setSoftwareInExecutionVersion() {
		String[] softwareVersion = VERSION.split(",");
		majorSw = Integer.parseInt(softwareVersion[0]);
		minorSw = Integer.parseInt(softwareVersion[1]);
		bugfixSw = Integer.parseInt(softwareVersion[2]);
		revisionSw = Integer.parseInt(softwareVersion[3]);
	}
	
	/**
	 * Metodo che dice se la versione passata come parametro e' piu' recente
	 * di quella del programma.
	 * @param versionMajor String che rappresenta la versione del programma da verificare come x.x.x.x
	 * @return "1" se la versionMajor e' maggiore di "VERSION" del software, "-1" se l'opposto e "0" se uguali.
	 */
	public static int verificaEsistenzaVersionePiuRecente(String versionMajor) {
		if(versionMajor.equals(VERSION)) {
			return 0;
		}
		
		//imposto le variabili globali, private e statiche della classe per eseguire il confronto delle versioni
		setSoftwareInExecutionVersion();
		
		setMajorOrServerVersion(versionMajor);
		
		if(majorServer==9 && minorServer==9 && bugfixServer==9 && revisionServer ==9) {
			Notification.showNormalOptionPane("versionNotSupported");
			System.exit(0);
		}

		//aggiornamentoDatabase false: il database e' aggiornato, altrimenti no
		if((majorServer > majorSw)) {
			return 1;
		}
		if(majorServer == majorSw && minorServer > minorSw) {
			return 1;
		}
		if(majorServer == majorSw && minorServer == minorSw && bugfixServer > bugfixSw) {
			return 1;
		}
		if(majorServer == majorSw && minorServer == minorSw && bugfixServer == bugfixSw && revisionServer  > revisionSw) {
			return 1;
		}
		return -1;
	}
	
	/**
	 * Get the commercial version number
	 * @return
	 */
	public static String getGuiFrameVersion() {
		return VERSION.replace(",", ".").substring(0,5);
	}
}
