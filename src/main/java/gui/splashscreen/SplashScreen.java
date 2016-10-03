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

package gui.splashscreen;


import gui.kcomponent.KColors;
import gui.kcomponent.KPanel;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.net.URL;

import javax.swing.JPanel;

public class SplashScreen implements ImageObserver {
	private static SplashScreen instance = new SplashScreen();
	/**
	 * @uml.property  name="image"
	 */
	private Image image ;
	/**
	 * @uml.property  name="label"
	 */
	private Label label = new Label ("Loading..." , Label.CENTER );
	/**
	 * @uml.property  name="panel"
	 * @uml.associationEnd  
	 */
	private JPanel panel = new KPanel();
	/**
	 * @uml.property  name="frame"
	 */
	private Frame frame ;
	/**
	 * @uml.property  name="splashTime"
	 */
	private long splashTime = 0;

	public static SplashScreen getInstance() {
		return instance;
	}
	
	public SplashScreen (String filename) {
		setImage(filename) ;
	} 

	public SplashScreen (URL url) {
		setImage(url) ;
	} 

	public SplashScreen() {
		//costruttore vuoto
	}

	public final void setImage (String filename) {
		image=Toolkit.getDefaultToolkit( ).getImage(filename) ;
	}

	public final void setImage (URL url) {
		image = Toolkit.getDefaultToolkit( ).getImage(url) ;
	}	

	public void splash (String filename){
		splash(Toolkit.getDefaultToolkit( ).getImage(filename)) ;
	}

	public void splash (URL url){
		splash(Toolkit.getDefaultToolkit( ).getImage(url)) ;
	}


	public void splash(){
		if (image!=null) {
			splash(image);
		} else {
			splash (Toolkit.getDefaultToolkit( ).getImage("splashbeta1.png"));
		}
	} 


	public void splash (Image img) {
		image = img ;
		frame = new Frame();
		frame.setUndecorated(true) ;

		if (!Toolkit.getDefaultToolkit().prepareImage(image,-1,-1,this)) {
			return;
		}
		splashScreen();
	}

	@Override
	public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
		boolean allbits = infoflags == ImageObserver . ALLBITS ;
		if( allbits ) {
			splashScreen() ;
		}
		return !allbits;
	}

	private void splashScreen() {
		if (frame == null) {
			return;
		}
		final int width = image.getWidth(null) ;
		final int height = image.getHeight(null);

		Canvas canvas = new Canvas() {
			private static final long serialVersionUID = 2501900998022630894L;

			public void update(Graphics g) {
				paint(g);
			}

			public void paint(Graphics g) {
				g.drawImage(image,0,0,this);
			}

			public Dimension getPreferredSize() {
				return new Dimension(width, height);
			}
		};

		frame.add(canvas,BorderLayout.CENTER);
		
		//ancora da migliorare
		//TODO MIGLIORARE BARRA SPLASHSCREEN
		label.setBackground(KColors.getNero());
		label.setForeground(KColors.getVerde());
		panel.setDoubleBuffered(true);
		panel.setLayout(new BorderLayout());
		panel.add(label, BorderLayout.CENTER);
		
		
		frame.add(panel,BorderLayout.SOUTH);
		frame.pack();

		Dimension screenSize = Toolkit.getDefaultToolkit( ).getScreenSize();
		Dimension frameSize = frame.getSize();
		frame.setLocation((screenSize.width - frameSize.width ) >> 1, (screenSize.height - frameSize.height) >> 1); 
		frame.setVisible(true);
		splashTime = System . currentTimeMillis();
	}


	public void showStatus(String s) {
		label.setText(s);
	}

	public void waitForSplash ( ) {
		MediaTracker mt = new MediaTracker(frame) ;
		mt.addImage(image,0) ;
		try{
			mt.waitForID(0) ;
		} catch (InterruptedException ie) {
			
		}
	}

	public void waitForSplash ( long ms ) {
		MediaTracker mt = new MediaTracker ( frame ) ;
		mt.addImage(image,0);
		try{
			mt.waitForID(0,ms);
		} catch (InterruptedException ie) {
			
		}
	}


	public void delayForSplash ( ) {
		int cpus = Runtime.getRuntime().availableProcessors();
		switch (cpus) {
		case 0 :
		case 1 :
			waitForSplash();
			break;
		case 2 :
		case 3 :
		default :
			waitForSplash(1000/cpus);
		}
	}


	public void splashFor (int ms ) {
		if ( splashTime == 0 ) {
			return ;
		}
		long splashDuration = System.currentTimeMillis( ) - splashTime ;

		if(splashDuration<ms) {
			try{
				Thread.sleep(ms - splashDuration) ;
			} catch (InterruptedException ie ) {
				//TODO
			}
		}
	}

	public void dispose () {
		frame.remove(label);
		frame.dispose();
		frame = null ;
		splashTime = 0 ;
	}
}