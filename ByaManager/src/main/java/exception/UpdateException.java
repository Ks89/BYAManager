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

package exception;

/**
 * Classe per gestire le eccezioni sui sistemi di aggiornamento
 */
public class UpdateException extends Exception {

	private static final long serialVersionUID = 1165041182093008723L;

	/**
	 * @author   Ks89
	 */
	public static enum Causa {/**
	 * @uml.property  name="cORRUPTED_JAR"
	 * @uml.associationEnd  
	 */
	CORRUPTED_JAR, /**
	 * @uml.property  name="iTUNESROWPROBLEM"
	 * @uml.associationEnd  
	 */
	ITUNESROWPROBLEM, /**
	 * @uml.property  name="iTUNESROWFORMAT"
	 * @uml.associationEnd  
	 */
	ITUNESROWFORMAT, /**
	 * @uml.property  name="iMPOSSIBLETOREADITUNESLOCALDB"
	 * @uml.associationEnd  
	 */
	IMPOSSIBLETOREADITUNESLOCALDB, /**
	 * @uml.property  name="iMPOSSIBLETOREADNEWITUNESVERSIONFROMAPPLE"
	 * @uml.associationEnd  
	 */
	IMPOSSIBLETOREADNEWITUNESVERSIONFROMAPPLE}; 
	/**
	 * @uml.property  name="causa"
	 * @uml.associationEnd  
	 */
	private Causa causa;

	/**
	 * Richiama la superclasse.
	 */
	public UpdateException() {
		super();
	}

	/**
	 * Richiama la superclasse.
	 * @param message
	 * @param cause
	 */
	public UpdateException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Richiama la superclasse.
	 * @param message
	 */
	public UpdateException(String message) {
		super(message);
	}

	/**
	 * Richiama la superclasse.
	 * @param cause
	 */
	public UpdateException(Throwable cause) {
		super(cause);
	}
	
	/**
	 * Costruttore che inizializza la causa che ha sollevato l'eccezione.
	 * @param causa Tipo enumerativo che rappresenta la causa che ha sollevato l'eccezione.
	 */
	public UpdateException(Causa causa) {
		this.causa = causa;
	}
	
	/**
	 * @return  Tipo enumerativo che rappresenta la causa che ha soolevato l'eccezione.
	 * @uml.property  name="causa"
	 */
	public Causa getCausa() {
		return causa;
	}

}
