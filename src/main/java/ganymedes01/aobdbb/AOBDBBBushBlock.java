package ganymedes01.aobdbb;

import ganymedes01.aobd.blocks.AOBDBlock;
import ganymedes01.aobd.ore.Ore;
import ganymedes01.aobdbb.lib.Reference;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class AOBDBBBushBlock extends AOBDBlock implements IPlantable, IGrowable {

	@SideOnly(Side.CLIENT)
	private IIcon[] fancyIcons;
	@SideOnly(Side.CLIENT)
	private IIcon[] fastIcons;

	public AOBDBBBushBlock(String base, Ore ore) {
		super(Material.leaves, base, ore);
		setHardness(0.3F);
		setTickRandomly(true);
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
			if (meta < 7)
				if (rand.nextInt(26) == 0)
					world.setBlockMetadataWithNotify(x, y, z, ++meta, 2);
		}
	}

	@Override
	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
		if (world.getFullBlockLightValue(x, y, z) < 13)
			if (world.getBlock(x, y - 1, z).isSideSolid(world, x, y - 1, z, ForgeDirection.UP))
				return super.canPlaceBlockAt(world, x, y, z);
		return false;
	}

	@Override
	public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
		return world.getBlockMetadata(x, y, z) >= 7;
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

	// Growable

	@Override
	public boolean func_149851_a(World p_149851_1_, int p_149851_2_, int p_149851_3_, int p_149851_4_, boolean p_149851_5_) {
		return p_149851_1_.getBlockMetadata(p_149851_2_, p_149851_3_, p_149851_4_) != 7;
	}

	@Override
	public boolean func_149852_a(World p_149852_1_, Random p_149852_2_, int p_149852_3_, int p_149852_4_, int p_149852_5_) {
		return true;
	}

	@Override
	public void func_149853_b(World world, Random rand, int x, int y, int z) {
		int meta = Math.min(7, world.getBlockMetadata(x, y, z) + MathHelper.getRandomIntegerInRange(rand, 2, 5));
		world.setBlockMetadataWithNotify(x, y, z, meta, 2);
	}

	// Rendering

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
		return Blocks.leaves.isOpaqueCube() && world.getBlock(x, y, z) == this ? false : super.shouldSideBeRendered(world, x, y, z, side);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		IIcon[] icons = Blocks.leaves.isOpaqueCube() ? fastIcons : fancyIcons;
		return icons[meta == 7 ? 1 : 0];
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
		int meta = world.getBlockMetadata(x, y, z);
		if (meta % 2 == 0)
			meta++;
		float rate = 1 - (meta + 1) * 2 / 16F;
		setBlockBounds(rate / 2F, 0, rate / 2F, 1 - rate / 2F, 1 - rate, 1 - rate / 2F);
	}

	@Override
	public void setBlockBoundsForItemRender() {
		setBlockBounds(0, 0, 0, 1, 1, 1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister reg) {
		fancyIcons = new IIcon[2];
		fastIcons = new IIcon[2];
		fancyIcons[0] = reg.registerIcon(getTextureName() + "_fancy");
		fastIcons[0] = reg.registerIcon(getTextureName() + "_fast");
		fancyIcons[1] = reg.registerIcon(getTextureName() + "_ripe_fancy");
		fastIcons[1] = reg.registerIcon(getTextureName() + "_ripe_fast");
	}
}