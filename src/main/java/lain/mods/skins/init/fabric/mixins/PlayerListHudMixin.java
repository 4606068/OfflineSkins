package lain.mods.skins.init.fabric.mixins;

import net.minecraft.client.gui.components.PlayerTabOverlay;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(PlayerTabOverlay.class)
public abstract class PlayerListHudMixin {

    @ModifyVariable(method = "render(Lnet/minecraft/client/gui/GuiGraphics;ILnet/minecraft/world/scores/Scoreboard;Lnet/minecraft/world/scores/Objective;)V", at = @At(value = "STORE", opcode = Opcodes.ISTORE, ordinal = 0), require = 0)
    private boolean onRenderSetFlag_nBXjeY(boolean result) {
        return true;
    }

}
