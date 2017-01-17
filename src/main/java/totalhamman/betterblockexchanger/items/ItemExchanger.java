package totalhamman.betterblockexchanger.items;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import totalhamman.betterblockexchanger.BetterBlockExchanger;
import totalhamman.betterblockexchanger.handlers.BlockExchangeHandler;

import static totalhamman.betterblockexchanger.utils.LogHelper.LogHelper;

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

    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (world.isRemote) {
            return EnumActionResult.PASS;
        }

        LogHelper("-------------------------------------------------------------------");

        IBlockState prevState = world.getBlockState(pos);
        Block block = prevState.getBlock();
        int meta = block.getMetaFromState(prevState);

        if (player.isSneaking()) {

            if (BlockExchangeHandler.BlockSuitableForSelection(stack, player, world, pos)) {
                LogHelper("Sneaking | Block Selected - " + BlockExchangeHandler.getBlockName(block, meta));

                player.addChatMessage(new TextComponentString("Selected Block - " + BlockExchangeHandler.getBlockName(block, meta)));
                BlockExchangeHandler.SetSelectedBlock(stack, player, world, pos, facing);
                player.swingArm(hand);

                return EnumActionResult.SUCCESS;
            } else {
                player.addChatMessage(new TextComponentString("Invalid Selected Block - " + BlockExchangeHandler.getBlockName(block, meta)));
                return EnumActionResult.FAIL;
            }
        } else {
            LogHelper("Not Sneaking | Block to exchange - " + BlockExchangeHandler.getBlockName(block, meta));

            if (BlockExchangeHandler.BlockSuitableForExchange(stack, player, world, pos)) {
                LogHelper("Block " + BlockExchangeHandler.getBlockName(block, meta) + " is suitable for exchange");
                if (BlockExchangeHandler.ExchangeBlocks(stack, player, world, pos, facing)) {
                    stack.damageItem(1, player);
                    player.swingArm(hand);

                    return EnumActionResult.SUCCESS;
                }
            } else {
                return EnumActionResult.FAIL;
            }
        }

        return EnumActionResult.PASS;
    }
}
