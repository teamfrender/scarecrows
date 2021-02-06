package com.frenderman.scarecrows.client.renderer;

import com.frenderman.scarecrows.client.renderer.model.ScarecrowEntityModel;
import com.frenderman.scarecrows.common.entity.ScarecrowEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.util.ResourceLocation;

public class ScarecrowEntityRenderer<T extends ScarecrowEntity> extends LivingRenderer<T, ScarecrowEntityModel<T>> {

    public ScarecrowEntityRenderer(EntityRendererManager rendererManager) {
        super(rendererManager, new ScarecrowEntityModel<>(), 0.0F);
    }

    @Override
    public ResourceLocation getEntityTexture(T entity) {
        return new ResourceLocation("");
    }
}
