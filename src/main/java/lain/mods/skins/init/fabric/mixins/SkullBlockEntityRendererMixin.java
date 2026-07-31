package lain.mods.skins.init.fabric.mixins;

import lain.mods.skins.init.fabric.FabricOfflineSkins;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SkullBlockRenderer.class)
public abstract class SkullBlockEntityRendererMixin {

    @Inject(method = "resolveSkullRenderType", at = @At("RETURN"), cancellable = true, require = 0)
    private void resolveSkullRenderType_inject(SkullBlock.Type type, SkullBlockEntity skullBlockEntity, CallbackInfoReturnable<RenderType> cir) {
        if (FabricOfflineSkins.PLAYERHEADS && type == SkullBlock.Types.PLAYER) {
            ResolvableProfile profile = skullBlockEntity.getOwnerProfile();
            if (profile != null && profile.partialProfile() != null) {
                Identifier loc = FabricOfflineSkins.getLocationSkin(profile.partialProfile(), null);
                if (loc != null)
                    cir.setReturnValue(SkullBlockRenderer.getPlayerSkinRenderType(loc));
            }
        }
    }
}
