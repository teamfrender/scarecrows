package com.frenderman.scarecrows.entity.scarecrow;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.util.math.MathHelper;

import java.util.Random;

@Environment(EnvType.CLIENT)
public class ScarecrowStuckArrowsFeatureRenderer extends FeatureRenderer<ScarecrowEntity, ScarecrowEntityModel> {
    private final EntityRenderDispatcher dispatcher;
    private ArrowEntity arrow;

    public ScarecrowStuckArrowsFeatureRenderer(LivingEntityRenderer<ScarecrowEntity, ScarecrowEntityModel> renderer) {
        super(renderer);
        this.dispatcher = renderer.getRenderManager();
    }

    @Override
    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, ScarecrowEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        int m = entity.getStuckArrowCount();
        Random random = new Random(entity.getEntityId());
        if (m > 0) {
            for (int n = 0; n < m; ++n) {
                matrixStack.push();
                ModelPart modelPart = this.getContextModel().getRandomPart(random);
                ModelPart.Cuboid cuboid = modelPart.getRandomCuboid(random);
                modelPart.rotate(matrixStack);
                float randX = random.nextFloat();
                float randY = random.nextFloat();
                float randZ = random.nextFloat();
                float x = MathHelper.lerp(randX, cuboid.minX, cuboid.maxX) / 16.0F;
                float y = MathHelper.lerp(randY, cuboid.minY, cuboid.maxY) / 16.0F;
                float z = MathHelper.lerp(randZ, cuboid.minZ, cuboid.maxZ) / 16.0F;
                matrixStack.translate(x, y, z);
                randX = -1.0F * (randX * 2.0F - 1.0F);
                randY = -1.0F * (randY * 2.0F - 1.0F);
                randZ = -1.0F * (randZ * 2.0F - 1.0F);
                this.renderObject(matrixStack, vertexConsumerProvider, light, entity, randX, randY, randZ, tickDelta);
                matrixStack.pop();
            }
        }
    }

    protected void renderObject(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, ScarecrowEntity entity, float directionX, float directionY, float directionZ, float tickDelta) {
        float f = MathHelper.sqrt(directionX * directionX + directionZ * directionZ);
        this.arrow = new ArrowEntity(entity.world, entity.getX(), entity.getY(), entity.getZ());
        this.arrow.yaw = (float)(Math.atan2(directionX, directionZ) * 57.2957763671875D);
        this.arrow.pitch = (float)(Math.atan2(directionY, f) * 57.2957763671875D);
        this.arrow.prevYaw = this.arrow.yaw;
        this.arrow.prevPitch = this.arrow.pitch;
        this.dispatcher.render(this.arrow, 0.0D, 0.0D, 0.0D, 0.0F, tickDelta, matrices, vertexConsumers, light);
    }
}
