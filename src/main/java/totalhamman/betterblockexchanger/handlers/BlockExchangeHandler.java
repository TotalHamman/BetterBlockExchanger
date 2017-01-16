package totalhamman.betterblockexchanger.handlers;

import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fluids.BlockFluidBase;
import totalhamman.betterblockexchanger.BetterBlockExchanger;
import totalhamman.betterblockexchanger.utils.ItemNBTHelper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static totalhamman.betterblockexchanger.BetterBlockExchanger.debugOn;

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
            if (block instanceof BlockRedstoneLight) {
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

        if (debugOn) BetterBlockExchanger.log.info(specialBlocks.toString());
        if (debugOn) BetterBlockExchanger.log.info(blacklistedBlocks.toString());
        if (debugOn) BetterBlockExchanger.log.info(creativeBlocks.toString());
        if (debugOn) BetterBlockExchanger.log.info(softBlocks.toString());

        if (debugOn) BetterBlockExchanger.log.info("End Special Blocklists Init");
    }

    public static boolean BlockSuitableForSelection(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos) {
        if (worldIn.getTileEntity(pos) != null) return false;
        if (worldIn.isAirBlock(pos)) return false;
        if (blacklistedBlocks.contains(worldIn.getBlockState(pos).getBlock())) return false;
        if (softBlocks.contains(worldIn.getBlockState(pos).getBlock())) return false;
        if (creativeBlocks.contains(worldIn.getBlockState(pos).getBlock())) return false;
        //if (!ItemExchanger.isCreative(stack) && ItemExchanger.creativeOverrideBlocks.contains(block)) return false;

        return true;
    }

    public static boolean BlockSuitableForExchange(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos) {
        if (!BlockSuitableForSelection(stack, playerIn, worldIn, pos)) return false;

        //TODO - Complete Exchange Unsuitable Logic

        return true;
    }

    public static List<BlockPos> GetBlocksToExchange(ItemStack stack, BlockPos pos, World world, EnumFacing side) {
        int range = 3;
        boolean replaceVisibleOnly = true;
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

    public static void ExchangeBlocks(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing facing) {
        Block newBlock = Block.REGISTRY.getObject(new ResourceLocation(ItemNBTHelper.getString(stack, "BlockName", "")));
        IBlockState newState = newBlock.getStateFromMeta(ItemNBTHelper.getByte(stack, "BlockData", (byte) 0));

        List<BlockPos> toExchange = GetBlocksToExchange(stack, pos, world, facing);

        //TODO - Complete Exchange Code
    }

    public static void SetSelectedBlock(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing facing) {
        ItemNBTHelper.setString(stack, "BlockName", Block.REGISTRY.getNameForObject(world.getBlockState(pos).getBlock()).toString());
        ItemNBTHelper.setByte(stack, "BlockData", (byte) world.getBlockState(pos).getBlock().getMetaFromState(world.getBlockState(pos)));
    }

}
