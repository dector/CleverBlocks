package com.me.snakevolution;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;

public class SoloScreen extends AbstractScreen
{
	private OrthographicCamera camera;
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

		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		camera = new OrthographicCamera(1, h / w);
		camera.setToOrtho(false, MyGame.screenWidth * MyGame.scale, MyGame.screenHeight * MyGame.scale);

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

		font = new BitmapFont();

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
			if (Gdx.input.isKeyPressed(Keys.UP))
				player.moveAtSpeed(0, 1);
			if (Gdx.input.isKeyPressed(Keys.DOWN))
				player.moveAtSpeed(0, -1);
			if (Gdx.input.isKeyPressed(Keys.RIGHT))
				player.moveAtSpeed(1, 0);
			if (Gdx.input.isKeyPressed(Keys.LEFT))
				player.moveAtSpeed(-1, 0);
		}
		else
		{
			if (Gdx.input.isKeyPressed(Keys.SPACE) || Gdx.input.isKeyPressed(Keys.ENTER))
				this.init();
		}

		// //////////////////// GAME LOGIC ///////////////////////////////

		for (Creature c : creatures)
		{
			if (c.overlaps(player))
			{
				if (player.fight(c))
				{
					updateEatHistory();
					if (lastSecondEatCount() >= 3)
						eatSound3.play();
					else if (lastSecondEatCount() >= 2)
						eatSound2.play();
					else
						eatSound1.play();
				}
				else
				{
					if (player.isAlive())
					{
						hurtSound.play();
						player.moveAway(c);
						c.moveAway(player);
					}
					else
						dieSound.play();
				}
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

			Iterator<Creature> ite = creatures.iterator();
			while (ite.hasNext())
			{
				Creature b = ite.next();
				if (!b.isAlive() || player.dst(b) > (MyGame.screenWidth * MyGame.scale) * 2)
					ite.remove();
				else
					b.normalize();
			}

			spawning();
		}
		else if (!won)
		// /////// BOSS MODE
		{
			player.computeDimensions();

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

		player.normalize();

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

		camera.setToOrtho(false, MyGame.screenWidth * MyGame.scale, MyGame.screenHeight * MyGame.scale);
		batch.setProjectionMatrix(camera.combined);

		float xOffset = ((MyGame.screenWidth * MyGame.scale) / 2) - player.x - (Block.blockSize / 2);
		float yOffset = ((MyGame.screenHeight * MyGame.scale) / 2) - player.y - (Block.blockSize / 2);

		batch.begin();

		for (int x = -1; x <= (MyGame.screenWidth * MyGame.scale) / MyGame.tileSize + 1; x++)
			for (int y = -1; y <= (MyGame.screenHeight * MyGame.scale) / MyGame.tileSize + 1; y++)
				batch.draw(tile, (x * MyGame.tileSize) + (xOffset % MyGame.tileSize), (y * MyGame.tileSize) + (yOffset % MyGame.tileSize));

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
			float x = MathUtils.random(0, player.x + MyGame.screenWidth * MyGame.scale);
			float y = MathUtils.random(0, player.y + MyGame.screenHeight * MyGame.scale);
			if (x < player.x + (MyGame.screenWidth * MyGame.scale / 2))
				x -= MyGame.screenWidth * MyGame.scale;
			else
				x += MyGame.screenWidth / 2 * MyGame.scale;

			if (y < player.x + (MyGame.screenHeight * MyGame.scale / 2))
				y -= MyGame.screenHeight * MyGame.scale;
			else
				y += MyGame.screenHeight / 2 * MyGame.scale;

			creatures.add(new Creature(x, y, MathUtils.random(player.body.size(), player.body.size() * 2)));
		}

		chances = Gdx.graphics.getDeltaTime() / 0.3f;
		if (MathUtils.random() < chances)
		{
			float x = MathUtils.random(0, player.x + MyGame.screenWidth * MyGame.scale);
			float y = MathUtils.random(0, player.y + MyGame.screenHeight * MyGame.scale);
			if (x < player.x + (MyGame.screenWidth * MyGame.scale / 2))
				x -= MyGame.screenWidth * MyGame.scale;
			else
				x += MyGame.screenWidth / 2 * MyGame.scale;

			if (y < player.x + (MyGame.screenHeight * MyGame.scale / 2))
				y -= MyGame.screenHeight * MyGame.scale;
			else
				y += MyGame.screenHeight / 2 * MyGame.scale;

			creatures.add(new Creature(x, y, MathUtils.random(player.body.size() / 2)));
		}

	}

	public void updateScale()
	{
		MyGame.scale = (0.95f * MyGame.scale) + (0.05f * getCurrentScale());
	}

	public float getCurrentScale()
	{
		player.computeDimensions();
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

}
