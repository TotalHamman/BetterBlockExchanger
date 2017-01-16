package totalhamman.betterblockexchanger.items;

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

    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

        playerIn.swingArm(hand);
        String blockName = worldIn.getBlockState(pos).getBlock().getLocalizedName();

        if (!playerIn.canPlayerEdit(pos.offset(facing), facing, stack)) {
            return EnumActionResult.FAIL;
        } else {
            if (!worldIn.isRemote) {
                if (playerIn.isSneaking()) {
                    if (debugOn) BetterBlockExchanger.log.info("Sneaking");

                    if (BlockExchangeHandler.BlockSuitableForSelection(stack, playerIn, worldIn, pos)) {
                        if (debugOn) BetterBlockExchanger.log.info("Is Suitable for Selection");



                        playerIn.addChatMessage(new TextComponentString("Selected Block - " + blockName));
                        return EnumActionResult.SUCCESS;
                    } else {
                        if (debugOn) BetterBlockExchanger.log.info("Is NOT Suitable for Selection");
                        playerIn.addChatMessage(new TextComponentString("Invalid Block - " + blockName));
                        return EnumActionResult.FAIL;
                    }
                }

                if (debugOn) BetterBlockExchanger.log.info("Not Sneaking");

                if (BlockExchangeHandler.BlockSuitableForExchange(stack, playerIn, worldIn, pos)) {
                    if (debugOn) BetterBlockExchanger.log.info("Is Suitable for Exchange");

//                    playerIn.addChatMessage(new TextComponentString("Exchanged Block - " + blockName));
                    return EnumActionResult.SUCCESS;
                } else {
                    if (debugOn) BetterBlockExchanger.log.info("Is NOT Suitable for Exchange");
                    playerIn.addChatMessage(new TextComponentString("Invalid Block - " + blockName));
                    return EnumActionResult.FAIL;
                }
            }
        }

        return EnumActionResult.PASS;
    }
}
