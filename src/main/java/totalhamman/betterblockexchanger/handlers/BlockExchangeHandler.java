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
import totalhamman.betterblockexchanger.helpers.NBTHelper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static totalhamman.betterblockexchanger.helpers.FacingHelper.getFacings;
import static totalhamman.betterblockexchanger.helpers.LogHelper.logHelper;

public class BlockExchangeHandler {

    private static final Set<Block> softBlocks = new HashSet<Block>();
    private static final Set<Block> specialBlocks = new HashSet<Block>();
    private static final Set<Block> blacklistedBlocks = new HashSet<Block>();
    private static final Set<Block> creativeBlocks = new HashSet<Block>();

    public static void initSpecialBlockLists() {
        for (Object o : Block.REGISTRY) {
            Block block = (Block) o;
            if (block instanceof BlockFence || block instanceof BlockFenceGate || block instanceof BlockTrapDoor || block instanceof BlockDoor || block instanceof BlockPistonBase
                    || block instanceof BlockLadder) {
                specialBlocks.add(block);
            }
        }

        for (Object o : Block.REGISTRY) {
            Block block = (Block) o;
            if (block instanceof BlockRedstoneLight || block instanceof BlockWorkbench) {
                blacklistedBlocks.add(block);
            }
        }

        for (Object o : Block.REGISTRY) {
            Block block = (Block) o;
            if (block == Blocks.BEDROCK || block == Blocks.END_PORTAL_FRAME) {
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

        logHelper(specialBlocks.toString());
        logHelper(blacklistedBlocks.toString());
        logHelper(creativeBlocks.toString());
        logHelper(softBlocks.toString());

        logHelper("End Special Blocklists Init");
    }

    public static boolean blockSuitableForSelection(EntityPlayer player, World world, BlockPos pos) {
        if (world.getTileEntity(pos) != null) return false;
        if (world.isAirBlock(pos)) return false;
        if (blacklistedBlocks.contains(world.getBlockState(pos).getBlock())) return false;
        if (softBlocks.contains(world.getBlockState(pos).getBlock())) return false;
        if (specialBlocks.contains((world.getBlockState(pos).getBlock()))) return false;
        if (creativeBlocks.contains(world.getBlockState(pos).getBlock()) && !player.capabilities.isCreativeMode) return false;

        return true;
    }

    public static boolean blockSuitableForExchange(ItemStack stack, EntityPlayer player, World world, BlockPos pos) {
        Block newBlock = Block.REGISTRY.getObject(new ResourceLocation(NBTHelper.getString(stack, "BlockName", "")));
        int newMeta = NBTHelper.getInteger(stack, "BlockData", (byte) 0);

        Block worldBlock = world.getBlockState(pos).getBlock();
        int worldMeta = worldBlock.getMetaFromState(world.getBlockState(pos));

        if (!blockSuitableForSelection(player, world, pos)) {
            player.addChatMessage(new TextComponentString("Invalid Block - " + getBlockName(worldBlock, worldMeta)));
            return false;
        }

        if (newBlock == worldBlock && newMeta == worldMeta) {
            player.addChatMessage(new TextComponentString(getBlockName(newBlock, newMeta) + " is the same block as selected block"));
            logHelper(getBlockName(newBlock, newMeta) + " is the same block as selected block");
            return false;
        }

        return true;
    }

    public static List<BlockPos> getBlocksToExchange(ItemStack stack, BlockPos pos, World world, EnumFacing side) {
        int range = NBTHelper.getInteger(stack, "SQMode", (byte) 0);
        IBlockState state = world.getBlockState(pos);

        List<BlockPos> exchangeList = new ArrayList<BlockPos>();
        List<BlockPos> checkedList = new ArrayList<BlockPos>();

        exchangeList.add(pos);
        checkedList.add(pos);

        buildExchangeList(world, pos, pos, state, side, range, exchangeList, checkedList);

        return exchangeList;
    }

    private static void buildExchangeList(World world, BlockPos pos, BlockPos origin, IBlockState origState, EnumFacing side, int range, List<BlockPos> exchangeList, List<BlockPos> checkedList) {
        EnumFacing[] facesAround = getFacings(side);

        for (EnumFacing dir : facesAround){
            BlockPos newPos = pos.offset(dir);

            if (checkedList.contains(newPos) || !blockInRange(origin, newPos, range)){
                continue;
            }

            checkedList.add(newPos);
            IBlockState state = world.getBlockState(newPos);

            boolean validExchange = !world.isAirBlock(newPos) && (world.isAirBlock(newPos.offset(side)) || (world.getBlockState(newPos.offset(side)) == Blocks.WATER))
                && state == origState && state.getBlock().isReplaceable(world, newPos.offset(side));

            if (validExchange) {
                exchangeList.add(newPos);
                buildExchangeList(world, newPos, origin, origState, side, range, exchangeList, checkedList);
            }
        }
    }

    private static boolean blockInRange(BlockPos origin, BlockPos pos, int range) {
        BlockPos diff = pos.subtract(origin);
        return Math.abs(diff.getX()) <= range && Math.abs(diff.getY()) <= range && Math.abs(diff.getZ()) <= range;
    }

    public static void setSelectedBlock(ItemStack stack, Block block, IBlockState state) {
        String blockName = Block.REGISTRY.getNameForObject(block).toString();

        NBTHelper.setString(stack, "BlockName", blockName);
        NBTHelper.setInteger(stack, "BlockData", (byte) block.getMetaFromState(state));

        logHelper("name - " + blockName + " | state - " + state);
    }

    public static boolean exchangeBlocks(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing facing) {
        Block newBlock = Block.REGISTRY.getObject(new ResourceLocation(NBTHelper.getString(stack, "BlockName", "")));
        int newMeta = NBTHelper.getInteger(stack, "BlockData", (byte) 0);
        IBlockState newState = newBlock.getStateFromMeta(newMeta);

        List<BlockPos> toExchange = getBlocksToExchange(stack, pos, world, facing);

        logHelper("~" + toExchange.toString() + "~");
        logHelper("~" + toExchange.size() + "~");

        for (BlockPos exchangePos : toExchange) {
            int slot = -1;
            if (player.capabilities.isCreativeMode) {
                placeBlockInWorld(world, exchangePos, newBlock, newState);
            } else {
                try {
                    slot = findItemInInventory(player.inventory, Item.getItemFromBlock(newBlock), newMeta);
                    logHelper("Found " + player.inventory.mainInventory[slot].stackSize + " of " + newBlock.getUnlocalizedName() + " in slot " + slot);
                } catch (ArrayIndexOutOfBoundsException e) {
                    player.addChatMessage(new TextComponentString("Out of " + getBlockName(newBlock, newMeta) + " in inventory"));
                    logHelper("No stacks of " + newBlock.getUnlocalizedName() + " found in inventory");
                    return false;
                }

                if (slot >= 0 && player.inventory.mainInventory[slot].stackSize > 0) {
                    Block oldBlock = world.getBlockState(exchangePos).getBlock();
                    int oldMeta = oldBlock.getMetaFromState(world.getBlockState(exchangePos));

                    if (!placeBlockInInventory(player, oldBlock, oldMeta)) {
                        player.addChatMessage(new TextComponentString("Out of space in inventory"));
                        return false;
                    } else {
                        if (!placeBlockInWorld(world, exchangePos, newBlock, newState)) {
                            player.addChatMessage(new TextComponentString("Out of " + getBlockName(newBlock, newMeta) + " in inventory"));
                            return false;
                        } else {
                            if (!consumeBlockInInventory(player, newBlock, newState)) {
                                return false;
                            } else {
                                logHelper("Damage Item");
                                stack.damageItem(1, player);
                            }
                        }
                    }
                }
            }
        }

        return true;
    }

    private static boolean placeBlockInInventory(EntityPlayer player, Block block, int meta) {
        ItemStack oldStack = new ItemStack(block, 1, meta);
        boolean successPlaceInInventory = player.inventory.addItemStackToInventory(oldStack);
        if (!successPlaceInInventory) {
            return false;
        }

        logHelper("Added " + 1 + " of " + oldStack.getUnlocalizedName() + " to Inventory");
        return true;
    }

    private static boolean consumeBlockInInventory(EntityPlayer player, Block block, IBlockState state) {
        if (!player.capabilities.isCreativeMode) {
            InventoryPlayer inv = player.inventory;
            Item item = Item.getItemFromBlock(block);
            int meta = block.getMetaFromState(state);

            int slot = findItemInInventory(inv, item, meta);

            if (slot < 0) {
                return false;
            } else {
                if (--inv.mainInventory[slot].stackSize <= 0){
                    inv.mainInventory[slot] = null;
                }

                logHelper("Block " + getBlockName(block, meta) + " Consumed");
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
        logHelper("Exchanging " + world.getBlockState(exchangePos).toString() + " at " + exchangePos + " with " + state.toString());

        world.destroyBlock(exchangePos, false);
        world.setBlockState(exchangePos, state);
        world.playEvent(2001, exchangePos, Block.getStateId(state));
        world.notifyBlockOfStateChange(exchangePos, block);

        return true;
    }

    public static String getBlockName(Block block, int meta) {
        ItemStack s = new ItemStack(block, 1, meta);
        return s.getDisplayName();
    }

}
