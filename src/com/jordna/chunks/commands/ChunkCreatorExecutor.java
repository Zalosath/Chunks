package com.jordna.chunks.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.jordna.chunks.main.Chunks;

import net.md_5.bungee.api.ChatColor;

public class ChunkCreatorExecutor implements CommandExecutor
{

    private Chunks main;

    public ChunkCreatorExecutor(Chunks ins)
    {
	this.main = ins;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String command, String[] args)
    {

	if ((sender instanceof Player))
	{

	    Player player = (Player) sender;

	    if (cmd.getName().equalsIgnoreCase("chunks") || cmd.getName().equalsIgnoreCase("c"))
	    {
		if (args.length != 0)
		{
		    if (args[0].equalsIgnoreCase("default"))
		    {
			if (player.hasPermission("chunks.commands.default.use"))
			{
			    main.getChunk().set("chunks.default-location.X", player.getLocation().getBlockX());
			    main.getChunk().set("chunks.default-location.Y", player.getLocation().getBlockY());
			    main.getChunk().set("chunks.default-location.Z", player.getLocation().getBlockZ());
			    main.getChunk().set("chunks.default-location.WORLD", player.getWorld().getName());

			    main.saveChunk();

			    player.sendMessage(ChatColor.BLUE + "[CHUNKS] New default has been set");

			    return true;
			}
			else
			{
			    player.sendMessage(ChatColor.RED + "[CHUNKS] You do not have access to that command");
			    return false;
			}
		    }

		    Player target = Bukkit.getPlayer(args[0]);
		    if (target != null)
		    {
			if (main.getSettings().getStringList("chunks.commands.c.enabled-worlds")
				.contains(player.getWorld().getName()))
			{
			    if (player.hasPermission("chunks.commands.c.use.other-person"))
			    {
				main.getMenu().openInventory(target);
				return true;
			    }
			    else
			    {
				player.sendMessage(ChatColor.RED + "[CHUNKS] You do not have access to that command");
				return false;
			    }
			}
			else
			{
			    player.sendMessage(ChatColor.RED + "[CHUNKS] That command is not enabled in this world");
			    return false;
			}
		    }
		    else
		    {
			player.sendMessage(ChatColor.RED + "[CHUNKS] Couldn't find that player");
			return false;
		    }
		}
		else
		{
		    if (main.getSettings().getStringList("chunks.commands.c.enabled-worlds")
			    .contains(player.getWorld().getName()))
		    {
			if (player.hasPermission("chunks.commands.c.use"))
			{
			    main.getMenu().openInventory(player);
			    return true;
			}
			else
			{
			    player.sendMessage(ChatColor.RED + "[CHUNKS] You do not have access to that command");
			    return false;
			}
		    }
		    else
		    {
			player.sendMessage(ChatColor.RED + "[CHUNKS] That command is not enabled in this world");
			return false;
		    }
		    /*
		     * if (args.length >= 1) { if (args[0].equalsIgnoreCase("create") ||
		     * args[0].equalsIgnoreCase("c")) { // CREATE A CHUNK FOR THE PLAYER
		     * //main.getChunkCreator().generateChunk(player,
		     * Bukkit.getWorld("chunksworld"), 63, 63, 3); } }
		     */
		}
	    }

	}
	else
	{
	    if (cmd.getName().equalsIgnoreCase("chunks") || cmd.getName().equalsIgnoreCase("c"))
	    {
		if (args.length != 0)
		{
		    Player target = Bukkit.getPlayer(args[0]);
		    if (target != null)
		    {
			main.getMenu().openInventory(target);
			return true;
		    }
		    else
		    {
			System.out.println("Chunks - Target couldn't be found");
			return false;
		    }
		}
	    }
	}

	return false;
    }

}
