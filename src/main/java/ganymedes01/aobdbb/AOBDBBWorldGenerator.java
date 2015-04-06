package ganymedes01.aobdbb;

import ganymedes01.aobd.ore.Ore;
import ganymedes01.aobdbb.configuration.BerryBushConfigs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.IChunkProvider;
import cpw.mods.fml.common.IWorldGenerator;

public class AOBDBBWorldGenerator implements IWorldGenerator {

	private final List<BlockPos> positions = new ArrayList<BlockPos>();

	public AOBDBBWorldGenerator() {
		for (int i = 0; i <= 1; i++)
			for (int j = 0; j <= 1; j++)
				for (int k = 0; k <= 1; k++)
					positions.add(new BlockPos(i, j, k));

		for (int i = -1; i <= 2; i++)
			for (int k = -1; k <= 2; k++)
				positions.add(new BlockPos(i, 0, k));
	}

	@Override
	public void generate(Random rand, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		if (world.provider.terrainType != WorldType.FLAT)
			generateBushes(rand, chunkX * 16, chunkZ * 16, world);
	}

	private void generateBushes(Random rand, int xPos, int zPos, World world) {
		for (Entry<Ore, BerryBushConfigs> entry : BerryBushAddon.bushMap.entrySet()) {
			BerryBushConfigs config = entry.getValue();
			if (!config.isEnabled())
				continue;
			Block bush = config.getBush();
			int minY = config.getMinY();
			int maxY = config.getMaxY();
			int veinMaxSize = config.getMaxVeinSize();
			double genChance = config.getGenChance();

			if (rand.nextDouble() <= genChance) {
				int x = xPos + rand.nextInt(16);
				int y = minY;
				int z = zPos + rand.nextInt(16);
				// Go from bottom to top trying to find an air block
				while (y < maxY) {
					if (isAir(world, x, y, z) && bush.canPlaceBlockAt(world, x, y, z)) {
						// When an air block is found, look around it for other air blocks
						int airsFound = 0;
						for (BlockPos pos : positions)
							if (isAir(world, x + pos.x, y + pos.y, z + pos.z))
								if (++airsFound >= 10)
									break;
						// If the amount of "airs" found is bigger than 10, generate the bushes
						if (airsFound >= 10) {
							int set = 0;
							for (BlockPos pos : positions)
								if (isAir(world, x + pos.x, y + pos.y, z + pos.z) && rand.nextFloat() >= 0.5F)
									if (world.setBlock(x + pos.x, y + pos.y, z + pos.z, bush, 7, 2))
										if (++set >= veinMaxSize || rand.nextFloat() >= 0.8F && set > 0)
											break;
							break;
						}
					}
					y++;
				}
			}
		}
	}

	private boolean isAir(World world, int x, int y, int z) {
		return world.getBlock(x, y, z).isAir(world, x, y, z);
	}

	class BlockPos {

		final int x, y, z;

		BlockPos(int x, int y, int z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}
	}
}