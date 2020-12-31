package com.frenderman.scarecrows.client.config;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class SCConfig {
    public static MiscGroup MISC = new MiscGroup();
    public static class MiscGroup {
        /**
         * Enables/disables the sad skin when a Scarecrow is in danger.
         */
        public Option sadcrow = new Option("sadcrow", false);
    }

    /**
     * A configuration option.
     */
    public static class Option {
        private final String id;
        public Object value;

        /**
         * Instantiates a new boolean configuration option.
         *
         * @param id The option's identifier.
         * @param bool The option's default value.
         */
        private Option(String id, boolean bool) {
            this.id = id;
            this.value = bool;
        }

        public Boolean getBoolean() {
            if (value instanceof Boolean) return (Boolean)value;
                else throw new RuntimeException();
        }

        public String getId() {
            return id;
        }
    }
}
