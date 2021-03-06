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

package it.stefanocappa.logic;

import it.stefanocappa.logic.listener.AddFromListListener;
import it.stefanocappa.logic.listener.CheckByamUpdateListener;
import it.stefanocappa.logic.listener.CheckDbUpdateListener;
import it.stefanocappa.logic.listener.CompleteExitListener;
import it.stefanocappa.logic.listener.SettingsListener;
import it.stefanocappa.logic.listener.menuitem.AboutListener;
import it.stefanocappa.logic.listener.menuitem.DonateListener;
import it.stefanocappa.logic.listener.menuitem.GoToByaSiteListener;
import it.stefanocappa.logic.listener.menuitem.GoToKsSiteListener;
import it.stefanocappa.logic.listener.menuitem.ReduceToIconListener;
import it.stefanocappa.logic.listener.menuitem.RestoreToDefaultListener;
import it.stefanocappa.logic.listener.systemtray.HideFrameListener;
import it.stefanocappa.gui.MainFrame;
import it.stefanocappa.gui.state.StateButton;
import it.stefanocappa.gui.state.StateMenuItem;
import it.stefanocappa.gui.systemtray.SystemTrayManager;

//creo graphicmanager e gli passo gli oggetti che voglio. Questo serve solo per poter chiamare i metodi NON STATICI.
//per i metodi static non e' necessaria nessuna inizializzazione.
//addirittura per i metodi static basta fare GraphicManager.metodoStatico
public final class ListenerRegister {
	private static ListenerRegister instance = new ListenerRegister();

	private ListenerRegister() {}

	public void registerListerenSystemTray() {
		SystemTrayManager.getInstance().getEsciTotalmenteItem().addActionListener(new CompleteExitListener());
		SystemTrayManager.getInstance().getRipristinaItem().addActionListener(new HideFrameListener());	
	}

	public void registerItemMenuListener() {
		StateMenuItem.getFileRiduciMenuItem().addActionListener(new ReduceToIconListener());
		StateMenuItem.getFileExitMenuItem().addActionListener(new CompleteExitListener());
		StateMenuItem.getReinizializzaMenuItem().addActionListener(new RestoreToDefaultListener());
		StateMenuItem.getSettingsMenuItem().addActionListener(new SettingsListener());
		StateMenuItem.getAboutMenuItem().addActionListener(new AboutListener());
		StateMenuItem.getCheckDBUpdateMenuItem().addActionListener(new CheckDbUpdateListener());
		StateMenuItem.getCheckByamUpdateMenuItem().addActionListener(new CheckByamUpdateListener());
		StateMenuItem.getDonazioneMenuItem().addActionListener(new DonateListener());
		StateMenuItem.getKsBlogMenuItem().addActionListener(new GoToKsSiteListener());
		StateMenuItem.getByaMenuItem().addActionListener(new GoToByaSiteListener());
	}

	public void registerListener() {
		MainFrame.getAddLista().addActionListener(new AddFromListListener());
		StateButton.getRefreshDBButton().addActionListener(new CheckDbUpdateListener());
		StateButton.getRefreshBYAMButton().addActionListener(new CheckByamUpdateListener());
	}

	public static ListenerRegister getInstance() {
		return instance;
	}
}
