package totalhamman.betterblockexchanger.proxy;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import totalhamman.betterblockexchanger.BetterBlockExchanger;
import totalhamman.betterblockexchanger.handlers.GUIEventHandler;
import totalhamman.betterblockexchanger.items.ItemMod;

import static totalhamman.betterblockexchanger.BetterBlockExchanger.debugOn;
import static totalhamman.betterblockexchanger.items.ModItems.*;

public class ClientProxy extends CommonProxy {
    @Override
    public void preInit(FMLPreInitializationEvent e) {
        super.preInit(e);
    }

    @Override
    public void init(FMLInitializationEvent e) {
        super.init(e);

        if (debugOn) BetterBlockExchanger.log.info("Init Models");
        ItemMod.initModel(Exchanger);
        ItemMod.initModel(BedrockChisel);
        ItemMod.initModel(BedrockShard);
    }

    @Override
    public void postInit(FMLPostInitializationEvent e) {
        super.postInit(e);

        MinecraftForge.EVENT_BUS.register(new GUIEventHandler((Minecraft.getMinecraft())));
    }

}
