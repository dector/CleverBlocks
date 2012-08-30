package com.me.cleverblocks;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;

public enum BlockType
{
	YELLOW(8, 1), RED(0, 3), BLUE(8, 2), GREEN(0,0);

	private float speed;
	private int defense;

	private BlockType(float speed, int defense)
	{
		this.speed = speed;
		this.defense = defense;
	}

	public float getSpeed()
	{
		return this.speed;
	}

	public int getDefense()
	{
		return this.defense;
	}

	public Texture getTexture()
	{
		switch (this)
		{
			case BLUE:
				return MyGame.blueBlockTexture;
			case RED:
				return MyGame.redBlockTexture;
			case GREEN:
				return MyGame.greenBlockTexture;
			case YELLOW:
			default:
				return MyGame.yellowBlockTexture;
		}
	}
	
	public static BlockType randomBlockType()
	{
		switch (MathUtils.random(2))
		{
			case 0:
				return BlockType.RED;
			case 1:
				return BlockType.GREEN;
			case 2:
			default:
				return BlockType.YELLOW;
		}
	}
}
