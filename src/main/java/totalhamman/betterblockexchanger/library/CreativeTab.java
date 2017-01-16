package totalhamman.betterblockexchanger.library;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import totalhamman.betterblockexchanger.BetterBlockExchanger;
import totalhamman.betterblockexchanger.items.ModItems;

import static totalhamman.betterblockexchanger.BetterBlockExchanger.debugOn;

public class CreativeTab extends CreativeTabs {

    public static final Logger log = LogManager.getLogger(Reference.MOD_ID);

    public CreativeTab(String label) {
        super(label);
        if (debugOn) BetterBlockExchanger.log.info("Creative Tab Name - " + label);
    }

    @Override
    public Item getTabIconItem () { return ModItems.Exchanger; }

}
