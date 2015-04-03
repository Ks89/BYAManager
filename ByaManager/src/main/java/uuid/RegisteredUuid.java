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

package uuid;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import model.User;
import notification.Notification;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import update.LinkServer;

/**
 * Metodo per registrare l'uuid sul server.
 */
public class RegisteredUuid extends Uuid {
	private static final Logger LOGGER = LogManager.getLogger(RegisteredUuid.class);

	private CloseableHttpClient httpClient;

	/**
	 * Classe che richiama la superclasse, inizializza le variabili e crea l'uuid (dalla superclasse).
	 */
	public RegisteredUuid() {
		super.creaUuid();
		this.httpClient = HttpClients.createDefault();
	}

	/**
	 * Metodo che verifica la registrazione e in caso non sia presente provvedere anche ad eseguirla.
	 */
	public void checkUuidRegistration() {
		try {
			LOGGER.info("checkUuidRegistration() - Avviato metodo");
			this.registraUuid();
		} catch (Exception e) {
			Notification.showNormalOptionPane("logicManagerUuidDangerousError");
			LOGGER.error("checkUuidRegistration() - Exception= " + e);
			System.exit(1);
		}
		LOGGER.info("checkUuidRegistration() - Terminato metodo");
	}

	/**
	 * Metodo per verificare la presenza del file uuid sul server. Se presente vuol dire che il programma
	 * e' gia' stato eseguito in precedenza, altrimenti lancia un eccezione e nel metodo registraUuid() 
	 * viene gestito l'upload di tale file.
	 * @param percorso String che rappresenta il percorso sul server.
	 * @throws IOException
	 */
	private void verificaPresenzaUuidRegistrato(String percorso) throws IOException {
		LOGGER.info("verificaPresenzaUuidRegistrato() - Avviato"); 
		HttpGet httpGet = new HttpGet(percorso);
		try {
			// Crea un response handler
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			this.httpClient.execute(httpGet, responseHandler);
			LOGGER.info("verificaPresenzaUuidRegistrato() - Presente!"); 
		} finally {
			// Quando l'istanza di httpget non e' piu' necessaria,
			// fermo la connessione per assicurarmi di deallocare tutte
			// le risorse in modo immediato con un agort
			httpGet.abort();
			LOGGER.info("verificaPresenzaUuidRegistrato() - terminato"); 
		}
	}

	/**
	 * Metodo per registrare l'uuid.
	 * @throws IOException
	 */
	private void registraUuid() throws IOException {
		Path percorso = User.getInstance().getDataPath();
		LOGGER.info("registraUuid() - PercorsoDati = " + percorso.toString()); 
		try {
			verificaPresenzaUuidRegistrato(LinkServer.UUIDLINKKS89 + super.getUuid() + ".txt");
		} catch(IOException e) { // perche' UUID ancora non registrato
			LOGGER.info("registraUuid() - Eccezione catchata, non presente, registro l'uuid");

			HttpPost httpPost = new HttpPost(LinkServer.UUIDLINKKS89 + "upload.php");

			//creo il file uuid per poi uploadarlo (il file e' vuoto)
			Path filePath = percorso.resolve(super.getUuid() + ".txt");
			Files.createFile(filePath);

			MultipartEntityBuilder mpEntity = MultipartEntityBuilder.create();
			mpEntity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			ContentBody cbFile = new FileBody(filePath.toFile(), ContentType.TEXT_PLAIN);
			mpEntity.addPart("userfile", cbFile);

			httpPost.setEntity(mpEntity.build());
			
			HttpResponse response = this.httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();

			if (entity != null) {
				EntityUtils.consume(entity);
				httpClient.close();
				filePath = null;
				LOGGER.info("entity consumata e DefaultConnectionManager terminato per uuidregistrato");
			}
		} finally {
			LOGGER.info("registraUuid() - pulizia file locali");
			//dopo qualunque operazione cancella il file uuid (col .txt)
			//TODO .delete puo' lanciare IOExecption in caso di problemi che viene rilasciata al metodo che ha chimato questo e  gestita
			//come un errore grave. Non e' proprio corretto, ma per ora si puo' lasciare cosi'.
			Path path = Paths.get(User.getInstance().getDataPath().toString() + System.getProperty("file.separator") + super.getUuid() + ".txt");
			Files.deleteIfExists(path);
			LOGGER.info("registraUuid() - pulizia file locali terminata con successo");
		}
	}
}