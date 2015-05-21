package ganymedes01.aobdbb.configuration;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class BerryBushConfigs {

	private boolean isEnabled = true;
	private Block bush;
	private Item berry;
	private int minY = 0, maxY = 40, maxVeinSize = 4, bushColour;
	private double genChance = 0.01, growthChance = 0.04;
	private int[] dimensionBlacklist, dimensionWhitelist;

	public BerryBushConfigs(String name, double factor) {
		genChance /= factor;
		growthChance /= factor;

		if ("cobalt".equals(name) || "ardite".equals(name) || "iron".equals(name)) {
			dimensionBlacklist = new int[] {};
			dimensionWhitelist = new int[] { -1 };
			minY = 0;
			maxY = 120;
		} else if ("endium".equals(name)) {
			dimensionBlacklist = new int[] {};
			dimensionWhitelist = new int[] { 1 };
			minY = 0;
			maxY = 120;
		} else {
			dimensionBlacklist = new int[] { -1, 1 };
			dimensionWhitelist = new int[] {};
		}
	}

	public boolean isEnabled() {
		return isEnabled;
	}

	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	public Block getBush() {
		return bush;
	}

	public void setBush(Block bush) {
		this.bush = bush;
	}

	public Item getBerry() {
		return berry;
	}

	public void setBerry(Item berry) {
		this.berry = berry;
	}

	public int getMinY() {
		return minY;
	}

	public void setMinY(int minY) {
		this.minY = minY;
	}

	public int getMaxY() {
		return maxY;
	}

	public void setMaxY(int maxY) {
		this.maxY = maxY;
	}

	public int getMaxVeinSize() {
		return maxVeinSize;
	}

	public void setMaxVeinSize(int maxVeinSize) {
		this.maxVeinSize = maxVeinSize;
	}

	public double getGenChance() {
		return genChance;
	}

	public void setGenChance(double genChance) {
		this.genChance = genChance;
	}

	public double getGrowthChance() {
		return growthChance;
	}

	public void setGrowthChance(double growthChance) {
		this.growthChance = growthChance;
	}

	public int getBushColour() {
		return bushColour & 0x00FFFFFF;
	}

	public void setBushColour(int bushColour) {
		this.bushColour = bushColour;
	}

	public int[] getDefaultBlacklistDims() {
		return dimensionBlacklist;
	}

	public int[] getDefaultWhitelistDims() {
		return dimensionWhitelist;
	}

	public void setDimensionBlacklist(int[] dimensionBlacklist) {
		this.dimensionBlacklist = dimensionBlacklist;
	}

	public void setDimensionWhitelist(int[] dimensionWhitelist) {
		this.dimensionWhitelist = dimensionWhitelist;
	}

	public boolean isDimensionAllowed(World world) {
		if (dimensionWhitelist.length != 0)
			return arrayContainsValue(dimensionWhitelist, world.provider.dimensionId);
		else
			return !arrayContainsValue(dimensionBlacklist, world.provider.dimensionId);
	}

	public boolean arrayContainsValue(int[] array, int value) {
		for (int id : array)
			if (id == value)
				return true;
		return false;
	}
}