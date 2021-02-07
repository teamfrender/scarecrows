package com.frenderman.scarecrows.common.entity;

import com.frenderman.scarecrows.common.register.SCItems;
import com.frenderman.scarecrows.common.register.SCSoundEvents;
import com.frenderman.scarecrows.common.tag.SCItemTags;
import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.*;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class ScarecrowEntity extends LivingEntity {

    private static final Map<DyeColor, Supplier<Item>> DROPS = Util.make(Maps.newEnumMap(DyeColor.class), (enumMap) -> {
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
    private static final DataParameter<Byte> COLOR = EntityDataManager.createKey(ScarecrowEntity.class, DataSerializers.BYTE);
    public static final DataParameter<Boolean> FRENCH = EntityDataManager.createKey(ScarecrowEntity.class, DataSerializers.BOOLEAN);
    public static final DataParameter<Boolean> HIDE_ARMS = EntityDataManager.createKey(ScarecrowEntity.class, DataSerializers.BOOLEAN);
    public static final DataParameter<Boolean> HIDE_POST = EntityDataManager.createKey(ScarecrowEntity.class, DataSerializers.BOOLEAN);
    public static final DataParameter<Boolean> MARKER = EntityDataManager.createKey(ScarecrowEntity.class, DataSerializers.BOOLEAN);
    public static final DataParameter<Byte> SCARECROW_FLAGS = EntityDataManager.createKey(ScarecrowEntity.class, DataSerializers.BYTE);

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
    public void recalculateSize() {
        double x = this.getPosX();
        double y = this.getPosY();
        double z = this.getPosZ();
        super.recalculateSize();
        this.setPosition(x, y, z);
    }

    private boolean canClip() {
        return !this.isMarker() && !this.hasNoGravity();
    }

    @Override
    public boolean isServerWorld() {
        return super.isServerWorld() && this.canClip();
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(COLOR, (byte)0);
        this.dataManager.register(FRENCH, false);
        this.dataManager.register(HIDE_ARMS, false);
        this.dataManager.register(HIDE_POST, false);
        this.dataManager.register(MARKER, false);
        this.dataManager.register(SCARECROW_FLAGS, (byte)0);
    }

    @Override
    public Iterable<ItemStack> getHeldEquipment() {
        return NonNullList.withSize(2, ItemStack.EMPTY);
    }

    @Override
    public Iterable<ItemStack> getArmorInventoryList() {
        return NonNullList.withSize(4, ItemStack.EMPTY);
    }

    @Override
    public ItemStack getItemStackFromSlot(EquipmentSlotType slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemStackToSlot(EquipmentSlotType slot, ItemStack stack) {}

    @Override
    public void writeAdditional(CompoundNBT tag) {
        super.writeAdditional(tag);

        tag.putBoolean("Invisible", this.isInvisible());
        tag.putBoolean("HideArms", this.shouldHideArms());
        tag.putBoolean("HidePost", this.shouldHidePost());
        if (this.isMarker()) {
            tag.putBoolean("Marker", this.isMarker());
        }

        tag.putByte("Color", (byte)this.getColor().getId());
        tag.putBoolean("French", this.isFrench());
        tag.putInt("StuckArrowCount", this.getArrowCountInEntity());
    }

    @Override
    public void readAdditional(CompoundNBT tag) {
        super.readAdditional(tag);

        this.setInvisible(tag.getBoolean("Invisible"));
        this.setHideArms(tag.getBoolean("HideArms"));
        this.setHidePost(tag.getBoolean("HidePost"));
        this.setMarker(tag.getBoolean("Marker"));
        this.noClip = !this.canClip();

        this.setColor(DyeColor.byId(tag.getByte("Color")));
        this.setFrench(tag.getBoolean("French"));
        this.setArrowCountInEntity(tag.getInt("StuckArrowCount"));
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    protected void collideWithEntity(Entity entity) {}

    @Override
    protected void collideWithNearbyEntities() {
        List<Entity> list = this.world.getEntitiesInAABBexcluding(this, this.getBoundingBox(), RIDEABLE_MINECART_PREDICATE);
        for (Entity value : list) {
            if (this.getDistanceSq(value) <= 0.2D) {
                value.applyEntityCollision(this);
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        this.arrowHitTimer = 2; // prevent arrow 'despawn'
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        this.world.setEntityState(this, (byte)32); // treat as if damaged...
        this.lastHitTime = this.world.getGameTime();

        if (!this.world.isRemote && !this.isAlive()) {
            if (DamageSource.OUT_OF_WORLD.equals(source)) {
                this.remove();
                return false;
            } else if (source.isProjectile() || (source.getTrueSource() instanceof LivingEntity && ((LivingEntity)source.getTrueSource()).getHeldItemMainhand().getItem().isIn(SCItemTags.INEFFECTIVE_SCARECROW_DAMAGERS))) {
                this.world.setEntityState(this, (byte)32); // treat as if damaged...
                this.lastDamageTime = this.world.getGameTime();

                return false; // ...but don't actually damage
            } else if (!this.isInvulnerableTo(source) && !this.invisible && !this.isMarker()) {
                if (source.isExplosion()) {
                    this.onBreak(source);
                    this.remove();
                    return false;
                } else if (DamageSource.IN_FIRE.equals(source)) {
                    if (this.isBurning()) {
                        this.updateHealth(source, 0.15F);
                    } else {
                        this.setFire(5);
                    }
                    return false;

                } else if (DamageSource.ON_FIRE.equals(source) && this.getHealth() > 0.5F) {
                    this.updateHealth(source, 4.0F);
                    return false;
                } else {
                    boolean isArrowProjectile = source.getImmediateSource() instanceof ArrowEntity;
                    boolean hasPiercing = isArrowProjectile && ((ArrowEntity) source.getImmediateSource()).getPierceLevel() > 0;
                    boolean sourceIsPlayer = source.getImmediateSource() instanceof PlayerEntity;
                    if (!sourceIsPlayer && !isArrowProjectile) {
                        return false;
                    } else if (source.getTrueSource() instanceof PlayerEntity && !((PlayerEntity) source.getTrueSource()).abilities.allowEdit) {
                        return false;
                    } else if (source.isCreativePlayer()) {
                        this.playBreakSound();
                        this.spawnBreakParticles();
                        this.remove();
                        return hasPiercing;
                    } else {
                        long currentWorldTime = this.world.getGameTime();
                        if (currentWorldTime - this.lastDamageTime > 5L && !isArrowProjectile) {
                            this.world.setEntityState(this, (byte) 32);
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
    public ActionResultType processInitialInteract(PlayerEntity player, Hand hand) {
        if (this.isAlive()) {
            ItemStack handStack = player.getHeldItem(hand);
            Item handItem = handStack.getItem();
            ItemStack heldStack = player.getHeldItemMainhand();

            if (handItem instanceof DyeItem && (this.getColor() != ((DyeItem) handItem).getDyeColor() || this.isFrench())) {
                if (!player.abilities.isCreativeMode) {
                    heldStack.shrink(1);
                }

                this.setColor(((DyeItem) handItem).getDyeColor(), !player.abilities.isCreativeMode);
                return ActionResultType.func_233537_a_(this.world.isRemote);
            } else if (handItem == Items.BREAD && !this.isFrench()) {
                if (!player.abilities.isCreativeMode) {
                    heldStack.shrink(1);
                }

                for(int i = 0; i < 4; i++) {
                    this.world.addParticle(ParticleTypes.CLOUD, this.getPosX() + this.rand.nextDouble() / 2.0D, this.getPosYHeight(0.5D), this.getPosZ() + this.rand.nextDouble() / 2.0D, 0.0D, this.rand.nextDouble() / 5.0D, 0.0D);
                }
                this.playSound(SCSoundEvents.ENTITY_SCARECROW_FRENCHIFY.get(), 1.0F, 1.0F);

                this.setFrench(true);
                return ActionResultType.func_233537_a_(this.world.isRemote);
            } else if (heldStack.isEmpty()) {
                int stuckArrowCount = this.getArrowCountInEntity();
                if (stuckArrowCount > 0) {
                    player.setHeldItem(hand, new ItemStack(Items.ARROW, stuckArrowCount));
                    this.setArrowCountInEntity(0);

                    return ActionResultType.func_233537_a_(this.world.isRemote);
                }
            }
        }
        return super.processInitialInteract(player, hand);
    }

    public int getMaxStuckArrowCount() {
        return 64;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void handleStatusUpdate(byte status) {
        if (status == 32) {
            if (this.world.isRemote) {
                this.world.playSound(this.getPosX(), this.getPosY(), this.getPosZ(), SCSoundEvents.ENTITY_SCARECROW_HIT.get(), this.getSoundCategory(), 0.3F, 1.0F, false);
                this.lastDamageTime = this.world.getGameTime();
                this.lastHitTime = this.world.getGameTime();
            }
        } else {
            super.handleStatusUpdate(status);
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean isInRangeToRenderDist(double distance) {
        double d = this.getBoundingBox().getAverageEdgeLength() * 4.0D;
        if (Double.isNaN(d) || d == 0.0D) {
            d = 4.0D;
        }

        d *= 64.0D;
        return distance < d * d;
    }

    private void spawnBreakParticles() {
        if (this.world instanceof ServerWorld) {
            for (Block block : new Block[]{ Blocks.PUMPKIN, Blocks.OAK_PLANKS }) {
                ((ServerWorld)this.world).spawnParticle(
                        new BlockParticleData(ParticleTypes.BLOCK, block.getDefaultState()),

                        this.getPosX(),
                        this.getPosYHeight(0.6666666666666666D),
                        this.getPosZ(),

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
        Block.spawnAsEntity(this.world, this.getPosition(), new ItemStack(DROPS.get(this.getColor()).get()));
        Block.spawnAsEntity(this.world, this.getPosition(), new ItemStack(Items.ARROW, this.getArrowCountInEntity()));
        if (this.isFrench()) Block.spawnAsEntity(this.world, this.getPosition(), new ItemStack(Items.BREAD));
        this.onBreak(damageSource);
    }

    private void onBreak(DamageSource damageSource) {
        this.playBreakSound();
        this.spawnDrops(damageSource);
    }

    private void playBreakSound() {
        this.world.playSound(null, this.getPosX(), this.getPosY(), this.getPosZ(), SCSoundEvents.ENTITY_SCARECROW_BREAK.get(), this.getSoundCategory(), 1.0F, 1.0F);
    }

    @Override
    protected float updateDistance(float bodyRotation, float headRotation) {
        this.prevRenderYawOffset = this.prevRotationYaw;
        this.renderYawOffset = this.rotationYaw;
        return 0.0F;
    }

    @Override
    public double getYOffset() {
        return this.isMarker() ? 0.0D : 0.10000000149011612D;
    }

    @Override
    public void travel(Vector3d movementInput) {
        if (this.canClip()) super.travel(movementInput);
    }

    @Override
    public void setRenderYawOffset(float yaw) {
        this.prevRenderYawOffset = this.prevRotationYaw = yaw;
        this.prevRotationYawHead = this.rotationYawHead = yaw;
    }

    @Override
    public void setRotationYawHead(float headYaw) {
        this.prevRenderYawOffset = this.prevRotationYaw = headYaw;
        this.prevRotationYawHead = this.rotationYawHead = headYaw;
    }

    @Override
    public void setInvisible(boolean invisible) {
        this.invisible = invisible;
        super.setInvisible(invisible);
    }

    @Override
    public void onKillCommand() {
        this.remove();
    }

    @Override
    public boolean isImmuneToExplosions() {
        return this.isInvisible();
    }

    @Override
    public PushReaction getPushReaction() {
        return this.isMarker() ? PushReaction.IGNORE : super.getPushReaction();
    }

    public boolean shouldHideArms() {
        return this.dataManager.get(HIDE_ARMS);
    }
    public void setHideArms(boolean hideArms) {
        this.dataManager.set(HIDE_ARMS, hideArms);
    }

    public boolean shouldHidePost() {
        return this.dataManager.get(HIDE_POST);
    }
    public void setHidePost(boolean hidePost) {
        this.dataManager.set(HIDE_POST, hidePost);
    }

    public boolean isMarker() {
        return this.dataManager.get(MARKER);
    }
    public void setMarker(boolean isMarker) {
        this.dataManager.set(MARKER, isMarker);
    }

    public DyeColor getColor() {
        return DyeColor.byId(this.dataManager.get(COLOR) & 15);
    }

    public void setColor(DyeColor color, boolean dropBreadIfFrench) {
        byte b = this.dataManager.get(COLOR);
        this.dataManager.set(COLOR, (byte)(b & 240 | color.getId() & 15));

        if (this.isFrench()) {
            if (dropBreadIfFrench) Block.spawnAsEntity(this.world, this.getPosition(), new ItemStack(Items.BREAD));
            this.setFrench(false);
        }
    }
    public void setColor(DyeColor color) {
        this.setColor(color, false);
    }

    public static Map<DyeColor, Supplier<Item>> getDrops() {
        return DROPS;
    }

    public boolean isFrench() {
        return this.dataManager.get(FRENCH);
    }
    public void setFrench(boolean isFrench) {
        this.dataManager.set(FRENCH, isFrench);
    }

    @Override
    public boolean canBeCollidedWith() {
        return super.canBeCollidedWith() && !this.isMarker();
    }

    @Override
    public boolean hitByEntity(Entity attacker) {
        return attacker instanceof PlayerEntity && !this.world.isBlockModifiable((PlayerEntity)attacker, this.getPosition());
    }

    @Override
    public HandSide getPrimaryHand() {
        return HandSide.RIGHT;
    }

    @Override
    protected SoundEvent getFallSound(int distance) {
        return SCSoundEvents.ENTITY_SCARECROW_FALL.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SCSoundEvents.ENTITY_SCARECROW_HIT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SCSoundEvents.ENTITY_SCARECROW_BREAK.get();
    }

    @Override
    public void func_241841_a(ServerWorld world, LightningBoltEntity lightning) {}

    @Override
    public void notifyDataManagerChange(DataParameter<?> data) {
        this.recalculateSize();
        this.preventEntitySpawning = !this.isMarker();

        super.notifyDataManagerChange(data);
    }

    @Override
    public boolean attackable() {
        return false;
    }

    @Override
    public EntitySize getSize(Pose pose) {
        return this.isMarker() ? EntitySize.fixed(0.0F, 0.0F) : super.getSize(pose);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public Vector3d func_241842_k(float tickDelta) {
        if (this.isMarker()) {
            AxisAlignedBB box = this.getBoundingBox(this.getPose()).offset(this.getPositionVec());
            BlockPos blockPos = this.getPosition();
            int i = Integer.MIN_VALUE;

            for (BlockPos blockPos2 : BlockPos.getAllInBoxMutable(new BlockPos(box.minX, box.minY, box.minZ), new BlockPos(box.maxX, box.maxY, box.maxZ))) {
                int j = Math.max(this.world.getLightFor(LightType.BLOCK, blockPos2), this.world.getLightFor(LightType.SKY, blockPos2));
                if (j == 15) {
                    return Vector3d.copyCentered(blockPos2);
                }

                if (j > i) {
                    i = j;
                    blockPos = blockPos2.toImmutable();
                }
            }

            return Vector3d.copyCentered(blockPos);
        } else {
            return super.func_241842_k(tickDelta);
        }
    }
}
