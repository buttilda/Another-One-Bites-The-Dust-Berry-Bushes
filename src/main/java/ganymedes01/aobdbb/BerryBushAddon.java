package ganymedes01.aobdbb;

import ganymedes01.aobd.api.IAOBDAddon;
import ganymedes01.aobd.blocks.AOBDBlock;
import ganymedes01.aobd.items.AOBDItem;
import ganymedes01.aobd.ore.Ore;
import ganymedes01.aobd.ore.OreFinder;
import ganymedes01.aobd.recipes.RecipesModule;
import ganymedes01.aobdbb.configuration.BerryBushConfigs;
import ganymedes01.aobdbb.configuration.ConfigHandler;
import ganymedes01.aobdbb.lib.Reference;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BerryBushAddon implements IAOBDAddon {

	public static Map<Ore, BerryBushConfigs> bushMap = new HashMap<Ore, BerryBushConfigs>();

	@Override
	public void receiveOreList(Collection<Ore> ores) {
		for (Ore ore : ores) {
			if (!ore.isEnabled())
				continue;
			if (!OreDictionary.getOres("oreberry" + ore.name(), false).isEmpty())
				continue;
			if (!OreDictionary.getOres("orebush" + ore.name(), false).isEmpty())
				continue;

			BerryBushConfigs config = ConfigHandler.INSTANCE.init(ore);
			bushMap.put(ore, config);
			if (!config.isEnabled())
				continue;

			// Create the berry item
			String base = "oreberry";
			AOBDItem berry = new AOBDItem(base, ore) {
				@Override
				protected String getFullName() {
					return "item." + Reference.MOD_ID + "." + base + ore.name() + ".name";
				}

				@Override
				protected String getShortName() {
					return "item." + Reference.MOD_ID + "." + base + ".name";
				}

				@Override
				@SideOnly(Side.CLIENT)
				public int getColorFromItemStack(ItemStack stack, int pass) {
					return pass == 0 ? ore.colour() : ore.getColour().darker().darker().getRGB() & 0x00FFFFFF;
				}
			};
			berry.setTextureName(Reference.MOD_ID + ":" + base);
			berry.setUnlocalizedName(Reference.MOD_ID + "." + base + ore);
			OreFinder.registerOre(base + ore.name(), berry);
			config.setBerry(berry);

			// Create the bush block
			base = "orebush";
			AOBDBlock bush = new AOBDBBBushBlock(berry, base, ore);
			OreFinder.registerOre(base + ore.name(), bush);
			config.setBush(bush);

			// Create (if necessary) the nugget item
			ItemStack nugget;
			if (OreDictionary.getOres("nugget" + ore.name()).isEmpty()) {
				AOBDItem nuggetItem = new AOBDItem("nugget", ore);
				nugget = new ItemStack(nuggetItem);
				OreFinder.registerOre("nugget" + ore.name(), nuggetItem);

				GameRegistry.addShapedRecipe(RecipesModule.getOreStack("ingot", ore), "xxx", "xxx", "xxx", 'x', "nugget" + ore.name());
				GameRegistry.addShapelessRecipe(new ItemStack(nuggetItem, 1, 9), "ingot" + ore.name());
			} else
				nugget = RecipesModule.getOreStack("nugget", ore);

			// Add smelting recipe
			GameRegistry.addSmelting(berry, nugget, 0.1F);
		}
	}
}