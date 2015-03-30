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

package logica.listener.menuitem;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.AbstractAction;

import preferences.Links;

public class GoToKsSiteListener extends AbstractAction {
	private static final long serialVersionUID = 1003812130275799754L;

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			//apre la finestra del browser col sito
			Desktop.getDesktop().browse(new URI(Links.ksBlogHomePage));
		} catch (IOException e1) {
			//					LOGGER.error("avviaProgramma() - IOException= " + e1);
		} catch (URISyntaxException e1) {
			//					LOGGER.error("avviaProgramma() - URISyntaxException= " + e1);
		}
	}

}
