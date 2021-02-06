package com.frenderman.scarecrows.common.register;

import com.frenderman.scarecrows.common.core.Scarecrows;
import com.frenderman.scarecrows.common.item.ScarecrowItem;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class SCItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Scarecrows.MOD_ID);

    public static final RegistryObject<Item> WHITE_SCARECROW = registerScarecrow(DyeColor.WHITE);
    public static final RegistryObject<Item> ORANGE_SCARECROW = registerScarecrow(DyeColor.ORANGE);
    public static final RegistryObject<Item> MAGENTA_SCARECROW = registerScarecrow(DyeColor.MAGENTA);
    public static final RegistryObject<Item> LIGHT_BLUE_SCARECROW = registerScarecrow(DyeColor.LIGHT_BLUE);
    public static final RegistryObject<Item> YELLOW_SCARECROW = registerScarecrow(DyeColor.YELLOW);
    public static final RegistryObject<Item> LIME_SCARECROW = registerScarecrow(DyeColor.LIME);
    public static final RegistryObject<Item> PINK_SCARECROW = registerScarecrow(DyeColor.PINK);
    public static final RegistryObject<Item> GRAY_SCARECROW = registerScarecrow(DyeColor.GRAY);
    public static final RegistryObject<Item> LIGHT_GRAY_SCARECROW = registerScarecrow(DyeColor.LIGHT_GRAY);
    public static final RegistryObject<Item> CYAN_SCARECROW = registerScarecrow(DyeColor.CYAN);
    public static final RegistryObject<Item> PURPLE_SCARECROW = registerScarecrow(DyeColor.PURPLE);
    public static final RegistryObject<Item> BLUE_SCARECROW = registerScarecrow(DyeColor.BLUE);
    public static final RegistryObject<Item> BROWN_SCARECROW = registerScarecrow(DyeColor.BROWN);
    public static final RegistryObject<Item> GREEN_SCARECROW = registerScarecrow(DyeColor.GREEN);
    public static final RegistryObject<Item> RED_SCARECROW = registerScarecrow(DyeColor.RED);
    public static final RegistryObject<Item> BLACK_SCARECROW = registerScarecrow(DyeColor.BLACK);



    private static RegistryObject<Item> registerItem(String name, Supplier<Item> itemSupplier) {
        return ITEMS.register(name, itemSupplier);
    }

    private static RegistryObject<Item> registerBlockItem(RegistryObject<Block> blockRegistryObject, @Nullable Item.Properties properties) {
        return registerItem(blockRegistryObject.getId().getPath(), () -> new BlockItem(blockRegistryObject.get(), properties == null ? properties() : properties));
    }

    private static RegistryObject<Item> registerScarecrow(DyeColor dyeColor) {
        return registerItem(dyeColor.getTranslationKey() + "_" + "scarecrow", () -> new ScarecrowItem(properties().maxStackSize(16), dyeColor));
    }

    private static RegistryObject<Item> registerSpawnEgg(String name, EntityType<?> entityType, int primaryColor, int secondaryColor) {
        return registerItem(name, () -> new SpawnEggItem(entityType, primaryColor, secondaryColor, properties()));
    }

    private static Item.Properties properties() {
        return new Item.Properties().group(Scarecrows.ITEM_GROUP);
    }
}
