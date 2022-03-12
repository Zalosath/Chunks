package com.jordna.chunks.chunks;

import org.bukkit.World;
import org.bukkit.entity.Player;

public class ChunkInformation 
{

	private World world;
	private Player player;
	private int xBorder;
	private int zBorder;
	private int difficulty;
	
	public ChunkInformation(World world, Player player, int xBorder, int zBorder, int difficulty)
	{
		this.world = world;
		this.player = player;
		this.xBorder = xBorder;
		this.zBorder = zBorder;
		this.difficulty = difficulty;
	}
	
	public World getWorld()
	{
		return this.world;
	}
	
	public Player getPlayer()
	{
		return this.player;
	}
	
	public int getBorderX()
	{
		return this.xBorder;
	}
	
	public int getBorderZ()
	{
		return this.zBorder;
	}
	
	public int getDifficulty()
	{
		return this.difficulty;
	}
	
	public void setWorld(World world)
	{
		this.world = world;
	}
	
	public void setPlayer(Player player)
	{
		this.player = player;
	}
	
	public void setBorderX(int xBorder)
	{
		this.xBorder = xBorder;
	}
	
	public void setBorderZ(int zBorder)
	{
		this.zBorder = zBorder;
	}
	
	public void setDifficulty(int difficulty)
	{
		this.difficulty = difficulty;
	}
}
