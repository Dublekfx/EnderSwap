package com.github.dublekfx.EnderSwap;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.plugin.java.JavaPlugin;

public final class EnderSwap extends JavaPlugin	implements Listener	{
	
	private Player pShooter, pTarget;
	private Location pShooterLoc, pTargetLoc;
	private Projectile pearl;
	private boolean swapToggle = true, pendingSwap = false;
	
	@Override
	public void onEnable()	{
		getServer().getPluginManager().registerEvents(this, this);
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
	
/*	@EventHandler 
	public void onEnderpearl(ProjectileHitEvent	event)	{
		if(event.getEntityType().equals(EntityType.ENDER_PEARL))	{
		//getLogger().info("event");
		if (swapToggle == true)	{
			pearl = event.getEntity();
			LivingEntity shoot = (Player) pearl.getShooter();				//Determine the shooter
			if(shoot.getType().equals(EntityType.PLAYER))	{
				pShooter = (Player) shoot;
			}
			//getLogger().info("Shooter is " + pShooter.getName());
		/*	List<Entity> nearEntity = pearl.getNearbyEntities(1, 1, 1);	//Choose a target
			getLogger().info(nearEntity.toString());
			ArrayList<Player> pList = new ArrayList<>();
			for(Entity e : nearEntity)	{
				if(e.getType().equals(EntityType.PLAYER))	{
					Player pNear = (Player) e;
					pList.add(pNear);
				}
			}
			if(pList.contains(pShooter))	{
				pList.remove(pShooter);
			}
			if(pList.size() == 1)	{
				pTarget = pList.get(0);
			}
			else	{
				int i = (int) (pList.size() * Math.random());
				pTarget = pList.get(i);
			}
			//getLogger().info("Target is " + pTarget.getName());
			pShooterLoc = pShooter.getLocation();
			pTargetLoc = pTarget.getLocation();
			if(!(pTarget.isSneaking()))	{
				pTarget.teleport(pShooterLoc);
			}
			pShooter.teleport(pTargetLoc);
			getLogger().info(pShooter.getName() + " has swapped with " + pTarget.getName());
			pTarget = null;
			pendingSwap = false;
		}
	}*/
	
	@EventHandler
	public void onPlayerDamage(EntityDamageByEntityEvent event)	{
		if(event.getEntityType().equals(EntityType.PLAYER) && event.getCause().equals(DamageCause.PROJECTILE) && event.getDamager().getType().equals(EntityType.ENDER_PEARL))	{
			pearl = (Projectile) event.getDamager();
			LivingEntity shoot = (Player) pearl.getShooter();				//Determine the shooter
			if(shoot.getType().equals(EntityType.PLAYER))	{
				pShooter = (Player) shoot;
			}			
			pTarget = (Player) event.getEntity();
			pShooterLoc = pShooter.getLocation();
			pTargetLoc = pTarget.getLocation();
			if(!(pTarget.isSneaking()))	{
				pTarget.teleport(pShooterLoc);
			}
			pShooter.teleport(pTargetLoc);
			getLogger().info(pShooter.getName() + " has swapped with " + pTarget.getName());
		}
	}
	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent event)	{
		if(event.getCause().equals(TeleportCause.ENDER_PEARL) && swapToggle == true && pendingSwap == true)	{
			event.setCancelled(true);
		}
	}
	
}
