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

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * Classe che si occupa di unire i file .part in un solo .ipsw 
 * verificando che le parti siano concatenate nella giusta sequenza 
 * e a cancellare i .part che ora non servono piu'
 */
public class FilesMerger {
	private static final Logger LOGGER = LogManager.getLogger(FilesMerger.class);
	/**
	 * @uml.property  name="downloadTempPath"
	 */
	private Path downloadTempPath;
	/**
	 * @uml.property  name="shaFiledownloadPath"
	 */
	private Path shaFiledownloadPath;
	/**
	 * @uml.property  name="nomeFile"
	 */
	private String nomeFile;

	/**
	 * Metodo per unire le parti del download.
	 * @param nomeFile String che rappresenta il nome del file associato al Download.
	 */
	public FilesMerger(Path downloadPath, String nomeFile) {
		this.nomeFile = nomeFile;
		this.downloadTempPath = downloadPath.resolve("temp");
		this.shaFiledownloadPath = downloadPath.resolve(nomeFile + ".sha");
	}

	/**
	 * Metodo che si occupa di concatenare le parti, richiamato da unisciParti.
	 * @param array di File che rappresenta la lista dei file ordinata per sequenza.
	 * @throws IOException
	 */
	private void mergeParts(Path[] pathPartsList) {
		FileChannel readChannel;
		long byteTransferred;

		try (
				FileChannel writeChannel = FileChannel.open(shaFiledownloadPath,StandardOpenOption.WRITE,StandardOpenOption.CREATE)
				) {
			for(Path pathPart : pathPartsList) {	
				readChannel = FileChannel.open(pathPart,StandardOpenOption.READ);
				byteTransferred = readChannel.transferTo(0, Files.size(pathPart), writeChannel);

				readChannel.close();

				if(byteTransferred!=Files.size(pathPart)) {
					throw new IOException("Byte da trasferire: " + Files.size(pathPart) + " , trasferiti: " + byteTransferred);
				}
			}
		} catch (IOException e) {
			LOGGER.error("concatenaParti() - IOException= " + e);
		}
	}

	/**
	 * Metodo che si occupa di creare l'ipsw verficando l'ordine della sequenza di concatenazione delle parti,
	 * per poi chiamare concatenaParti() e per finire il rimuoviParti() che si occupa di cancellare i file
	 * che non servono piu' a nulla, cioe' tutti i .part.
	 */
	public void createCompleteIpsw() {
		Path[] pathPartsList = new Path[4];
		List<Path> pathList = FileList.getFileList(downloadTempPath); //lista delle parti cosi' come vengono rilevate, senza ordine preciso

		int temp; //variabile usata per memorizzare il numero del file .part in modo temporaneo

		for(Path pathPart: pathList) {
			if(pathPart!=null && pathPart.endsWith(nomeFile+".part"+pathPart.toString().charAt(pathPart.toString().length()-1)) 
					&& pathPart.getFileName().toString().contains(".part")) {					
				temp = Integer.parseInt(pathPart.getFileName().toString().replace(nomeFile + ".part", ""));
				pathPartsList[temp-1] = pathPart;
			}	
		}

		//qui ho ottenuto la lista ordinata delle parti, cosi' la passo al concatenatore delle parti
		this.mergeParts(pathPartsList);
	}

	/**
	 * Metodo che si occupa di rimuovere i file nella cartella dei
	 * download temporanei quando il nome del file contiene quello passato come
	 * parametro durante la creazione di questa classe.
	 */
	public void removeParts() {
		//cancella le parti del file scaricato
		List<Path> fileList = FileList.getFileList(downloadTempPath);
		for(Path partPath : fileList) {
			if(partPath.endsWith(nomeFile)) {
				try {
					Files.deleteIfExists(partPath);
				} catch (IOException e) {
					LOGGER.error("Errore durante la rimozione di: " + partPath.toString());
				}
			}
		}
	}


	//	private void concatenaParti2(File[] listaFileParti) {
	//	FileChannel readChannel;
	//	long byteTransferred;
	//
	//	try (
	//			FileChannel writeChannel = FileChannel.open(listaFileParti[0].toPath(),StandardOpenOption.WRITE, StandardOpenOption.APPEND);
	//	) {
	//		for(int i=1; i<4; i++) {
	//			readChannel = FileChannel.open(listaFileParti[i].toPath(),StandardOpenOption.READ);
	//			byteTransferred = readChannel.transferTo(0, listaFileParti[i].length(), writeChannel);
	//
	//			readChannel.close();
	//
	//			if(byteTransferred!=listaFileParti[i].length()) {
	//				throw new IOException("Byte da trasferire: " + listaFileParti[i].length() + " , trasferiti: " + byteTransferred);
	//			}
	//		}
	//	} catch (IOException e) {
	//		LOGGER.error("concatenaParti() - IOException= " + e);
	//	}
	//}


}
