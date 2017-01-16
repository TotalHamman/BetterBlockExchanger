package totalhamman.betterblockexchanger.items;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import totalhamman.betterblockexchanger.BetterBlockExchanger;
import totalhamman.betterblockexchanger.handlers.BlockExchangeHandler;

import static totalhamman.betterblockexchanger.BetterBlockExchanger.debugOn;

public class ItemExchanger extends ItemMod {

    public ItemExchanger() {
        super();
        this.setMaxStackSize(1);
        this.setUnlocalizedName("exchanger");
        this.setCreativeTab(BetterBlockExchanger.TabBetterBlockExchanger);
        this.setMaxDamage(1728);
        this.setNoRepair();
        this.setRegistryName(this.getUnlocalizedName().substring(5));
        GameRegistry.register(this);
    }

    public EnumActionResult onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (world.isRemote) {
            return EnumActionResult.PASS;
        }

        player.swingArm(hand);
        IBlockState prevState = world.getBlockState(pos);

        if (player.isSneaking()) {
            String blockName = Block.REGISTRY.getNameForObject(prevState.getBlock()).toString();

            if (BlockExchangeHandler.BlockSuitableForSelection(stack, player, world, pos)) {
                player.addChatMessage(new TextComponentString("Selected Block - " + blockName));
                BlockExchangeHandler.SetSelectedBlock(stack, player, world, pos, facing);
            } else {
                player.addChatMessage(new TextComponentString("Invalid Selected Block - " + blockName));
                return EnumActionResult.FAIL;
            }
        } else {
            if (BlockExchangeHandler.BlockSuitableForExchange(stack, player, world, pos)) {
                BlockExchangeHandler.ExchangeBlocks(stack, player, world, pos, facing);
            } else {
                return EnumActionResult.FAIL;
            }
        }

        return EnumActionResult.SUCCESS;
    }
}
