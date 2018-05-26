package com.isn.platformer.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.isn.platformer.Platformer;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
		//On d�finit la taille de la fen�tre
		config.width = Platformer.SCREEN_WIDTH * 2;
		config.height = Platformer.SCREEN_HEIGHT * 2;

		//On affiche une image dans la fen�tre
		config.addIcon("sprites//stand.png", Files.FileType.Internal);
		
		//On lance l'application avec nos configurations
		new LwjglApplication(new Platformer(), config);
	}
}
