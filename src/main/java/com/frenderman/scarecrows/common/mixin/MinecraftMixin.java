package com.frenderman.scarecrows.common.mixin;

import com.frenderman.scarecrows.misc.ClientMixinHooks;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {

    @Inject(method = "middleClickMouse", at = @At("HEAD"))
    private void onMiddleClickMouse(CallbackInfo ci) {
        Minecraft minecraft = Minecraft.class.cast(this);
        ClientMixinHooks.middleClickMouse(ci, minecraft);
    }
}
