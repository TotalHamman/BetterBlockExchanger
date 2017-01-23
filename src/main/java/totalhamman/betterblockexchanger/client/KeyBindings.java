package totalhamman.betterblockexchanger.client;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

@SideOnly(Side.CLIENT)
public class KeyBindings {

    public static KeyBinding modeKey;

    public static void init() {
        modeKey = new KeyBinding("key.sqmodifier", Keyboard.KEY_B, "key.categories.betterblockexchanger");
        ClientRegistry.registerKeyBinding(modeKey);
    }
}
