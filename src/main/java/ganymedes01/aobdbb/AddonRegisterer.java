package ganymedes01.aobdbb;

import ganymedes01.aobd.api.AOBDAddonManager;

public class AddonRegisterer {

	public static void registerAddon() {
		AOBDAddonManager.registerAddon(new BerryBushAddon());
	}
}