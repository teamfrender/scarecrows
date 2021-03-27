package com.frenderman.scarecrows.entity.crow;

import com.frenderman.scarecrows.entity.scarecrow.ScarecrowEntity;
import com.frenderman.scarecrows.init.SCSoundEvents;
import com.frenderman.scarecrows.tag.SCItemTags;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CropBlock;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.TargetFinder;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.control.FlightMoveControl;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.BirdNavigation;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.Random;

public class CrowEntity extends AnimalEntity implements Flutterer {
    public static final String id = "crow";

    private static final Item COOKIE = Items.COOKIE;
    public float flapProgress;
    public float maxWingDeviation;
    public float prevMaxWingDeviation;
    public float prevFlapProgress;
    private boolean wasOnGround;

    public CrowEntity(EntityType<? extends CrowEntity> entityType, World world) {
        super(entityType, world);

        this.setPathfindingPenalty(PathNodeType.DANGER_FIRE, -1.0F);
        this.setPathfindingPenalty(PathNodeType.DAMAGE_FIRE, -1.0F);
        this.setPathfindingPenalty(PathNodeType.COCOA, -1.0F);
        this.setPathfindingPenalty(PathNodeType.WATER, -1.0F);

        this.moveControl = new FlightMoveControl(this, 10, false);
        this.setCanPickUpLoot(true);
    }

    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, EntityData entityData, CompoundTag entityTag) {
        if (entityData == null) entityData = new PassiveEntity.PassiveData(false);
        return super.initialize(world, difficulty, spawnReason, entityData, entityTag);
    }

    @Override
    public boolean isBaby() {
        return false;
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new EscapeDangerGoal(this, 1.25D));
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new CrowEntity.FleeEntityAirGoal<>(this, ScarecrowEntity.class, 8.0F, 3.75D));
        this.goalSelector.add(1, new CrowEntity.FleeEntityAirGoal<>(this, PlayerEntity.class, 8.0F, 3.75D));
        this.goalSelector.add(2, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(2, new LookAtEntityGoal(this, ScarecrowEntity.class, 8.0F));
        this.goalSelector.add(2, new LookAtEntityGoal(this, VillagerEntity.class, 8.0F));
        this.goalSelector.add(3, new CrowEntity.FlyOntoCropGoal(this, 1.5D, 1.0F));
        this.goalSelector.add(4, new FlyOntoTreeGoal(this, 1.5D));
    }

    public static DefaultAttributeContainer.Builder createCrowAttributes() {
        return MobEntity.createMobAttributes()
            .add(EntityAttributes.GENERIC_MAX_HEALTH, 6.0D)
            .add(EntityAttributes.GENERIC_FLYING_SPEED, 0.4000000059604645D)
            .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.20000000298023224D);
    }

    @Override
    protected EntityNavigation createNavigation(World world) {
        BirdNavigation birdNavigation = new BirdNavigation(this, world);
        birdNavigation.setCanPathThroughDoors(false);
        birdNavigation.setCanSwim(true);
        birdNavigation.setCanEnterOpenDoors(true);

        return birdNavigation;
    }

    @Override
    protected float getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions) {
        return dimensions.height * 0.6F;
    }

    @Override
    public void tickMovement() {
        super.tickMovement();

        Vec3d vec3d = this.getVelocity();
        if (!this.isOnGround() && vec3d.y < 0.0D) {
            this.setVelocity(vec3d.multiply(1.0D, 0.6D, 1.0D));
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.world.isClient() && this.isAlive()) {
            BlockPos pos = this.getBlockPos();
            boolean currentPosIsAvailable = CrowEntity.isAvailableCrop(this.world, pos);
            if (CrowEntity.isAvailableCrop(this.world, pos.up()) || currentPosIsAvailable) {
                if (this.isOnGround()) {
                    if (currentPosIsAvailable) {
                        Vec3d posBottomCenter = Vec3d.ofBottomCenter(pos);
                        this.refreshPositionAndAngles(posBottomCenter.getX(), posBottomCenter.getY(), posBottomCenter.getZ(), this.yaw, this.pitch);
                    }

                    if (!wasOnGround) {
                        this.addVelocity(0.0D, 0.3D, 0.0D);
                        wasOnGround = true;
                    } else {
                        BlockState state = this.world.getBlockState(pos);
                        if (state.contains(FarmlandBlock.MOISTURE)) {
                            this.world.breakBlock(pos, false, this);
                            this.world.setBlockState(pos, state);
                        }

                        this.world.breakBlock(pos.up(), true, this);
                        wasOnGround = false;
                    }
                }
            } else {
                wasOnGround = false;
            }
        }
    }

    @Override
    protected void loot(ItemEntity itemEntity) {
        ItemStack currentItem = this.getEquippedStack(EquipmentSlot.MAINHAND);

        if (currentItem.isEmpty() && itemEntity != null && !itemEntity.cannotPickup() && itemEntity.isOnGround() && itemEntity.getStack().getItem().isIn(SCItemTags.CROPS)) {
            ItemStack newItem = itemEntity.getStack();

            this.equipLootStack(EquipmentSlot.MAINHAND, newItem);
            this.method_29499(itemEntity);
            this.sendPickup(itemEntity, newItem.getCount());
            itemEntity.remove();
        }
    }

    @Override
    public boolean cannotDespawn() {
        return this.hasVehicle() || !this.getMainHandStack().isEmpty();
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (itemStack.getItem() == COOKIE) {
            if (!player.abilities.creativeMode) {
                itemStack.decrement(1);
            }

            this.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 900));
            if (player.isCreative() || !this.isInvulnerable()) {
                this.damage(DamageSource.player(player), Float.MAX_VALUE);
            }

            return ActionResult.success(this.world.isClient);
        }

        return super.interactMob(player, hand);
    }

    public boolean isBreedingItem(ItemStack stack) {
        return false;
    }

    @SuppressWarnings("unused")
    public static boolean canSpawn(EntityType<CrowEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        BlockState blockState = world.getBlockState(pos.down());
        return (blockState.isIn(BlockTags.LEAVES) || blockState.isOf(Blocks.GRASS_BLOCK) || blockState.isIn(BlockTags.LOGS) || blockState.isOf(Blocks.AIR)) && world.getBaseLightLevel(pos, 0) > 8;
    }

    @Override
    public boolean handleFallDamage(float fallDistance, float damageMultiplier) {
        return false;
    }

    @Override
    protected void fall(double heightDifference, boolean onGround, BlockState landedState, BlockPos landedPosition) {}

    @Override
    public boolean canBreedWith(AnimalEntity other) {
        return false;
    }

    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return null;
    }

    @Override
    public boolean tryAttack(Entity target) {
        return target.damage(DamageSource.mob(this), 3.0F);
    }

    @Override
    public SoundEvent getAmbientSound() {
        return SCSoundEvents.ENTITY_CROW_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SCSoundEvents.ENTITY_CROW_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SCSoundEvents.ENTITY_CROW_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SCSoundEvents.ENTITY_CROW_STEP, 0.15F, 1.0F);
    }

    @Override
    protected float playFlySound(float distance) {
        this.playSound(SCSoundEvents.ENTITY_CROW_FLY, 0.15F, 1.0F);
        return distance + this.maxWingDeviation / 2.0F;
    }

    @Override
    protected boolean hasWings() {
        return true;
    }

    @Override
    protected float getSoundPitch() {
        return getSoundPitch(this.random);
    }

    public static float getSoundPitch(Random random) {
        return (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F;
    }

    @Override
    public SoundCategory getSoundCategory() {
        return SoundCategory.NEUTRAL;
    }

    @Override
    public boolean isPushable() {
        return true;
    }

    @Override
    protected void pushAway(Entity entity) {
        if (!(entity instanceof PlayerEntity)) {
            super.pushAway(entity);
        }
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        }

        return super.damage(source, amount);
    }

    public static boolean isAvailableCrop(World world, BlockPos blockPos) {
        BlockState state = world.getBlockState(blockPos);
        return state.getBlock() instanceof CropBlock/* && ((CropBlock) state.getBlock()).isMature(state)*/;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public Vec3d method_29919() {
        return new Vec3d(0.0D, 0.5F * this.getStandingEyeHeight(), this.getWidth() * 0.4F);
    }

    static class FlyOntoCropGoal extends WanderAroundFarGoal {
        private Vec3d foundVector;

        public FlyOntoCropGoal(PathAwareEntity pathAwareEntity, double speed, float choiceProbability) {
            super(pathAwareEntity, speed, choiceProbability);
        }

        @Override
        public boolean canStart() {
            return !this.mob.world.getBlockState(this.mob.getBlockPos()).isIn(BlockTags.CROPS) && super.canStart();
        }

        @Override
        protected Vec3d getWanderTarget() {
            foundVector = null;
            Vec3d pos = null;
            if (this.mob.isTouchingWater()) {
                pos = TargetFinder.findGroundTarget(this.mob, 15, 15);
            }

            if (this.mob.getRandom().nextFloat() <= this.probability) {
                pos = this.getBlockTarget();
            }

            return pos == null ? super.getWanderTarget() : pos;
        }

        private Vec3d getBlockTarget() {
            BlockPos pos = this.mob.getBlockPos();

            Box box = new Box(pos, pos).expand(8);
            Iterable<BlockPos> iterator = BlockPos.iterate((int)box.minX, (int)box.minY, (int)box.minZ, (int)box.maxX, (int)box.maxY, (int)box.maxZ);
            iterator.forEach(blockPos -> {
                if (CrowEntity.isAvailableCrop(this.mob.world, blockPos) && this.mob.getRandom().nextFloat() <= 0.5F) foundVector = Vec3d.ofBottomCenter(blockPos);
            });

            return foundVector;
        }
    }

    static class FleeEntityAirGoal<T extends LivingEntity> extends FleeEntityGoal<T> {
        private final TargetPredicate withinRangePredicate;

        public FleeEntityAirGoal(PathAwareEntity mob, Class<T> fleeFromType, float distance, double fastSpeed) {
            super(mob, fleeFromType, distance, fastSpeed, fastSpeed);
            withinRangePredicate = new TargetPredicate().setBaseMaxDistance(distance).setPredicate(inclusionSelector.and(extraInclusionSelector));
        }

        @Override
        public boolean canStart() {
            this.targetEntity = this.mob.world.getClosestEntityIncludingUngeneratedChunks(this.classToFleeFrom, this.withinRangePredicate, this.mob, this.mob.getX(), this.mob.getY(), this.mob.getZ(), this.mob.getBoundingBox().expand(this.fleeDistance, 3.0D, this.fleeDistance));
            if (this.targetEntity == null) {
                return false;
            } else {
                Vec3d vec3d = TargetFinder.findAirTarget(this.mob, 16, 7, this.targetEntity.getPos(), 1.5707964F, 2, 1);
                if (vec3d == null) {
                    return false;
                } else if (this.targetEntity.squaredDistanceTo(vec3d.x, vec3d.y, vec3d.z) < this.targetEntity.squaredDistanceTo(this.mob)) {
                    return false;
                } else {
                    this.fleePath = this.fleeingEntityNavigation.findPathTo(vec3d.x, vec3d.y, vec3d.z, 0);
                    return this.fleePath != null;
                }
            }
        }
    }
}
