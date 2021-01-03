package com.frenderman.scarecrows.entity.scarecrow;

import com.frenderman.scarecrows.tag.SCItemTags;
import com.frenderman.scarecrows.init.SCItems;
import com.frenderman.scarecrows.init.SCSoundEvents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LightType;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.Predicate;

public class ScarecrowEntity extends LivingEntity {
    public static final String id = "scarecrow";

    public static final TrackedData<Byte> SCARECROW_FLAGS = DataTracker.registerData(ScarecrowEntity.class, TrackedDataHandlerRegistry.BYTE);
    private static final Predicate<Entity> RIDEABLE_MINECART_PREDICATE = (entity) -> entity instanceof AbstractMinecartEntity && ((AbstractMinecartEntity)entity).getMinecartType() == AbstractMinecartEntity.Type.RIDEABLE;
    private boolean invisible;
    public long lastHitTime;

    public ScarecrowEntity(EntityType<? extends ScarecrowEntity> entityType, World world) {
        super(entityType, world);
        this.stepHeight = 0.0F;
    }

    @Override
    public void calculateDimensions() {
        double d = this.getX();
        double e = this.getY();
        double f = this.getZ();
        super.calculateDimensions();
        this.updatePosition(d, e, f);
    }

    private boolean canClip() {
        return !this.isMarker() && !this.hasNoGravity();
    }

    @Override
    public boolean canMoveVoluntarily() {
        return super.canMoveVoluntarily() && this.canClip();
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(SCARECROW_FLAGS, (byte)0);
    }

    @Override
    public Iterable<ItemStack> getItemsHand() {
        return DefaultedList.ofSize(2, ItemStack.EMPTY);
    }

    @Override
    public Iterable<ItemStack> getArmorItems() {
        return DefaultedList.ofSize(4, ItemStack.EMPTY);
    }

    @Override
    public ItemStack getEquippedStack(EquipmentSlot slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public void equipStack(EquipmentSlot slot, ItemStack stack) {}

    @Override
    public boolean equip(int slot, ItemStack item) {
        EquipmentSlot equipmentSlot7;
        if (slot == 98) {
            equipmentSlot7 = EquipmentSlot.MAINHAND;
        } else if (slot == 99) {
            equipmentSlot7 = EquipmentSlot.OFFHAND;
        } else if (slot == 100 + EquipmentSlot.HEAD.getEntitySlotId()) {
            equipmentSlot7 = EquipmentSlot.HEAD;
        } else if (slot == 100 + EquipmentSlot.CHEST.getEntitySlotId()) {
            equipmentSlot7 = EquipmentSlot.CHEST;
        } else if (slot == 100 + EquipmentSlot.LEGS.getEntitySlotId()) {
            equipmentSlot7 = EquipmentSlot.LEGS;
        } else {
            if (slot != 100 + EquipmentSlot.FEET.getEntitySlotId()) {
                return false;
            }

            equipmentSlot7 = EquipmentSlot.FEET;
        }

        if (!item.isEmpty() && !MobEntity.canEquipmentSlotContain(equipmentSlot7, item) && equipmentSlot7 != EquipmentSlot.HEAD) {
            return false;
        } else {
            this.equipStack(equipmentSlot7, item);
            return true;
        }
    }

    @Override
    public void writeCustomDataToTag(CompoundTag tag) {
        super.writeCustomDataToTag(tag);

        tag.putBoolean("Invisible", this.isInvisible());
        tag.putBoolean("NoShowArms", this.shouldHideArms());
        tag.putBoolean("NoPost", this.shouldHidePost());
        if (this.isMarker()) {
            tag.putBoolean("Marker", this.isMarker());
        }

        tag.putInt("StuckArrowCount", this.getStuckArrowCount());
    }

    @Override
    public void readCustomDataFromTag(CompoundTag tag) {
        super.readCustomDataFromTag(tag);

        this.setInvisible(tag.getBoolean("Invisible"));
        this.setNoShowArms(tag.getBoolean("NoShowArms"));
        this.setHidePost(tag.getBoolean("NoPost"));
        this.setMarker(tag.getBoolean("Marker"));
        this.noClip = !this.canClip();

        this.setStuckArrowCount(tag.getInt("StuckArrowCount"));
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected void pushAway(Entity entity) {}

    @Override
    protected void tickCramming() {
        List<Entity> list = this.world.getOtherEntities(this, this.getBoundingBox(), RIDEABLE_MINECART_PREDICATE);

        for (Entity value : list) {
            if (this.squaredDistanceTo(value) <= 0.2D) {
                value.pushAwayFrom(this);
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        this.stuckArrowTimer = 2;
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (!this.world.isClient && !this.removed) {
            if (DamageSource.OUT_OF_WORLD.equals(source)) {
                this.remove();
                return false;
            } else if (source.isProjectile() || (source.getAttacker() instanceof LivingEntity && ((LivingEntity)source.getAttacker()).getMainHandStack().getItem().isIn(SCItemTags.WOODEN_TOOLS))) {
                this.world.sendEntityStatus(this, (byte)32);
                this.lastHitTime = this.world.getTime();

                return false;
            } if (!this.isInvulnerableTo(source) && !this.invisible && !this.isMarker()) {
                if (source.isExplosive()) {
                    this.onBreak(source);
                    this.remove();
                    return false;
                } else if (DamageSource.IN_FIRE.equals(source)) {
                    if (this.isOnFire()) {
                        this.updateHealth(source, 0.15F);
                    } else {
                        this.setOnFireFor(5);
                    }

                    return false;
                } else if (DamageSource.ON_FIRE.equals(source) && this.getHealth() > 0.5F) {
                    this.updateHealth(source, 4.0F);
                    return false;
                } else {
                    boolean bl = source.getSource() instanceof PersistentProjectileEntity;
                    boolean bl2 = bl && ((PersistentProjectileEntity)source.getSource()).getPierceLevel() > 0;
                    boolean bl3 = "player".equals(source.getName());
                    if (!bl3 && !bl) {
                        return false;
                    } else if (source.getAttacker() instanceof PlayerEntity && !((PlayerEntity)source.getAttacker()).abilities.allowModifyWorld) {
                        return false;
                    } else if (source.isSourceCreativePlayer()) {
                        this.playBreakSound();
                        this.spawnBreakParticles();
                        this.remove();
                        return bl2;
                    } else {
                        long l = this.world.getTime();
                        if (l - this.lastHitTime > 5L && !bl) {
                            this.world.sendEntityStatus(this, (byte)32);
                            this.lastHitTime = l;
                        } else {
                            this.breakAndDropItem(source);
                            this.spawnBreakParticles();
                            this.remove();
                        }

                        return true;
                    }
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public ActionResult interact(PlayerEntity player, Hand hand) {
        int stuckArrowCount = this.getStuckArrowCount();
        if (stuckArrowCount > 0) {
            ItemStack heldStack = player.getMainHandStack();
            if (heldStack.isEmpty()) {
                player.setStackInHand(hand, new ItemStack(Items.ARROW, stuckArrowCount));
                this.setStuckArrowCount(0);

                return ActionResult.success(this.world.isClient);
            }
        }

        return super.interact(player, hand);
    }
    public int getMaxStuckArrowCount() {
        return 64;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void handleStatus(byte status) {
        if (status == 32) {
            if (this.world.isClient) {
                this.world.playSound(this.getX(), this.getY(), this.getZ(), SCSoundEvents.ENTITY_SCARECROW_HIT, this.getSoundCategory(), 0.3F, 1.0F, false);
                this.lastHitTime = this.world.getTime();
            }
        } else {
            super.handleStatus(status);
        }

    }

    @Override
    @Environment(EnvType.CLIENT)
    public boolean shouldRender(double distance) {
        double d = this.getBoundingBox().getAverageSideLength() * 4.0D;
        if (Double.isNaN(d) || d == 0.0D) {
            d = 4.0D;
        }

        d *= 64.0D;
        return distance < d * d;
    }

    private void spawnBreakParticles() {
        if (this.world instanceof ServerWorld) {
            for (Block block : new Block[]{ Blocks.PUMPKIN, Blocks.OAK_PLANKS }) {
                ((ServerWorld)this.world).spawnParticles(
                    new BlockStateParticleEffect(ParticleTypes.BLOCK, block.getDefaultState()),

                    this.getX(),
                    this.getBodyY(0.6666666666666666D),
                    this.getZ(),

                    10,

                    this.getWidth() / 4.0F,
                    this.getHeight() / 4.0F,
                    this.getWidth() / 4.0F,

                    0.05D
                );
            }
        }
    }

    private void updateHealth(DamageSource damageSource, float amount) {
        float f = this.getHealth();
        f -= amount;
        if (f <= 0.5F) {
            this.onBreak(damageSource);
            this.remove();
        } else {
            this.setHealth(f);
        }
    }

    private void breakAndDropItem(DamageSource damageSource) {
        Block.dropStack(this.world, this.getBlockPos(), new ItemStack(SCItems.SCARECROW));
        Block.dropStack(this.world, this.getBlockPos(), new ItemStack(Items.ARROW, this.getStuckArrowCount()));
        this.onBreak(damageSource);
    }

    private void onBreak(DamageSource damageSource) {
        this.playBreakSound();
        this.drop(damageSource);
    }

    private void playBreakSound() {
        this.world.playSound(null, this.getX(), this.getY(), this.getZ(), SCSoundEvents.ENTITY_SCARECROW_BREAK, this.getSoundCategory(), 1.0F, 1.0F);
    }

    @Override
    protected float turnHead(float bodyRotation, float headRotation) {
        this.prevBodyYaw = this.prevYaw;
        this.bodyYaw = this.yaw;
        return 0.0F;
    }

    @Override
    public double getHeightOffset() {
        return this.isMarker() ? 0.0D : 0.10000000149011612D;
    }

    @Override
    public void travel(Vec3d movementInput) {
        if (this.canClip()) {
            super.travel(movementInput);
        }
    }

    @Override
    public void setYaw(float yaw) {
        this.prevBodyYaw = this.prevYaw = yaw;
        this.prevHeadYaw = this.headYaw = yaw;
    }

    @Override
    public void setHeadYaw(float headYaw) {
        this.prevBodyYaw = this.prevYaw = headYaw;
        this.prevHeadYaw = this.headYaw = headYaw;
    }

    @Override
    public void setInvisible(boolean invisible) {
        this.invisible = invisible;
        super.setInvisible(invisible);
    }

    @Override
    public void kill() {
        this.remove();
    }

    @Override
    public boolean isImmuneToExplosion() {
        return this.isInvisible();
    }

    @Override
    public PistonBehavior getPistonBehavior() {
        return this.isMarker() ? PistonBehavior.IGNORE : super.getPistonBehavior();
    }

    private void setNoShowArms(boolean showArms) {
        this.dataTracker.set(SCARECROW_FLAGS, this.setBitField(this.dataTracker.get(SCARECROW_FLAGS), 4, showArms));
    }
    public boolean shouldHideArms() {
        return (this.dataTracker.get(SCARECROW_FLAGS) & 4) != 0;
    }

    private void setHidePost(boolean hidePost) {
        this.dataTracker.set(SCARECROW_FLAGS, this.setBitField(this.dataTracker.get(SCARECROW_FLAGS), 8, hidePost));
    }
    public boolean shouldHidePost() {
        return (this.dataTracker.get(SCARECROW_FLAGS) & 8) != 0;
    }

    private void setMarker(boolean marker) {
        this.dataTracker.set(SCARECROW_FLAGS, this.setBitField(this.dataTracker.get(SCARECROW_FLAGS), 16, marker));
    }
    public boolean isMarker() {
        return (this.dataTracker.get(SCARECROW_FLAGS) & 16) != 0;
    }

    private byte setBitField(byte value, int bitField, boolean set) {
        if (set) {
            value = (byte)(value | bitField);
        } else {
            value = (byte)(value & ~bitField);
        }

        return value;
    }

    @Override
    public boolean collides() {
        return super.collides() && !this.isMarker();
    }

    @Override
    public boolean handleAttack(Entity attacker) {
        return attacker instanceof PlayerEntity && !this.world.canPlayerModifyAt((PlayerEntity)attacker, this.getBlockPos());
    }

    @Override
    public Arm getMainArm() {
        return Arm.RIGHT;
    }

    @Override
    protected SoundEvent getFallSound(int distance) {
        return SCSoundEvents.ENTITY_SCARECROW_FALL;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SCSoundEvents.ENTITY_SCARECROW_HIT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SCSoundEvents.ENTITY_SCARECROW_BREAK;
    }

    @Override
    public void onStruckByLightning(ServerWorld world, LightningEntity lightning) {}

    @Override
    public void onTrackedDataSet(TrackedData<?> data) {
        if (SCARECROW_FLAGS.equals(data)) {
            this.calculateDimensions();
            this.inanimate = !this.isMarker();
        }

        super.onTrackedDataSet(data);
    }

    @Override
    public boolean isMobOrPlayer() {
        return false;
    }

    @Override
    public EntityDimensions getDimensions(EntityPose pose) {
        return this.isMarker() ? EntityDimensions.fixed(0.0F, 0.0F) : super.getDimensions(pose);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public Vec3d method_31166(float tickDelta) {
        if (this.isMarker()) {
            Box box = this.getDimensions(this.getPose()).method_30757(this.getPos());
            BlockPos blockPos = this.getBlockPos();
            int i = Integer.MIN_VALUE;

            for (BlockPos blockPos2 : BlockPos.iterate(new BlockPos(box.minX, box.minY, box.minZ), new BlockPos(box.maxX, box.maxY, box.maxZ))) {
                int j = Math.max(this.world.getLightLevel(LightType.BLOCK, blockPos2), this.world.getLightLevel(LightType.SKY, blockPos2));
                if (j == 15) {
                    return Vec3d.ofCenter(blockPos2);
                }

                if (j > i) {
                    i = j;
                    blockPos = blockPos2.toImmutable();
                }
            }

            return Vec3d.ofCenter(blockPos);
        } else {
            return super.method_31166(tickDelta);
        }
    }
}
