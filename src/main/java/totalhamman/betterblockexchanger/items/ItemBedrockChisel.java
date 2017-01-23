package totalhamman.betterblockexchanger.items;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import totalhamman.betterblockexchanger.BetterBlockExchanger;

public class ItemBedrockChisel extends ItemMod {

    public ItemBedrockChisel() {
        super();
        this.setMaxStackSize(1);
        this.setMaxDamage(8);
        this.setNoRepair();
        this.setUnlocalizedName("bedrock_chisel");
        this.setRegistryName("bedrock_chisel");
        this.setCreativeTab(BetterBlockExchanger.TabBetterBlockExchanger);
        GameRegistry.register(this);
    }

    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

        if (world.getBlockState(pos).getBlock() == Blocks.BEDROCK) {
            int x = pos.getX();
            int y = pos.getY();
            int z = pos.getZ();
            int dur = 0;

            if (!world.isRemote) world.spawnEntityInWorld(new EntityItem(world, x, y, z, new ItemStack(ModItems.BedrockShard)));

            stack.damageItem(1, player);
            player.swingArm(hand);

            if (player.isPotionActive(MobEffects.MINING_FATIGUE)) dur = player.getActivePotionEffect(MobEffects.MINING_FATIGUE).getDuration();
            player.addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE, 200 + dur, 3));

            return EnumActionResult.SUCCESS;
        }

        return EnumActionResult.FAIL;
    }

}

