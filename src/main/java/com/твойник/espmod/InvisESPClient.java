package com.example.espmod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;

public class InvisESPClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Подписываемся на событие после отрисовки сущностей
        WorldRenderEvents.AFTER_ENTITIES.register(InvisESPRenderer::render);
    }
}
