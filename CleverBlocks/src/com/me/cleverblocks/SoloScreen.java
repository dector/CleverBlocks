package com.me.cleverblocks;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;

public class SoloScreen extends AbstractScreen
{
	private SpriteBatch batch;
	private Texture tile;
	private ArrayList<Creature> creatures;
	private Creature player;
	private Sound eatSound1;
	private Sound eatSound2;
	private Sound eatSound3;
	private Sound hurtSound;
	private Sound dieSound;
	private Music music;
	private long[] eatHistory;
	private BitmapFont font;
	private Music bossMusic;
	private boolean bossIncoming;
	private Texture bossTexture;
	private Creature boss;
	private Creature missiles[];
	private Texture missileTexture;
	private boolean won;
	private long bossTime;

	public SoloScreen(MyGame game)
	{
		super(game);

		batch = new SpriteBatch();

		MyGame.redBlockTexture = new Texture(Gdx.files.internal("data/redBlock.png"));
		MyGame.yellowBlockTexture = new Texture(Gdx.files.internal("data/yellowBlock.png"));
		MyGame.blueBlockTexture = new Texture(Gdx.files.internal("data/blueBlock.png"));
		MyGame.greenBlockTexture = new Texture(Gdx.files.internal("data/greenBlock.png"));
		missileTexture = new Texture(Gdx.files.internal("data/deathBlock.png"));
		bossTexture = new Texture(Gdx.files.internal("data/boss.png"));
		tile = new Texture(Gdx.files.internal("data/tile.png"));

		eatSound1 = Gdx.audio.newSound(Gdx.files.internal("data/eatSound1.wav"));
		eatSound2 = Gdx.audio.newSound(Gdx.files.internal("data/eatSound2.wav"));
		eatSound3 = Gdx.audio.newSound(Gdx.files.internal("data/eatSound3.wav"));
		hurtSound = Gdx.audio.newSound(Gdx.files.internal("data/hurtSound.wav"));
		dieSound = Gdx.audio.newSound(Gdx.files.internal("data/dieSound.wav"));
		music = Gdx.audio.newMusic(Gdx.files.internal("data/music.mp3"));
		bossMusic = Gdx.audio.newMusic(Gdx.files.internal("data/music.mp3"));
		music.setLooping(true);
		music.setVolume(0.5f);
		music.play();

		font = new BitmapFont(Gdx.files.internal("data/arial-15.fnt"), Gdx.files.internal("data/arial-15.png"), false);

		init();
	}

	public void init()
	{
		creatures = new ArrayList<Creature>();
		for (int i = 0; i < 100; i++)
			creatures.add(new Creature());

		player = new Creature(500, 500, BlockType.BLUE);
		
		eatHistory = new long[5];
		bossIncoming = false;
		won = false;
	}

	@Override
	public void dispose()
	{
		batch.dispose();
		MyGame.redBlockTexture.dispose();
		MyGame.yellowBlockTexture.dispose();
	}

	@Override
	public void render(float delta)
	{
		// //////////////////////// CONTROLS //////////////////////////////

		if (player.isAlive() && !won)
		{
			float accelerometerX = Gdx.input.getAccelerometerX();
			float accelerometerY = Gdx.input.getAccelerometerY();
			if ((Math.abs(accelerometerX) > MyGame.minAccelero && Math.abs(accelerometerX) < MyGame.maxAccelero) || (Math.abs(accelerometerY) > MyGame.minAccelero && Math.abs(accelerometerY) < MyGame.maxAccelero))
			{
				player.moveAtSpeed((accelerometerY - MyGame.minAccelero) / (MyGame.maxAccelero - MyGame.minAccelero), -(accelerometerX - MyGame.minAccelero) / (MyGame.maxAccelero - MyGame.minAccelero));
				Gdx.app.log("Move", "Moving: " + accelerometerY + " " + -accelerometerX);
			}
			else
			{
				float deltaX = 0.0f;
				float deltaY = 0.0f;
				if (Gdx.input.isKeyPressed(Keys.UP) || Gdx.input.isKeyPressed(Keys.Z) || Gdx.input.isKeyPressed(Keys.W))
					deltaY = 1.0f;
				if (Gdx.input.isKeyPressed(Keys.DOWN) || Gdx.input.isKeyPressed(Keys.S))
					deltaY = -1.0f;
				if (Gdx.input.isKeyPressed(Keys.RIGHT) || Gdx.input.isKeyPressed(Keys.D))
					deltaX = 1.0f;
				if (Gdx.input.isKeyPressed(Keys.LEFT) || Gdx.input.isKeyPressed(Keys.Q) || Gdx.input.isKeyPressed(Keys.A))
					deltaX = -1.0f;

				player.moveAtSpeed(deltaX, deltaY);

				if (deltaX == 0.0f && deltaY == 0.0f && Gdx.input.isButtonPressed(Buttons.LEFT))
				{
					Vector3 touchPos = new Vector3();
					touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
					camera.unproject(touchPos);
					deltaX = touchPos.x - (MyGame.screenWidth / 2);
					deltaY = touchPos.y - (MyGame.screenHeight / 2);

					player.moveAtSpeed(deltaX, deltaY);
				}
			}

		}
		else
		{
			if (Gdx.input.isKeyPressed(Keys.SPACE) || Gdx.input.isKeyPressed(Keys.ENTER) || Gdx.input.isTouched())
				this.init();
		}

		// ///////////////////// GAME LOGIC ///////////////////////////////

		for (Creature c : creatures)
		{
			if (c.overlaps(player))
			{
//				if (false) // player.fight(c)
//				{
//					updateEatHistory();
//					if (lastSecondEatCount() >= 3)
//						eatSound3.play();
//					else if (lastSecondEatCount() >= 2)
//						eatSound2.play();
//					else
//						eatSound1.play();
//				}
//				else
//				{
//					if (player.isAlive())
//					{
//						hurtSound.play();
//						player.moveAway(c);
//						c.moveAway(player);
//					}
//					else
//						dieSound.play();
//				}
			}
		}

		if (!bossIncoming)
		{
			for (Creature c : creatures)
			{
				c.randomMove();
			}

			for (Creature c : creatures)
			{
				for (Creature d : creatures)
				{
					if (d != c && c.overlaps(d))
					{
						d.fight(c);
					}
				}
			}

			for (int i = 0; i < creatures.size(); i++)
			{
				Creature b = creatures.get(i);
				if (!b.isAlive() || player.dst(b) > (MyGame.screenWidth * MyGame.scale) * 3)
					respawn(b);
			}

			spawning();
		}
		else if (!won)
		// /////// BOSS MODE
		{
			if (elapsedTime() > 1000 * 3) // launch missiles
			{
				restartTimer();
				for (int i = 0; i < 3; i++)
				{
					missiles[i] = new Creature(boss.x + boss.width / 2, boss.y + boss.height / 2);
					missiles[i].width = missileTexture.getWidth();
					missiles[i].height = missileTexture.getHeight();
				}
			}

			if (missiles[0] != null)
			{
				for (int i = 0; i < 15; i++)
				{
					missiles[0].moveAtSpeed(0, -1);
					missiles[1].moveAtSpeed(1, -1);
					missiles[2].moveAtSpeed(-1, -1);
				}
				for (Creature m : missiles)
				{
					if (m.overlaps(new Rectangle(player.x - player.width / 2, player.y - player.height / 2, player.width, player.height)))
						player.kill();
				}
			}

			for (Creature c : creatures)
			{
				c.moveAway(player, 2);
			}

			if (boss.overlaps(new Rectangle(player.x - player.width / 2, player.y - player.height / 2, player.width, player.height)))
			{
				won = true;
				bossIncoming = false;
			}

		}

		if (player.body.size() > 800 && !bossIncoming)
		{
			bossIncoming = true;
			spawnBoss();
			music.stop();
			bossMusic.play();
			bossMusic.setLooping(true);
			bossMusic.setVolume(0.5f);
			bossTime = TimeUtils.millis();
		}

		// //////////////////// RENDERING ///////////////////////////////

		if (player.isAlive())
			updateScale();

		super.render(delta);
		camera.setToOrtho(false, MyGame.screenWidth * MyGame.scale, MyGame.screenHeight * MyGame.scale);
		batch.setProjectionMatrix(camera.combined);

		float xOffset = ((MyGame.screenWidth * MyGame.scale) / 2) - player.centerX;
		float yOffset = ((MyGame.screenHeight * MyGame.scale) / 2) - player.centerY;

		batch.begin();

		drawTiles(batch, xOffset, yOffset);

		for (Creature c : creatures)
		{
			c.draw(batch, xOffset, yOffset);
		}

		if (player.isAlive())
			player.draw(batch, xOffset, yOffset);

		if (bossIncoming && !won)
		{
			// boss rendering

			boss.drawOne(batch, bossTexture, xOffset, yOffset);

			// Gdx.app.log("Boss", boss.toString());
			// Gdx.app.log("Player", player.toString());

			if (missiles[0] != null)
			{
				missiles[0].draw(batch, missileTexture, xOffset, yOffset);
				missiles[1].draw(batch, missileTexture, xOffset, yOffset);
				missiles[2].draw(batch, missileTexture, xOffset, yOffset);
			}
		}

		// HUD rendering
		font.setScale(1.0f);
		camera.setToOrtho(false, MyGame.screenWidth, MyGame.screenHeight);
		batch.setProjectionMatrix(camera.combined);

		String message = "LIFE: " + player.life() + "\n" + "POWER: " + player.defense() + "\n" + "SPEED: " + player.speed();

		if (MyGame.debugMode)
			message += "\n" + "FPS: " + 1 / Gdx.graphics.getDeltaTime();
		message += "\n" + "Accelerometer X: " + Gdx.input.getAccelerometerX();
		message += "\n" + "Accelerometer Y: " + Gdx.input.getAccelerometerY();

		font.drawMultiLine(batch, message, 10, MyGame.screenHeight - 10);

		if (!player.isAlive())
		{
			font.setScale(3.0f);

			float strX = ((MyGame.screenWidth) - font.getBounds(MyGame.endMessage).width) / 2;
			float strY = ((MyGame.screenHeight) + font.getBounds(MyGame.endMessage).height) / 2;
			font.draw(batch, MyGame.endMessage, strX, strY);

			message = "Press Enter to try again.";
			strX = ((MyGame.screenWidth) - font.getBounds(message).width) / 2;
			strY = (MyGame.screenHeight) / 4 + font.getBounds(message).height / 2;
			font.draw(batch, message, strX, strY);
		}

		if (TimeUtils.millis() - bossTime < 1000 * 2 && player.isAlive() && !won)
		{
			font.setScale(3.0f);
			message = "BOSS INCOMING!";
			float strX = ((MyGame.screenWidth) - font.getBounds(message).width) / 2;
			float strY = (MyGame.screenHeight) / 2 + font.getBounds(message).height / 2;
			font.draw(batch, message, strX, strY);
		}

		if (won)
		{
			font.setScale(3.0f);
			message = "YOU WON!";
			float strX = ((MyGame.screenWidth) - font.getBounds(message).width) / 2;
			float strY = (MyGame.screenHeight) / 2 + font.getBounds(message).height / 2;
			font.draw(batch, message, strX, strY);

			message = "Press Enter to try again.";
			strX = ((MyGame.screenWidth) - font.getBounds(message).width) / 2;
			strY = (MyGame.screenHeight) / 4 + font.getBounds(message).height / 2;
			font.draw(batch, message, strX, strY);
		}

		batch.end();
	}

	public void spawnBoss()
	{
		float x = player.x;
		float y = player.y + ((MyGame.screenHeight) / 2) * MyGame.scale;

		boss = new Creature(x, y);
		boss.width = bossTexture.getWidth();
		boss.height = bossTexture.getHeight();
		restartTimer();
		missiles = new Creature[3];
	}

	public void spawning()
	{
		float chances = Gdx.graphics.getDeltaTime() / MyGame.spawningSpeed;
		if (MathUtils.random() < chances)
		{
			Creature c = new Creature(MathUtils.random(player.body.size(), player.body.size() * 2));
			respawn(c);
			creatures.add(c);
		}

		chances = Gdx.graphics.getDeltaTime() / 0.3f;
		if (MathUtils.random() < chances)
		{
			Creature c = new Creature(MathUtils.random(player.body.size(), player.body.size() * 2));
			respawn(c);
			creatures.add(c);
		}
	}

	public void respawn(Creature c)
	{
		float x = MathUtils.random(player.centerX - (MyGame.screenWidth / 2) * MyGame.scale, player.centerX + (MyGame.screenWidth / 2) * MyGame.scale);
		float y = MathUtils.random(player.centerY - (MyGame.screenHeight / 2) * MyGame.scale, player.centerY + (MyGame.screenHeight / 2) * MyGame.scale);

		c.moveTo(x, y);
		c.moveAwayDistance(player, (MyGame.screenWidth + (c.width / 2)) * MyGame.scale);

	}

	public void updateScale()
	{
		MyGame.scale = (0.99f * MyGame.scale) + (0.01f * getCurrentScale());
	}

	public float getCurrentScale()
	{
		float maxRatio = Math.max(player.width / MyGame.screenWidth, player.height / MyGame.screenHeight);
		float newRatio = maxRatio / MyGame.maxRatio;

		return newRatio;
	}

	public void updateEatHistory()
	{
		long currentTime = TimeUtils.millis();
		long minValue = currentTime;
		int minIndex = 0;
		for (int i = 0; i < 5; i++)
		{
			if (eatHistory[i] < minValue)
			{
				minIndex = i;
				minValue = eatHistory[i];
			}
		}

		eatHistory[minIndex] = currentTime;
	}

	public long getAverageEatTime()
	{
		long currentTime = TimeUtils.millis();
		long sum = 0;
		for (int i = 0; i < 5; i++)
		{
			sum += eatHistory[i];
		}

		return (sum / 5) - currentTime;
	}

	public long lastSecondEatCount()
	{
		long currentTime = TimeUtils.millis();
		int sum = 0;
		for (int i = 0; i < 5; i++)
		{
			if (currentTime - eatHistory[i] < 1000)
				sum++;
		}

		return sum;
	}

	public void drawTiles(SpriteBatch s, float xOffset, float yOffset)
	{
		camera.setToOrtho(false, MyGame.screenWidth * MyGame.scale / 64, MyGame.screenHeight * MyGame.scale / 64);
		batch.setProjectionMatrix(camera.combined);

		for (int x = -1; x <= (MyGame.screenWidth * MyGame.scale / 64) / MyGame.tileSize + 1; x++)
			for (int y = -1; y <= (MyGame.screenHeight * MyGame.scale / 64) / MyGame.tileSize + 1; y++)
				batch.draw(tile, (x * MyGame.tileSize) + ((xOffset / 64) % MyGame.tileSize), (y * MyGame.tileSize) + ((yOffset / 64) % MyGame.tileSize));

		camera.setToOrtho(false, MyGame.screenWidth * MyGame.scale, MyGame.screenHeight * MyGame.scale);
		batch.setProjectionMatrix(camera.combined);
	}
}
