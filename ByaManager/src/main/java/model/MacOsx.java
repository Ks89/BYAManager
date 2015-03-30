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

public class MacOsx extends OperativeSystem implements ItunesCompatibileOs, UnixCompatibileOs, AllCompatibleOs {

	public MacOsx(String platform) {
		this.generateExecExtension();
		this.generateArchitecture(platform);
		this.generateShortName(platform);
		this.generateCompleteName(platform);

	}

	@Override
	public final void generateCompleteName(String platform) {
		super.setCompleteName("Mac OSX");
	}

	@Override
	public final void generateShortName(String platform) {
		super.setShortName("Mac");

	}

	@Override
	public final void generateArchitecture(String platform) {
		super.setArchitecture("64");
	}

	@Override
	public final void generateExecExtension() {
		super.setExecExtension(".dmg");
	}
}