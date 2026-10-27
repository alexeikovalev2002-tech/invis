package com.example.espmod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;

public class InvisESPClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // В 1.21.11 подписываемся на END_MAIN, чтобы отрисовать поверх мира
        LevelRenderEvents.END_MAIN.register(InvisESPRenderer::render);
    }
}
