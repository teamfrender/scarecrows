package com.frenderman.scarecrows.entity.scarecrow;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.CompositeEntityModel;

import java.util.Random;

public class ScarecrowEntityModel extends CompositeEntityModel<ScarecrowEntity> {
    private final ModelPart post;
    private final ModelPart body;
    private final ModelPart legs;
    private final ModelPart leftLeg;
    private final ModelPart rightLeg;
    private final ModelPart arms;
    private final ModelPart leftArm;
    private final ModelPart rightArm;
    private final ModelPart head;
    private final ModelPart hat;

    public ScarecrowEntityModel() {
        textureWidth = 64;
        textureHeight = 64;

        post = new ModelPart(this);
        post.setPivot(0.0F, 24.0F, 0.0F);
        post.setTextureOffset(40, 0).addCuboid(-1.0F, -17.0F, -1.0F, 2.0F, 17.0F, 2.0F, 0.0F, false);
        post.setTextureOffset(56, 22).addCuboid(-1.0F, -27.0F, -1.0F, 2.0F, 10.0F, 2.0F, 0.0F, false);

        body = new ModelPart(this);
        body.setPivot(0.0F, 3.0F, 0.0F);

        legs = new ModelPart(this);
        legs.setPivot(0.0F, 6.0F, 0.5F);
        body.addChild(legs);

        leftLeg = new ModelPart(this);
        leftLeg.setPivot(2.0F, -0.3F, -1.0F);
        legs.addChild(leftLeg);
        setRotationAngle(leftLeg, -0.3491F, 0.0F, -0.6981F);
        leftLeg.setTextureOffset(0, 48).addCuboid(-1.5321F, -1.6227F, -1.3131F, 3.0F, 13.0F, 3.0F, 0.0F, false);
        leftLeg.setTextureOffset(48, 0).addCuboid(-2.0321F, -1.6227F, -1.8131F, 4.0F, 13.0F, 4.0F, 0.0F, false);

        rightLeg = new ModelPart(this);
        rightLeg.setPivot(-2.0F, 0.0F, 0.0F);
        legs.addChild(rightLeg);
        setRotationAngle(rightLeg, -0.3491F, 0.0F, 0.6981F);
        rightLeg.setTextureOffset(0, 48).addCuboid(-1.4679F, -1.2807F, -2.2528F, 3.0F, 13.0F, 3.0F, 0.0F, true);
        rightLeg.setTextureOffset(48, 0).addCuboid(-1.9679F, -2.2807F, -2.7528F, 4.0F, 13.0F, 4.0F, 0.0F, true);

        arms = new ModelPart(this);
        arms.setPivot(0.0F, -4.0F, 0.0F);
        body.addChild(arms);

        leftArm = new ModelPart(this);
        leftArm.setPivot(4.0F, 2.0F, -0.5F);
        arms.addChild(leftArm);
        setRotationAngle(leftArm, 0.0F, 0.0F, 0.3054F);
        leftArm.setTextureOffset(36, 34).addCuboid(-1.3007F, -1.9537F, -1.0F, 11.0F, 3.0F, 3.0F, 0.0F, true);

        rightArm = new ModelPart(this);
        rightArm.setPivot(-4.0F, 2.0F, 0.0F);
        arms.addChild(rightArm);
        setRotationAngle(rightArm, 0.0F, 0.0436F, -0.3491F);
        rightArm.setTextureOffset(36, 34).addCuboid(-9.2554F, -2.2214F, -1.5112F, 11.0F, 3.0F, 3.0F, 0.0F, false);

        head = new ModelPart(this);
        head.setPivot(0.0F, -5.0F, 0.0F);
        body.addChild(head);
        setRotationAngle(head, 0.2618F, -0.0436F, -0.2618F);

        hat = new ModelPart(this);
        hat.setPivot(1.81F, 26.4485F, 1.6737F);
        head.addChild(hat);
        hat.setTextureOffset(0, 48).addCuboid(-9.5514F, -35.3844F, -9.4346F, 16.0F, 0.0F, 16.0F, 0.0F, false);
    }

	@Override
	public Iterable<ModelPart> getParts() {
		return ImmutableList.of(post, body);
	}

	public ModelPart getRandomPart(Random random) {
        ModelPart[] availableParts = new ModelPart[]{ post, hat };
        return availableParts[random.nextInt(availableParts.length)];
    }

	@Override
    public void setAngles(ScarecrowEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.arms.visible = !entity.shouldHideArms();
        this.post.visible = !entity.shouldHidePost();
    }

    public void setRotationAngle(ModelPart bone, float x, float y, float z) {
        bone.pitch = x;
        bone.yaw = y;
        bone.roll = z;
    }
}
