package com.example.espmod;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.VertexRendering;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

public class InvisESPRenderer {
    private static final double RANGE = 100.0D;

    public static void render(LevelRenderContext context) {
        Minecraft client = Minecraft.getInstance();
        if (client.player == null || client.level == null) return;

        PoseStack poseStack = context.poseStack();
        Vec3 cameraPos = context.camera().getPosition();
        MultiBufferSource consumers = context.bufferSource();
        if (poseStack == null || consumers == null) return;

        for (Player target : client.level.players()) {
            if (target == client.player || target.distanceTo(client.player) > RANGE) continue;
            if (!target.isInvisible()) continue;

            // 1. Отрисовка белого хитбокса вокруг невидимого игрока
            AABB box = target.getBoundingBox().move(-cameraPos.x, -cameraPos.y, -cameraPos.z);
            VertexRendering.drawBox(
                poseStack, 
                consumers.getBuffer(RenderTypes.lines()), 
                box, 
                1.0f, 1.0f, 1.0f, 1.0f
            );

            // 2. Отрисовка Ника и ХП над головой
            poseStack.pushPose();
            poseStack.translate(
                target.getX() - cameraPos.x, 
                target.getY() + target.getBbHeight() + 0.5 - cameraPos.y, 
                target.getZ() - cameraPos.z
            );
            poseStack.mulPose(context.camera().rotation());
            poseStack.scale(-0.025f, -0.025f, 0.025f);

            String text = String.format("%s [%.1f❤]", target.getName().getString(), target.getHealth());
            Matrix4f matrix = poseStack.last().pose();
            
            client.font.drawInBatch(
                Component.literal(text),
                0, 0,
                0xFFFFFFFF,
                false,
                matrix,
                consumers,
                Font.DisplayMode.SEE_THROUGH,
                0,
                15728880
            );
            poseStack.popPose();
        }
    }
}
