package com.frenderman.scarecrows.misc;

import com.frenderman.scarecrows.common.core.config.ScarecrowsConfig;
import com.frenderman.scarecrows.common.entity.CrowEntity;
import com.frenderman.scarecrows.common.entity.ScarecrowEntity;
import com.frenderman.scarecrows.common.register.SCEntities;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.spawner.CatSpawner;
import net.minecraft.world.spawner.WorldEntitySpawner;

import java.util.Random;

public class CommonMixinHooks {

    public static void cropsBlockRandomTick(ServerWorld world, BlockPos pos, Random random) {
        if (ScarecrowsConfig.COMMON.crowSpawningEnabled()
                && world.getDimensionType().hasSkyLight()
                && random.nextFloat() <= 0.05F
                && world.getEntitiesWithinAABB(PlayerEntity.class, new AxisAlignedBB(pos).grow(16.0D), null).isEmpty()
                && world.getEntitiesWithinAABB(ScarecrowEntity.class, new AxisAlignedBB(pos).grow(8.0D), null).isEmpty()
                && world.getEntitiesWithinAABB(CrowEntity.class, new AxisAlignedBB(pos).grow(30.0D), null).size() <= 5
        ) {
            BlockPos spawnPos;
            BlockState blockState;
            FluidState fluidState;

            do {
                spawnPos = pos.up(20 + random.nextInt(15)).east(-10 + random.nextInt(21)).south(-10 + random.nextInt(21));
                blockState = world.getBlockState(spawnPos);
                fluidState = world.getFluidState(spawnPos);
            } while (!WorldEntitySpawner.func_234968_a_(world, spawnPos, blockState, fluidState, SCEntities.CROW.get()));

            ILivingEntityData entityData = null;
            DifficultyInstance localDifficulty = world.getDifficultyForLocation(pos);
            int spawnCount = 1 + random.nextInt(localDifficulty.getDifficulty().getId() + 1);

            for (int i = 0; i < spawnCount; i++) {
                CrowEntity crowEntity = SCEntities.CROW.get().create(world);
                assert crowEntity != null;
                crowEntity.moveToBlockPosAndAngles(spawnPos, 0.0F, 0.0F);
                entityData = crowEntity.onInitialSpawn(world, localDifficulty, SpawnReason.NATURAL, entityData, null);
                world.func_242417_l(crowEntity);
            }
        }
    }
}
