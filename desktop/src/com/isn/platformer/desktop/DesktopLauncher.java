package com.isn.platformer.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.isn.platformer.Platformer;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
		//On définit la taille de la fenêtre
		config.width = Platformer.SCREEN_WIDTH * 2;
		config.height = Platformer.SCREEN_HEIGHT * 2;

		//On affiche une image dans la fenêtre
		config.addIcon("sprites//stand.png", Files.FileType.Internal);
		
		//On lance l'application avec nos configurations
		new LwjglApplication(new Platformer(), config);
	}
}
