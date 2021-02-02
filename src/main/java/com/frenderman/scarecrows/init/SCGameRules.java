package com.frenderman.scarecrows.init;

import com.frenderman.scarecrows.Scarecrows;
import net.fabricmc.fabric.api.gamerule.v1.CustomGameRuleCategory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.GameRules;

public class SCGameRules {
    public static CustomGameRuleCategory CATEGORY = new CustomGameRuleCategory(new Identifier(Scarecrows.MOD_ID, "category"), new TranslatableText("gamerule." + Scarecrows.MOD_ID + ".category").formatted(Formatting.BOLD).formatted(Formatting.YELLOW));

    public static GameRules.Key<GameRules.BooleanRule> DO_CROW_SPAWNS = register("doCrowSpawns", GameRuleFactory.createBooleanRule(true));

    public SCGameRules() {}

    private static <T extends GameRules.Rule<T>> GameRules.Key<T> register(String id, GameRules.Type<T> type) {
        return GameRuleRegistry.register(Scarecrows.MOD_ID + "." + id, CATEGORY, type);
    }
}
