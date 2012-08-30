package com.me.cleverblocks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;

public class IntroScreen extends AbstractScreen
{
	private Texture introTexture;

	public IntroScreen(MyGame game)
	{
		super(game);

		introTexture = new Texture(Gdx.files.internal("data/intro.png"));
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
