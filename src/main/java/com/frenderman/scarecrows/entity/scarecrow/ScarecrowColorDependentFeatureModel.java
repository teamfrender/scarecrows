package com.frenderman.scarecrows.entity.scarecrow;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.CompositeEntityModel;

public class ScarecrowColorDependentFeatureModel extends CompositeEntityModel<ScarecrowEntity> {
    private final ModelPart body;
    private final ModelPart arms;
    private final ModelPart leftArm;
    private final ModelPart rightArm;
    private final ModelPart head;

	public ScarecrowColorDependentFeatureModel() {
        textureWidth = 64;
        textureHeight = 64;

        body = new ModelPart(this);
        body.setPivot(0.0F, 3.0F, 0.0F);
        body.setTextureOffset(0, 23).addCuboid(-4.0F, -5.0F, -2.5F, 8.0F, 12.0F, 5.0F, 0.0F, false);

        arms = new ModelPart(this);
        arms.setPivot(0.0F, -4.0F, 0.0F);
        body.addChild(arms);

        leftArm = new ModelPart(this);
        leftArm.setPivot(4.0F, 2.0F, -0.5F);
        arms.addChild(leftArm);
        setRotationAngle(leftArm, 0.0F, 0.0F, 0.3054F);
        leftArm.setTextureOffset(36, 34).addCuboid(-1.3007F, -1.9537F, -1.0F, 11.0F, 3.0F, 3.0F, 0.0F, true);
        leftArm.setTextureOffset(34, 40).addCuboid(-1.3007F, -2.4537F, -1.5F, 11.0F, 4.0F, 4.0F, 0.0F, true);

        rightArm = new ModelPart(this);
        rightArm.setPivot(-4.0F, 2.0F, 0.0F);
        arms.addChild(rightArm);
        setRotationAngle(rightArm, 0.0F, 0.0436F, -0.3491F);
        rightArm.setTextureOffset(36, 34).addCuboid(-9.2554F, -2.2214F, -1.5112F, 11.0F, 3.0F, 3.0F, 0.0F, false);
        rightArm.setTextureOffset(34, 40).addCuboid(-9.2554F, -2.7214F, -2.0112F, 11.0F, 4.0F, 4.0F, 0.0F, false);

        head = new ModelPart(this);
        head.setPivot(0.0F, -5.0F, 0.0F);
        body.addChild(head);
        setRotationAngle(head, 0.2618F, -0.0436F, -0.2618F);
        head.setTextureOffset(0, 0).addCuboid(-4.7414F, -12.9359F, -4.7609F, 10.0F, 13.0F, 10.0F, 0.0F, false);
	}

	@Override
	public Iterable<ModelPart> getParts() {
		return ImmutableList.of(body);
	}

	@Override
	public void setAngles(ScarecrowEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.arms.visible = !entity.shouldHideArms();
    }

	public void setRotationAngle(ModelPart bone, float x, float y, float z) {
        bone.pitch = x;
        bone.yaw = y;
        bone.roll = z;
	}
}
