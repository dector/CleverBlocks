package com.me.cleverblocks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class MenuScreen extends AbstractScreen
{
	private TextureRegion menuTexture;
	private Rectangle soloButton;
	private Rectangle multiButton;
	private Rectangle introButton;
	public MenuScreen(MyGame game)
	{
		super(game);

		Texture menuTex = new Texture(Gdx.files.internal("data/menu.png"));
		menuTexture = new TextureRegion(menuTex, 800, 480);
		// buttonTexture = new Texture(Gdx.files.internal("data/button.png"));
		introButton = new Rectangle(287, MyGame.screenHeight - 218, 228, 80);
		soloButton = new Rectangle(295, MyGame.screenHeight - 300, 205, 71);
		multiButton = new Rectangle(200, MyGame.screenHeight - 397, 394, 72);
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

		if (elapsedTime() > 1000 * 0.5f)
		{
			if (Gdx.input.isButtonPressed(Buttons.LEFT))
			{
				Vector3 touchPos = new Vector3();
				touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
				camera.unproject(touchPos);

				if (soloButton.contains(touchPos.x, touchPos.y))
					game.setScreen(game.getScreen("solo"));
				if (introButton.contains(touchPos.x, touchPos.y))
					game.setScreen(game.getScreen("intro"));
				if (multiButton.contains(touchPos.x, touchPos.y))
					game.setScreen(game.getScreen("multi"));
			}
		}

		batch.begin();
		batch.draw(menuTexture, (MyGame.screenWidth - menuTexture.getRegionWidth()) / 2,
                (MyGame.screenHeight - menuTexture.getRegionHeight()) / 2);
		batch.end();
	}

}
