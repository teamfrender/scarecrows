package com.frenderman.scarecrows.common.register;

import com.frenderman.scarecrows.common.core.Scarecrows;
import net.minecraft.world.GameRules;

public class SCGameRules {

    public static GameRules.RuleKey<GameRules.BooleanValue> DO_CROWS_SPAWN;

    public static void register() {
        DO_CROWS_SPAWN = registerGameRule("doCrowsSpawn", GameRules.Category.MOBS, GameRules.BooleanValue.create(true));
    }

    private static <T extends GameRules.RuleValue<T>> GameRules.RuleKey<T> registerGameRule(String ruleName, GameRules.Category category, GameRules.RuleType<T> ruleType) {
        return GameRules.register(Scarecrows.MOD_ID + ":" + ruleName, category, ruleType);
    }
}
