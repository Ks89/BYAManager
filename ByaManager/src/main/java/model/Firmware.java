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

/**
 *	Classe che rappresenta un singolo firmware con versione, build, dispositivo ecc...
 */
public class Firmware extends FileWithVersion{

	/**
	 * @uml.property  name="device"
	 * @uml.associationEnd  
	 */
	private Device device;
//	private List<JailbreakSoftware> jailbrekTools;
	/**
	 * @uml.property  name="build"
	 */
	private String build;

	public Firmware() {
		super();
	}

	/**
	 * @return
	 * @uml.property  name="device"
	 */
	public Device getDevice() {
		return device;
	}
	/**
	 * @param device
	 * @uml.property  name="device"
	 */
	public void setDevice(Device device) {
		this.device = device;
	}
	
	
	/**
	 * @return  String rappresentante la build.
	 * @uml.property  name="build"
	 */
	public String getBuild() {
		return build;
	}

	/**
	 * @param build  Una String per impostare la build.
	 * @uml.property  name="build"
	 */
	public void setBuild(String build) {
		this.build = build;
	}
	
//	public void addJailbreakTool(JailbreakSoftware jailbreakSoftware) {
//		this.jailbrekTools.add(jailbreakSoftware);
//	}
//	
//	public void removeJailbreakTool(JailbreakSoftware jailbreakSoftware) {
//		this.jailbrekTools.remove(jailbreakSoftware);
//	}
//	
//	public List<JailbreakSoftware> getJailbreakTools() {
//		return this.jailbrekTools;
//	}
	
	public String getFileName() {
		return this.getDevice().getNomeDispositivo() + "_" + this.getVersion() + "_" + this.getBuild() + "_Restore.ipsw";
	}
	
	//metodo essenziale richiamato dal salvataggio del database del firmware
	//Infatti viene richiamato per salvare su file il percorso + hash
	public String toString() {
		return super.getUri().toString() + "___" + super.getHash();
	}
}
