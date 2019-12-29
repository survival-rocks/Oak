package me.justeli.oak.breaking;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.ArrayList;

/**
 * Created by Eli on 4/24/2017.
 * Spigot Plugins: me.justeli.oak.breaking
 */

public class GetValues implements TreeBlock
{
    private Block block;
    GetValues (Block block)
    {
        this.block = block;
    }

    @Override
    public boolean isWood ()
    {
        Material material = block.getType();
        return material.equals(Material.OAK_LOG)
                || material.equals(Material.BIRCH_LOG)
                || material.equals(Material.SPRUCE_LOG)
                || material.equals(Material.JUNGLE_LOG)
                || material.equals(Material.DARK_OAK_LOG)
                || material.equals(Material.ACACIA_LOG);
    }

    @Override
    public boolean isLeaves ()
    {
        Material material = block.getType();
        return material.equals(Material.OAK_LEAVES)
                || material.equals(Material.BIRCH_LEAVES)
                || material.equals(Material.SPRUCE_LEAVES)
                || material.equals(Material.JUNGLE_LEAVES)
                || material.equals(Material.DARK_OAK_LEAVES)
                || material.equals(Material.ACACIA_LEAVES);
    }

    @Override
    public Block getSapling ()
    {
        Block sapling = null;

        for ( Block below : getBlocks (LoopType.UNDER, 10) )
        {
            Material l = below.getType();
            TreeBlock treeBlock = new GetValues(below);
            if (treeBlock.isWood())
                continue;

            if (l.equals(Material.DIRT) || l.equals(Material.GRASS_BLOCK) || l.equals(Material.PODZOL))
                sapling = below.getLocation().add(0, 1, 0).getBlock();

            else break;
        }

        return sapling;
    }

    enum LoopType
    {
        FLAT,
        CUBIC,
        UNDER,
        ABOVE,
    }

    @Override
    public boolean hasLeaves ()
    {
        int air=0, top=0;

        for (Block part : getBlocks(LoopType.CUBIC, 2) )
        {
            TreeBlock treeBlock = new GetValues(part);
            if (treeBlock.isLeaves())
                top++;

        }

        if (top >= 5)
            return true;

        final int current = top;

        for (Block above : getBlocks(LoopType.ABOVE, 50))
        {
            TreeBlock treeBlock = new GetValues(above);
            if (treeBlock.isWood())
                continue;

            if (treeBlock.isLeaves())
            {
                if (top == current)
                    block = above.getLocation().subtract(0, 1, 0).getBlock();

                top++;
            }

            else air++;

            if (air > 2)
                break;
        }

        for (Block part : getBlocks(LoopType.CUBIC, 1) )
        {
            TreeBlock treeBlock = new GetValues(part);
            if (treeBlock.isLeaves())
                top++;

        }

        return top >= 5 && air <= 3;
    }

    @Override
    public ArrayList<Block> getBlocks (LoopType loopType, int radius)
    {
        ArrayList<Block> blocks = new ArrayList<>();
        double sX = block.getLocation().getX();
        double sY = block.getLocation().getY();
        double sZ = block.getLocation().getZ();

        if (loopType.equals(LoopType.UNDER))
        {
            for (double y = sY -1; y >= sY - radius; y--)
            {
                Location loc = new Location(block.getWorld(), sX, y, sZ);
                blocks.add(loc.getBlock());
            }
        }

        else if (loopType.equals(LoopType.ABOVE))
        {
            for (double y = sY +1; y <= sY + radius; y++)
            {
                Location loc = new Location(block.getWorld(), sX, y, sZ);
                blocks.add(loc.getBlock());
            }
        }

        else if (loopType.equals(LoopType.FLAT))
        {
            for (double x = sX - radius; x <= sX + radius; x++)
            {
                for(double z = sZ - radius; z <= sZ + radius; z++)
                {
                    Location loc = new Location(block.getWorld(), x, sY, z);
                    blocks.add(loc.getBlock());
                }
            }
        }

        else if (loopType.equals(LoopType.CUBIC))
        {
            for (double x = sX - radius; x <= sX + radius; x++)
            {
                for (double y = sY - radius; y <= sY + radius; y++)
                {
                    for(double z = sZ - radius; z <= sZ + radius; z++)
                    {
                        Location loc = new Location(block.getWorld(), x, y, z);
                        if (!loc.getBlock().equals(block))
                            blocks.add(loc.getBlock());
                    }
                }
            }
        }

        return blocks;
    }

}
