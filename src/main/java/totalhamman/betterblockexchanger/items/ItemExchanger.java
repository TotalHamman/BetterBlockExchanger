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
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import totalhamman.betterblockexchanger.BetterBlockExchanger;
import totalhamman.betterblockexchanger.handlers.BlockExchangeHandler;
import totalhamman.betterblockexchanger.helpers.NBTHelper;

import java.util.List;

import static totalhamman.betterblockexchanger.helpers.LogHelper.logHelper;

public class ItemExchanger extends ItemMod {

    public static final int SQMODE_INITIAL = 0;
    public static final int SQMODE_1X1 = 0;
    public static final int SQMODE_3X3 = 1;
    public static final int SQMODE_5X5 = 2;
    public static final int SQMODE_7X7 = 3;

    public static final String[] sqModeList = new String[] {"1x1", "3x3", "5x5", "7x7"};


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
        int sqMode = getSQMode(stack);
        sqMode++;

        if (sqMode > SQMODE_7X7) sqMode = SQMODE_INITIAL;
        player.addChatMessage(new TextComponentString("Exchanger mode set to " + sqModeList[sqMode]));
        NBTHelper.setInteger(stack, "SQMode", sqMode);
    }

    private int getSQMode(ItemStack stack) {
        return NBTHelper.getInteger(stack, "SQMode", (byte) 0);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean bool) {
        super.addInformation(stack, player, tooltip, bool);

        NBTTagCompound compound = stack.getTagCompound();
        if (compound == null) {
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

        IBlockState prevState = world.getBlockState(pos);
        Block block = prevState.getBlock();
        int meta = block.getMetaFromState(prevState);

        if (player.isSneaking()) {

            if (BlockExchangeHandler.blockSuitableForSelection(stack, player, world, pos)) {
                logHelper("Sneaking | Block Selected - " + BlockExchangeHandler.getBlockName(block, meta));

                player.addChatMessage(new TextComponentString("Selected Block - " + BlockExchangeHandler.getBlockName(block, meta)));
                BlockExchangeHandler.setSelectedBlock(stack, player, world, pos, facing);
                return EnumActionResult.SUCCESS;
            } else {
                player.addChatMessage(new TextComponentString("Invalid Selected Block - " + BlockExchangeHandler.getBlockName(block, meta)));
                return EnumActionResult.FAIL;
            }
        } else {
            logHelper("Not Sneaking | Block to exchange - " + BlockExchangeHandler.getBlockName(block, meta));

            if (BlockExchangeHandler.blockSuitableForExchange(stack, player, world, pos)) {
                logHelper("Block " + BlockExchangeHandler.getBlockName(block, meta) + " is suitable for exchange");
                if (BlockExchangeHandler.exchangeBlocks(stack, player, world, pos, facing)) {
                    return EnumActionResult.SUCCESS;
                }
            } else {
                return EnumActionResult.FAIL;
            }
        }

        return EnumActionResult.SUCCESS;
    }
}
