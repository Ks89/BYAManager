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

public class Os {
	private String osName;
	private String osVersion;
	private String architecture;
	private OperativeSystem operativeSystemInstance;

	public Os() {
		this.osName = System.getProperty("os.name");
		this.osVersion = System.getProperty("os.version"); 
		this.architecture = System.getProperty("os.arch");
		this.operativeSystemInstance = this.createOperativeSystem();
	}

	private OperativeSystem createOperativeSystem() {
		if(osName.contains("Windows")) {
			return new Windows("32"); //TODO far si che rilevi se 32 o 64 bit da architettura
		}
		if(osName.contains("Mac")) {
			return new MacOsx("0");
		} else {
			return new Linux("l32"); //e' di certo linux
		}
	}

	public boolean is64Bit() {
		return this.architecture.contains("64");
	}

	public boolean isItunesCompatible() {
		return osName.contains("Mac") || osName.contains("Windows");
	}

	public boolean isLinux() {
		return osName.contains("Linux");
	}

	public boolean isMac() {
		return osName.contains("Mac");
	}

	public boolean isWindows() {
		return osName.contains("Windows");
	}

	public String getOsName() {
		return osName;
	}

	public String getOsVersion() {
		return osVersion;
	}

	public String getArchitecture() {
		return architecture;
	}

	public OperativeSystem getOperativeSystemInstance() {
		return operativeSystemInstance;
	}

	public void setOsName(String osName) {
		this.osName = osName;
	}

	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
	}

	public void setArchitecture(String architecture) {
		this.architecture = architecture;
	}

	public void setOperativeSystemInstance(OperativeSystem operativeSystemInstance) {
		this.operativeSystemInstance = operativeSystemInstance;
	}
}
