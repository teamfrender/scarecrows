package com.frenderman.scarecrows.init;

import com.frenderman.scarecrows.Scarecrows;
import com.frenderman.scarecrows.entity.scarecrow.ScarecrowEntity;
import com.frenderman.scarecrows.item.*;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class SCItems {
    public static final Item WHITE_SCARECROW = registerScarecrow("white", DyeColor.WHITE);
    public static final Item ORANGE_SCARECROW = registerScarecrow("orange", DyeColor.ORANGE);
    public static final Item MAGENTA_SCARECROW = registerScarecrow("magenta", DyeColor.MAGENTA);
    public static final Item LIGHT_BLUE_SCARECROW = registerScarecrow("light_blue", DyeColor.LIGHT_BLUE);
    public static final Item YELLOW_SCARECROW = registerScarecrow("yellow", DyeColor.YELLOW);
    public static final Item LIME_SCARECROW = registerScarecrow("lime", DyeColor.LIME);
    public static final Item PINK_SCARECROW = registerScarecrow("pink", DyeColor.PINK);
    public static final Item GRAY_SCARECROW = registerScarecrow("gray", DyeColor.GRAY);
    public static final Item LIGHT_GRAY_SCARECROW = registerScarecrow("light_gray", DyeColor.LIGHT_GRAY);
    public static final Item CYAN_SCARECROW = registerScarecrow("cyan", DyeColor.CYAN);
    public static final Item PURPLE_SCARECROW = registerScarecrow("purple", DyeColor.PURPLE);
    public static final Item BLUE_SCARECROW = registerScarecrow("blue", DyeColor.BLUE);
    public static final Item BROWN_SCARECROW = registerScarecrow("brown", DyeColor.BROWN);
    public static final Item GREEN_SCARECROW = registerScarecrow("green", DyeColor.GREEN);
    public static final Item RED_SCARECROW = registerScarecrow("red", DyeColor.RED);
    public static final Item BLACK_SCARECROW = registerScarecrow("black", DyeColor.BLACK);

    public SCItems() {
        /*DispenserBlock.registerBehavior(SCARECROW, new ItemDispenserBehavior() {
            public ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
                Direction direction = pointer.getBlockState().get(DispenserBlock.FACING);
                BlockPos blockPos = pointer.getBlockPos().offset(direction);
                World world = pointer.getWorld();
                ScarecrowEntity entity = new ScarecrowEntity(world, (double)blockPos.getX() + 0.5D, blockPos.getY(), (double)blockPos.getZ() + 0.5D);
                EntityType.loadFromEntityTag(world, null, entity, stack.getTag());
                entity.yaw = direction.asRotation();
                world.spawnEntity(entity);
                stack.decrement(1);
                return stack;
            }
        });*/
    }

    private static Item register(String id, Item item) {
        return Registry.register(Registry.ITEM, new Identifier(Scarecrows.MOD_ID, id), item);
    }
    private static Item registerScarecrow(String dyeId, DyeColor dyeColor) {
        return register(dyeId + "_" + ScarecrowEntity.id, new ScarecrowItem(dyeColor, new FabricItemSettings().maxCount(16).group(Scarecrows.ITEM_GROUP)));
    }
}
