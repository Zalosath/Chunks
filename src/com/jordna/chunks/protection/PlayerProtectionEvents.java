package com.jordna.chunks.protection;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.jordna.chunks.main.Chunks;

public class PlayerProtectionEvents implements Listener
{

    private Chunks main;

    public PlayerProtectionEvents(Chunks ins)
    {
	this.main = ins;
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e)
    {

	if (!main.getProtectionManager().canDoHere(e.getPlayer(), e.getBlock().getLocation()))
	{
	    e.setCancelled(true);
	}

    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e)
    {
	if (!main.getProtectionManager().canDoHere(e.getPlayer(), e.getPlayer().getLocation()))
	{
	    e.setCancelled(true);
	}
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e)
    {

	if (!main.getProtectionManager().canDoHere(e.getPlayer(), e.getBlock().getLocation()))
	{
	    e.setCancelled(true);
	}

    }

}
