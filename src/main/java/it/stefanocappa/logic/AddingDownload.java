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

package it.stefanocappa.logic;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import it.stefanocappa.model.FileWeb;
 
public final class AddingDownload {
	private static AddingDownload instance = new AddingDownload();
	private Map<URI,FileWeb> addingFileWeb;

	private AddingDownload() {
		this.addingFileWeb = new HashMap<URI,FileWeb>();
	}
	
	public static AddingDownload getInstance() {
		return instance;
	}
	
	public void putFileWeb(FileWeb fileWeb) {
		this.addingFileWeb.put(fileWeb.getUri(), fileWeb);
	}
	
	public boolean containsUri(URI uri) {
		return this.addingFileWeb.containsKey(uri);
	}
	
	public FileWeb removeUri(URI uri) {
		return this.addingFileWeb.remove(uri);
	}
	
	public Map<URI, FileWeb> getAddingFileWeb() {
		return addingFileWeb;
	}
}
