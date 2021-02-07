package com.frenderman.scarecrows.common.tag;

import com.frenderman.scarecrows.common.core.Scarecrows;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;

public class SCItemTags {

    /**
     * Items that will not damage a Scarecrow.
     */
    public static final ITag.INamedTag<Item> INEFFECTIVE_SCARECROW_DAMAGERS = register("ineffective_scarecrow_damagers");

    /**
     * Vanilla crops defined in {@link net.minecraft.tags.BlockTags} as item form and their respective seeds.
     */
    public static final ITag.INamedTag<Item> CROPS = register("crops");

    private static ITag.INamedTag<Item> register(String path) {
        return ItemTags.makeWrapperTag(Scarecrows.resourceLoc(path).toString());
    }
}
