package com.frenderman.scarecrows;

import com.frenderman.scarecrows.client.config.SCConfig;
import com.frenderman.scarecrows.client.config.SCConfigManager;
import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class ScarecrowsModMenu implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parentScreen -> {
            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(parentScreen)
                    .setDefaultBackgroundTexture(new Identifier("textures/block/hay_block_top.png"))
                    .setTitle(new TranslatableText("config." + Scarecrows.MOD_ID + ".title"))
                    .setSavingRunnable(SCConfigManager::save);

            builder.setGlobalized(true);
            builder.setGlobalizedExpanded(false);

            ConfigEntryBuilder entryBuilder = builder.entryBuilder();

            //
            // MISC CATEGORY
            //

            ConfigCategory MISC = builder.getOrCreateCategory(new TranslatableText("config." + Scarecrows.MOD_ID + ".category.misc"));

            String sadcrow = SCConfig.MISC.sadcrow.getId();
            MISC.addEntry(
                    entryBuilder.startBooleanToggle(
                        new TranslatableText("config." + Scarecrows.MOD_ID + ".category.misc." + sadcrow),
                        SCConfig.MISC.sadcrow.getBoolean()
                    )
                    .setDefaultValue(true)
                    .setSaveConsumer(value -> SCConfig.MISC.sadcrow.value = value)
                    .setTooltip(new TranslatableText("config." + Scarecrows.MOD_ID + ".category.misc." + sadcrow + ".tooltip"))
                    .build()
            );

            return builder.build();
        };
    }
}
