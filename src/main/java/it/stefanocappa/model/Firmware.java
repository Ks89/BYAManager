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

package it.stefanocappa.model;

/**
 *	Classe che rappresenta un singolo firmware con versione, build, dispositivo ecc...
 */
public class Firmware extends FileWithVersion{
	private Device device;
	private String build;

	public Firmware() {
		super();
	}
	
	public String getFileName() {
		return this.getDevice().getNomeDispositivo() + "_" + this.getVersion() + "_" + this.getBuild() + "_Restore.ipsw";
	}
	
	//metodo essenziale richiamato dal salvataggio del database del firmware
	//Infatti viene richiamato per salvare su file il percorso + hash
	public String toString() {
		return super.getUri().toString() + "___" + super.getHash();
	}

	public Device getDevice() {
		return device;
	}

	public String getBuild() {
		return build;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	public void setBuild(String build) {
		this.build = build;
	}
}
