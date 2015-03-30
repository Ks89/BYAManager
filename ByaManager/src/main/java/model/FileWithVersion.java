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
 * @author  Ks89
 */
public abstract class FileWithVersion extends FileWeb {
	
	/**
	 * @uml.property  name="version"
	 */
	private String version;
	/**
	 * @uml.property  name="changelog"
	 * @uml.associationEnd  
	 */
	private Changelog changelog;

	public FileWithVersion() {
		super();
	}
	
	/**
	 * @return  version String rappresentante la versione.
	 * @uml.property  name="version"
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param version  Una String rappresentante la versione.
	 * @uml.property  name="version"
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * @return
	 * @uml.property  name="changelog"
	 */
	public Changelog getChangelog() {
		return changelog;
	}

	/**
	 * @param changelog
	 * @uml.property  name="changelog"
	 */
	public void setChangelog(Changelog changelog) {
		this.changelog = changelog;
	}
	
	public String getOsName() {
		return this.getOperativeSystemList().get(0).getShortName(); //TODO sistemare il get(0)
	}
	
	/**
	 * @uml.property  name="fileName"
	 */
	public abstract String getFileName();
}
