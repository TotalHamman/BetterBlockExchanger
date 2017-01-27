package totalhamman.betterblockexchanger.items;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import totalhamman.betterblockexchanger.library.Reference;

import java.util.List;

import static totalhamman.betterblockexchanger.helpers.LogHelper.logHelper;

public class ItemMod extends Item {

    public static void initModel(Item item) {
        logHelper("Register Item Render - " + item.getUnlocalizedName().substring(5));

        ModelResourceLocation modelRL = new ModelResourceLocation(Reference.MOD_ID + ":" + item.getUnlocalizedName().substring(5), "inventory");
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0, modelRL);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean bool) {
        super.addInformation(stack, player, tooltip, bool);
    }
}
