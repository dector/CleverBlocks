package com.me.cleverblocks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.TimeUtils;

public class MultiScreen extends AbstractScreen
{
	private TextureRegion multiTexture;
	private long startTime;

	public MultiScreen(MyGame game)
	{
		super(game);
		Texture multiTex = new Texture(Gdx.files.internal("data/multi.png"));
        multiTexture = new TextureRegion(multiTex, 800, 480);
	}

	@Override
	public void show()
	{
		super.show();

		startTime = TimeUtils.millis();
	}

	@Override
	public void render(float delta)
	{
		super.render(delta);
		if (TimeUtils.millis() - startTime > 1000)
		{
			if (Gdx.input.isButtonPressed(Buttons.LEFT) || Gdx.input.isKeyPressed(Keys.ESCAPE) || Gdx.input.isKeyPressed(Keys.SPACE) || Gdx.input.isKeyPressed(Keys.ENTER))
				game.setScreen(game.getScreen("menu"));
		}

		batch.begin();
		batch.draw(multiTexture, 0, 0);
		batch.end();
	}
}
