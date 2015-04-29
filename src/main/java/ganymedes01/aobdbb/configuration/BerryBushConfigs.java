package ganymedes01.aobdbb.configuration;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

public class BerryBushConfigs {

	private boolean isEnabled = true;
	private Block bush;
	private Item berry;
	private int minY = 0, maxY = 256, maxVeinSize = 4, bushColour;
	private double genChance = 0.02F, growthChance = 0.03F;

	public BerryBushConfigs(double factor) {
		genChance /= factor;
		growthChance /= factor;
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
}