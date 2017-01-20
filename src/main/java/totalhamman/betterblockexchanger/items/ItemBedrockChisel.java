package totalhamman.betterblockexchanger.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import totalhamman.betterblockexchanger.BetterBlockExchanger;

public class ItemBedrockChisel extends ItemMod {

    public ItemBedrockChisel() {
        super();
        this.setMaxStackSize(1);
        this.setUnlocalizedName("bedrock_chisel");
        this.setCreativeTab(BetterBlockExchanger.TabBetterBlockExchanger);
        this.setMaxDamage(8);
        this.setNoRepair();
        this.setRegistryName(this.getUnlocalizedName().substring(5));
        GameRegistry.register(this);
    }

    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

        if (worldIn.getBlockState(pos).getBlock() == Blocks.BEDROCK) {
            playerIn.inventory.addItemStackToInventory(new ItemStack(ModItems.BedrockShard));
            stack.damageItem(1, playerIn);
            playerIn.swingArm(hand);
            return EnumActionResult.SUCCESS;
        }

        return EnumActionResult.FAIL;
    }

}

