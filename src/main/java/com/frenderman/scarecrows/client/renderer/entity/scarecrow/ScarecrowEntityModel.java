package com.frenderman.scarecrows.client.renderer.entity.scarecrow;

import com.frenderman.scarecrows.common.entity.ScarecrowEntity;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;

import java.util.Random;

public class ScarecrowEntityModel<T extends ScarecrowEntity> extends SegmentedModel<T> {

    private final ModelRenderer post;
    private final ModelRenderer body;
    private final ModelRenderer legs;
    private final ModelRenderer leftLeg;
    private final ModelRenderer rightLeg;
    private final ModelRenderer arms;
    private final ModelRenderer leftArm;
    private final ModelRenderer rightArm;
    private final ModelRenderer head;
    private final ModelRenderer hat;

    public ScarecrowEntityModel() {
        textureWidth = 64;
        textureHeight = 64;

        post = new ModelRenderer(this);
        post.setRotationPoint(0.0F, 24.0F, 0.0F);
        post.setTextureOffset(40, 0).addBox(-1.0F, -17.0F, -1.0F, 2.0F, 17.0F, 2.0F, 0.0F, false);
        post.setTextureOffset(56, 22).addBox(-1.0F, -27.0F, -1.0F, 2.0F, 10.0F, 2.0F, 0.0F, false);

        body = new ModelRenderer(this);
        body.setRotationPoint(0.0F, 3.0F, 0.0F);

        legs = new ModelRenderer(this);
        legs.setRotationPoint(0.0F, 6.0F, 0.5F);
        body.addChild(legs);

        leftLeg = new ModelRenderer(this);
        leftLeg.setRotationPoint(2.0F, -0.3F, -1.0F);
        legs.addChild(leftLeg);
        setRotationAngle(leftLeg, -0.3491F, 0.0F, -0.6981F);
        leftLeg.setTextureOffset(0, 48).addBox(-1.5321F, -1.6227F, -1.3131F, 3.0F, 13.0F, 3.0F, 0.0F, false);
        leftLeg.setTextureOffset(48, 0).addBox(-2.0321F, -1.6227F, -1.8131F, 4.0F, 13.0F, 4.0F, 0.0F, false);

        rightLeg = new ModelRenderer(this);
        rightLeg.setRotationPoint(-2.0F, 0.0F, 0.0F);
        legs.addChild(rightLeg);
        setRotationAngle(rightLeg, -0.3491F, 0.0F, 0.6981F);
        rightLeg.setTextureOffset(0, 48).addBox(-1.4679F, -1.2807F, -2.2528F, 3.0F, 13.0F, 3.0F, 0.0F, true);
        rightLeg.setTextureOffset(48, 0).addBox(-1.9679F, -2.2807F, -2.7528F, 4.0F, 13.0F, 4.0F, 0.0F, true);

        arms = new ModelRenderer(this);
        arms.setRotationPoint(0.0F, -4.0F, 0.0F);
        body.addChild(arms);

        leftArm = new ModelRenderer(this);
        leftArm.setRotationPoint(4.0F, 2.0F, -0.5F);
        arms.addChild(leftArm);
        setRotationAngle(leftArm, 0.0F, 0.0F, 0.3054F);
        leftArm.setTextureOffset(36, 34).addBox(-1.3007F, -1.9537F, -1.0F, 11.0F, 3.0F, 3.0F, 0.0F, true);

        rightArm = new ModelRenderer(this);
        rightArm.setRotationPoint(-4.0F, 2.0F, 0.0F);
        arms.addChild(rightArm);
        setRotationAngle(rightArm, 0.0F, 0.0436F, -0.3491F);
        rightArm.setTextureOffset(36, 34).addBox(-9.2554F, -2.2214F, -1.5112F, 11.0F, 3.0F, 3.0F, 0.0F, false);

        head = new ModelRenderer(this);
        head.setRotationPoint(0.0F, -5.0F, 0.0F);
        body.addChild(head);
        setRotationAngle(head, 0.2618F, -0.0436F, -0.2618F);

        hat = new ModelRenderer(this);
        hat.setRotationPoint(1.81F, 26.4485F, 1.6737F);
        head.addChild(hat);
        hat.setTextureOffset(0, 48).addBox(-9.5514F, -35.3844F, -9.4346F, 16.0F, 0.0F, 16.0F, 0.0F, false);
    }

    @Override
    public Iterable<ModelRenderer> getParts() {
        return ImmutableList.of(this.post, this.body);
    }

    public ModelRenderer getRandomPart(Random random) {
        ModelRenderer[] availableParts = new ModelRenderer[]{ post, hat };
        return availableParts[random.nextInt(availableParts.length)];
    }

    @Override
    public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    public void setRotationAngle(ModelRenderer bone, float x, float y, float z) {
        bone.rotateAngleX = x;
        bone.rotateAngleY = y;
        bone.rotateAngleZ = z;
    }
}
