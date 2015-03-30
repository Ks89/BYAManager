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

package logica.listener.popupitem;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URI;

import javax.swing.AbstractAction;

import org.apache.log4j.Logger;



public class DownloadInBrowserListener extends AbstractAction {
	private static final long serialVersionUID = 8923186438221476133L;
	private static final Logger LOGGER = Logger.getLogger(DownloadInBrowserListener.class);
	private URI uri;
	
	public DownloadInBrowserListener(URI uri) {
		this.uri = uri;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		//apro il link del file nel browser predefinito
		try {
			Desktop.getDesktop().browse(uri);
		} catch (IOException e1) {
			LOGGER.error("avviaProgramma() - urlItem - IOException= " + e1);
		}
	}

}
