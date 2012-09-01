package com.me.cleverblocks;

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
	public static final float baseSpeed = 60;
	public static final int baseDefense = 0;
	public static final int bigSize = 10;
	public ArrayList<Block> body;
	private float xDir;
	private float yDir;
	private long lastGap;
	private long lastWound;
	private boolean isDead;
	public float centerX;
	public float centerY;

	public Creature()
	{
		this(0, 0);
	}
	
	public Creature(int n)
	{
		this(0, 0, n);
	}

	public Creature(float x, float y, BlockType t)
	{
		super(x, y, 1, 1);
		body = new ArrayList<Block>();
		body.add(new Block(x, y, t));

		lastWound = 0;
		lastGap = 0;
		isDead = false;
		
		this.normalize();
	}

	public Creature(float x, float y)
	{
		super(x, y, Block.blockSize, Block.blockSize);
		body = new ArrayList<Block>();
		body.add(new Block(x, y));

		lastGap = 0;
		lastWound = 0;
		isDead = false;
		
		this.normalize();
	}

	public Creature(float x, float y, int n)
	{
		this(x, y);
		for (int i = 1; i < n; i++)
			this.addBlock(new Block(x, y));
		
		this.normalize();
	}

	public Creature(float x, float y, BlockType t, int n)
	{
		this(x, y, t);
		for (int i = 1; i < n; i++)
			this.addBlock(new Block(x, y));
		
		this.normalize();
	}

	public void kill()
	{
		isDead = true;
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

	public void moveToward(Creature c)
	{
		float deltax = c.x - this.x;
		float deltay = c.y - this.y;

		moveAtSpeed(deltax, deltay);
	}

	public void moveAway(Creature c)
	{
		float deltax = this.centerX - c.centerX;
		float deltay = this.centerY - c.centerY;

		moveAtSpeed(deltax, deltay);
	}
	
	public void moveAway(Creature c, int n)
	{
		for (int i = 0; i < n; i++)
			moveAway(c);
	}
	
	public void moveAwayDistance(Creature c, float d)
	{
		float deltax = this.centerX - c.centerX;
		float deltay = this.centerY - c.centerY;
		
		Vector2 vect = new Vector2(deltax, deltay);
		vect.nor();
		
		this.move(vect.x * d, vect.y * d);
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
		
		updateCenter();
	}
	
	public void updateCenter()
	{
		this.centerX = this.x + this.width / 2;
		this.centerY = this.y + this.height / 2;
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
		if (Math.sqrt((x * x) + (y * y)) > 1.0f)
		{
			Vector2 delta = new Vector2(x, y);
			delta.nor();
			this.move(delta.x * speed() * Gdx.graphics.getDeltaTime(), delta.y * speed() * Gdx.graphics.getDeltaTime());
		}
		else
			this.move(x * speed() * Gdx.graphics.getDeltaTime(), y * speed() * Gdx.graphics.getDeltaTime());
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
		if (body.size() > 1)
		{
			int k = 1;
			float sq = (float) Math.sqrt(body.size());
			int csq = MathUtils.ceil(sq);
			int fsq = MathUtils.floor(sq);

			for (int y = 0; (y < csq) && k < body.size(); y++)
			{
				for (int x = 0; (x < csq) && k < body.size(); x++)
				{
					if (x != 0 || y != 0)
					{
						body.get(k).x = this.x + x * Block.blockSize;
						body.get(k).y = this.y + y * Block.blockSize;
						k++;
					}
				}
			}

			this.width = (csq + 1) * Block.blockSize;
			this.height = (fsq == csq ? fsq : csq) * Block.blockSize;
		}
		else
		{
			this.width = Block.blockSize;
			this.height = Block.blockSize;
		}
		
		this.updateCenter();
	}

	public void eat(Creature c, Block b)
	{
		c.body.remove(b);
		this.body.add(b);
		
		this.normalize();
		c.normalize();
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
		
		c.normalize();
		this.normalize();
	}

	public boolean fight(Creature c)
	{
		return fight(this, c);
	}

	public boolean overlaps(Creature c)
	{
		if (this.body.size() < Creature.bigSize && this.body.size() < Creature.bigSize)
		{
			if (this.dst(c) > MyGame.screenHeight * MyGame.scale / 2)
				return false;
			for (Block a : this.body)
				for (Block b : c.body)
				{
					if (b.overlaps(a))
						return true;
				}
			return false;
		}
		else
		{
			return super.overlaps(c); // faster collision detection
		}
	}

	public boolean overlapsSuper(Creature c)
	{
		return super.overlaps(c);
	}

	public float dst(Creature c)
	{
		float deltax = this.centerX - c.centerX;
		float deltay = this.centerY - c.centerY;

		return (float) Math.sqrt((deltax * deltax) + (deltay * deltay));
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
