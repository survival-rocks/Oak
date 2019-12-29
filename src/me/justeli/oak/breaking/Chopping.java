package me.justeli.oak.breaking;

import java.util.ArrayList;

import me.justeli.oak.time.Execute;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

class Chopping
{
	static void startAnalyze (Material woodType, Block cutBlock, Player p)
	{
		ArrayList<Block> blocks = new ArrayList<>();

		if (new GetValues(cutBlock).getSapling() == null)
			return;

		blocks = addBlocks(blocks, cutBlock);

		// Some smart tree size determinations.
		if (blocks.size() > 10 && !p.hasPermission("trees.large"))
			return;

		startBreak (blocks, p);

		// Saplings!
		final ArrayList<Block> finalBlocks = blocks;
		plantSapling (finalBlocks, woodType);
	}

	private static ArrayList<Block> addBlocks (ArrayList<Block> blocks, Block b)
	{
		TreeBlock block = new GetValues(b);
		for ( Block check : block.getBlocks(GetValues.LoopType.CUBIC, 1) )
		{
			TreeBlock treeBlock = new GetValues(check);
			if ( treeBlock.isWood() && !blocks.contains(check) && treeBlock.hasLeaves())
			{
				blocks.add(check);
				addBlocks(blocks, check);
			}
		}

		return blocks;
	}

	private static void startBreak (ArrayList<Block> blocks, Player p)
	{
		int i = 0;
		for (Block loop : blocks)
		{
			doBreak(loop, p, i);
			i++;
		}
	}

	private static void doBreak (Block block, Player p, int time)
	{
		ItemStack tool = p.getInventory().getItemInMainHand();
		Execute.later( 2 * time, () ->
		{
			TreeBlock treeBlock = new GetValues(block);
			if (treeBlock.isWood())
			{
				block.getLocation().getWorld().spawnParticle(Particle.BLOCK_CRACK,
						block.getLocation().add(0.5, 0.5, 0.5), 50, 0, 0, 0, block.getBlockData());

				block.breakNaturally();
				// TODO: Loop with crack animation on block.
			}
		});

		int durability = tool.getDurability();
		if (durability > tool.getType().getMaxDurability()) tool.setAmount(0);
		else
		{
			int usage = 1;
			if (tool.getEnchantments().containsKey(Enchantment.DURABILITY))
			{
				float level = tool.getEnchantmentLevel(Enchantment.DURABILITY);
				if (Math.random() < 1/(level+1)) usage = 0;
			}
			tool.setDurability((short) (durability + usage));
		}
	}

	private static void plantSapling (final ArrayList<Block> finalBlocks, Material cutBlock)
	{
		Execute.later (finalBlocks.size()*2, () ->
		{
			for (Block sapling : finalBlocks)
				if (new GetValues(sapling).getSapling() != null)
				{
					Block species = sapling.getLocation().getBlock();

					species.setType(toSapling(cutBlock));
					species.getLocation().getWorld().spawnParticle(Particle.CLOUD, species.getLocation().add(0.5, 0.5, 0.5), 30, 0, 0, 0, 0.02);
				}
		});
	}

	private static Material toSapling (Material block)
	{
		switch (block)
		{
			case OAK_LOG:
				return Material.OAK_SAPLING;
			case BIRCH_LOG:
				return Material.BIRCH_SAPLING;
			case SPRUCE_LOG:
				return Material.SPRUCE_SAPLING;
			case JUNGLE_LOG:
				return Material.JUNGLE_SAPLING;
			case ACACIA_LOG:
				return Material.ACACIA_SAPLING;
			case DARK_OAK_LOG:
				return Material.DARK_OAK_SAPLING;
			default:
				return null;
		}
	}

}