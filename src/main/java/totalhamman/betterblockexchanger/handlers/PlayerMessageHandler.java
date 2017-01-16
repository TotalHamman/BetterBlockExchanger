package totalhamman.betterblockexchanger.handlers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;

public class PlayerMessageHandler {

    public static void PlayerChatMessage(EntityPlayer playerIn, String msg) {
        playerIn.addChatMessage(new TextComponentString(msg));
    }

}
