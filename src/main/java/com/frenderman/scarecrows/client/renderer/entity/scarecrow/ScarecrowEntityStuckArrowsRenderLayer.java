package com.frenderman.scarecrows.client.renderer.entity.scarecrow;

import com.frenderman.scarecrows.common.entity.ScarecrowEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.model.pipeline.VertexBufferConsumer;

import java.util.Random;

public class ScarecrowEntityStuckArrowsRenderLayer<T extends ScarecrowEntity> extends LayerRenderer<T, ScarecrowEntityModel<T>> {

    private final EntityRendererManager dispatcher;
    private ArrowEntity arrow;

    public ScarecrowEntityStuckArrowsRenderLayer(LivingRenderer<T, ScarecrowEntityModel<T>> renderer) {
        super(renderer);
        this.dispatcher = renderer.getRenderManager();
    }

    @Override
    public void render(MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        int m = entity.getArrowCountInEntity();
        Random random = new Random(entity.getEntityId());
        if (m > 0) {
            for (int n = 0; n < m; ++n) {
                matrixStack.push();
                ModelRenderer modelPart = this.getEntityModel().getRandomPart(random);
                ModelRenderer.ModelBox cuboid = modelPart.getRandomCube(random);
                modelPart.translateRotate(matrixStack);
                float randX = random.nextFloat();
                float randY = random.nextFloat();
                float randZ = random.nextFloat();
                float x = MathHelper.lerp(randX, cuboid.posX1, cuboid.posX2) / 16.0F;
                float y = MathHelper.lerp(randY, cuboid.posY1, cuboid.posY2) / 16.0F;
                float z = MathHelper.lerp(randZ, cuboid.posZ1, cuboid.posZ2) / 16.0F;
                matrixStack.translate(x, y, z);
                randX = -1.0F * (randX * 2.0F - 1.0F);
                randY = -1.0F * (randY * 2.0F - 1.0F);
                randZ = -1.0F * (randZ * 2.0F - 1.0F);
                this.renderObject(matrixStack, renderTypeBuffer, light, entity, randX, randY, randZ, tickDelta);
                matrixStack.pop();
            }
        }
    }

    protected void renderObject(MatrixStack matrices, IRenderTypeBuffer vertexConsumers, int light, ScarecrowEntity entity, float directionX, float directionY, float directionZ, float tickDelta) {
        float f = MathHelper.sqrt(directionX * directionX + directionZ * directionZ);
        this.arrow = new ArrowEntity(entity.world, entity.getPosX(), entity.getPosY(), entity.getPosZ());
        this.arrow.rotationYaw = (float)(Math.atan2(directionX, directionZ) * 57.2957763671875D);
        this.arrow.rotationPitch = (float)(Math.atan2(directionY, f) * 57.2957763671875D);
        this.arrow.prevRotationYaw = this.arrow.rotationYaw;
        this.arrow.prevRotationPitch = this.arrow.rotationPitch;
        this.dispatcher.renderEntityStatic(this.arrow, 0.0D, 0.0D, 0.0D, 0.0F, tickDelta, matrices, vertexConsumers, light);
    }
}
