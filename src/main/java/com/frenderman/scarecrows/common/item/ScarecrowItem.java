package com.frenderman.scarecrows.common.item;

import com.frenderman.scarecrows.common.entity.ScarecrowEntity;
import com.frenderman.scarecrows.common.register.SCEntities;
import net.minecraft.entity.SpawnReason;
import net.minecraft.item.*;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class ScarecrowItem extends Item {

    private final DyeColor dyeColor;

    public ScarecrowItem(Properties properties, DyeColor dyeColor) {
        super(properties);
        this.dyeColor = dyeColor;
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        Direction direction = context.getFace();
        if (direction == Direction.DOWN) {
            return ActionResultType.FAIL;
        } else {
            World world = context.getWorld();
            BlockItemUseContext blockItemUseContext = new BlockItemUseContext(context);
            BlockPos blockPos = blockItemUseContext.getPos();
            ItemStack itemStack = context.getItem();
            Vector3d vec3d = new Vector3d(blockPos.getX(), blockPos.getY(), blockPos.getZ());
            AxisAlignedBB box = SCEntities.SCARECROW.get().getSize().func_242286_a(vec3d);

            if (world.hasNoCollisions(null, box, (entity) -> true) && world.getEntitiesWithinAABBExcludingEntity(null, box).isEmpty()) {
                if (world instanceof ServerWorld) {
                    ServerWorld serverWorld = (ServerWorld)world;
                    ScarecrowEntity entity = SCEntities.SCARECROW.get().create(serverWorld, itemStack.getTag(), null, context.getPlayer(), blockPos, SpawnReason.SPAWN_EGG, true, true);
                    if (entity == null) {
                        return ActionResultType.FAIL;
                    }

                    serverWorld.func_242417_l(entity);
                    entity.setColor(this.getDyeColor());
                    float f = (float)MathHelper.floor((MathHelper.wrapDegrees(context.getPlacementYaw() - 180.0F) + 22.5F) / 45.0F) * 45.0F;
                    entity.setPositionAndRotation(entity.getPosX(), entity.getPosY(), entity.getPosZ(), f, 0.0F);
                    world.addEntity(entity);
                    world.playSound(null, entity.getPosX(), entity.getPosY(), entity.getPosZ(), SoundEvents.ENTITY_ARMOR_STAND_PLACE, SoundCategory.BLOCKS, 0.75F, 0.8F);
                }
                itemStack.shrink(1);
                return ActionResultType.func_233537_a_(world.isRemote);
            } else {
                return ActionResultType.FAIL;
            }
        }
    }

    public DyeColor getDyeColor() {
        return this.dyeColor;
    }
}
