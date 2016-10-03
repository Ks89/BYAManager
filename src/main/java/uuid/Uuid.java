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
 * Class that represents the uuid.
 */
public abstract class Uuid {
	
	private String uuid;

	/**
	 * Method to generate a simple uuid using this informations: 
	 * BYAManager's version, os version, language of the os, the sha512 of your mac address.<br></br>
	 * With this operation i can't reverse the sha512 and your identity is safe.
	 * I use this uuid only for anonymous statistical purposes.
	 */
	public void generateUuid() {
		this.uuid = Version.getVersion() + "_" + System.getProperty("os.version")  + "_" + Translator.getTraduzione() + "_" + DigestUtils.sha512Hex(UUIDGen.getMACAddress().replace(":", "").toUpperCase());
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
}
