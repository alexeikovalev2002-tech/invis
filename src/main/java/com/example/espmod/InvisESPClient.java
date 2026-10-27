package com.example.espmod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;

public class InvisESPClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Подписываемся на актуальное событие рендера уровня
        LevelRenderEvents.AFTER_ENTITIES.register(InvisESPRenderer::render);
    }
}
