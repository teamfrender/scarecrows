package com.frenderman.scarecrows.init;

import com.frenderman.scarecrows.Scarecrows;
import com.frenderman.scarecrows.entity.crow.CrowEntity;
import com.frenderman.scarecrows.entity.scarecrow.ScarecrowEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Heightmap;

public class SCEntities {
    public static final EntityType<ScarecrowEntity> SCARECROW = register(
        ScarecrowEntity.id,
        FabricEntityTypeBuilder.createLiving()
            .entityFactory(ScarecrowEntity::new).spawnGroup(SpawnGroup.MISC)
            .dimensions(EntityDimensions.fixed(0.7F, 2.5F))
            .trackRangeBlocks(10)
    );
    public static final EntityType<CrowEntity> CROW = register(
        CrowEntity.id,
        FabricEntityTypeBuilder.createMob()
            .entityFactory(CrowEntity::new).spawnGroup(SpawnGroup.CREATURE)
            .dimensions(EntityDimensions.fixed(0.5F, 0.9F))
            .spawnRestriction(SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING, CrowEntity::canSpawn)
            .trackRangeBlocks(8),
        new int[] { 3158064, 7237230 }
    );

    public SCEntities() {
        registerDefaultAttributes(SCARECROW, ScarecrowEntity.createLivingAttributes());
        registerDefaultAttributes(CROW, CrowEntity.createCrowAttributes());
    }

    private static <T extends Entity> EntityType<T> register(String id, EntityType<T> entityType, int[] spawnEggColors) {
        if (spawnEggColors != null)
            Registry.register(Registry.ITEM, new Identifier(Scarecrows.MOD_ID, id + "_spawn_egg"), new SpawnEggItem(entityType, spawnEggColors[0], spawnEggColors[1], new Item.Settings().maxCount(64).group(Scarecrows.ITEM_GROUP)));

        return Registry.register(Registry.ENTITY_TYPE, new Identifier(Scarecrows.MOD_ID, id), entityType);
    }
    private static <T extends Entity> EntityType<T> register(String id, FabricEntityTypeBuilder<T> entityType, int[] spawnEggColors) {
        return register(id, entityType.build(), spawnEggColors);
    }
    private static <T extends Entity> EntityType<T> register(String id, FabricEntityTypeBuilder<T> entityType) {
        return register(id, entityType.build(), null);
    }

    public static void registerDefaultAttributes(EntityType<? extends LivingEntity> type, DefaultAttributeContainer.Builder builder) {
        FabricDefaultAttributeRegistry.register(type, builder);
    }

    public static Identifier texture(String path) {
        return Scarecrows.texture("entity/" + path);
    }
}
