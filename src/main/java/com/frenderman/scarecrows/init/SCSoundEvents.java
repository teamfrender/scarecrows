package com.frenderman.scarecrows.init;

import com.frenderman.scarecrows.Scarecrows;
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
    private static SoundEvent createScarecrowSound(String id) {
        return register("entity.scarecrow." + id);
    }

    public SCSoundEvents() {}

    private static SoundEvent register(String id) {
        Identifier identifier = new Identifier(Scarecrows.MOD_ID, id);
        return Registry.register(Registry.SOUND_EVENT, identifier, new SoundEvent(identifier));
    }
}
