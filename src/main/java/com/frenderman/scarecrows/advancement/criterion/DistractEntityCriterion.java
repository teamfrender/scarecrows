package com.frenderman.scarecrows.advancement.criterion;

import com.frenderman.scarecrows.Scarecrows;
import com.google.gson.JsonObject;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.EntityPredicate.Extended;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class DistractEntityCriterion extends AbstractCriterion<DistractEntityCriterion.Conditions> {
    public DistractEntityCriterion() {}

    public Identifier getId() {
        return new Identifier(Scarecrows.MOD_ID, "distract_entity");
    }

    public DistractEntityCriterion.Conditions conditionsFromJson(JsonObject jsonObject, Extended extended, AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer) {
        return new DistractEntityCriterion.Conditions(this.getId(), Extended.getInJson(jsonObject, "player", advancementEntityPredicateDeserializer));
    }

    public void trigger(ServerPlayerEntity player) {
        this.test(player, (conditions) -> true);
    }

    public static class Conditions extends AbstractCriterionConditions {
        public Conditions(Identifier id, Extended playerPredicate) {
            super(id, playerPredicate);
        }
    }
}
