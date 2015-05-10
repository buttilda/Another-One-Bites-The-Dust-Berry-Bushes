package ganymedes01.aobdbb;

import ganymedes01.aobdbb.configuration.ConfigHandler;
import ganymedes01.aobdbb.lib.Reference;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION_NUMBER, dependencies = Reference.DEPENDENCIES, guiFactory = Reference.GUI_FACTORY_CLASS)
public class AOBDBB {

	@Instance(Reference.MOD_ID)
	public static AOBDBB instance;

	public static boolean doBushesPrick = false;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		ConfigHandler.INSTANCE.preInit(event.getSuggestedConfigurationFile());
		FMLCommonHandler.instance().bus().register(ConfigHandler.INSTANCE);

		AddonRegisterer.registerAddon();

		if (event.getSide() == Side.CLIENT)
			RenderingRegistry.registerBlockHandler(new BushBlockRenderer());

		MinecraftForge.EVENT_BUS.register(this);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		ConfigHandler.INSTANCE.init();

		GameRegistry.registerWorldGenerator(new AOBDBBWorldGenerator(), 0);
	}
}