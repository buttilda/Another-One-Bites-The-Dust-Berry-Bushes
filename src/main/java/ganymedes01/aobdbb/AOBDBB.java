package ganymedes01.aobdbb;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import ganymedes01.aobdbb.configuration.ConfigHandler;
import ganymedes01.aobdbb.integrations.MFRIntegration;
import ganymedes01.aobdbb.lib.Reference;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.BonemealEvent;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION_NUMBER, dependencies = Reference.DEPENDENCIES, guiFactory = Reference.GUI_FACTORY_CLASS)
public class AOBDBB {

	@Instance(Reference.MOD_ID)
	public static AOBDBB instance;

	public static boolean doBushesPrick = false;
	public static boolean canBushedBeBonemealed = false;

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

		if (Loader.isModLoaded("MineFactoryReloaded"))
			MFRIntegration.registerBushes();
	}

	@SubscribeEvent
	public void onBonemeal(BonemealEvent event) {
		World world = event.world;
		int x = event.x;
		int y = event.y;
		int z = event.z;

		if (world.getBlock(x, y, z) instanceof AOBDBBBushBlock) {
			int meta = world.getBlockMetadata(x, y, z);
			int maxMeta = AOBDBB.canBushedBeBonemealed ? AOBDBBBushBlock.MAX_GROWTH_META + 1 : AOBDBBBushBlock.MAX_GROWTH_META;
			if (meta < maxMeta) {
				world.setBlockMetadataWithNotify(x, y, z, meta + 1, 2);
				event.setResult(Result.ALLOW);
			}
		}
	}
}