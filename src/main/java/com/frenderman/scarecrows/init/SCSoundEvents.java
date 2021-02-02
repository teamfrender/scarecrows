package com.frenderman.scarecrows.init;

import com.frenderman.scarecrows.Scarecrows;
import com.frenderman.scarecrows.entity.crow.CrowEntity;
import com.frenderman.scarecrows.entity.scarecrow.ScarecrowEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class SCSoundEvents {
    //
    // SCARECROW
    //
    public static final SoundEvent ENTITY_SCARECROW_BREAK = createScarecrowSound("break");
    public static final SoundEvent ENTITY_SCARECROW_FALL = createScarecrowSound("fall");
    public static final SoundEvent ENTITY_SCARECROW_HIT = createScarecrowSound("hit");
    public static final SoundEvent ENTITY_SCARECROW_FRENCHIFY = createScarecrowSound("frenchify");
    private static SoundEvent createScarecrowSound(String id) {
        return createEntitySound(ScarecrowEntity.id, id);
    }

    //
    // CROW
    //
    public static final SoundEvent ENTITY_CROW_AMBIENT = createCrowSound("ambient");
    public static final SoundEvent ENTITY_CROW_DEATH = createCrowSound("death");
    public static final SoundEvent ENTITY_CROW_HURT = createCrowSound("hurt");
    public static final SoundEvent ENTITY_CROW_STEP = SoundEvents.ENTITY_PARROT_STEP;
    public static final SoundEvent ENTITY_CROW_FLY = createCrowSound("fly");
    private static SoundEvent createCrowSound(String id) {
        return createEntitySound(CrowEntity.id, id);
    }

    public SCSoundEvents() {}

    private static SoundEvent register(String id) {
        Identifier identifier = new Identifier(Scarecrows.MOD_ID, id);
        return Registry.register(Registry.SOUND_EVENT, identifier, new SoundEvent(identifier));
    }

    private static SoundEvent createEntitySound(String entity, String id) {
        return register("entity." + entity + "." + id);
    }
}
