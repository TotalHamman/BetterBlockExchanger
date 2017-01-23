package totalhamman.betterblockexchanger.handlers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import totalhamman.betterblockexchanger.items.ItemExchanger;

import java.util.List;

public class RenderOverlayHandler {

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void RenderWorldLastEvent(RenderWorldLastEvent event) {

        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        World world = player.getEntityWorld();
        ItemStack stack = player.getHeldItemMainhand();
        Minecraft mc = Minecraft.getMinecraft();
        float partialTicks = event.getPartialTicks();

        RayTraceResult mouseOver = mc.objectMouseOver;

        if (stack != null && stack.getItem() instanceof ItemExchanger && mouseOver != null && mouseOver.getBlockPos() != null && mouseOver.sideHit != null && stack.getTagCompound() != null) {
            List<BlockPos> blocks = BlockExchangeHandler.getBlocksToExchange(stack, mouseOver.getBlockPos(), world, mc.objectMouseOver.sideHit);

            Tessellator tessellator = Tessellator.getInstance();
            VertexBuffer buffer = tessellator.getBuffer();

            double offsetX = player.prevPosX + (player.posX - player.prevPosX) * (double) partialTicks;
            double offsetY = player.prevPosY + (player.posY - player.prevPosY) * (double) partialTicks;
            double offsetZ = player.prevPosZ + (player.posZ - player.prevPosZ) * (double) partialTicks;

            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.color(1F, 1F, 1F, 1F);
            GlStateManager.glLineWidth(2.0F);
            GlStateManager.disableTexture2D();

            for (BlockPos block : blocks) {
                if (world.isAirBlock(block)) {
                    continue;
                }

                double renderX = block.getX() - offsetX;
                double renderY = block.getY() - offsetY;
                double renderZ = block.getZ() - offsetZ;

                AxisAlignedBB boundingBox = new AxisAlignedBB(renderX, renderY, renderZ, renderX + 1, renderY + 1, renderZ + 1).expand(0.001, 0.001, 0.001);
                float colour = 1F;
                if (!world.getBlockState(block.offset(mc.objectMouseOver.sideHit)).getBlock().isReplaceable(world, block.offset(mc.objectMouseOver.sideHit))) {
                    GlStateManager.disableDepth();
                    colour = 0.2F;
                }

                buffer.begin(3, DefaultVertexFormats.POSITION_COLOR);
                buffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).color(colour, colour, colour, colour).endVertex();
                buffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).color(colour, colour, colour, colour).endVertex();
                buffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).color(colour, colour, colour, colour).endVertex();
                buffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).color(colour, colour, colour, colour).endVertex();
                buffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).color(colour, colour, colour, colour).endVertex();
                tessellator.draw();
                buffer.begin(3, DefaultVertexFormats.POSITION_COLOR);
                buffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).color(colour, colour, colour, colour).endVertex();
                buffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).color(colour, colour, colour, colour).endVertex();
                buffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).color(colour, colour, colour, colour).endVertex();
                buffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).color(colour, colour, colour, colour).endVertex();
                buffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).color(colour, colour, colour, colour).endVertex();
                tessellator.draw();
                buffer.begin(1, DefaultVertexFormats.POSITION_COLOR);
                buffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).color(colour, colour, colour, colour).endVertex();
                buffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).color(colour, colour, colour, colour).endVertex();
                buffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).color(colour, colour, colour, colour).endVertex();
                buffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).color(colour, colour, colour, colour).endVertex();
                buffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).color(colour, colour, colour, colour).endVertex();
                buffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).color(colour, colour, colour, colour).endVertex();
                buffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).color(colour, colour, colour, colour).endVertex();
                buffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).color(colour, colour, colour, colour).endVertex();
                tessellator.draw();

                if (!world.getBlockState(block.offset(mc.objectMouseOver.sideHit)).getBlock().isReplaceable(world, block.offset(mc.objectMouseOver.sideHit))) {
                    GlStateManager.enableDepth();
                }
            }

            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
        }

    }

}
