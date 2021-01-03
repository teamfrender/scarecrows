package com.frenderman.scarecrows;

import static com.frenderman.scarecrows.Scarecrows.log;

import com.frenderman.scarecrows.client.config.SCConfigManager;
import com.frenderman.scarecrows.entity.scarecrow.ScarecrowEntityRenderer;
import com.frenderman.scarecrows.init.SCEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;

public class ScarecrowsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        log("Initializing client");

        EntityRendererRegistry.INSTANCE.register(SCEntities.SCARECROW, ((entityRenderDispatcher, context) -> new ScarecrowEntityRenderer(entityRenderDispatcher)));
        SCConfigManager.load();

        log("Initialized client");
    }
}
