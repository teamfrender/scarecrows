package com.frenderman.scarecrows.common.register;

import com.frenderman.scarecrows.common.core.Scarecrows;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class SCSoundEvents {

    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Scarecrows.MOD_ID);

    //
    // SCARECROW
    //
    public static final RegistryObject<SoundEvent> ENTITY_SCARECROW_BREAK = createScarecrowSound("break");
    public static final RegistryObject<SoundEvent> ENTITY_SCARECROW_FALL = createScarecrowSound("fall");
    public static final RegistryObject<SoundEvent> ENTITY_SCARECROW_HIT = createScarecrowSound("hit");
    public static final RegistryObject<SoundEvent> ENTITY_SCARECROW_FRENCHIFY = createScarecrowSound("frenchify");

    //
    // CROW
    //
    public static final RegistryObject<SoundEvent> ENTITY_CROW_AMBIENT = createCrowSound("ambient");
    public static final RegistryObject<SoundEvent> ENTITY_CROW_DEATH = createCrowSound("death");
    public static final RegistryObject<SoundEvent> ENTITY_CROW_HURT = createCrowSound("hurt");
    public static final RegistryObject<SoundEvent> ENTITY_CROW_FLY = createCrowSound("fly");
    // Unless we have a custom sound for this, it is a bit redundant
    //public static final RegistryObject<SoundEvent> ENTITY_CROW_STEP;



    private static RegistryObject<SoundEvent> createScarecrowSound(String id) {
        return createEntitySound("scarecrow", id);
    }

    private static RegistryObject<SoundEvent> createCrowSound(String id) {
        return createEntitySound("crow", id);
    }

    private static RegistryObject<SoundEvent> createEntitySound(String entity, String id) {
        return register("entity." + entity + "." + id);
    }

    private static RegistryObject<SoundEvent> register(String name) {
        return SOUNDS.register(name, () -> new SoundEvent(Scarecrows.resourceLoc(name)));
    }
}
