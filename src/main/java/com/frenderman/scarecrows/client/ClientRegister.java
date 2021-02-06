package com.frenderman.scarecrows.client;

import com.frenderman.scarecrows.client.renderer.ScarecrowEntityRenderer;
import com.frenderman.scarecrows.common.core.Scarecrows;
import com.frenderman.scarecrows.common.register.SCEntities;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Scarecrows.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientRegister {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        registerEntityRenderers();
    }

    private static void registerEntityRenderers() {
        RenderingRegistry.registerEntityRenderingHandler(SCEntities.SCARECROW.get(), ScarecrowEntityRenderer::new);
    }
}
