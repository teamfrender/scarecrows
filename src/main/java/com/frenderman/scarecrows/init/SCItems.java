package com.frenderman.scarecrows.init;

import com.frenderman.scarecrows.Scarecrows;
import com.frenderman.scarecrows.entity.scarecrow.ScarecrowEntity;
import com.frenderman.scarecrows.item.*;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

@SuppressWarnings("unused")
public class SCItems {
    public static final Item WHITE_SCARECROW = registerScarecrow(DyeColor.WHITE);
    public static final Item ORANGE_SCARECROW = registerScarecrow(DyeColor.ORANGE);
    public static final Item MAGENTA_SCARECROW = registerScarecrow(DyeColor.MAGENTA);
    public static final Item LIGHT_BLUE_SCARECROW = registerScarecrow(DyeColor.LIGHT_BLUE);
    public static final Item YELLOW_SCARECROW = registerScarecrow(DyeColor.YELLOW);
    public static final Item LIME_SCARECROW = registerScarecrow(DyeColor.LIME);
    public static final Item PINK_SCARECROW = registerScarecrow(DyeColor.PINK);
    public static final Item GRAY_SCARECROW = registerScarecrow(DyeColor.GRAY);
    public static final Item LIGHT_GRAY_SCARECROW = registerScarecrow(DyeColor.LIGHT_GRAY);
    public static final Item CYAN_SCARECROW = registerScarecrow(DyeColor.CYAN);
    public static final Item PURPLE_SCARECROW = registerScarecrow(DyeColor.PURPLE);
    public static final Item BLUE_SCARECROW = registerScarecrow(DyeColor.BLUE);
    public static final Item BROWN_SCARECROW = registerScarecrow(DyeColor.BROWN);
    public static final Item GREEN_SCARECROW = registerScarecrow(DyeColor.GREEN);
    public static final Item RED_SCARECROW = registerScarecrow(DyeColor.RED);
    public static final Item BLACK_SCARECROW = registerScarecrow(DyeColor.BLACK);
    public SCItems() {}

    private static Item register(String id, Item item) {
        return Registry.register(Registry.ITEM, new Identifier(Scarecrows.MOD_ID, id), item);
    }
    private static Item registerScarecrow(DyeColor dyeColor) {
        return register(dyeColor.getName() + "_" + ScarecrowEntity.id, new ScarecrowItem(dyeColor, new FabricItemSettings().maxCount(16).group(Scarecrows.ITEM_GROUP)));
    }
}
