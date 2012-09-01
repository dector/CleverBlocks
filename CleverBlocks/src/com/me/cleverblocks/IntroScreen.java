package com.me.cleverblocks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class IntroScreen extends AbstractScreen
{
	private TextureRegion introTexture;

	public IntroScreen(MyGame game)
	{
		super(game);

		Texture introTex = new Texture(Gdx.files.internal("data/intro.png"));
		introTexture = new TextureRegion(introTex, 800, 480);
	}

	public void render(float delta)
	{
		super.render(delta);

		if (elapsedTime() > 1000)
		{
			if (Gdx.input.isButtonPressed(Buttons.LEFT) || Gdx.input.isTouched()|| Gdx.input.isKeyPressed(Keys.ESCAPE) || Gdx.input.isKeyPressed(Keys.SPACE) || Gdx.input.isKeyPressed(Keys.ENTER))
				game.setScreen(game.getScreen("menu"));
		}

		batch.begin();
		batch.draw(introTexture, 0, 0);
		batch.end();
	}

}
