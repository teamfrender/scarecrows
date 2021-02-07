package com.frenderman.scarecrows.common.mixin;

import com.frenderman.scarecrows.misc.CommonMixinHooks;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropsBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(CropsBlock.class)
public class CropsBlockMixin {

    @Inject(method = "randomTick", at = @At("TAIL"))
    private void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
        CommonMixinHooks.cropsBlockRandomTick(world, pos, random);
    }
}
