package me.justeli.oak.time;

import me.justeli.oak.main.Oak;
import org.bukkit.Bukkit;

/**
 * Created by Eli on 4/16/2017.
 * Spigot Plugins: me.justeli.trees.time
 */

public class Execute
{
    public static void later (int ticks, Runnable runnable)
    {
        Bukkit.getScheduler().scheduleSyncDelayedTask(Oak.main, runnable, ticks);
    }
}
