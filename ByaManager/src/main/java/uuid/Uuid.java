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

package uuid;

import localization.Translator;

import org.apache.commons.codec.digest.DigestUtils;

import update.Version;


import com.eaio.uuid.UUIDGen;


/**
 * Classe astratta che rappresenta l'uuid, generato richiamando la classe
 * MacAddress.
 */
public abstract class Uuid {
	
	/**
	 * @uml.property  name="uuid"
	 */
	private String uuid;

	/**
	 * Metodo per creare l'uuid e impostarlo nella variabile globale.
	 */
	public void creaUuid() {
		this.uuid = Version.getVersion() + "_" + System.getProperty("os.version")  + "_" + Translator.getTraduzione() + "_" + DigestUtils.sha512Hex(UUIDGen.getMACAddress().replace(":", "").toUpperCase());
	}

	/**
	 * @return  String che rappresenta l'uuid esadecimale.
	 * @uml.property  name="uuid"
	 */
	public String getUuid() {
		return this.uuid;
	}
}
