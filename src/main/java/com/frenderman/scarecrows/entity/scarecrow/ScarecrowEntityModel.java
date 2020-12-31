package com.frenderman.scarecrows.entity.scarecrow;

import com.google.common.collect.ImmutableList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.CompositeEntityModel;

import java.util.Random;

@Environment(EnvType.CLIENT)
public class ScarecrowEntityModel extends CompositeEntityModel<ScarecrowEntity> {
    private final ModelPart post;
    private final ModelPart scarecrow;
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
        post.setTextureOffset(56, 22).addCuboid(-1.0F, -27.0F, -1.0F, 2.0F, 10.0F, 2.0F, 0.0F, false);
        post.setTextureOffset(40, 0).addCuboid(-1.0F, -17.0F, -1.0F, 2.0F, 17.0F, 2.0F, 0.0F, false);

        scarecrow = new ModelPart(this);
        scarecrow.setPivot(0.0F, 24.0F, 0.0F);
        scarecrow.setTextureOffset(0, 23).addCuboid(-4.0F, -26.0F, -2.5F, 8.0F, 12.0F, 5.0F, 0.0F, false);

        legs = new ModelPart(this);
        legs.setPivot(0.0F, -22.0F, 0.5F);
        scarecrow.addChild(legs);


        leftLeg = new ModelPart(this);
        leftLeg.setPivot(0.0F, 1.0F, 0.0F);
        legs.addChild(leftLeg);
        setRotationAngle(leftLeg, -0.3491F, 0.0F, 0.6981F);
        leftLeg.setTextureOffset(48, 0).addCuboid(0.3567F, 3.2464F, -0.7411F, 4.0F, 13.0F, 4.0F, 0.0F, true);
        leftLeg.setTextureOffset(0, 48).addCuboid(0.8567F, 4.2464F, -0.2411F, 3.0F, 13.0F, 3.0F, 0.0F, true);

        rightLeg = new ModelPart(this);
        rightLeg.setPivot(0.0F, 0.7F, 0.0F);
        legs.addChild(rightLeg);
        setRotationAngle(rightLeg, -0.3491F, 0.0F, -0.6981F);
        rightLeg.setTextureOffset(48, 0).addCuboid(-4.3567F, 4.2464F, -0.7411F, 4.0F, 13.0F, 4.0F, 0.0F, false);
        rightLeg.setTextureOffset(0, 48).addCuboid(-3.8567F, 4.2464F, -0.2411F, 3.0F, 13.0F, 3.0F, 0.0F, false);

        arms = new ModelPart(this);
        arms.setPivot(0.0F, 0.0F, 0.0F);
        scarecrow.addChild(arms);


        leftArm = new ModelPart(this);
        leftArm.setPivot(-5.0F, -31.0F, 0.0F);
        arms.addChild(leftArm);
        setRotationAngle(leftArm, 0.0F, 0.0436F, -0.3491F);
        leftArm.setTextureOffset(34, 40).addCuboid(-11.0502F, 5.1382F, -2.0895F, 11.0F, 4.0F, 4.0F, 0.0F, false);
        leftArm.setTextureOffset(36, 34).addCuboid(-11.0502F, 5.6382F, -1.5895F, 11.0F, 3.0F, 3.0F, 0.0F, false);

        rightArm = new ModelPart(this);
        rightArm.setPivot(4.0F, -30.0F, -0.5F);
        arms.addChild(rightArm);
        setRotationAngle(rightArm, 0.0F, 0.0F, 0.3054F);
        rightArm.setTextureOffset(34, 40).addCuboid(0.8042F, 4.2223F, -1.5F, 11.0F, 4.0F, 4.0F, 0.0F, true);
        rightArm.setTextureOffset(36, 34).addCuboid(0.8042F, 4.7223F, -1.0F, 11.0F, 3.0F, 3.0F, 0.0F, true);

        head = new ModelPart(this);
        head.setPivot(0.0F, -33.0F, 0.0F);
        scarecrow.addChild(head);
        setRotationAngle(head, 0.2618F, -0.0436F, -0.2618F);
        head.setTextureOffset(0, 0).addCuboid(-6.5514F, -6.3844F, -6.4346F, 10.0F, 13.0F, 10.0F, 0.0F, false);

        hat = new ModelPart(this);
        hat.setPivot(0.0F, 33.0F, 0.0F);
        head.addChild(hat);
        hat.setTextureOffset(0, 48).addCuboid(-9.5514F, -35.3844F, -9.4346F, 16.0F, 0.0F, 16.0F, 0.0F, false);
    }

    @Override
    public Iterable<ModelPart> getParts() {
        return ImmutableList.of(post, scarecrow);
    }

    public ModelPart getRandomPart(Random random) {
        ModelPart[] availableParts = new ModelPart[]{ post, scarecrow };
        return availableParts[random.nextInt(availableParts.length)];
    }

    @Override
    public void setAngles(ScarecrowEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.leftArm.visible = !entity.shouldHideArms();
        this.rightArm.visible = !entity.shouldHideArms();
        this.post.visible = !entity.shouldHidePost();
    }

    public void setRotationAngle(ModelPart bone, float x, float y, float z) {
        bone.pitch = x;
        bone.yaw = y;
        bone.roll = z;
    }
}
