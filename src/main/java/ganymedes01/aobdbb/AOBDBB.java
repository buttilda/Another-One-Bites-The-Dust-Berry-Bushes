package ganymedes01.aobdbb;

import ganymedes01.aobd.api.AOBDAddonManager;
import ganymedes01.aobdbb.lib.Reference;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION_NUMBER, dependencies = Reference.DEPENDENCIES)//, guiFactory = Reference.GUI_FACTORY_CLASS)
public class AOBDBB {

	@Instance(Reference.MOD_ID)
	public static AOBDBB instance;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		AOBDAddonManager.registerAddon(new BerryBushAddon());
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		GameRegistry.registerWorldGenerator(new AOBDBBWorldGenerator(), 0);
	}
}