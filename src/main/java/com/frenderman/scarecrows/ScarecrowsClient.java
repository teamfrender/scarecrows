package com.frenderman.scarecrows;

import com.frenderman.scarecrows.entity.scarecrow.ScarecrowEntityRenderer;
import com.frenderman.scarecrows.init.SCEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;

public class ScarecrowsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.INSTANCE.register(SCEntities.SCARECROW, ((entityRenderDispatcher, context) -> new ScarecrowEntityRenderer(entityRenderDispatcher)));
    }
}
