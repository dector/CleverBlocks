package com.me.cleverblocks;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.me.cleverblocks.MyGame;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Clever Blocks";
		cfg.useGL20 = true;
		cfg.resizable = false;
		cfg.width = 800;
		cfg.height = 480;
		
		new LwjglApplication(new MyGame(), cfg);
	}
}
