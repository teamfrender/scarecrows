package com.frenderman.scarecrows.tag;

import com.frenderman.scarecrows.Scarecrows;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

public class SCItemTags {
    public static final Tag<Item> WOODEN_TOOLS = register("wooden_tools");

    private static Tag<Item> register(String path) {
        return TagRegistry.item(new Identifier(Scarecrows.MOD_ID, path));
    }
}
