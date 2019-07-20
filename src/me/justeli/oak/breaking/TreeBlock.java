package me.justeli.oak.breaking;

import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.ArrayList;

/**
 * Created by Eli on 6/25/2017.
 * spigotPlugins: me.justeli.oak.breaking
 */
public interface TreeBlock
{
    Block getSapling ();

    boolean hasLeaves ();

    boolean isWood ();

    boolean isLeaves ();

    ArrayList<Block> getBlocks (GetValues.LoopType loopType, int radius);
}
