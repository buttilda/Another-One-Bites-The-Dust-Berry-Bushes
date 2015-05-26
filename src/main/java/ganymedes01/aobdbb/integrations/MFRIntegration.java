package ganymedes01.aobdbb.integrations;

import ganymedes01.aobdbb.AOBDBBBushBlock;
import ganymedes01.aobdbb.BerryBushAddon;
import ganymedes01.aobdbb.configuration.BerryBushConfigs;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import powercrystals.minefactoryreloaded.api.FactoryRegistry;
import powercrystals.minefactoryreloaded.api.HarvestType;
import powercrystals.minefactoryreloaded.api.IFactoryHarvestable;

public class MFRIntegration {

	public static void registerBushes() {
		for (BerryBushConfigs config : BerryBushAddon.bushMap.values())
			FactoryRegistry.sendMessage("registerHarvestable", new HarvestableOreBush(config.getBush(), config.getBerry()));
	}

	public static class HarvestableOreBush implements IFactoryHarvestable {

		private final Block bush;
		private final Item berry;

		public HarvestableOreBush(Block bush, Item berry) {
			this.bush = bush;
			this.berry = berry;
		}

		@Override
		public Block getPlant() {
			return bush;
		}

		@Override
		public HarvestType getHarvestType() {
			return HarvestType.Column;
		}

		@Override
		public boolean breakBlock() {
			return false;
		}

		@Override
		public boolean canBeHarvested(World world, Map<String, Boolean> harvesterSettings, int x, int y, int z) {
			return world.getBlockMetadata(x, y, z) > AOBDBBBushBlock.MAX_GROWTH_META;
		}

		@Override
		public List<ItemStack> getDrops(World world, Random rand, Map<String, Boolean> harvesterSettings, int x, int y, int z) {
			return Arrays.asList(new ItemStack(berry));
		}

		@Override
		public void preHarvest(World world, int x, int y, int z) {
		}

		@Override
		public void postHarvest(World world, int x, int y, int z) {
			world.setBlockMetadataWithNotify(x, y, z, AOBDBBBushBlock.MAX_GROWTH_META - 1, 2);
		}
	}
}