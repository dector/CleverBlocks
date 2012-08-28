package com.me.snakevolution;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;

public class MyGame extends Game
{
	public static float gameWidth = 1000;
	public static float gameHeight = 1000;
	public static float screenWidth = 800;
	public static float screenHeight = 480;
	public static float maxRatio = 0.2f;
	public static float scale = 0.1f;
	public static float scalingSpeed = 2.0f;
	public static int tileSize = 1024;
	public static Texture redBlockTexture;
	public static Texture yellowBlockTexture;
	public static Texture blueBlockTexture;
	public static Texture greenBlockTexture;
	public static String endMessage = "YOU DIED";
	public static float spawningSpeed = 3.0f;
	public static float lifeCooldown = 0.1f;

	@Override
	public void resize(int width, int height)
	{
	}

	@Override
	public void pause()
	{
	}

	@Override
	public void resume()
	{
	}

	@Override
	public void create()
	{
		setScreen(getScreen("splash"));
	}
	
	public Screen getScreen(String name)
	{
		if (name == "menu")
			return new MenuScreen(this);
		if (name == "intro")
			return new IntroScreen(this);
		if (name == "splash")
			return new SplashScreen(this);
		if (name == "solo")
			return new SoloScreen(this);
		if (name == "multi")
			return new MultiScreen(this);
		else
			return getScreen("menu");
	}
	
	public void setScreen(Screen screen)
	{
		super.setScreen(screen);
	}
}
