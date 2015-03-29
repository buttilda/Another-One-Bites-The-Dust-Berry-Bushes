package ganymedes01.aobdbb;

import ganymedes01.aobd.api.IAOBDAddon;
import ganymedes01.aobd.items.AOBDItem;
import ganymedes01.aobd.ore.Ore;
import ganymedes01.aobd.ore.OreFinder;
import ganymedes01.aobdbb.lib.Reference;

import java.util.Collection;

public class BerryBushAddon implements IAOBDAddon {

	@Override
	public void receiveOreList(Collection<Ore> ores) {
		for (Ore ore : ores) {
			String base = "berry";
			AOBDItem berry = new AOBDItem(base, ore);
			berry.setTextureName(Reference.MOD_ID + ":" + base);
			berry.setUnlocalizedName(Reference.MOD_ID + "." + base + ore);
			
			OreFinder.registerOre(base + ore.name(), berry);
		}
	}
}