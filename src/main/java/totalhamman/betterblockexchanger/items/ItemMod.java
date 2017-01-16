package totalhamman.betterblockexchanger.items;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import totalhamman.betterblockexchanger.BetterBlockExchanger;
import totalhamman.betterblockexchanger.library.Reference;

import static totalhamman.betterblockexchanger.BetterBlockExchanger.debugOn;

public class ItemMod extends Item {

    public static void initModel(Item item) {
        if (debugOn) BetterBlockExchanger.log.info("Register Item Render - " + item.getUnlocalizedName().substring(5));

        ModelResourceLocation modelRL = new ModelResourceLocation(Reference.MOD_ID + ":" + item.getUnlocalizedName().substring(5), "inventory");
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0, modelRL);
    }
}
