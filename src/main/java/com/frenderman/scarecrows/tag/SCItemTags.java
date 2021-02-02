package com.frenderman.scarecrows.tag;

import com.frenderman.scarecrows.Scarecrows;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

public class SCItemTags {
    /**
     * Items that will not damage a Scarecrow.
     */
    public static final Tag<Item> INEFFECTIVE_SCARECROW_DAMAGERS = register("ineffective_scarecrow_damagers");
    /**
     * Vanilla crops defined in {@link net.minecraft.tag.BlockTags} as item form and their respective seeds.
     */
    public static final Tag<Item> CROPS = register("crops");

    private static Tag<Item> register(String path) {
        return TagRegistry.item(new Identifier(Scarecrows.MOD_ID, path));
    }
}
