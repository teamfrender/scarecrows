package com.frenderman.scarecrows.init;

import com.frenderman.scarecrows.Scarecrows;
import com.frenderman.scarecrows.enchantment.DistractingEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class SCEnchantments {
    public static final Enchantment DISTRACTING = register(DistractingEnchantment.id, new DistractingEnchantment());

    public SCEnchantments() {}

    private static Enchantment register(String id, Enchantment enchantment) {
        return Registry.register(Registry.ENCHANTMENT, new Identifier(Scarecrows.MOD_ID, id), enchantment);
    }
}
