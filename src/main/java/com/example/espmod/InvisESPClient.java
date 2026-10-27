package com.example.espmod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;

public class InvisESPClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // В 1.21.11 события рендера переехали в пакет level
        LevelRenderEvents.AFTER_ENTITIES.register(InvisESPRenderer::render);
    }
}
