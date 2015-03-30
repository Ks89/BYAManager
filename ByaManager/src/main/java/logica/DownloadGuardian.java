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

/**
 *	Classe che implementa la funzione GOD: "guardian of download",
 *	cioe' un sistema per controllare che i processi del download siano ok, che la velocita' sia equilibrata ecc...
 */
public class DownloadGuardian extends Thread {

	/**
	 * @uml.property  name="abilitazione"
	 */
	private boolean abilitazione = true;

	/**
	 * Metodo per abilitare o disabilitare il calcoloScaricato
	 * in base allo stato.
	 * @param stato boolean per impostare l'abilitazione o la disabilitazione.
	 */
	public void abilitaCalcoloScaricato(boolean stato) {
		abilitazione = stato;
	}

	/**
	 * @return un boolean con lo stato dell'abilitazione.
	 */
	public boolean getAbilitazione() {
		return this.abilitazione;
	}

	public void run() {
//		while(abilitazione) {
//			//inserisco ritardo ad ogni scansione
//			try {
//				Thread.sleep(10000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//			long average = 0;
//			List<Download> downloadList = DownloadList.getInstance().getDownloadListCopied();
//			for(Download download : downloadList) {
//				if(download.getStatus()==0) { //se il download e' in corso 
//					for(Process process : download.getProcessList()) {
//						average += process.getDownloaded();
//					}
//					average = average/4;
//					for(Process process : download.getProcessList()) {
//						if(process.getDownloaded()*2<average) {
//							System.out.println(process.getDownloaded()*2 + "," + average);
//							//metti in pausa il singolo processo, attendi 3 secondi e riavvialo
//							process.pause();
//							try {
//								Thread.sleep(3000);
//							} catch (InterruptedException e) {
//								e.printStackTrace();
//							}
//							process.riprendi();
//						}
//					}
//					if(download.getVelocita()==0) {
//						//se e' fermo metti in pausa il download
//						try {
//							Thread.sleep(2000);
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}
//						if(download.getVelocita()==0) {
//							download.mettiInPausa();
//							System.out.println("---->>>>>>>PAUSA FORZATA DA GOD!!!");
//						}
//					}
//				}
//			}
//		}
	}
}