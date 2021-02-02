package com.frenderman.scarecrows.entity.crow;

import com.frenderman.scarecrows.init.SCEntities;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class CrowEntityRenderer extends MobEntityRenderer<CrowEntity, CrowEntityModel> {
    public CrowEntityRenderer(EntityRenderDispatcher entityRenderDispatcher) {
        super(entityRenderDispatcher, new CrowEntityModel(), 0.3F);
        this.addFeature(new CrowHeldItemFeatureRenderer(this));
    }

    @Override
    public float getAnimationProgress(CrowEntity entity, float tickDelta) {
        float sin = MathHelper.lerp(tickDelta, entity.prevFlapProgress, entity.flapProgress);
        float lerp = MathHelper.lerp(tickDelta, entity.prevMaxWingDeviation, entity.maxWingDeviation);
        return (MathHelper.sin(sin) + 1.0F) * lerp;
    }

    @Override
    public Identifier getTexture(CrowEntity entity) {
        return SCEntities.texture(CrowEntity.id + "/" + CrowEntity.id);
    }
}
