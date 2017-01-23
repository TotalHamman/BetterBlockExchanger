package totalhamman.betterblockexchanger.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import totalhamman.betterblockexchanger.items.ItemExchanger;

import static totalhamman.betterblockexchanger.helpers.LogHelper.logHelper;

public class PacketToggleMode implements IMessage, IMessageHandler<PacketToggleMode, IMessage> {

    @Override
    public void fromBytes(ByteBuf buf) { }

    @Override
    public void toBytes(ByteBuf buf) { }

    public PacketToggleMode() { }

    @Override
    public IMessage onMessage(PacketToggleMode message, MessageContext context) {
        EntityPlayerMP playerMP = context.getServerHandler().playerEntity;
        ItemStack heldItem = playerMP.getHeldItemMainhand();
        logHelper("onMessage Held Item - " + heldItem);
        if (heldItem != null && heldItem.getItem() instanceof ItemExchanger) {
            ItemExchanger exchanger = (ItemExchanger) (heldItem.getItem());
            exchanger.switchMode(playerMP, heldItem);
        }
        return null;
    }
}