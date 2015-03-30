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

public abstract class OperativeSystem {

	/**
	 * @uml.property  name="completeName"
	 * @uml.associationEnd  
	 */
	private String completeName;
	/**
	 * @uml.property  name="shortName"
	 */
	private String shortName;
	/**
	 * @uml.property  name="architecture"
	 */
	private String architecture;
	/**
	 * @uml.property  name="execExtension"
	 */
	private String execExtension;
	
	public abstract void generateCompleteName(String platform);
	public abstract void generateShortName(String platform);
	public abstract void generateArchitecture(String platform);
	public abstract void generateExecExtension();
	
	
	/**
	 * @return
	 * @uml.property  name="completeName"
	 */
	public String getCompleteName() {
		return completeName;
	}
	/**
	 * @param completeName
	 * @uml.property  name="completeName"
	 */
	public void setCompleteName(String completeName) {
		this.completeName = completeName;
	}
	/**
	 * @return
	 * @uml.property  name="shortName"
	 */
	public String getShortName() {
		return shortName;
	}
	/**
	 * @param shortName
	 * @uml.property  name="shortName"
	 */
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	/**
	 * @return
	 * @uml.property  name="architecture"
	 */
	public String getArchitecture() {
		return architecture;
	}
	/**
	 * @param architecture
	 * @uml.property  name="architecture"
	 */
	public void setArchitecture(String architecture) {
		this.architecture = architecture;
	}
	/**
	 * @return
	 * @uml.property  name="execExtension"
	 */
	public String getExecExtension() {
		return execExtension;
	}
	/**
	 * @param execExtension
	 * @uml.property  name="execExtension"
	 */
	public void setExecExtension(String execExtension) {
		this.execExtension = execExtension;
	}
}
