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

public class Windows extends OperativeSystem implements ItunesCompatibileOs, AllCompatibleOs {
	
	public Windows(String platform) {
		this.generateExecExtension();
		this.generateArchitecture(platform);
		
		//questi 2 metodi dipendono dal generate Architecture, 
		//perche' se l'arch non e' settata essi non possono fare il get del parametro richiesto
		//quindi, tenerli sempre cosi' per ultimi
		this.generateShortName(platform);
		this.generateCompleteName(platform);
		
	}
	
	@Override
	public final void generateCompleteName(String platform) {
		super.setCompleteName("Windows " + super.getArchitecture() + "bit");
	}

	@Override
	public final void generateShortName(String platform) {
		super.setShortName("Win" + super.getArchitecture());

	}

	@Override
	public final void generateArchitecture(String platform) {
		if(platform.equals("32")) {
			super.setArchitecture("32");
		} else {
			super.setArchitecture("64");			
		}
	}

	@Override
	public final void generateExecExtension() {
		super.setExecExtension(".exe");
	}
}