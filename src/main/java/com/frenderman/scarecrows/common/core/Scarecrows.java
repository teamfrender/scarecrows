package com.frenderman.scarecrows.common.core;

import com.frenderman.scarecrows.common.register.SCEntities;
import com.frenderman.scarecrows.common.register.SCItems;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Scarecrows.MOD_ID)
public class Scarecrows {

    public static final String MOD_ID = "scarecrows";
    public static final String MOD_NAME = "Scarecrows";

    public static Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static final ItemGroup ITEM_GROUP = new ItemGroup(MOD_ID) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(Items.HAY_BLOCK);
        }
    };

    public Scarecrows() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        SCItems.ITEMS.register(eventBus);
        SCEntities.ENTITIES.register(eventBus);
    }

    public static void log(Level level, String message){
        LOGGER.log(level, "[" + MOD_NAME + "] " + message);
    }
    public static void log(String message) {
        log(Level.INFO, message);
    }
}
