package com.me.snakevolution;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class SplashScreen extends AbstractScreen
{
	private Texture splashTexture;
	
	public SplashScreen(MyGame game)
	{
		super(game);
		splashTexture = new Texture(Gdx.files.internal("data/splash.png"));
	}
	
	@Override
	public void show()
	{
		super.show();
	}
	
	@Override
	public void render(float delta)
	{
		super.render(delta);
		
		batch.begin();
		batch.draw(splashTexture, (MyGame.screenWidth - splashTexture.getWidth()) / 2,  (MyGame.screenHeight - splashTexture.getHeight()) / 2);
		batch.end();
		
		if (elapsedTime() > 1000 * 3)
			game.setScreen(game.getScreen("menu"));
	}

}
