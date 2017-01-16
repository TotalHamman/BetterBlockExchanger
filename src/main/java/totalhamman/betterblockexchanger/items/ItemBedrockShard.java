package totalhamman.betterblockexchanger.items;

import net.minecraftforge.fml.common.registry.GameRegistry;
import totalhamman.betterblockexchanger.BetterBlockExchanger;
import totalhamman.betterblockexchanger.items.ItemMod;

public class ItemBedrockShard extends ItemMod {
	
	public ItemBedrockShard() {
		super();
		this.setMaxStackSize(64);
        this.setUnlocalizedName("bedrock_shard");
        this.setCreativeTab(BetterBlockExchanger.TabBetterBlockExchanger);
		this.setRegistryName(this.getUnlocalizedName().substring(5));
		GameRegistry.register(this);
	}

}
