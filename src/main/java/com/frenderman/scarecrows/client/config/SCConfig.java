package com.frenderman.scarecrows.client.config;

import me.andante.chord.client.config.Option;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class SCConfig {
    public static MiscGroup MISC = new MiscGroup();
    public static class MiscGroup {
        /**
         * Enables/disables the sad skin when a Scarecrow is in danger.
         */
        public Option<Boolean> sadcrow = new Option<>("sadcrow", false);
    }
}
