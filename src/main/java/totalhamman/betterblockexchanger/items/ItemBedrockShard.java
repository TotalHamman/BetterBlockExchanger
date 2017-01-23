package totalhamman.betterblockexchanger.items;

import net.minecraftforge.fml.common.registry.GameRegistry;
import totalhamman.betterblockexchanger.BetterBlockExchanger;
import totalhamman.betterblockexchanger.items.ItemMod;

public class ItemBedrockShard extends ItemMod {
	
	public ItemBedrockShard() {
		super();
		this.setMaxStackSize(64);
        this.setUnlocalizedName("bedrock_shard");
		this.setRegistryName("bedrock_shard");
		this.setCreativeTab(BetterBlockExchanger.TabBetterBlockExchanger);
		GameRegistry.register(this);
	}
}
