package ganymedes01.aobdbb;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ganymedes01.aobdbb.AOBDBBBushBlock.RenderingStage;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;

@SideOnly(Side.CLIENT)
public class BushBlockRenderer implements ISimpleBlockRenderingHandler {

	@Override
	public void renderInventoryBlock(Block block, int meta, int modelID, RenderBlocks renderer) {
		if (block instanceof AOBDBBBushBlock) {
			AOBDBBBushBlock bush = (AOBDBBBushBlock) block;
			RenderingStage old = bush.renderingStage;
			bush.renderingStage = RenderingStage.BUSH;

			Tessellator tessellator = Tessellator.instance;
			GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

			renderer.setRenderBounds(0, 0, 0, 1, 1, 1);
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, -1.0F, 0.0F);
			renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 0, meta));
			tessellator.setNormal(0.0F, 1.0F, 0.0F);
			renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 1, meta));
			tessellator.setNormal(0.0F, 0.0F, -1.0F);
			renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 2, meta));
			tessellator.setNormal(0.0F, 0.0F, 1.0F);
			renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 3, meta));
			tessellator.setNormal(-1.0F, 0.0F, 0.0F);
			renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 4, meta));
			tessellator.setNormal(1.0F, 0.0F, 0.0F);
			renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 5, meta));
			tessellator.draw();

			bush.renderingStage = old;
		}
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		boolean rendered = false;

		if (block instanceof AOBDBBBushBlock) {
			AOBDBBBushBlock bush = (AOBDBBBushBlock) block;
			bush.renderingStage = RenderingStage.BUSH;
			rendered |= renderer.renderStandardBlock(bush, x, y, z);

			if (world.getBlockMetadata(x, y, z) > AOBDBBBushBlock.MAX_GROWTH_META) {
				bush.renderingStage = RenderingStage.BERRY;
				rendered |= renderer.renderStandardBlock(bush, x, y, z);
				bush.renderingStage = RenderingStage.BERRY_OVERLAY;
				rendered |= renderer.renderStandardBlock(bush, x, y, z);
				bush.renderingStage = RenderingStage.BUSH;
			}
		}

		return rendered;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return true;
	}

	@Override
	public int getRenderId() {
		return AOBDBBBushBlock.RENDER_ID;
	}
}