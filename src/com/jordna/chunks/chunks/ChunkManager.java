package com.jordna.chunks.chunks;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.jordna.chunks.main.Chunks;

public class ChunkManager 
{

	private List<Chunk> chunks = new ArrayList<Chunk>();
	
	private Chunks main;
	public ChunkManager(Chunks instance)
	{
		this.main = instance;
	}
	
	public void addChunk(Chunk chunk)
	{
		if (!chunks.contains(chunk))
			chunks.add(chunk);
	}
	
	public void removeChunk(Chunk chunk)
	{
		if (chunks.contains(chunk))
		{
			chunks.remove(chunk);
		}
	}
	
	public void addTrusted(Chunk chunk, Player trusted)
	{
		chunk.addTrusted(trusted);
		
		List<String> alreadyTrusted = main.getChunks().getStringList("chunks." + chunk.getOwner() + ".trusted");
		alreadyTrusted.add(trusted.getUniqueId().toString());
		
		main.getChunks().set("chunks." + chunk.getOwner() + ".trusted", alreadyTrusted);
		main.saveChunks();
	}
	
	public void removeTrusted(Chunk chunk, Player trusted)
	{
		if (chunk.getTrusted().contains(trusted.getUniqueId().toString()))
		{
			chunk.removeTrusted(trusted);
			
			List<String> alreadyTrusted = main.getChunks().getStringList("chunks." + chunk.getOwner() + ".trusted");
			alreadyTrusted.remove(trusted.getUniqueId().toString());
			
			main.getChunks().set("chunks." + chunk.getOwner() + ".trusted", alreadyTrusted);
			main.saveChunks();
		}
	}
	
	public void addMember(Chunk chunk, Player member)
	{
		chunk.addMember(member);
		
		List<String> alreadyMembers = main.getChunks().getStringList("chunks." + chunk.getOwner() + ".members");
		alreadyMembers.add(member.getUniqueId().toString());
		
		main.getChunks().set("chunks." + chunk.getOwner() + ".members", alreadyMembers);
		main.saveChunks();
	}
	
	public void removeMember(Chunk chunk, Player member)
	{
		if (chunk.getMembers().contains(member.getUniqueId().toString()))
		{
			chunk.removeMember(member);
			
			List<String> alreadyMembers = main.getChunks().getStringList("chunks." + chunk.getOwner() + ".members");
			alreadyMembers.remove(member.getUniqueId().toString());
			
			main.getChunks().set("chunks." + chunk.getOwner() + ".members", alreadyMembers);
			main.saveChunks();
		}
	}
	
	public Chunk getChunk(String playerName)
	{
		Player player = Bukkit.getPlayer(playerName);
		if (player == null)
			return null;
		
		for (Chunk i : chunks)
			if (i.getOwner().equals(player.getUniqueId().toString()) || i.getMembers().contains(player.getUniqueId().toString()))
				return i;
		return null;
	}

	public Chunk getChunkFromOwner(String owner)
	{
		for (Chunk i : chunks)
		{
			if (i.getOwner().equalsIgnoreCase(owner))
				return i;
		}
		return null;
	}
	
	public Location getHome(Player player)
	{
		for (Chunk i : chunks)
		{
			if (i.getMembers().contains(player.getUniqueId().toString()))
				return i.getHome();
		}
		return null;
	}
	
	public void setHome(Player player, Chunk chunk, Location location)
	{
		if (location.clone().subtract(0, 1, 0).getBlock().getType().isSolid())
		{
			chunk.setHome(location);
			
			this.main.getChunks().set("chunks." + player.getUniqueId().toString() + ".X", location.getX());
			this.main.getChunks().set("chunks." + player.getUniqueId().toString() + ".Y", location.getY());
			this.main.getChunks().set("chunks." + player.getUniqueId().toString() + ".Z", location.getZ());
			this.main.saveChunks();
			
			player.sendMessage(ChatColor.BLUE + "[CHUNKS] Home set at X " + ChatColor.GRAY + location.getBlockX() + ChatColor.BLUE + " Y " + ChatColor.GRAY + location.getBlockY() + ChatColor.BLUE + " Z " + ChatColor.GRAY + location.getBlockZ());
		}
		else
		{
			player.sendMessage(ChatColor.RED + "[CHUNKS] Home can only be set on top of a solid block");
		}
	}
	
	public void addChunk(String ownerUUID)
	{
		double x = Double.parseDouble(main.getChunks().get("chunks." + ownerUUID + ".X").toString());
		double y = Double.parseDouble(main.getChunks().get("chunks." + ownerUUID + ".Y").toString());
		double z = Double.parseDouble(main.getChunks().get("chunks." + ownerUUID + ".Z").toString());
		World w = Bukkit.getWorld(main.getChunks().get("chunks." + ownerUUID + ".WORLD").toString());
		
		Location home = new Location(w, x, y, z);
		
		double xCentre = Double.parseDouble(main.getChunks().get("chunks." + ownerUUID + ".centre.X").toString());
		double zCentre = Double.parseDouble(main.getChunks().get("chunks." + ownerUUID + ".centre.Z").toString());
		Location centre = new Location(w, xCentre, 67, zCentre);

		List<String> members = main.getChunks().getStringList("chunks." + ownerUUID + ".members");
		
		List<String> trusted = main.getChunks().getStringList("chunks." + ownerUUID + ".trusted");
		
		this.addChunk(new Chunk(ownerUUID, home, centre, members, trusted));
	}
	
	public Chunk getOverlapsChunk(Location centre)
	{
		int chunkSize = (main.getSettings().getInt("chunks.default-size-in-chunks") * 32);
		
		for (Chunk i : chunks)
		{
			Location copy = centre.clone();
			copy.subtract(i.getCentre());
			
			if (Math.abs(copy.getBlockX()) < chunkSize && Math.abs(copy.getBlockZ()) < chunkSize)
			{
				return i;
			}
		}
		return null;
	}
	
	public Chunk getChunkIn(Location location)
	{
		int chunkSize = main.getSettings().getInt("chunks.default-size-in-chunks") * 16;
		
		for (Chunk i : chunks)
		{
			Location copy = location.clone();
			copy.subtract(i.getCentre());
			
			if (Math.abs(copy.getBlockX()) < chunkSize && Math.abs(copy.getBlockZ()) < chunkSize)
			{
				return i;
			}
		}
		return null;
	}
	
	public boolean hasChunk(Player player)
	{
		for (Chunk i : chunks)
		{
			if (i.getOwner().equals(player.getUniqueId().toString()) || i.getMembers().contains(player.getUniqueId().toString()))
				return true;
		}
		return false;
	}
}
