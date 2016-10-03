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

import java.awt.image.BufferedImage;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;


import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * @author  Stefano Cappa
 */
public abstract class FileWeb {
	private BufferedImage icon;
	private URI uri;
	private String hash; //hash calcolato con SHA1
	private int size;
	private List<OperativeSystem> operativeSystemList; //contiene architettura, os compatibile ecc...

	
	private static final Logger LOGGER = LogManager.getLogger(FileWeb.class);

	public FileWeb() {
		this.hash = null;
		this.size=0;
		this.operativeSystemList = new ArrayList<OperativeSystem>();
	}

	/**
	 * Metodo per impostare il percorso dopo averlo validato.
	 * @param percorso Una String rappresentante il percorso.
	 */
	public void setPercorso(String percorso) {
		try {
			this.uri = new URI(percorso);
		} catch (URISyntaxException e) {
			LOGGER.info("setPercorso() - Percorso non http://=" + percorso);
			LOGGER.error("setPercorso() - URISyntaxException=", e);
		}

	}
	
	public void addOperativeSystem(OperativeSystem operativeSystem) {
		this.operativeSystemList.add(operativeSystem);
	}
	
	public abstract String getFileName();

	public BufferedImage getIcon() {
		return icon;
	}

	public URI getUri() {
		return uri;
	}

	public String getHash() {
		return hash;
	}

	public int getSize() {
		return size;
	}

	public List<OperativeSystem> getOperativeSystemList() {
		return operativeSystemList;
	}

	public void setIcon(BufferedImage icon) {
		this.icon = icon;
	}

	public void setUri(URI uri) {
		this.uri = uri;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public void setOperativeSystemList(List<OperativeSystem> operativeSystemList) {
		this.operativeSystemList = operativeSystemList;
	}
}
