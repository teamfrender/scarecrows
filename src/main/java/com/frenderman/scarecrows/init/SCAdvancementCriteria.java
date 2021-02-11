package com.frenderman.scarecrows.init;

import com.frenderman.scarecrows.advancement.criterion.DistractEntityCriterion;
import net.fabricmc.fabric.api.object.builder.v1.advancement.CriterionRegistry;
import net.minecraft.advancement.criterion.Criterion;

public class SCAdvancementCriteria {
    public static final DistractEntityCriterion DISTRACT_ENTITY = register(new DistractEntityCriterion());

    public SCAdvancementCriteria() {}

    private static <T extends Criterion<?>> T register(T criterion) {
        return CriterionRegistry.register(criterion);
    }
}
