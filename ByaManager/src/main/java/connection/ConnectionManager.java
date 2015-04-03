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
package connection;

import java.io.IOException;

import org.apache.http.HttpHost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

/**
 *	Class that represents the connection manager for Downloads and Processes
 */
public class ConnectionManager {
	
	private PoolingHttpClientConnectionManager poolingConnManager;
	private CloseableHttpClient httpclient;
	private HttpHost proxy = null;
	
	private static ConnectionManager instance = new ConnectionManager();

	public static ConnectionManager getInstance() {
		return instance;
	}	
	
	private ConnectionManager() {}
	
	/**
	 * Metodo per inizializzare il connectionManager.
	 */
	public void initConnectionManager(boolean proxyActivationState, String proxyServer, String proxyPort) {
		// Create an HttpClient with the PoolingHttpClientConnectionManager.
        // This connection manager must be used if more than one thread will
        // be using the HttpClient.
		poolingConnManager = new PoolingHttpClientConnectionManager();
		poolingConnManager.setMaxTotal(100);
		poolingConnManager.setDefaultMaxPerRoute(20);
		
		if(proxyActivationState) {
			//esempio proxy che funziona "195.246.54.5" 8080 da http://proxylist.hidemyass.com/
			//verificare la porta, che sia http, e soprattutto la velocita' che permette.
			//sceglierne uno veloce e soprattutto, avere pazienza nell'avvio del download, perche' sara' comunque
			//piu' lento
            proxy = new HttpHost(proxyServer, Integer.parseInt(proxyPort), "http");
            httpclient = HttpClients.custom().setConnectionManager(poolingConnManager).setProxy(proxy).build();
		} else {
			httpclient = HttpClients.custom().setConnectionManager(poolingConnManager).build();
		}
	}

	public void closeConnectionManagerResources() throws IOException {
		System.out.println("closing http client");
		this.httpclient.close();
		this.poolingConnManager.shutdown();
	}
	
	public PoolingHttpClientConnectionManager getPoolingConnManager() {
		return poolingConnManager;
	}
	public CloseableHttpClient getHttpclient() {
		return httpclient;
	}

	public HttpHost getProxy() {
		return proxy;
	}
}
