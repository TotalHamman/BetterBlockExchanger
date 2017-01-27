package totalhamman.betterblockexchanger.handlers;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import totalhamman.betterblockexchanger.items.ModItems;

public class CraftingRecipeHandler {
    public static void init() {

        addShapedRecipe(new ItemStack(ModItems.Exchanger),
                "E  ", " S ", "  R",
                'E', new ItemStack(Items.ENDER_EYE),
                'S', new ItemStack(ModItems.BedrockShard),
                'R', new ItemStack(Items.REDSTONE));

        addShapedRecipe(new ItemStack(ModItems.BedrockChisel),
                "D  ", " O ", "  O",
                'D', new ItemStack(Items.DIAMOND),
                'O', new ItemStack(Item.getItemFromBlock(Blocks.OBSIDIAN)));

    }

    public static void addShapedRecipe(ItemStack output, Object... recipe) {
        GameRegistry.addRecipe(new ShapedOreRecipe(output, recipe));
    }

    public static void addShapelessRecipe(ItemStack output, Object... recipe) {
        GameRegistry.addRecipe(new ShapelessOreRecipe(output, recipe));
    }

    public static void addSmeltingRecipe(Item input, ItemStack output, Float exp) {
        GameRegistry.addSmelting(input, output, exp);
    }

    public static void addSmeltingRecipe(Block input, ItemStack output, Float exp) {
        GameRegistry.addSmelting(input, output, exp);
    }

    public static void addSmeltingRecipe(ItemStack input, ItemStack output, Float exp) {
        GameRegistry.addSmelting(input, output, exp);
    }

}
