package lain.mods.skins.impl.fabric;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mojang.authlib.GameProfile;
import lain.mods.skins.init.fabric.FabricOfflineSkins;
//import net.minecraft.client.resources.SkinTextures;

import net.minecraft.world.entity.player.PlayerSkin;
import net.minecraft.world.entity.player.PlayerModelType;
import net.minecraft.core.ClientAsset;
import net.minecraft.resources.Identifier;

//import net.minecraft.resources.Identifier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.function.Supplier;

public class SkinUtils {

    //private static final Function<GameProfile, ClientAsset.Texture> SKIN = (profile) -> FabricOfflineSkins.getLocationSkin(profile, null);
    //private static final Function<GameProfile, ClientAsset.Texture> CAPE = (profile) -> FabricOfflineSkins.getLocationCape(profile, null);
    private static final Function<GameProfile, ClientAsset.Texture> SKIN = (profile) -> {
        Identifier id = FabricOfflineSkins.getLocationSkin(profile, null);
        return id != null ? new ClientAsset.DownloadedTexture(id, id.toString()) : null;
    };
    private static final Function<GameProfile, ClientAsset.Texture> CAPE = (profile) -> {
        Identifier id = FabricOfflineSkins.getLocationCape(profile, null);
        return id != null ? new ClientAsset.DownloadedTexture(id, id.toString()) : null;
    };

    private static final Function<GameProfile, PlayerModelType> MODEL = (profile) -> PlayerModelType.byLegacyServicesName(FabricOfflineSkins.getSkinType(profile, null));

    private static final LoadingCache<GameProfile, Supplier<PlayerSkin>> textureSuppliers = CacheBuilder
            .newBuilder()
            .expireAfterAccess(15, TimeUnit.SECONDS)
            .build(new CacheLoader<GameProfile, Supplier<PlayerSkin>>() {
                @Override
                public Supplier<PlayerSkin> load(GameProfile profile) throws Exception {
                    AtomicReference<PlayerSkin> HOLDER = new AtomicReference<>();
                    return () -> {
                        PlayerSkin textures = HOLDER.get();
                        ClientAsset.Texture skinTexture = SKIN.apply(profile);
                        ClientAsset.Texture capeTexture = CAPE.apply(profile);
                        PlayerModelType model = MODEL.apply(profile);
                        if (textures == null) {
                            if (skinTexture != null) {
                                if (!HOLDER.compareAndSet(null, textures = PlayerSkin.insecure(skinTexture, capeTexture, null, model)))
                                    textures = HOLDER.get();
                            }
                        } else if (skinTexture != null) {
                            if (textures.body() != skinTexture || textures.cape() != capeTexture || textures.model() != model) {
                                if (!HOLDER.compareAndSet(textures, textures = PlayerSkin.insecure(skinTexture, capeTexture, null, model)))
                                    textures = HOLDER.get();
                            }
                        }
                        return textures;
                    };
                }
            });

    public static PlayerSkin textures(GameProfile profile) {
        return textureSuppliers.getUnchecked(profile).get();
    }

}
