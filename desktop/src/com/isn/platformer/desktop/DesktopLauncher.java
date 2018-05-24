package com.isn.platformer.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.isn.platformer.Platformer;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = Platformer.SCREEN_WIDTH * 2;
		config.height = Platformer.SCREEN_HEIGHT * 2;
		config.resizable = false;
		new LwjglApplication(new Platformer(), config);
	}
}
