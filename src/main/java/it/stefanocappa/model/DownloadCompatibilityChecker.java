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

import java.util.List;

public final class DownloadCompatibilityChecker {

	private DownloadCompatibilityChecker() {}

	public static boolean isCompatibileWithMyOs(List<OperativeSystem> downloadOperativeSystem, OperativeSystem myPperativeSystem) {
		for(OperativeSystem operativeSystem : downloadOperativeSystem) {
			if(myPperativeSystem instanceof Windows) {
				return operativeSystem instanceof Windows;
			}
			if(myPperativeSystem instanceof MacOsx) {
				return operativeSystem instanceof MacOsx;
			} 
			if(myPperativeSystem instanceof Linux) {
				return operativeSystem instanceof Linux;
			}
		}
		return false;
	}
}
