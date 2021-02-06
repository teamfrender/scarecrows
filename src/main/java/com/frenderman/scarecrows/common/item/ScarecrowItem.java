package com.frenderman.scarecrows.common.item;

import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;

public class ScarecrowItem extends Item {

    private final DyeColor dyeColor;

    public ScarecrowItem(Properties properties, DyeColor dyeColor) {
        super(properties);
        this.dyeColor = dyeColor;
    }

    public DyeColor getDyeColor() {
        return this.dyeColor;
    }
}
