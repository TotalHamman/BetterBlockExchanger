package totalhamman.betterblockexchanger;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import totalhamman.betterblockexchanger.library.CreativeTab;
import totalhamman.betterblockexchanger.library.Reference;
import totalhamman.betterblockexchanger.proxy.CommonProxy;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME,  version = Reference.VERSION, dependencies = Reference.DEPENDENCIES)
public class BetterBlockExchanger {

    public static final Logger log = LogManager.getLogger(Reference.MOD_ID);
    public static final boolean debugOn = true;

    @Mod.Instance(Reference.MOD_ID)
    public static BetterBlockExchanger instance;

    @SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
    public static CommonProxy proxy;

    public static final CreativeTab TabBetterBlockExchanger = new CreativeTab("BetterBlockExchanger");

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        if (debugOn) BetterBlockExchanger.log.info("Running preInit");
        proxy.preInit(e);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        if (debugOn) BetterBlockExchanger.log.info("RUnning Init");
        proxy.init(e);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        if (debugOn) BetterBlockExchanger.log.info("Running PostInit");
        proxy.postInit(e);
    }

}
