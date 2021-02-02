package com.frenderman.scarecrows.mixin;

import com.frenderman.scarecrows.entity.crow.CrowEntity;
import com.frenderman.scarecrows.entity.scarecrow.ScarecrowEntity;
import com.frenderman.scarecrows.init.SCEntities;
import com.frenderman.scarecrows.init.SCGameRules;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.SpawnHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(CropBlock.class)
public class CropBlockMixin {
    @Inject(method = "randomTick", at = @At("TAIL"))
    private void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
        if (world.getGameRules().getBoolean(SCGameRules.DO_CROW_SPAWNS)
         && world.getDimension().hasSkyLight()
         && random.nextFloat() <= 0.05F
         && world.getEntitiesByClass(PlayerEntity.class, new Box(pos).expand(16.0D), null).isEmpty()
         && world.getEntitiesByClass(ScarecrowEntity.class, new Box(pos).expand(8.0D), null).isEmpty()
         && world.getEntitiesByClass(CrowEntity.class, new Box(pos).expand(30.0D), null).size() <= 5
        ) {
            BlockPos spawnPos;
            BlockState blockState;
            FluidState fluidState;

            do {
                spawnPos = pos.up(20 + random.nextInt(15)).east(-10 + random.nextInt(21)).south(-10 + random.nextInt(21));
                blockState = world.getBlockState(spawnPos);
                fluidState = world.getFluidState(spawnPos);
            } while (!SpawnHelper.isClearForSpawn(world, spawnPos, blockState, fluidState, SCEntities.CROW));

            EntityData entityData = null;
            LocalDifficulty localDifficulty = world.getLocalDifficulty(pos);
            int spawnCount = 1 + random.nextInt(localDifficulty.getGlobalDifficulty().getId() + 1);

            for (int i = 0; i < spawnCount; i++) {
                CrowEntity crowEntity = SCEntities.CROW.create(world);
                assert crowEntity != null;
                crowEntity.refreshPositionAndAngles(spawnPos, 0.0F, 0.0F);
                entityData = crowEntity.initialize(world, localDifficulty, SpawnReason.NATURAL, entityData, null);
                world.spawnEntityAndPassengers(crowEntity);
            }
        }
    }
}
