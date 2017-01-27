package totalhamman.betterblockexchanger.proxy;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import totalhamman.betterblockexchanger.BetterBlockExchanger;
import totalhamman.betterblockexchanger.handlers.*;
import totalhamman.betterblockexchanger.helpers.FacingHelper;
import totalhamman.betterblockexchanger.items.ModItems;
import totalhamman.betterblockexchanger.network.PacketHandler;
import totalhamman.betterblockexchanger.client.KeyBindings;

import static totalhamman.betterblockexchanger.BetterBlockExchanger.debugOn;
import static totalhamman.betterblockexchanger.helpers.LogHelper.logHelper;

public class CommonProxy {
    public void preInit(FMLPreInitializationEvent e) {
        logHelper("Init Items");
        ModItems.init();

        logHelper("Init Recipes");
        CraftingRecipeHandler.init();

        logHelper("Register Packet Handler");
        PacketHandler.registerMessages("btrblockexch");
    }

    public void init(FMLInitializationEvent e) {
        logHelper("Register / Init Key Bindings Handler");
        MinecraftForge.EVENT_BUS.register(new KeyBindingsHandler());
        KeyBindings.init();

        logHelper("Register Render Handler");
        MinecraftForge.EVENT_BUS.register(new RenderOverlayHandler());

        logHelper("Register World Event Handler");
        MinecraftForge.EVENT_BUS.register(new WorldEventHandler());
    }

    public void postInit(FMLPostInitializationEvent e) {
        logHelper("Init Special Blocklists");
        BlockExchangeHandler.initSpecialBlockLists();

        logHelper("Init EnumFacings");
        FacingHelper.initFacings();
    }
}
