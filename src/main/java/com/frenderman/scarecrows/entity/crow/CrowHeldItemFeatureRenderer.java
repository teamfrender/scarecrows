package com.frenderman.scarecrows.entity.crow;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class CrowHeldItemFeatureRenderer extends FeatureRenderer<CrowEntity, CrowEntityModel> {
    public CrowHeldItemFeatureRenderer(FeatureRendererContext<CrowEntity, CrowEntityModel> featureRendererContext) {
        super(featureRendererContext);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CrowEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        matrices.push();

        /*f (entity.isOnGround()) {
            matrices.translate(-0.025D, 1.486D, -0.14D);
            matrices.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(90.0F));
        } else {
            matrices.translate(-0.025D, 1.41D, 0.003D);
        }*/

        float o = MathHelper.abs(entity.pitch) / 60.0F;
        if (entity.pitch < 0.0F) {
            matrices.translate(0.0D, 0.7F - o * 0.05F, -0.03F);
        } else {
            matrices.translate(0.0D, 0.7F + o * 0.8F, -0.5F + o * 0.2F);
        }

        matrices.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(90.0F));
        matrices.translate(0, 0, 0);

        ItemStack itemStack = entity.getEquippedStack(EquipmentSlot.MAINHAND);
        MinecraftClient.getInstance().getHeldItemRenderer().renderItem(entity, itemStack, ModelTransformation.Mode.GROUND, false, matrices, vertexConsumers, light);

        matrices.pop();
    }
}
