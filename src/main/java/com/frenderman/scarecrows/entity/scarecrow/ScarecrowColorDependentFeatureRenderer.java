package com.frenderman.scarecrows.entity.scarecrow;

import com.frenderman.scarecrows.client.config.SCConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class ScarecrowColorDependentFeatureRenderer extends FeatureRenderer<ScarecrowEntity, ScarecrowEntityModel> {
    private final ScarecrowColorDependentFeatureModel model = new ScarecrowColorDependentFeatureModel();

    public ScarecrowColorDependentFeatureRenderer(FeatureRendererContext<ScarecrowEntity, ScarecrowEntityModel> featureRendererContext) {
        super(featureRendererContext);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, ScarecrowEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        Identifier skin = ScarecrowEntityRenderer.getTexture(
            SCConfig.MISC.sadcrow.getBoolean()
                ? "sad"
                : entity.isFrench()
                    ? "f" /* la baguette */ + "re" /* hon hon hon */ + "nch"
                    : entity.getColor().toString()
        );

        render(this.getContextModel(), this.model, skin, matrices, vertexConsumers, light, entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch, tickDelta, 1.0F, 1.0F, 1.0F);
    }
}
