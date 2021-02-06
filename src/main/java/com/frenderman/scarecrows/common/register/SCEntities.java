package com.frenderman.scarecrows.common.register;

import com.frenderman.scarecrows.common.core.Scarecrows;
import com.frenderman.scarecrows.common.entity.ScarecrowEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class SCEntities {

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, Scarecrows.MOD_ID);

    public static final RegistryObject<EntityType<ScarecrowEntity>> SCARECROW =  registerType("scarecrow", EntityType.Builder.create(ScarecrowEntity::new, EntityClassification.MISC).size(0.7F, 2.5F).trackingRange(10));

    private static <T extends Entity> RegistryObject<EntityType<T>> registerType(String name, EntityType.Builder<T> builder) {
        return ENTITIES.register(name, () -> builder.build(name));
    }
}
