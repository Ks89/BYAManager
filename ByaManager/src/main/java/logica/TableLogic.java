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

import gui.MainFrame;

/**
 * Classe per la gestione della tabella.
 */
public final class TableLogic {

	private static int dimensioneDaMostrare;
	
	private TableLogic() {}
	
	/**
	 * Metodo per ottenere il dispositivo dato un Processo.
	 * @param uriStringDownload String che rappresenta l'URI di cui si vuole la build del firmware associato.
	 * @return Una String che rappresenta il dispositivo.
	 */
	public static String getDispositivo(String uriStringDownload) {
		String[] parziale1 = uriStringDownload.split(",");
		//se la dimensione della splittata e' 2 e' ok, altrimenti e' l'url particolare dell'iphone 3,1 con 4.0
		//quindi il programma si bloccherebbe allora metto un if che corregge questo problema in questa situazione
		String[] parziale3;
		if(parziale1.length==2) {
			parziale3 = parziale1[0].split("/");
			return parziale3[parziale3.length-1] + "," + parziale1[1].split("_")[0]; //return del dispositivo
		} else {
			parziale3 = parziale1[1].split("/");
			return parziale3[parziale3.length-1] + "," + parziale1[1].split("_")[0]; //return del dispositivo
		}
	}

	/**
	 * Metodo per ottenere la versione del firmware, dato un processo.
	 * @param uriStringDownload String che rappresenta l'URI di cui si vuole la build del firmware associato.
	 * @return Una String che rappresenta la versione del firmware associato al processo.
	 */
	public static String getVersione(String uriStringDownload) {
		String build = getBuild(uriStringDownload); 
		
		String[] parzialeVersione = uriStringDownload.split(",");
		//se la dimensione della splittata e' 2 e' ok, altrimenti e' l'url particolare dell'iphone 3,1 con 4.0
		//quindi il programma si bloccherebbe allora metto un if che corregge questo problema in questa situazione
		if(parzialeVersione.length==2) {
			return parzialeVersione[1].split("_")[1] + build; //versione (eventualmente conprende anche la build)
		} else {
			return parzialeVersione[2].split("_")[1] + build; //versione (eventualmente conprende anche la build)
		}
	}

	/**
	 * Metodo per ottenere la build del firmware, dato un processo.
	 * @param uriStringDownload String che rappresenta l'URI di cui si vuole la build del firmware associato.
	 * @return Una String che rappresenta la build del firmware associato al processo.
	 */
	private static String getBuild(String uriStringDownload) {
		String[] parziale1 = uriStringDownload.split("_");
		return " (" + parziale1[parziale1.length-2] + ")";
	}


	
	
	/**
	 * Metodo per impostare la velocita' globale.
	 * @param velocitaGlobale long che rappresenta la velocita' globale.
	 */
	public static void setVelocitaGlobale(long velocitaGlobale) {
		if(velocitaGlobale==0) {
			MainFrame.getInstance().setTextLabelVelocitaGlobale("   ");
		} else {
			if(dimensioneDaMostrare==2) {
				//in B,KiB,MiB,GiB
				MainFrame.getInstance().setTextLabelVelocitaGlobale((velocitaGlobale/1024) + " KiB/S  ");
			} else {
				//in B,KB,MB,GB
				MainFrame.getInstance().setTextLabelVelocitaGlobale((velocitaGlobale/1000) + " KB/S  ");
			}
		}
	}

	public static int getDimensioneDaMostrare() {
		return dimensioneDaMostrare;
	}
	
	/**
	 * @param dimensioneDaMostrare int che indica che dimensione mostrare,
	 * cioe' in byte, Multipli dei bynary byte o Multipli dei byte ecc...
	 */
	public static void setDimensioneDaMostrare(int dimDaMostrare) {
		dimensioneDaMostrare = dimDaMostrare;
	}
}
