package totalhamman.betterblockexchanger.compat.jei;

import mezz.jei.api.BlankModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import net.minecraft.item.ItemStack;
import totalhamman.betterblockexchanger.items.ModItems;

import javax.annotation.Nonnull;

@JEIPlugin
public class JEIAddon extends BlankModPlugin {

    @Override
    public void register(@Nonnull IModRegistry registry) {
        registry.addDescription(new ItemStack(ModItems.BedrockShard), "description.betterblockexchanger.bedrockshard");
    }
}
