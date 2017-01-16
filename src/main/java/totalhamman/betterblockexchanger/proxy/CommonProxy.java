package totalhamman.betterblockexchanger.proxy;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import totalhamman.betterblockexchanger.BetterBlockExchanger;
import totalhamman.betterblockexchanger.handlers.BlockExchangeHandler;
import totalhamman.betterblockexchanger.handlers.CraftingRecipeHandler;
import totalhamman.betterblockexchanger.handlers.RenderOverlayHandler;
import totalhamman.betterblockexchanger.items.ModItems;

import static totalhamman.betterblockexchanger.BetterBlockExchanger.debugOn;


public class CommonProxy {
    public void preInit(FMLPreInitializationEvent e) {

        if (debugOn) BetterBlockExchanger.log.info("Init Items");
        ModItems.init();

        if (debugOn) BetterBlockExchanger.log.info("Init Recipes");
        CraftingRecipeHandler.init();

        //if (debugOn) BetterBlockExchanger.log.info("Register EventHandler");
    }

    public void init(FMLInitializationEvent e) {
        MinecraftForge.EVENT_BUS.register(new RenderOverlayHandler());
    }

    public void postInit(FMLPostInitializationEvent e) {
        if (debugOn) BetterBlockExchanger.log.info("Init Special Blocklists");
        BlockExchangeHandler.initSpecialBlockLists();
    }
}
