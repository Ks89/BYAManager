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

package it.stefanocappa.uuid;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import it.stefanocappa.model.User;
import it.stefanocappa.notification.Notification;

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

import it.stefanocappa.update.LinkServer;

/**
 * Method to register the uuid generated by {@link uuid.Uuid}
 */
public class RegisteredUuid extends Uuid {
	private static final Logger LOGGER = LogManager.getLogger(RegisteredUuid.class);

	private CloseableHttpClient httpClient;

	/**
	 * Public constructor of this class.
	 */
	public RegisteredUuid() {
		super.generateUuid();
		this.httpClient = HttpClients.createDefault();
	}

	/**
	 * Method that check if the registration is available or not. If not, this method registers the uuid on my server. 
	 */
	public void checkUuidRegistration() {
		try {
			LOGGER.info("checkUuidRegistration() - Method started");
			this.registerUuid();
		} catch (Exception e) {
			//It's impossible to register the uuid
			LOGGER.error("checkUuidRegistration()", e);
			Notification.showNormalOptionPane("logicManagerUuidDangerousError");
			Runtime.getRuntime().exit(0);
		}
		LOGGER.info("checkUuidRegistration() - Method completed");
	}

	/**
	 * Method to check if the uuid is already available on my server. 
	 * If yes, ok. If not httpclient rises an exception that i catch to register the uuid. 
	 * @param serverFilePath String that represents the path (http://....filename.txt) of the uuid file on my server.
	 * @throws IOException Used to know the uuid is not available on my server. 
	 */
	private void isUuidRegistered(String serverFilePath) throws IOException {
		LOGGER.info("isUuidRegistered() - Started"); 
		HttpGet httpGet = new HttpGet(serverFilePath);
		try {
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			this.httpClient.execute(httpGet, responseHandler);
			LOGGER.info("isUuidRegistered() - Available!"); 
		} finally {
			//if httpget is not required, 
			//i stop all connections with 
			httpGet.abort();
			LOGGER.info("isUuidRegistered() - Completed"); 
		}
	}

	/**
	 * Method  to register the uuid. Here i catch the IOException of isUuidRegistered, 
	 * but i throw another IOException if there are other problems.
	 * @throws IOException If there are errors. I catch this exception in {@link #checkUuidRegistration()}
	 */
	private void registerUuid() throws IOException {
		Path percorso = User.getInstance().getDataPath();
		LOGGER.info("registerUuid() - PercorsoDati = " + percorso.toString()); 
		try {
			isUuidRegistered(LinkServer.UUIDLINKKS89 + super.getUuid() + ".txt");
		} catch(IOException e) { 
			// UUID non registered. I must register this new uuid here.
			LOGGER.info("registerUuid() - Catched exception, registering uuid...");

			//i use a php file on my server to upload this txt.
			HttpPost httpPost = new HttpPost(LinkServer.UUIDLINKKS89 + "upload.php");

			//create a blank uuid's txt file
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
				LOGGER.info("registerUuid() - entity consumed and httpclient closed");
			}
		} finally {
			LOGGER.info("registerUuid() - Cleaning local files");
			
			//now i remove the uuid file
			Path path = Paths.get(User.getInstance().getDataPath().toString() + System.getProperty("file.separator") + super.getUuid() + ".txt");
			Files.deleteIfExists(path);
			
			LOGGER.info("registerUuid() - Method completed");
		}
	}
}