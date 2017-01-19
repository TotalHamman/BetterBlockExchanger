package totalhamman.betterblockexchanger.items;

import net.minecraft.item.Item;
import totalhamman.betterblockexchanger.BetterBlockExchanger;

import static totalhamman.betterblockexchanger.BetterBlockExchanger.debugOn;

public final class ModItems {

    public static Item Exchanger;
    public static Item BedrockChisel;
    public static Item BedrockShard;

    public static void init() {
        if (debugOn) BetterBlockExchanger.log.info("Creating Items");

        Exchanger = new ItemExchanger();
        BedrockChisel = new ItemBedrockChisel();
        BedrockShard = new ItemBedrockShard();

    }
}

