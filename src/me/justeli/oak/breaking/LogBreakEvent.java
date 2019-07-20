package me.justeli.oak.breaking;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class LogBreakEvent implements Listener
{
	@EventHandler (ignoreCancelled = true)
	public void onWoodBreak (BlockBreakEvent e)
	{
		if (!e.isCancelled())
		{
			TreeBlock log = new GetValues(e.getBlock());
			if (log.isWood())
			{
				Player p = e.getPlayer();
				if (p.hasPermission("trees.cut") && p.getGameMode().equals(GameMode.SURVIVAL))
				{
					Material item = p.getInventory().getItemInMainHand().getType();
					Block start = e.getBlock();

					if (	item.equals(Material.DIAMOND_AXE) ||
							item.equals(Material.GOLDEN_AXE) ||
							item.equals(Material.IRON_AXE) ||
							item.equals(Material.STONE_AXE)	)
					{
						Chopping.startAnalyze (start.getType(), start, p);
					}

				}
			}
		}
	}

}
