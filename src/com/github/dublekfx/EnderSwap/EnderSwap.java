package com.github.dublekfx.EnderSwap;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class EnderSwap extends JavaPlugin	implements Listener	{
	
	private Player pShooter, pTarget;
	private Location pShooterLoc;
	private Projectile pearl;
	private boolean swapToggle = true;
	
	@Override
	public void onEnable()	{
		getLogger().info("EnderSwap is " + swapToggle);
	}
		
	
	@Override
	public void onDisable()	{
		
	}
	
	public boolean onCommand (CommandSender sender, Command cmd, String label, String[] args)	{
		if (cmd.getName().equalsIgnoreCase("enderswap"))	{
			if ( (sender instanceof Player && sender.hasPermission("EnderSwap.toggle")) || !(sender instanceof Player) )	{
				swapToggle = (swapToggle == true) ? false : true;
				sender.sendMessage("EnderSwap is " + swapToggle);
				getLogger().info("EnderSwap is " + swapToggle);
				return true;
			}
		}
		return false;
	}
	
	@EventHandler
	public void onEnderpearl(ProjectileHitEvent	event)	{
		if (swapToggle == true)	{
			pearl = event.getEntity();
			LivingEntity shoot = (Player) pearl.getShooter();				//Determine the shooter
			if(shoot.getType().equals(EntityType.PLAYER))	{
				pShooter = (Player) shoot;
			}		
			List<Entity> nearEntity = pearl.getNearbyEntities(.5, .5, .5);	//Choose a target
			ArrayList<Player> pList = new ArrayList<>();
			for(Entity e : nearEntity)	{
				if(e.getType().equals(EntityType.PLAYER))	{
					Player pNear = (Player) e;
					pList.add(pNear);
				}
			}
			if(pList.size() == 1)	{
				pTarget = pList.get(0);
			}
			else	{
				int i = (int) (pList.size() * Math.random());
				pTarget = pList.get(i);
			}
			pShooterLoc = pShooter.getLocation();
			if(!(pTarget.isSneaking()))	{
				pTarget.teleport(pShooterLoc);
			}
		}
	}
}
