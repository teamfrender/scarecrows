package com.frenderman.scarecrows.common.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import java.util.Random;

public class CrowEntity extends CreatureEntity {

    public CrowEntity(EntityType<? extends CreatureEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public static boolean canCrowSpawn(EntityType<? extends MobEntity> typeIn, IWorld world, SpawnReason reason, BlockPos pos, Random randomIn) {
        BlockState blockState = world.getBlockState(pos.down());
        return (blockState.isIn(BlockTags.LEAVES) || blockState.isIn(Blocks.GRASS_BLOCK) || blockState.isIn(BlockTags.LOGS) || blockState.isIn(Blocks.AIR)) && world.getLightSubtracted(pos, 0) > 8;
    }
}
