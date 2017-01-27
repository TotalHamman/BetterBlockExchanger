package totalhamman.betterblockexchanger.items;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import totalhamman.betterblockexchanger.BetterBlockExchanger;
import totalhamman.betterblockexchanger.handlers.BlockExchangeHandler;

import java.util.List;

import static totalhamman.betterblockexchanger.helpers.ChatHelper.msgPlayer;
import static totalhamman.betterblockexchanger.helpers.LogHelper.logHelper;

public class ItemExchanger extends ItemMod {

    public static final int SQMODE_INITIAL = 0;
    public static final int SQMODE_1X1 = 0;
    public static final int SQMODE_3X3 = 1;
    public static final int SQMODE_5X5 = 2;
    public static final int SQMODE_7X7 = 3;
    public static final int SQMODE_9X9 = 4;
    public static final int SQMODE_17X17 = 5;
    public static final int SQMODE_33X33 = 6;
    public static final int SQMODE_49X49 = 7;
    public static final int SQMODE_65X65 = 8;

    public static final String[] sqModeList = new String[] {"1x1", "3x3", "5x5", "7x7", "9x9", "17x17", "33x33", "49X49", "65x65"};
    public static final Integer[] sqModeRange = new Integer[] {0, 1, 2, 3, 4, 8, 16, 24, 32};

    public ItemExchanger() {
        super();
        this.setMaxStackSize(1);
        this.setMaxDamage(1728);
        this.setNoRepair();
        this.setUnlocalizedName("exchanger");
        this.setRegistryName("exchanger");
        this.setCreativeTab(BetterBlockExchanger.TabBetterBlockExchanger);
        GameRegistry.register(this);
    }

    public void switchMode(EntityPlayer player, ItemStack stack) {
        if (stackTagCompoundNull(stack)) setDefaultTagCompound(stack);

        int sqMode = stack.getTagCompound().getInteger("SQMode");
        sqMode++;

        if (!player.capabilities.isCreativeMode) {
            if (sqMode > SQMODE_17X17) sqMode = SQMODE_INITIAL;
        } else {
            if (sqMode > SQMODE_65X65) sqMode = SQMODE_INITIAL;
        }

        stack.getTagCompound().setInteger("SQMode", sqMode);
        msgPlayer(player, "Exchanger mode set to " + sqModeList[sqMode]);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean bool) {
        super.addInformation(stack, player, tooltip, bool);

        if (stackTagCompoundNull(stack)) setDefaultTagCompound(stack);
        NBTTagCompound compound = stack.getTagCompound();

        if (compound == null || Block.getBlockFromName(compound.getString("BlockName")) == null) {
            tooltip.add(ChatFormatting.RED + "No Selected Block");
        } else {
            String name = compound.getString("BlockName");
            Block block = Block.getBlockFromName(name);

            int meta = compound.getByte("BlockData");

            tooltip.add(ChatFormatting.LIGHT_PURPLE + "Selected Block: " + BlockExchangeHandler.getBlockName(block, meta));
            tooltip.add(ChatFormatting.LIGHT_PURPLE + "Selected Mode: " + sqModeList[compound.getInteger("SQMode")]);
        }

        tooltip.add("Sneak right click on block to select.");
        tooltip.add("Right click on a block to exchange.");
        tooltip.add("Mode key (default 'b') to switch modes.");
    }

    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (world.isRemote) {
            return EnumActionResult.PASS;
        }

        logHelper("-------------------------------------------------------------------");

        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        int meta = block.getMetaFromState(state);

        if (player.isSneaking()) {

            if (BlockExchangeHandler.blockSuitableForSelection(player, world, pos)) {
                logHelper("Sneaking | Block Selected - " + BlockExchangeHandler.getBlockName(block, meta));

                msgPlayer(player, "Selected Block - " + BlockExchangeHandler.getBlockName(block, meta));
                BlockExchangeHandler.setSelectedBlock(stack, block, state);
                return EnumActionResult.SUCCESS;
            } else {
                msgPlayer(player, "Invalid Selected Block - " + BlockExchangeHandler.getBlockName(block, meta));
                return EnumActionResult.FAIL;
            }
        } else {
            logHelper("Not Sneaking | Block to exchange - " + BlockExchangeHandler.getBlockName(block, meta));

            if (BlockExchangeHandler.blockSuitableForExchange(stack, player, world, pos)) {
                logHelper("Block " + BlockExchangeHandler.getBlockName(block, meta) + " is suitable for exchange");

                boolean success = BlockExchangeHandler.exchangeBlocks(stack, player, world, pos, facing);

                if (success) {
                    return EnumActionResult.SUCCESS;

                }

            } else {
                return EnumActionResult.FAIL;
            }
        }

        return EnumActionResult.SUCCESS;
    }

    public static boolean stackTagCompoundNull(ItemStack stack) {
        return stack.getTagCompound() == null;
    }

    public static void setDefaultTagCompound(ItemStack stack) {
        stack.setTagCompound(new NBTTagCompound());
        stack.getTagCompound().setString("BlockName", "");
        stack.getTagCompound().setInteger("BlockData", 0);
        stack.getTagCompound().setInteger("SQMode", 0);
    }

}
