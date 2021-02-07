package com.frenderman.scarecrows.common.register;

import com.frenderman.scarecrows.common.core.Scarecrows;
import com.frenderman.scarecrows.common.entity.CrowEntity;
import com.frenderman.scarecrows.common.entity.ScarecrowEntity;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class SCEntities {

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, Scarecrows.MOD_ID);

    public static final RegistryObject<EntityType<ScarecrowEntity>> SCARECROW = registerType("scarecrow", EntityType.Builder.create(ScarecrowEntity::new, EntityClassification.MISC).size(0.7F, 2.5F).trackingRange(10));
    public static final RegistryObject<EntityType<CrowEntity>> CROW = registerType("crow", EntityType.Builder.create(CrowEntity::new, EntityClassification.CREATURE).size(0.5F, 0.9F).trackingRange(8));

    public static void registerEntityPlacement() {
        EntitySpawnPlacementRegistry.register(CROW.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING, CrowEntity::canCrowSpawn);
    }

    public static void registerAttributes() {
        registerAttribute(SCEntities.SCARECROW.get(), ScarecrowEntity.registerAttributes().create());
    }

    private static void registerAttribute(EntityType<? extends LivingEntity> entityType, AttributeModifierMap modifierMap) {
        GlobalEntityTypeAttributes.put(entityType, modifierMap);
    }

    public static ResourceLocation texture(String path) {
        return Scarecrows.texture("entity/" + path);
    }

    private static <T extends Entity> RegistryObject<EntityType<T>> registerType(String name, EntityType.Builder<T> builder) {
        return ENTITIES.register(name, () -> builder.build(name));
    }
}
