package com.jordna.chunks.chunks;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Chunk 
{

	private String owner; // Unique ID
	private Location home;
	private Location centre;
	private List<String> members = new ArrayList<String>();
	private List<String> trusted = new ArrayList<String>(); // Unique ID's
	
	public Chunk(String owner, Location home, Location centre, List<String> members, List<String> trusted)
	{
		this.owner = owner;
		this.home = home;
		this.centre = centre;
		this.members = members;
		this.trusted = trusted;
	}
	
	public String getOwner()
	{
		return this.owner;
	}
	
	public Location getHome()
	{
		return this.home;
	}
	
	public Location getCentre()
	{
		return this.centre;
	}
	
	public List<String> getMembers()
	{
		return this.members;
	}
	
	public List<String> getTrusted()
	{
		return this.trusted;
	}
	
	public void setOwner(Player player)
	{
		this.owner = player.getUniqueId().toString();
	}
	
	public void setHome(Location location)
	{
		this.home = location;
	}
	
	public void setCentre(Location location)
	{
		this.centre = location;
	}
	
	public boolean isMember(Player player)
	{
		return members.contains(player.getUniqueId().toString());
	}
	
	public boolean addMember(Player player) // Return TRUE if player was added, return FALSE if player is already trusted
	{
		if (members.contains(player.getUniqueId().toString()))
			return false;
		
		this.members.add(player.getUniqueId().toString());
		return true;
	}
	
	public boolean removeMember(Player player) // TRUE FOR SUCCESS FALSE FOR FAILURE
	{
		if (!members.contains(player.getUniqueId().toString()))
			return false;
		
		members.remove(player.getUniqueId().toString());
		return true;
	}
	
	public boolean isTrusted(Player player)
	{
		return trusted.contains(player.getUniqueId().toString());
	}
	
	public boolean addTrusted(Player player) // Return TRUE if player was added, return FALSE if player is already trusted
	{
		if (trusted.contains(player.getUniqueId().toString()))
			return false;
		
		this.trusted.add(player.getUniqueId().toString());
		return true;
	}
	
	public boolean removeTrusted(Player player) // TRUE FOR SUCCESS FALSE FOR FAILURE
	{
		if (!trusted.contains(player.getUniqueId().toString()))
			return false;
		
		trusted.remove(player.getUniqueId().toString());
		return true;
	}
	
}
