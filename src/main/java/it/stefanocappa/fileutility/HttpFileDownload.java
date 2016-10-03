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

package it.stefanocappa.fileutility;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public final class HttpFileDownload {
	private static final Logger LOGGER = LogManager.getLogger(HttpFileDownload.class);

	private HttpFileDownload() {}
	
	/**
	 * Scarica un generico file dato il suo nome e link al server.
	 * Usato sia per scaricare i database, sia per il version.xml ecc...
	 * @param nomeFile Nome del file
	 * @param httpLink String che rappresenta il link al file da scaricare
	 * @throws URISyntaxException
	 * @throws IOException 
	 */
	public static void httpFileDownload(Path downloadPath, String httpLink) throws URISyntaxException,IOException {
		LOGGER.info("fileDownload() - Avviato metodo con link http: " + httpLink + " e path file: " + downloadPath.toString());
		int bufferSize = 2048;

		try (
				FileChannel fileChannel = FileChannel.open(downloadPath,StandardOpenOption.CREATE, StandardOpenOption.APPEND);
				BufferedInputStream in = new BufferedInputStream(new URI(httpLink).toURL().openStream())
				) {
			
			FileLock lock = fileChannel.tryLock();

			byte[] byteArray = new byte[bufferSize];
			ByteBuffer bb = ByteBuffer.allocateDirect(bufferSize);
			int read = in.read(byteArray);

			while (read != -1) {
				if (read < bufferSize) {
					byte[] arrayTemp = byteArray.clone();
					byteArray = new byte[read];
					System.arraycopy(arrayTemp, 0, byteArray, 0, read); //consigliato da sonar al posto di un for
					bb = ByteBuffer.allocateDirect(read);
				}	

				bb.clear();

				if(lock!=null && lock.isValid() && fileChannel.isOpen()) {
					bb.put(byteArray);
					bb.flip();
					fileChannel.write(bb);
					lock.release();
					lock.close();
					lock = fileChannel.tryLock();
				}
				
				read = in.read(byteArray);
			}
			LOGGER.info("fileDownload() - Terminato metodo");
		}
	}
}
