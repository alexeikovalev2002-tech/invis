package com.твойник.espmod;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

public class InvisESPRenderer {
    private static final double RANGE = 100.0D; // Радиус действия
    private static final float[] WHITE = {1.0f, 1.0f, 1.0f, 1.0f};

    public static void render(WorldRenderContext context) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.world == null) return;

        MatrixStack matrices = context.matrixStack();
        Vec3d cameraPos = context.camera().getPos();
        VertexConsumerProvider consumers = context.consumers();

        // Проходим по всем игрокам в мире
        for (PlayerEntity target : client.world.getPlayers()) {
            // Пропускаем себя и игроков вне радиуса
            if (target == client.player || target.distanceTo(client.player) > RANGE) continue;
            
            // Нас интересуют только невидимые игроки
            if (!target.isInvisible()) continue;

            // 1. Отрисовка белого хитбокса
            Box box = target.getBoundingBox().offset(-cameraPos.x, -cameraPos.y, -cameraPos.z);
            WorldRenderer.drawBox(matrices, consumers.getBuffer(RenderLayer.getLines()), box, 
                                  WHITE[0], WHITE[1], WHITE[2], WHITE[3]);
            matrices.pop();

            // 2. Отрисовка Ника и ХП
            String name = target.getName().getString();
            float health = target.getHealth();
            String text = String.format("%s [%.1f❤]", name, health);
            
            // Позиционируем текст над головой
            matrices.push();
            matrices.translate(target.getX() - cameraPos.x, target.getY() + target.getHeight() + 0.5 - cameraPos.y, target.getZ() - cameraPos.z);
            matrices.multiply(context.camera().getRotation());
            matrices.scale(-0.025f, -0.025f, 0.025f); // Масштаб текста

            Matrix4f matrix = matrices.peek().getPositionMatrix();
            client.textRenderer.draw(
                text,
                0, 0, // Координаты (центрируем вручную при необходимости)
                0xFFFFFFFF, // Белый цвет
                false, // Без тени
                matrix,
                consumers,
                TextRenderer.TextLayerType.SEE_THROUGH, // Видно сквозь стены
                0,
                15728880 // Свет
            );
            matrices.pop();
        }
    }
}
