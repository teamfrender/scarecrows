package com.frenderman.scarecrows.entity.scarecrow;

import com.frenderman.scarecrows.init.SCEntities;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class ScarecrowEntityRenderer extends LivingEntityRenderer<ScarecrowEntity, ScarecrowEntityModel> {
    public ScarecrowEntityRenderer(EntityRenderDispatcher entityRenderDispatcher) {
        super(entityRenderDispatcher, new ScarecrowEntityModel(), 0.0F);
        this.addFeature(new ScarecrowStuckArrowsFeatureRenderer(this));
    }

    @Override
    protected void setupTransforms(ScarecrowEntity entity, MatrixStack matrixStack, float f, float g, float h) {
        matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(180.0F - g));
        float i = (float)(entity.world.getTime() - entity.lastHitTime) + h;
        if (i < 5.0F) {
            matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(MathHelper.sin(i / 1.5F * 3.1415927F) * 3.0F));
        }
    }

    @Override
    protected boolean hasLabel(ScarecrowEntity entity) {
        double d = this.dispatcher.getSquaredDistanceToCamera(entity);
        float f = entity.isInSneakingPose() ? 32.0F : 64.0F;
        return !(d >= (double) (f * f)) && entity.isCustomNameVisible();
    }

    @Override
    protected RenderLayer getRenderLayer(ScarecrowEntity entity, boolean bl, boolean bl2, boolean bl3) {
        if (!entity.isMarker()) {
            return super.getRenderLayer(entity, bl, bl2, bl3);
        } else {
            Identifier identifier = this.getTexture(entity);
            if (bl2) {
                return RenderLayer.getEntityTranslucent(identifier, false);
            } else {
                return bl ? RenderLayer.getEntityCutoutNoCull(identifier, false) : null;
            }
        }
    }

    @Override
    public Identifier getTexture(ScarecrowEntity entity) {
        String modifier = "";
        if (!((entity.world.getTime() - entity.lastHitTime) > 20) || !entity.isOnGround() || entity.isOnFire() || entity.isSubmergedInWater()) {
            modifier = "sad";
        }

        return SCEntities.texture(ScarecrowEntity.id + "/" + ScarecrowEntity.id + (modifier.equals("") ? "" : "_" + modifier));
    }
}
