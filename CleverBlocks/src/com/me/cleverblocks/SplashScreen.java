package com.me.cleverblocks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class SplashScreen extends AbstractScreen
{
	private TextureRegion splashTexture;
	
	public SplashScreen(MyGame game)
	{
		super(game);
		Texture splashTex = new Texture(Gdx.files.internal("data/splash.png"));
		splashTexture = new TextureRegion(splashTex, 800, 480);
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
		batch.draw(splashTexture, (MyGame.screenWidth - splashTexture.getRegionWidth()) / 2,
                (MyGame.screenHeight - splashTexture.getRegionHeight()) / 2);
		batch.end();
		
		if (elapsedTime() > 1000 * 3)
			game.setScreen(game.getScreen("menu"));
	}

}
