package lain.mods.skins.impl.fabric;

import com.mojang.authlib.minecraft.MinecraftSessionService;
import java.net.Proxy;
import net.minecraft.client.Minecraft;

public class MinecraftUtils {

    public static Proxy getProxy() {
        return Minecraft.getInstance().getProxy();
    }

    public static MinecraftSessionService getSessionService() {
        return Minecraft.getInstance().services().sessionService();
    }

}
