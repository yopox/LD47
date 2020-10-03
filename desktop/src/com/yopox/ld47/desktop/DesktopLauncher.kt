package com.yopox.ld47.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.yopox.ld47.LD47;
import com.yopox.ld47.screens.Screen;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = (int) Screen.WIDTH;
		config.height = (int) Screen.HEIGHT;
		new LwjglApplication(new LD47(), config);
	}
}
