package com.frenderman.scarecrows.client.renderer.entity.scarecrow;

import com.frenderman.scarecrows.common.entity.ScarecrowEntity;
import com.frenderman.scarecrows.common.register.SCEntities;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;

public class ScarecrowEntityRenderer<T extends ScarecrowEntity> extends LivingRenderer<T, ScarecrowEntityModel<T>> {

    public ScarecrowEntityRenderer(EntityRendererManager rendererManager) {
        super(rendererManager, new ScarecrowEntityModel<>(), 0.0F);
        this.addLayer(new ScarecrowEntityStuckArrowsRenderLayer<>(this));
    }

    @Override
    protected void applyRotations(ScarecrowEntity entity, MatrixStack matrixStack, float animationProgress, float bodyYaw, float tickDelta) {
        matrixStack.rotate(Vector3f.YP.rotationDegrees(180.0F - bodyYaw));
        float i = (float)(entity.world.getGameTime() - entity.lastHitTime) + tickDelta;
        if (i < 5.0F) {
            matrixStack.rotate(Vector3f.YP.rotationDegrees(MathHelper.sin(i / 5F * 3.1415927F) * 3.0F));
        }
    }

    @Override
    protected boolean canRenderName(T entity) {
        double d = this.renderManager.squareDistanceTo(entity);
        float f = entity.isSneaking() ? 32.0F : 64.0F;
        return !(d >= (double) (f * f)) && entity.isCustomNameVisible();
    }

    @Override
    protected RenderType func_230496_a_(T entity, boolean showBody, boolean translucent, boolean showOutline) {
        if (!entity.isMarker()) {
            return super.func_230496_a_(entity, showBody, translucent, showOutline);
        } else {
            ResourceLocation location = this.getEntityTexture(entity);
            if (translucent) {
                return RenderType.getEntityTranslucent(location, false);
            } else {
                return showBody ? RenderType.getEntityCutoutNoCull(location, false) : null;
            }
        }
    }

    @Override
    public ResourceLocation getEntityTexture(T entity) {
        return SCEntities.texture(getBasePath());
    }

    public static ResourceLocation getTexture(String extension) {
        return SCEntities.texture(getBasePath() + "_" + extension);
    }
    private static String getBasePath() {
        return "scarecrow/scarecrow";
    }
}
