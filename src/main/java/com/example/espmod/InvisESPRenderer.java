package com.example.espmod;

import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexRendering;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

public class InvisESPRenderer {
    private static final double RANGE = 100.0D;

    public static void render(LevelRenderContext context) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.world == null) return;

        MatrixStack matrices = context.poseStack();
        Vec3d cameraPos = context.camera().getPos();
        VertexConsumerProvider consumers = context.consumers();
        
        if (matrices == null || consumers == null) return;

        for (PlayerEntity target : client.world.getPlayers()) {
            if (target == client.player || target.distanceTo(client.player) > RANGE) continue;
            if (!target.isInvisible()) continue;

            // 1. Белый хитбокс (Yarn: VertexRendering.drawBox и RenderLayer.getLines)
            Box box = target.getBoundingBox().offset(-cameraPos.x, -cameraPos.y, -cameraPos.z);
            VertexConsumer lines = consumers.getBuffer(RenderLayer.getLines());
            VertexRendering.drawBox(matrices, lines, box, 1.0f, 1.0f, 1.0f, 1.0f);

            // 2. Ник и ХП над головой
            matrices.push();
            matrices.translate(
                target.getX() - cameraPos.x, 
                target.getY() + target.getHeight() + 0.5 - cameraPos.y, 
                target.getZ() - cameraPos.z
            );
            matrices.multiply(context.camera().getRotation());
            matrices.scale(-0.025f, -0.025f, 0.025f);

            String text = String.format("%s [%.1f❤]", target.getName().getString(), target.getHealth());
            Matrix4f matrix = matrices.peek().getPositionMatrix();
            
            client.textRenderer.draw(
                Text.literal(text),
                0, 0,
                0xFFFFFFFF,
                false,
                matrix,
                consumers,
                TextRenderer.TextLayerType.SEE_THROUGH,
                0,
                15728880
            );
            matrices.pop();
        }
    }
}
