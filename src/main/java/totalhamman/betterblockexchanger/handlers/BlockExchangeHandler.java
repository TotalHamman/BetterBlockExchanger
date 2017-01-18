package totalhamman.betterblockexchanger.handlers;

import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fluids.BlockFluidBase;
import totalhamman.betterblockexchanger.utils.ItemNBTHelper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static totalhamman.betterblockexchanger.utils.LogHelper.LogHelper;

public class BlockExchangeHandler {

    public static final Set<Block> softBlocks = new HashSet<Block>();
    public static final Set<Block> specialBlocks = new HashSet<Block>();
    public static final Set<Block> blacklistedBlocks = new HashSet<Block>();
    public static final Set<Block> creativeBlocks = new HashSet<Block>();

    public static void initSpecialBlockLists() {
        for (Object o : Block.REGISTRY) {
            Block block = (Block) o;
            if (block instanceof BlockFence || block instanceof BlockFenceGate || block instanceof BlockTorch) {
                specialBlocks.add(block);
            }
        }

        for (Object o : Block.REGISTRY) {
            Block block = (Block) o;
            if (block instanceof BlockRedstoneLight || block instanceof BlockWorkbench || block instanceof BlockEndPortal) {
                blacklistedBlocks.add(block);
            }
        }

        for (Object o : Block.REGISTRY) {
            Block block = (Block) o;
            if (block == Blocks.BEDROCK) {
                creativeBlocks.add(block);
            }
        }

        for (Object o : Block.REGISTRY) {
            Block block = (Block) o;
            if (block instanceof BlockFluidBase || block instanceof BlockLiquid || block instanceof IPlantable || block instanceof BlockTorch) {
                softBlocks.add(block);
            }
        }

        softBlocks.add(Blocks.SNOW);
        softBlocks.add(Blocks.VINE);
        softBlocks.add(Blocks.FIRE);

        LogHelper(specialBlocks.toString());
        LogHelper(blacklistedBlocks.toString());
        LogHelper(creativeBlocks.toString());
        LogHelper(softBlocks.toString());

        LogHelper("End Special Blocklists Init");
    }

    public static boolean BlockSuitableForSelection(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos) {
        if (worldIn.getTileEntity(pos) != null) return false;
        if (worldIn.isAirBlock(pos)) return false;
        if (blacklistedBlocks.contains(worldIn.getBlockState(pos).getBlock())) return false;
        if (softBlocks.contains(worldIn.getBlockState(pos).getBlock())) return false;
        if (creativeBlocks.contains(worldIn.getBlockState(pos).getBlock())) return false;

        return true;
    }

    public static boolean BlockSuitableForExchange(ItemStack stack, EntityPlayer player, World world, BlockPos pos) {
        Block newBlock = Block.REGISTRY.getObject(new ResourceLocation(ItemNBTHelper.getString(stack, "BlockName", "")));
        IBlockState newState = newBlock.getStateFromMeta(ItemNBTHelper.getByte(stack, "BlockData", (byte) 0));
        int newMeta = ItemNBTHelper.getByte(stack, "BlockData", (byte) 0);

        Block worldBlock = world.getBlockState(pos).getBlock();
        int worldMeta = worldBlock.getMetaFromState(world.getBlockState(pos));

        if (!BlockSuitableForSelection(stack, player, world, pos)) {
            player.addChatMessage(new TextComponentString("Invalid Block - " + getBlockName(worldBlock, worldMeta)));
            return false;
        }

        if (newBlock == worldBlock && newMeta == worldMeta) {
            player.addChatMessage(new TextComponentString(getBlockName(newBlock, newMeta) + " is the same block as selected block"));
            LogHelper(getBlockName(newBlock, newMeta) + " is the same block as selected block");
            return false;
        }

        //TODO - Complete Exchange Unsuitable Logic

        return true;
    }

    public static List<BlockPos> GetBlocksToExchange(ItemStack stack, BlockPos pos, World world, EnumFacing side) {
        int range = 3;
        IBlockState state = world.getBlockState(pos);

        List<BlockPos> exchangeList = new ArrayList<BlockPos>();
        List<BlockPos> checkedList = new ArrayList<BlockPos>();

        exchangeList.add(pos);
        checkedList.add(pos);

        BuildExchangeList(world, pos, pos, state, side, range, exchangeList, checkedList);

        return exchangeList;
    }

    private static void BuildExchangeList(World world, BlockPos pos, BlockPos origin, IBlockState state, EnumFacing side, int range, List<BlockPos> exchangeList, List<BlockPos> checkedList) {
        //TODO - Build List Logic
    }

    private boolean BlockInRange(BlockPos origin, BlockPos pos, int range) {
        BlockPos diff = pos.subtract(origin);
        return Math.abs(diff.getX()) <= range && Math.abs(diff.getY()) <= range && Math.abs(diff.getZ()) <= range;
    }

    public static void SetSelectedBlock(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing facing) {
        ItemNBTHelper.setString(stack, "BlockName", Block.REGISTRY.getNameForObject(world.getBlockState(pos).getBlock()).toString());
        ItemNBTHelper.setByte(stack, "BlockData", (byte) world.getBlockState(pos).getBlock().getMetaFromState(world.getBlockState(pos)));

        LogHelper("name - " + world.getBlockState(pos).getBlock().getUnlocalizedName() + " | state - " + world.getBlockState(pos).toString());
    }

    public static boolean ExchangeBlocks(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing facing) {
        Block newBlock = Block.REGISTRY.getObject(new ResourceLocation(ItemNBTHelper.getString(stack, "BlockName", "")));
        IBlockState newState = newBlock.getStateFromMeta(ItemNBTHelper.getByte(stack, "BlockData", (byte) 0));
        int meta = ItemNBTHelper.getByte(stack, "BlockData", (byte) 0);

        List<BlockPos> toExchange = GetBlocksToExchange(stack, pos, world, facing);

        for (BlockPos exchangePos : toExchange) {

            int slot = -1;
            try {
                slot = findItemInInventory(player.inventory, Item.getItemFromBlock(newBlock), meta);
                LogHelper("Found " + player.inventory.mainInventory[slot].stackSize + " of " + newBlock.getUnlocalizedName() + " in slot " + slot);
            } catch (ArrayIndexOutOfBoundsException e) {
                player.addChatMessage(new TextComponentString("Out of " + getBlockName(newBlock, meta) + " in inventory"));
                LogHelper("No stacks of " + newBlock.getUnlocalizedName() + " found in inventory");
            }


            if (slot >= 0 && player.inventory.mainInventory[slot].stackSize > 0) {
                Block oldBlock = world.getBlockState(exchangePos).getBlock();
                int oldMeta = oldBlock.getMetaFromState(world.getBlockState(exchangePos));

                if(placeBlockInInventory(world, player, oldBlock, oldMeta, 1) &&  placeBlockInWorld(world, exchangePos, newBlock, newState)
                    && consumeBlockInInventory(player, newBlock, newState)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean placeBlockInInventory(World world, EntityPlayer player, Block block, int meta, int cnt) {
        ItemStack oldStack = new ItemStack(block, cnt, meta);
        LogHelper("Added " + oldStack.stackSize + " of " + oldStack.getUnlocalizedName() + " to Inventory");
        player.inventory.addItemStackToInventory(oldStack);
        return true;
    }

    private static boolean consumeBlockInInventory(EntityPlayer player, Block block, IBlockState state) {
        if (!player.capabilities.isCreativeMode) {
            InventoryPlayer inv = player.inventory;
            Item item = Item.getItemFromBlock(block);
            int meta = block.getMetaFromState(state);

            int slot = findItemInInventory(inv, item, meta);

            if (slot < 0) {
                player.addChatMessage(new TextComponentString("Out of " + getBlockName(block, meta) + " in inventory"));
                return false;
            } else {
                if (--inv.mainInventory[slot].stackSize <= 0){
                    inv.mainInventory[slot] = null;
                }

                LogHelper("Block " + getBlockName(block, meta) + " Consumed");
            }
        }

        return true;
    }

    private static int findItemInInventory(InventoryPlayer inv, Item item, int meta) {
        for (int i = 0; i < inv.mainInventory.length; i++) {
            if (inv.mainInventory[i] != null && inv.mainInventory[i].getItem() == item && inv.mainInventory[i].getItemDamage() == meta) {
                return i;
            }
        }
        return -1;
    }

    private static boolean placeBlockInWorld(World world, BlockPos exchangePos, Block block, IBlockState state) {
        LogHelper("Exchanging " + world.getBlockState(exchangePos).toString() + " at " + exchangePos + " with " + state.toString());
        world.destroyBlock(exchangePos, false);
        world.setBlockState(exchangePos, state);

        return true;
    }

    public static String getBlockName(Block block, int meta) {
        ItemStack s = new ItemStack(block, 1, meta);
        return s.getDisplayName();
    }

}
