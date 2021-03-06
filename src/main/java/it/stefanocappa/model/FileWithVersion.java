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


public abstract class FileWithVersion extends FileWeb {
	
	private String version;
	private Changelog changelog;

	public FileWithVersion() {
		super();
	}
	
	public String getOsName() {
		return this.getOperativeSystemList().get(0).getShortName(); //TODO sistemare il get(0)
	}
	
	public abstract String getFileName();

	public String getVersion() {
		return version;
	}

	public Changelog getChangelog() {
		return changelog;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public void setChangelog(Changelog changelog) {
		this.changelog = changelog;
	}
}
