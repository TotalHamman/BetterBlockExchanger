package totalhamman.betterblockexchanger.handlers;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import totalhamman.betterblockexchanger.network.PacketHandler;
import totalhamman.betterblockexchanger.network.PacketToggleMode;
import totalhamman.betterblockexchanger.client.KeyBindings;

public class KeyBindingsHandler {
    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (KeyBindings.modeKey.isPressed()) {
            PacketHandler.INSTANCE.sendToServer(new PacketToggleMode());
        }
    }
}
