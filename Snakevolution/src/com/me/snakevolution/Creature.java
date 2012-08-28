package com.me.snakevolution;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;

@SuppressWarnings("serial")
public class Creature extends Rectangle
{
	public static float baseSpeed = 50;
	public static int baseDefense = 0;
	public ArrayList<Block> body;
	private float xDir;
	private float yDir;
	private long lastGap;
	private long lastWound;
	private boolean isDead;

	public Creature()
	{
		this(MathUtils.random(0.0f, MyGame.gameWidth), MathUtils.random(0.0f, MyGame.gameHeight));
	}

	public Creature(float x, float y, BlockType t)
	{
		super(x, y, 1, 1);
		body = new ArrayList<Block>();
		body.add(new Block(x, y, t));

		lastWound = 0;
		lastGap = 0;
		isDead = false;
	}

	public Creature(float x, float y)
	{
		super(x, y, 1, 1);
		body = new ArrayList<Block>();
		body.add(new Block(x, y));

		lastGap = 0;
		lastWound = 0;
		isDead = false;
	}
	
	public Creature(float x, float y, int n)
	{
		this(x, y);
		for (int i = 1; i < n; i++)
			this.addBlock(new Block(x,y));
	}
	
	public Creature(float x, float y, BlockType t, int n)
	{
		this(x, y, t);
		for (int i = 1; i < n; i++)
			this.addBlock(new Block(x,y));
	}

	public boolean isAlive()
	{
		return (!isDead && body.size() > 0);
	}

	public void addBlock(Block b)
	{
		body.add(b);
	}

	public void deleteBlock(Block b)
	{
		body.remove(b);
	}

	public void randomMove()
	{
		if (TimeUtils.millis() > lastGap + (1000 * (2 + MathUtils.random(5))))
		{
			lastGap = TimeUtils.millis();
			float x = MathUtils.random(-1.0f, 1.0f);
			float y = MathUtils.random(-1.0f, 1.0f);
			xDir = x;
			yDir = y;
			moveAtSpeed(x, y);
		}
		else
			this.moveAtSpeed(xDir, yDir);
	}

	public void moveAway(Creature c)
	{
		float deltax = this.x - c.x;
		float deltay = this.y - c.y;

		moveAtSpeed(deltax, deltay);
	}
	
	public void moveToward(Creature c)
	{
		float deltax = c.x - this.x;
		float deltay = c.y - this.y;

		moveAtSpeed(deltax, deltay);
	}
	
	public void moveAway(Creature c, int n)
	{
		for (int i = 0; i < n; i++)
			moveAway(c);
	}
	
	public void moveAwaySuper(Creature c, int n)
	{
		float deltax = this.x - (c.x - c.width / 2);
		float deltay = this.x - (c.x - c.width / 2);
		for (int i = 0; i < n; i++)
		{
			moveAtSpeed(deltax, deltay);
		}
	}

	public void moveTo(float x, float y)
	{
		float deltax = x - this.x;
		float deltay = y - this.y;

		this.move(deltax, deltay);
	}

	public void move(float x, float y)
	{
		this.x += x;
		this.y += y;

		for (Block block : body)
		{
			block.x += x;
			block.y += y;
		}
	}

	public void computeDimensions()
	{
		if (body.size() > 0)
		{
			Block firstBlock = body.get(0);
			float x_min = firstBlock.x;
			float y_min = firstBlock.y;
			float x_max = x_min;
			float y_max = y_min;

			for (Block b : body)
			{
				if (b.x < x_min)
					x_min = b.x;
				if (b.y < y_min)
					y_min = b.y;
				if (b.x > x_max)
					x_max = b.x;
				if (b.y > y_max)
					y_max = b.y;
			}

			x_max += Block.blockSize;
			y_max += Block.blockSize;

			this.width = x_max - x_min;
			this.height = y_max - y_min;
		}
	}

	public int life()
	{
		int life = 0;
		for (Block b : body)
		{
			if (b.type == BlockType.GREEN)
				life += 1;
		}

		return life;
	}

	public float speed()
	{
		float speed = Creature.baseSpeed;
		for (Block b : body)
		{
			speed += b.type.getSpeed();
		}

		return speed;
	}

	public int defense()
	{
		int defense = Creature.baseDefense;
		for (Block b : body)
		{
			defense += b.type.getDefense();
		}

		return defense;
	}

	public boolean looseOneLife()
	{
		if (TimeUtils.millis() - lastWound > 1000 * MyGame.lifeCooldown)
		{
			Iterator<Block> ite = body.iterator();

			while (ite.hasNext())
			{
				Block b = ite.next();
				if (b.type == BlockType.GREEN)
				{
					lastWound = TimeUtils.millis();
					ite.remove();
					return true;
				}
			}
			isDead = true;
			return false;
		}
		return true;
	}

	public void moveAtSpeed(float x, float y)
	{
		Vector2 delta = new Vector2(x, y);
		delta.nor();
		this.move(delta.x * speed() * Gdx.graphics.getDeltaTime(), delta.y * speed() * Gdx.graphics.getDeltaTime());
	}

	public void draw(SpriteBatch s, Texture t, float xOffset, float yOffset)
	{
		for (Block b : body)
		{
			s.draw(t, b.x + xOffset, b.y + yOffset);
		}
	}

	public void draw(SpriteBatch s, float xOffset, float yOffset)
	{
		for (Block b : body)
		{
			s.draw(b.type.getTexture(), b.x + xOffset, b.y + yOffset);
		}
	}
	
	public void drawOne(SpriteBatch s, Texture t, float xOffset, float yOffset)
	{
		s.draw(t, x + xOffset, y + yOffset);
	}
	public void normalize()
	{
		boolean done = false;

//		if (body.size() > 1)
//		{
//			int w = (int) Math.sqrt(2 * (body.size()));
//
//			int k = 1;
//			for (int i = -w/2; i < w/2 && !done; i++)
//				for (int j = -w/2 + i; j < w/2 -i && !done; j++)
//				{
//					if (x != 0 || y != 0)
//					{
//						this.body.get(k).x = this.x + i * Block.blockSize;
//						this.body.get(k).y = this.y + j * Block.blockSize;
//						k++;
//						if (k == body.size())
//							done = true;
//					}
//				}
//		}
		
		if (body.size() > 1)
		{
			int k = 1;
			int w = MathUtils.ceil((float) Math.sqrt(body.size() + 1));

			for (int i = (-w / 2); (i <= w / 2) && !done; i++)
				for (int j = (-w / 2); (j <= w / 2) && !done; j++)
				{
					if (i != 0 || j != 0)
					{
						this.body.get(k).x = this.x + i * Block.blockSize;
						this.body.get(k).y = this.y - j * Block.blockSize;
						
						k++;
						if (k == body.size())
							done = true;
					}
				}
			
			if (k < body.size())
				Gdx.app.log("Status", "Normalization failed");
		}
	}

	public void eat(Creature c, Block b)
	{
		c.body.remove(b);
		this.body.add(b);
	}

	public void eat(Creature c)
	{
		Iterator<Block> ite = c.body.iterator();
		while (ite.hasNext())
		{
			Block b = ite.next();
			this.addBlock(b);
			ite.remove();
		}
	}

	public boolean fight(Creature c)
	{
		return fight(this, c);
	}

	public boolean overlaps(Creature c)
	{
		for (Block a : this.body)
			for (Block b : c.body)
			{
				if (b.overlaps(a))
					return true;
			}

		return false;
	}
	
	public boolean overlapsSuper(Creature c)
	{
		return super.overlaps(c);
	}
	
	public static boolean fight(Creature a, Creature b)
	{
		int defenseA = a.defense();
		int defenseB = b.defense();

		if (a.life() == a.body.size())
		{
			b.eat(a);
			return true;
		}
		if (b.life() == b.body.size())
		{
			a.eat(b);
			return true;
		}

		if (defenseA > defenseB)
		{
			
			if (defenseA > 3 * defenseB || !b.looseOneLife() || !b.isAlive())
			{
				a.eat(b);
				return true;
			}
			return false;
		}
		if (defenseB > defenseA)
		{
			if (defenseB > 3 * defenseA || !a.looseOneLife() || !a.isAlive())
			{
				b.eat(a);
				return true;
			}
			return false;
		}
		return false;
	}
}
