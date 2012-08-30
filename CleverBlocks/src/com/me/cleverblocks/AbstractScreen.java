package com.me.cleverblocks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.TimeUtils;

public abstract class AbstractScreen implements Screen
{
	protected final SpriteBatch batch;
	protected final MyGame game;
	protected OrthographicCamera camera;
	private long startTime;
	
	public AbstractScreen(MyGame game)
	{
		this.game = game;
		this.batch = new SpriteBatch();
		this.camera = new OrthographicCamera();

	}
	
	@Override
	public void show()
	{
		camera.setToOrtho(false, MyGame.screenWidth, MyGame.screenHeight);
		startTime = TimeUtils.millis();
	}
	
	@Override
	public void render(float delta)
	{
        Gdx.gl.glClearColor( 0f, 0f, 0f, 1f );
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT );
        
        //Gdx.app.log("Status", "Rendering screen: " + getName() );
	}
	
	@Override
	public void resize(int height, int width)
	{
	}

	@Override
	public void hide()
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
	public void dispose()
	{
		batch.dispose();
	}
	
	protected long elapsedTime()
	{
		return TimeUtils.millis() - startTime;
	}
	
	protected void restartTimer()
	{
		startTime = TimeUtils.millis();
	}
}
