package com.frenderman.scarecrows.init;

import com.frenderman.scarecrows.Scarecrows;
import com.frenderman.scarecrows.entity.scarecrow.ScarecrowEntity;
import net.minecraft.sound.SoundEvent;
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

    public SCSoundEvents() {}

    private static SoundEvent register(String id) {
        Identifier identifier = new Identifier(Scarecrows.MOD_ID, id);
        return Registry.register(Registry.SOUND_EVENT, identifier, new SoundEvent(identifier));
    }

    private static SoundEvent createEntitySound(String entity, String id) {
        return register("entity." + entity + "." + id);
    }
}
