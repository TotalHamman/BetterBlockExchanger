package totalhamman.betterblockexchanger.handlers;

import net.minecraft.block.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fluids.BlockFluidBase;
import totalhamman.betterblockexchanger.BetterBlockExchanger;
import totalhamman.betterblockexchanger.items.ItemExchanger;

import java.util.HashSet;
import java.util.Set;

import static totalhamman.betterblockexchanger.BetterBlockExchanger.debugOn;

public class BlockExchangeHandler {

    public static final Set<Block> softBlocks = new HashSet<Block>();
    public static final Set<Block> specialBlocks = new HashSet<Block>();
    public static final Set<Block> blacklistedBlocks = new HashSet<Block>();
    public static final Set<Block> creativeBlocks = new HashSet<Block>();

    public static void initSpecialBlocklists() {
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


        return true;
    }


}
