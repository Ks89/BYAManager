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
	private int dimension;
	private List<OperativeSystem> operativeSystem; //contiene architettura, os compatibile ecc...

	
	private static final Logger LOGGER = LogManager.getLogger(FileWeb.class);

	public FileWeb() {
		this.hash = null;
		this.dimension=0;
		this.operativeSystem = new ArrayList<OperativeSystem>();
	}
	
	public BufferedImage getIcon() {
		return icon;
	}

	public void setIcon(BufferedImage icon) {
		this.icon = icon;
	}

	public URI getUri() {
		return uri;
	}

	/**
	 * Metodo per impostare il percorso dopo averlo validato.
	 * @param percorso Una String rappresentante il percorso.
	 */
	public void setPercorso(String percorso) {
		try {
			if (percorso.toLowerCase().startsWith("http://")) {
				this.uri = new URI(percorso);
			} else {
				LOGGER.info("setPercorso() - Percorso non http://=" + percorso);
			}
		} catch (URISyntaxException e) {
			LOGGER.error("setPercorso() - URISyntaxException=", e);
		}

	}
	
	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public int getDimension() {
		return dimension;
	}

	public void setDimension(int dimension) {
		this.dimension = dimension;
	}
	
	public List<OperativeSystem> getOperativeSystemList() {
		return operativeSystem;
	}

	public void addOperativeSystem(OperativeSystem operativeSystem) {
		this.operativeSystem.add(operativeSystem);
	}
	
	public abstract String getFileName();
}
