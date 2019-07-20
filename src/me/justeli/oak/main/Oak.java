package me.justeli.oak.main;

import me.justeli.oak.breaking.LogBreakEvent;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Eli on 4/16/2017.
 * Spigot Plugins: me.justeli.trees.main
 */
public class Oak extends JavaPlugin
{
    public static Oak main;

    public void onEnable ()
    {
        main = this;
        registerEvents();
    }

    private void registerEvents ()
    {
        register (new Listener[]
        {
            new LogBreakEvent()
        });
    }

    private void register (Listener[] listener)
    {
        PluginManager m = getServer().getPluginManager();

        for (Listener lis : listener)
            m.registerEvents(lis, this);
    }
}
