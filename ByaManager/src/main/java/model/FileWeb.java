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

import org.apache.log4j.Logger;

/**
 * @author  Ks89
 */
public abstract class FileWeb {
	
	/**
	 * @uml.property  name="icon"
	 */
	private BufferedImage icon;
	/**
	 * @uml.property  name="uri"
	 */
	private URI uri;
	/**
	 * @uml.property  name="hash"
	 */
	private String hash; //hash calcolato con SHA1
	/**
	 * @uml.property  name="dimension"
	 */
	private int dimension;
	/**
	 * @uml.property  name="operativeSystem"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="model.OperativeSystem"
	 */
	private List<OperativeSystem> operativeSystem; //contiene architettura, os compatibile ecc...

	
	private static final Logger LOGGER = Logger.getLogger(FileWeb.class);

	public FileWeb() {
		this.hash = null;
		this.dimension=0;
		this.operativeSystem = new ArrayList<OperativeSystem>();
	}
	
	/**
	 * @return
	 * @uml.property  name="icon"
	 */
	public BufferedImage getIcon() {
		return icon;
	}

	/**
	 * @param icon
	 * @uml.property  name="icon"
	 */
	public void setIcon(BufferedImage icon) {
		this.icon = icon;
	}

	/**
	 * @return  percorso URI rappresentante il percorso.
	 * @uml.property  name="uri"
	 */
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
	
	/**
	 * @return  hash String rappresentante l'hash.
	 * @uml.property  name="hash"
	 */
	public String getHash() {
		return hash;
	}

	/**
	 * @param hash  Una String rappresentante l'hash.
	 * @uml.property  name="hash"
	 */
	public void setHash(String hash) {
		this.hash = hash;
	}

	/**
	 * @return  dimensione int rappresentante la dimensione.
	 * @uml.property  name="dimension"
	 */
	public int getDimension() {
		return dimension;
	}

	/**
	 * @param dimension  Un int rappresentante la dimensione.
	 * @uml.property  name="dimension"
	 */
	public void setDimension(int dimension) {
		this.dimension = dimension;
	}
	
	public List<OperativeSystem> getOperativeSystemList() {
		return operativeSystem;
	}

	public void addOperativeSystem(OperativeSystem operativeSystem) {
		this.operativeSystem.add(operativeSystem);
	}
	
	/**
	 * @uml.property  name="fileName"
	 */
	public abstract String getFileName();
}
