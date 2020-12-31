package com.frenderman.scarecrows.init;

import com.frenderman.scarecrows.Scarecrows;
import com.frenderman.scarecrows.item.*;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class SCItems {
    public static final Item SCARECROW = register("scarecrow", new ScarecrowItem(new FabricItemSettings().maxCount(16).group(Scarecrows.ITEM_GROUP)));

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
}
