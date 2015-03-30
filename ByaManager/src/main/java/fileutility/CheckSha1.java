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


package fileutility;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

import org.apache.commons.codec.digest.DigestUtils;

/**
 *	Classe di appoggio per il metodo contenuto in Download.
 *	Questa classe fornisce 2 metodi, uno calcola lo SHA1 di un file da in ingresso
 *	e lo restituisce in String esadecimale, l'altro serve per confrontare se due
 *	SHA dati in ingresso sono uguali o no, tenendo conto anche dell'opzione "non presente".
 */
public final class CheckSha1 {

	private CheckSha1(){}

	public static String getSha1(Path pathToCheck) throws IOException {
		try (
				InputStream is = new FileInputStream(pathToCheck.toFile().getAbsolutePath())
				) {
			return DigestUtils.shaHex(is).toUpperCase();
		} catch (IOException e) {
			throw e;
		}
	}

	public static boolean isCorrect(String hashFileScaricato, String hashCorretto) {
		return hashCorretto.equals(hashFileScaricato) || hashCorretto.equals("non presente");
	}

}
