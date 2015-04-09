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

package model;

import lombok.Getter;

/**
 * Classe che estende Dispositivo aggiugnengo il nome commerciale.
 */
public class CommercialDevice extends Device{

	private static final String[] NOMICODICE = {"AppleTV2,1","AppleTV3,1","AppleTV3,2",
		
		"iPad1,1","iPad2,1","iPad2,2","iPad2,3","iPad2,4",
		"iPad3,1","iPad3,2","iPad3,3",
		"iPad3,4","iPad3,5","iPad3,6",
		
		"iPad2,5","iPad2,6","iPad2,7",
		"iPad4,4","iPad4,5","iPad4,6",
		"iPad4,7","iPad4,8","iPad4,9",
		
		"iPad4,1","iPad4,2","iPad4,3",
		"iPad5,3","iPad5,4",
		
		"iPhone1,1","iPhone1,2","iPhone2,1","iPhone3,1","iPhone3,2","iPhone3,3","iPhone4,1",
		"iPhone5,1","iPhone5,2","iPhone5,3","iPhone5,4","iPhone6,1","iPhone6,2","iPhone7,1","iPhone7,2",
		
		"iPod1,1","iPod2,1","iPod3,1","iPod4,1","iPod5,1"};
	private static final String[] NOMICOMMERCIALE = {"AppleTV 2G","AppleTV 3G","AppleTV 3G Rev A.",
		
		"iPad 1","iPad 2 WiFi","iPad 2 3G","iPad 2 CDMA","iPad 2 WiFi R2",
		"iPad 3 WiFi","iPad 3 3G","iPad 3 CDMA",
		"iPad 4 WiFi","iPad 4 3G","iPad 4 CDMA",
		
		"iPad mini 1 WiFi","iPad mini 1 3G","iPad mini 1 CDMA",
		"iPad mini 2 WiFi","iPad mini 2 3G","iPad mini 2 China",
		"iPad mini 3 WiFi","iPad mini 3 3G","iPad mini 3 China",
		
		"iPad Air WiFi","iPad Air 3G","iPad Air TD-LTE China",
		"iPad Air 2 WiFi","iPad Air 2 3G+CDMA",
		
		"iPhone 2G","iPhone 3G","iPhone 3GS","iPhone 4","iPhone 4 Rev","iPhone 4 CDMA","iPhone 4S",
		"iPhone 5","iPhone 5 CDMA","iPhone 5c","iPhone 5c CDMA", "iPhone 5S", "iPhone 5S CDMA", "iPhone 6 Plus","iPhone 6",
		
		"iPod Touch 1G","iPod Touch 2G","iPod Touch 3G","iPod Touch 4G","iPod Touch 5G"};

	@Getter private String nomeCommerciale;
	@Getter private String product;
	@Getter private String version;

	/**
	 * Costruttore che imposta il nome in codice ed assegna il nome Commerciale.
	 * @param nomeDispositivo String che rappresenta il nome in codice del dispositivo.
	 */
	public CommercialDevice(String nomeDispositivoInCodice) {
		super(nomeDispositivoInCodice);
		this.assegnaNomeCommerciale();
		this.setProductAndVersion();
	}

	/**
	 * Metodo che assegna il nome commerciale al dispositivo fornito, 
	 * in base a quello in codice.
	 */
	public final void assegnaNomeCommerciale() {
		for(int i=0; i<NOMICODICE.length; i++) {
			if(super.getNomeDispositivo().equals("iPhone3,Vfgb5/iPhone3")) {
				this.nomeCommerciale = "iPhone4";
				return;
			} else {
				if(super.getNomeDispositivo().equals(NOMICODICE[i])) {
					this.nomeCommerciale = NOMICOMMERCIALE[i];
					return;
				}
			}
		}
		this.nomeCommerciale = super.getNomeDispositivo();
	}
	
	private void setProductAndVersion() {
		if(nomeCommerciale.contains("iPod")) {
			this.product = "iPod Touch";
			this.version = nomeCommerciale.replace("iPod Touch ", "");
		} else {
			this.product = nomeCommerciale.split(" ")[0];
			this.version = nomeCommerciale.replace(product + " ", "");
		}
	}
}
