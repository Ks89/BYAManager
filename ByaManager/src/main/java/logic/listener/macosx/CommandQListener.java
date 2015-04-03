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

package logic.listener.macosx;

//import logica.LogicManager;

import com.apple.eawt.QuitHandler;
import com.apple.eawt.QuitResponse;
import com.apple.eawt.AppEvent.QuitEvent;

public class CommandQListener implements QuitHandler {

	public void handleQuitRequestWith(QuitEvent quitEvent, QuitResponse quitResponse) {
		//mette i download in pausa e poi termina l'applicazione
//		LogicManager.getInstance().mettiPausaPreChiusura();
		quitResponse.performQuit();
	}

}
