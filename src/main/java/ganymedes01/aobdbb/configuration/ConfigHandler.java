package ganymedes01.aobdbb.configuration;

import ganymedes01.aobd.lib.Reference;
import ganymedes01.aobd.ore.Ore;
import ganymedes01.aobdbb.BerryBushAddon;

import java.io.File;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class ConfigHandler {

	public static ConfigHandler INSTANCE = new ConfigHandler();
	public Configuration configFile;
	public Set<String> usedCategories = new HashSet<String>();

	public void preInit(File file) {
		configFile = new Configuration(file, true);
	}

	public void init() {
		usedCategories.clear();
		for (Entry<Ore, BerryBushConfigs> entry : BerryBushAddon.bushMap.entrySet())
			init(entry.getKey().name(), entry.getValue());
	}

	public BerryBushConfigs init(Ore ore) {
		String name = ore.name();

		BerryBushConfigs config = new BerryBushConfigs((float) ore.energy(1));
		init(name, config);

		return config;
	}

	public void init(String name, BerryBushConfigs config) {
		config.setEnabled(getBoolean(name, "Enabled", true, true));
		config.setMinY(getInt(name, "Min Y", config.getMinY()));
		config.setMaxY(getInt(name, "Max Y", config.getMaxY()));
		config.setMaxVeinSize(getInt(name, "Max vein size", config.getMaxVeinSize()));
		config.setGenChance(getDouble(name, "Gen chance", config.getGenChance()));
		config.setGenChance(getDouble(name, "Growth chance", config.getGrowthChance()));
		usedCategories.add(name);

		if (configFile.hasChanged())
			configFile.save();
	}

	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs) {
		if (Reference.MOD_ID.equals(eventArgs.modID)) {
			configFile.load();
			init();
		}
	}

	private boolean getBoolean(String category, String name, boolean requiresRestart, boolean def) {
		return configFile.get(category, name, def).setRequiresMcRestart(requiresRestart).getBoolean(def);
	}

	private double getDouble(String category, String name, double def) {
		return configFile.get(category, name, def).getDouble(def);
	}

	private int getInt(String category, String name, int def) {
		return configFile.get(category, name, def).getInt(def);
	}
}