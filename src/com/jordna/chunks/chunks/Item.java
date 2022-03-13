package com.jordna.chunks.chunks;

import org.bukkit.Material;

public class Item
{

    public Material material;
    public float probability;
    public int highestLevel;
    public int lowestLevel;

    public Item(Material m, float prob, int highestLevel, int lowestLevel)
    {
	this.material = m;
	this.probability = prob;
	this.highestLevel = highestLevel;
	this.lowestLevel = lowestLevel;
    }

    public Item(Item i)
    {
	this.material = i.material;
	this.probability = i.probability;
	this.highestLevel = i.highestLevel;
	this.lowestLevel = i.lowestLevel;
    }

}
