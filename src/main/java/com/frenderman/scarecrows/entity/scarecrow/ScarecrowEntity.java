package com.frenderman.scarecrows.entity.scarecrow;

import com.frenderman.scarecrows.init.SCItems;
import com.frenderman.scarecrows.init.SCSoundEvents;
import com.frenderman.scarecrows.tag.SCItemTags;
import com.google.common.collect.Maps;
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
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.*;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LightType;
import net.minecraft.world.World;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class ScarecrowEntity extends LivingEntity {
    public static final String id = "scarecrow";

    private static final Map<DyeColor, ItemConvertible> DROPS = Util.make(Maps.newEnumMap(DyeColor.class), (enumMap) -> {
        enumMap.put(DyeColor.WHITE, SCItems.WHITE_SCARECROW);
        enumMap.put(DyeColor.ORANGE, SCItems.ORANGE_SCARECROW);
        enumMap.put(DyeColor.MAGENTA, SCItems.MAGENTA_SCARECROW);
        enumMap.put(DyeColor.LIGHT_BLUE, SCItems.LIGHT_BLUE_SCARECROW);
        enumMap.put(DyeColor.YELLOW, SCItems.YELLOW_SCARECROW);
        enumMap.put(DyeColor.LIME, SCItems.LIME_SCARECROW);
        enumMap.put(DyeColor.PINK, SCItems.PINK_SCARECROW);
        enumMap.put(DyeColor.GRAY, SCItems.GRAY_SCARECROW);
        enumMap.put(DyeColor.LIGHT_GRAY, SCItems.LIGHT_GRAY_SCARECROW);
        enumMap.put(DyeColor.CYAN, SCItems.CYAN_SCARECROW);
        enumMap.put(DyeColor.PURPLE, SCItems.PURPLE_SCARECROW);
        enumMap.put(DyeColor.BLUE, SCItems.BLUE_SCARECROW);
        enumMap.put(DyeColor.BROWN, SCItems.BROWN_SCARECROW);
        enumMap.put(DyeColor.GREEN, SCItems.GREEN_SCARECROW);
        enumMap.put(DyeColor.RED, SCItems.RED_SCARECROW);
        enumMap.put(DyeColor.BLACK, SCItems.BLACK_SCARECROW);
    });
    private static final TrackedData<Byte> COLOR = DataTracker.registerData(ScarecrowEntity.class, TrackedDataHandlerRegistry.BYTE);
    public static final TrackedData<Boolean> FRENCH = DataTracker.registerData(ScarecrowEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    public static final TrackedData<Boolean> HIDE_ARMS = DataTracker.registerData(ScarecrowEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    public static final TrackedData<Boolean> HIDE_POST = DataTracker.registerData(ScarecrowEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    public static final TrackedData<Boolean> MARKER = DataTracker.registerData(ScarecrowEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    public static final TrackedData<Byte> SCARECROW_FLAGS = DataTracker.registerData(ScarecrowEntity.class, TrackedDataHandlerRegistry.BYTE);

    private static final Predicate<Entity> RIDEABLE_MINECART_PREDICATE = (entity) -> entity instanceof AbstractMinecartEntity && ((AbstractMinecartEntity)entity).getMinecartType() == AbstractMinecartEntity.Type.RIDEABLE;

    private boolean invisible;
    public long lastDamageTime;
    public long lastHitTime;

    public ScarecrowEntity(EntityType<? extends ScarecrowEntity> entityType, World world) {
        super(entityType, world);

        this.setColor(DyeColor.CYAN);
        this.stepHeight = 0.0F;
    }

    @Override
    public void calculateDimensions() {
        double x = this.getX();
        double y = this.getY();
        double z = this.getZ();
        super.calculateDimensions();
        this.updatePosition(x, y, z);
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
        this.dataTracker.startTracking(COLOR, (byte)0);
        this.dataTracker.startTracking(FRENCH, false);
        this.dataTracker.startTracking(HIDE_ARMS, false);
        this.dataTracker.startTracking(HIDE_POST, false);
        this.dataTracker.startTracking(MARKER, false);
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
    public void writeCustomDataToTag(CompoundTag tag) {
        super.writeCustomDataToTag(tag);

        tag.putBoolean("Invisible", this.isInvisible());
        tag.putBoolean("HideArms", this.shouldHideArms());
        tag.putBoolean("HidePost", this.shouldHidePost());
        if (this.isMarker()) {
            tag.putBoolean("Marker", this.isMarker());
        }

        tag.putByte("Color", (byte)this.getColor().getId());
        tag.putBoolean("French", this.isFrench());
        tag.putInt("StuckArrowCount", this.getStuckArrowCount());
    }

    @Override
    public void readCustomDataFromTag(CompoundTag tag) {
        super.readCustomDataFromTag(tag);

        this.setInvisible(tag.getBoolean("Invisible"));
        this.setHideArms(tag.getBoolean("HideArms"));
        this.setHidePost(tag.getBoolean("HidePost"));
        this.setMarker(tag.getBoolean("Marker"));
        this.noClip = !this.canClip();

        this.setColor(DyeColor.byId(tag.getByte("Color")));
        this.setFrench(tag.getBoolean("French"));
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
        this.stuckArrowTimer = 2; // prevent arrow 'despawn'
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        this.world.sendEntityStatus(this, (byte)32); // treat as if damaged...
        this.lastHitTime = this.world.getTime();

        if (!this.world.isClient && !this.removed) {
            if (DamageSource.OUT_OF_WORLD.equals(source)) {
                this.remove();
                return false;
            } else if (source.isProjectile() || (source.getAttacker() instanceof LivingEntity && ((LivingEntity)source.getAttacker()).getMainHandStack().getItem().isIn(SCItemTags.INEFFECTIVE_SCARECROW_DAMAGERS))) {
                this.world.sendEntityStatus(this, (byte)32); // treat as if damaged...
                this.lastDamageTime = this.world.getTime();

                return false; // ...but don't actually damage
            } else if (!this.isInvulnerableTo(source) && !this.invisible && !this.isMarker()) {
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
                    boolean isPersistentProjectile = source.getSource() instanceof PersistentProjectileEntity;
                    boolean hasPiercing = isPersistentProjectile && ((PersistentProjectileEntity) source.getSource()).getPierceLevel() > 0;
                    boolean sourceIsPlayer = source.getSource() instanceof PlayerEntity;
                    if (!sourceIsPlayer && !isPersistentProjectile) {
                        return false;
                    } else if (source.getAttacker() instanceof PlayerEntity && !((PlayerEntity) source.getAttacker()).abilities.allowModifyWorld) {
                        return false;
                    } else if (source.isSourceCreativePlayer()) {
                        this.playBreakSound();
                        this.spawnBreakParticles();
                        this.remove();
                        return hasPiercing;
                    } else {
                        long currentWorldTime = this.world.getTime();
                        if (currentWorldTime - this.lastDamageTime > 5L && !isPersistentProjectile) {
                            this.world.sendEntityStatus(this, (byte) 32);
                            this.lastDamageTime = currentWorldTime;
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
        if (this.isAlive()) {
            ItemStack handStack = player.getStackInHand(hand);
            Item handItem = handStack.getItem();
            ItemStack heldStack = player.getMainHandStack();

            if (handItem instanceof DyeItem && (this.getColor() != ((DyeItem) handItem).getColor() || this.isFrench())) {
                if (!player.abilities.creativeMode) {
                    heldStack.decrement(1);
                }

                this.setColor(((DyeItem) handItem).getColor(), !player.abilities.creativeMode);
                return ActionResult.success(this.world.isClient);
            } else if (handItem == Items.BREAD && !this.isFrench()) {
                if (!player.abilities.creativeMode) {
                    heldStack.decrement(1);
                }

                for(int i = 0; i < 4; i++) {
                    this.world.addParticle(ParticleTypes.CLOUD, this.getX() + this.random.nextDouble() / 2.0D, this.getBodyY(0.5D), this.getZ() + this.random.nextDouble() / 2.0D, 0.0D, this.random.nextDouble() / 5.0D, 0.0D);
                }
                this.playSound(SCSoundEvents.ENTITY_SCARECROW_FRENCHIFY, 1.0F, 1.0F);

                this.setFrench(true);
                return ActionResult.success(this.world.isClient);
            } else if (heldStack.isEmpty()) {
                int stuckArrowCount = this.getStuckArrowCount();
                if (stuckArrowCount > 0) {
                    player.setStackInHand(hand, new ItemStack(Items.ARROW, stuckArrowCount));
                    this.setStuckArrowCount(0);

                    return ActionResult.success(this.world.isClient);
                }
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
                this.lastDamageTime = this.world.getTime();
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
        Block.dropStack(this.world, this.getBlockPos(), new ItemStack(DROPS.get(this.getColor())));
        Block.dropStack(this.world, this.getBlockPos(), new ItemStack(Items.ARROW, this.getStuckArrowCount()));
        if (this.isFrench()) Block.dropStack(this.world, this.getBlockPos(), new ItemStack(Items.BREAD));
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
        if (this.canClip()) super.travel(movementInput);
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

    public boolean shouldHideArms() {
        return this.dataTracker.get(HIDE_ARMS);
    }
    public void setHideArms(boolean hideArms) {
        this.dataTracker.set(HIDE_ARMS, hideArms);
    }

    public boolean shouldHidePost() {
        return this.dataTracker.get(HIDE_POST);
    }
    public void setHidePost(boolean hidePost) {
        this.dataTracker.set(HIDE_POST, hidePost);
    }

    public boolean isMarker() {
        return this.dataTracker.get(MARKER);
    }
    public void setMarker(boolean isMarker) {
        this.dataTracker.set(MARKER, isMarker);
    }

    public DyeColor getColor() {
        return DyeColor.byId(this.dataTracker.get(COLOR) & 15);
    }
    public void setColor(DyeColor color, boolean dropBreadIfFrench) {
        byte b = this.dataTracker.get(COLOR);
        this.dataTracker.set(COLOR, (byte)(b & 240 | color.getId() & 15));

        if (this.isFrench()) {
            if (dropBreadIfFrench) Block.dropStack(this.world, this.getBlockPos(), new ItemStack(Items.BREAD));
            this.setFrench(false);
        }
    }
    public void setColor(DyeColor color) {
        this.setColor(color, false);
    }

    public static Map<DyeColor, ItemConvertible> getDrops() {
        return DROPS;
    }

    public boolean isFrench() {
        return this.dataTracker.get(FRENCH);
    }
    public void setFrench(boolean isFrench) {
        this.dataTracker.set(FRENCH, isFrench);
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
        this.calculateDimensions();
        this.inanimate = !this.isMarker();

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
