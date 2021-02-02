package com.frenderman.scarecrows.entity.crow;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.CompositeEntityModel;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class CrowEntityModel extends CompositeEntityModel<CrowEntity> {
    private final ModelPart body;
    private final ModelPart torso;
    private final ModelPart head;
    public final ModelPart beak;
    private final ModelPart wings;
    private final ModelPart leftWing;
    private final ModelPart rightWing;
    private final ModelPart tail;

    public final ModelPart legs;
    private final ModelPart leftLeg;
    private final ModelPart rightLeg;

    public CrowEntityModel() {
        textureWidth = 64;
        textureHeight = 64;

        body = new ModelPart(this);
        body.setPivot(0.0F, 24.0F, 0.0F);

        torso = new ModelPart(this);
        torso.setPivot(0.0F, -7.0F, 0.5F);
        body.addChild(torso);
        torso.setTextureOffset(18, 45).addCuboid(-2.5F, -3.0F, -4.5F, 5.0F, 6.0F, 9.0F, 0.0F, false);

        head = new ModelPart(this);
        head.setPivot(0.0F, -8.0F, -3.0F);
        body.addChild(head);
        head.setTextureOffset(50, 51).addCuboid(-1.5F, -7.0F, -2.0F, 3.0F, 9.0F, 4.0F, 0.0F, false);

        beak = new ModelPart(this);
        beak.setPivot(0.0F, -5.0F, -2.0F);
        head.addChild(beak);
        beak.setTextureOffset(52, 46).addCuboid(-1.5F, -1.0F, -3.0F, 3.0F, 2.0F, 3.0F, 0.0F, false);

        wings = new ModelPart(this);
        wings.setPivot(0.0F, 0.0F, 0.0F);
        body.addChild(wings);

        leftWing = new ModelPart(this);
        leftWing.setPivot(-2.5F, -9.0F, 2.0F);
        wings.addChild(leftWing);
        leftWing.setTextureOffset(8, 52).addCuboid(-1.0F, 0.0F, -4.0F, 1.0F, 4.0F, 8.0F, 0.0F, false);

        rightWing = new ModelPart(this);
        rightWing.setPivot(2.5F, -9.0F, 2.0F);
        wings.addChild(rightWing);
        rightWing.setTextureOffset(8, 52).addCuboid(0.0F, 0.0F, -4.0F, 1.0F, 4.0F, 8.0F, 0.0F, true);

        tail = new ModelPart(this);
        tail.setPivot(0.0F, -9.0F, 6.0F);
        body.addChild(tail);
        tail.setTextureOffset(0, 47).addCuboid(-2.0F, -1.0F, -1.0F, 4.0F, 2.0F, 4.0F, 0.0F, false);

        legs = new ModelPart(this);
        legs.setPivot(0.0F, 20.0F, 2.0F);

        leftLeg = new ModelPart(this);
        leftLeg.setPivot(-1.0F, 0.0F, 0.0F);
        legs.addChild(leftLeg);
        leftLeg.setTextureOffset(0, 57).addCuboid(-0.5F, 0.0F, -3.0F, 1.0F, 4.0F, 3.0F, 0.0F, false);

        rightLeg = new ModelPart(this);
        rightLeg.setPivot(1.0F, 0.0F, 0.0F);
        legs.addChild(rightLeg);
        rightLeg.setTextureOffset(0, 57).addCuboid(-0.5F, 0.0F, -3.0F, 1.0F, 4.0F, 3.0F, 0.0F, false);
    }

    @Override
    public Iterable<ModelPart> getParts() {
        return ImmutableList.of(body, legs);
    }

    @Override
    public void setAngles(CrowEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        BlockPos pos = entity.getBlockPos();
        this.head.pitch = (CrowEntity.isAvailableCrop(entity.world, pos) || CrowEntity.isAvailableCrop(entity.world, pos.up())) ? 90.0F : headPitch * 0.017453292F;
        this.head.yaw = headYaw * 0.017453292F;

        this.rightWing.roll = entity.isOnGround() ? 0.0F : MathHelper.sin(limbAngle * 1.5F) - 45;
        this.leftWing.roll = -this.rightWing.roll;
    }
}
