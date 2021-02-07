package com.frenderman.scarecrows.common.core.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class ScarecrowsConfig {

    public static final Common COMMON;
    public static final ForgeConfigSpec COMMON_SPEC;

    static {
        Pair<Common, ForgeConfigSpec> commonPair = new ForgeConfigSpec.Builder().configure(Common::new);
        COMMON = commonPair.getLeft();
        COMMON_SPEC = commonPair.getRight();
    }

    public static class Common {

        private final ForgeConfigSpec.BooleanValue sadcrow;
        private final ForgeConfigSpec.BooleanValue doCrowSpawning;

        public Common(ForgeConfigSpec.Builder configBuilder) {
            configBuilder.push("general");

            configBuilder.comment("Enables/disables the sad skin when a Scarecrow is in danger.");
            this.sadcrow = configBuilder.define("sadcrow", false);

            configBuilder.comment("Enables/disables crows spawning in the world");
            this.doCrowSpawning = configBuilder.define("doCrowSpawning", true);

            configBuilder.pop();
        }

        public boolean sadcrowEnabled() {
            return this.sadcrow.get();
        }

        public boolean crowSpawningEnabled() {
            return this.doCrowSpawning.get();
        }
    }
}
