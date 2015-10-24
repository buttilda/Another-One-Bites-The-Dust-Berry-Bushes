package ganymedes01.aobdbb;

import ganymedes01.aobd.blocks.AOBDBlock;
import ganymedes01.aobd.ore.Ore;
import ganymedes01.aobdbb.configuration.BerryBushConfigs;
import ganymedes01.aobdbb.lib.Reference;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class AOBDBBBushBlock extends AOBDBlock implements IPlantable {

	public static enum RenderingStage {
		BUSH,
		BERRY,
		BERRY_OVERLAY
	}

	public static final int RENDER_ID = RenderingRegistry.getNextAvailableRenderId();
	public static final int MAX_GROWTH_META = 7;

	@SideOnly(Side.CLIENT)
	private IIcon fancy, fast, berry, berryOverlay;

	public RenderingStage renderingStage = RenderingStage.BUSH;

	private final Item berryItem;
	private final Ore ore;

	public AOBDBBBushBlock(Item berry, String base, Ore ore) {
		super(Material.leaves, base, ore);
		this.ore = ore;
		berryItem = berry;

		setHardness(0.3F);
		setTickRandomly(true);
		setHarvestLevel("axe", 2);
		setStepSound(soundTypeGrass);
		setBlockName(Reference.MOD_ID + "." + base + ore);
		setBlockTextureName(Reference.MOD_ID + ":" + base);
	}

	// Name

	@Override
	protected String getFullName() {
		return "tile." + Reference.MOD_ID + "." + base + ore.name() + ".name";
	}

	@Override
	protected String getShortName() {
		return "tile." + Reference.MOD_ID + "." + base + ".name";
	}

	// Plant stuff

	@Override
	public void updateTick(World world, int x, int y, int z, Random rand) {
		if (world.getBlockLightValue(x, y + 1, z) < 9) {
			int meta = world.getBlockMetadata(x, y, z);
			if (meta <= MAX_GROWTH_META) {
				BerryBushConfigs config = BerryBushAddon.bushMap.get(ore);
				if (config != null && rand.nextDouble() < config.getGrowthChance())
					world.setBlockMetadataWithNotify(x, y, z, ++meta, 2);
			}
		}
	}

	@Override
	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
		if (World.doesBlockHaveSolidTopSurface(world, x, y - 1, z))
			return super.canPlaceBlockAt(world, x, y, z);
		return false;
	}

	@Override
	public boolean canBlockStay(World world, int x, int y, int z) {
		return World.doesBlockHaveSolidTopSurface(world, x, y - 1, z);
	}

	@Override
	public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
		return world.getBlockMetadata(x, y, z) >= MAX_GROWTH_META;
	}

	@Override
	public EnumPlantType getPlantType(IBlockAccess world, int x, int y, int z) {
		return EnumPlantType.Cave;
	}

	@Override
	public Block getPlant(IBlockAccess world, int x, int y, int z) {
		return this;
	}

	@Override
	public int getPlantMetadata(IBlockAccess world, int x, int y, int z) {
		return world.getBlockMetadata(x, y, z);
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block neighbour) {
		if (!canBlockStay(world, x, y, z)) {
			dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
			world.setBlock(x, y, z, Blocks.air, 0, 2); // Need this flag or it can cause an StackOverflow on worldgen.
		}
	}

	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int meta, int fortune) {
		ArrayList<ItemStack> drops = super.getDrops(world, x, y, z, meta, fortune);
		if (meta > MAX_GROWTH_META)
			drops.add(new ItemStack(berryItem));
		return drops;
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		int meta = world.getBlockMetadata(x, y, z);
		if (meta > MAX_GROWTH_META) {
			addToPlayerInventory(player, new ItemStack(berryItem), x, y, z);
			if (!player.capabilities.isCreativeMode)
				world.setBlockMetadataWithNotify(x, y, z, MAX_GROWTH_META, 2);
			return true;
		}

		return false;
	}

	private void addToPlayerInventory(EntityPlayer player, ItemStack stack, double x, double y, double z) {
		if (!player.worldObj.isRemote) {
			EntityItem entity = new EntityItem(player.worldObj, x + 0.5, y, z + 0.5, stack);
			entity.motionX = 0;
			entity.motionY = 0;
			entity.motionZ = 0;
			entity.delayBeforeCanPickup = 0;
			player.worldObj.spawnEntityInWorld(entity);

			entity.onCollideWithPlayer(player);
		}
	}

	// Rendering

	@Override
	@SideOnly(Side.CLIENT)
	public int colorMultiplier(IBlockAccess world, int x, int y, int z) {
		return getRenderColor(world.getBlockMetadata(x, y, z));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderColor(int meta) {
		switch (renderingStage) {
			case BERRY:
				return ore.colour();
			case BUSH:
				BerryBushConfigs config = BerryBushAddon.bushMap.get(ore);
				return config.getBushColour();
			case BERRY_OVERLAY:
				return ore.getColour().darker().darker().getRGB();
			default:
				return 0xFFFFFF;
		}
	}

	@Override
	public int getRenderType() {
		return RENDER_ID;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side) {
		ForgeDirection dir = ForgeDirection.getOrientation(side).getOpposite();
		if (world.getBlockMetadata(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ) < 7)
			return true;
		return Blocks.leaves.isOpaqueCube() && world.getBlock(x, y, z) == this && world.getBlockMetadata(x, y, z) >= MAX_GROWTH_META ? false : super.shouldSideBeRendered(world, x, y, z, side);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		switch (renderingStage) {
			case BERRY:
				return berry;
			case BERRY_OVERLAY:
				return berryOverlay;
			case BUSH:
			default:
				return Blocks.leaves.isOpaqueCube() ? fast : fancy;
		}
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
		int meta = Math.min(MAX_GROWTH_META + 1, world.getBlockMetadata(x, y, z) + 1);
		float rate = (float) meta / (float) (MAX_GROWTH_META + 1);
		float t = (1F - rate) / 2F;
		setBlockBounds(t, 0, t, rate + t, rate, rate + t);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
		return null;
	}

	@Override
	public void setBlockBoundsForItemRender() {
		setBlockBounds(0, 0, 0, 1, 1, 1);
	}

	@Override
	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
		if (AOBDBB.doBushesPrick && entity instanceof EntityLivingBase)
			entity.attackEntityFrom(DamageSource.cactus, 1.0F);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister reg) {
		fancy = reg.registerIcon(getTextureName() + "_fancy");
		fast = reg.registerIcon(getTextureName() + "_fast");
		berry = reg.registerIcon(getTextureName() + "_berries");
		berryOverlay = reg.registerIcon(getTextureName() + "_berries_overlay");
	}
}