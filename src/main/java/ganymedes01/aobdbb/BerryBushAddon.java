package ganymedes01.aobdbb;

import ganymedes01.aobd.api.IAOBDAddon;
import ganymedes01.aobd.blocks.AOBDBlock;
import ganymedes01.aobd.items.AOBDItem;
import ganymedes01.aobd.ore.Ore;
import ganymedes01.aobd.ore.OreFinder;
import ganymedes01.aobdbb.lib.Reference;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class BerryBushAddon implements IAOBDAddon {

	private static final List<String> blacklist = Arrays.asList("iron", "gold", "aluminium", "tin", "copper");

	@Override
	public void receiveOreList(Collection<Ore> ores) {
		for (Ore ore : ores) {
			if (blacklist.contains(ore.name()))
				continue;
			String base = "berry";
			AOBDItem berry = new AOBDItem(base, ore) {
				@Override
				protected String getFullName() {
					return "item." + Reference.MOD_ID + "." + base + ore.name() + ".name";
				}

				@Override
				protected String getShortName() {
					return "item." + Reference.MOD_ID + "." + base + ".name";
				}
			};
			berry.setTextureName(Reference.MOD_ID + ":" + base);
			berry.setUnlocalizedName(Reference.MOD_ID + "." + base + ore);

			OreFinder.registerOre(base + ore.name(), berry);

			base = "bush";
			AOBDBlock bush = new AOBDBBBushBlock(base, ore);
			OreFinder.registerOre(base + ore.name(), bush);
		}
	}
}