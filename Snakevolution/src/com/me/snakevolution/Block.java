package com.me.snakevolution;

import com.badlogic.gdx.math.Rectangle;

@SuppressWarnings("serial")
public class Block extends Rectangle
{	
	public static float blockSize = 16;
	public BlockType type;
	
	public Block(float x, float y, BlockType t)
	{
		super(x,y,16,16);
		type = t;
	}
	
	public Block(float x, float y)
	{
		this(x, y, BlockType.randomBlockType());
	}
}
