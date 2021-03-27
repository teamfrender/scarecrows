package com.frenderman.scarecrows.client.config;

import com.frenderman.scarecrows.Scarecrows;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import me.andante.chord.client.config.Option;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;

public class SCConfigManager {
    private static final File FILE = FabricLoader.getInstance().getConfigDir().toFile().toPath().resolve(Scarecrows.MOD_ID + ".json").toFile();

    public static void save() {
        JsonObject jsonObject = new JsonObject();

        SCConfig.MiscGroup MISC = SCConfig.MISC;
        jsonObject.addProperty(MISC.sadcrow.getId(), MISC.sadcrow.getValueForSave());

        try (PrintWriter out = new PrintWriter(FILE)) {
            out.println(jsonObject.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("ConstantConditions")
    public static void load() {
        try {
            String json = new String(Files.readAllBytes(FILE.toPath()));
            if (!json.isEmpty()) {
                JsonObject jsonObject = (JsonObject) new JsonParser().parse(json);

                SCConfig.MiscGroup MISC = SCConfig.MISC;
                MISC.sadcrow.value = SCConfigManager.load(jsonObject, MISC.sadcrow).getAsBoolean();
            }
        } catch (IOException ignored) {
            Scarecrows.log(Level.WARN, "Could not load configuration file! Saving and loading default values.");
            SCConfigManager.save();
        } catch (NullPointerException e) {
            Scarecrows.log(Level.WARN, "Configuration failed to load fully from file due to " + e.toString() + ". This is probably just a configuration update.");
        } catch (IllegalArgumentException e) {
            Scarecrows.log(Level.ERROR, "Configuration option failed to load: " + e.toString());
        }
    }
    private static JsonPrimitive load(JsonObject jsonObject, Option<?> option) {
        try {
            return jsonObject.getAsJsonPrimitive(option.getId());
        } catch (RuntimeException e) {
            Object optionDefault = option.getDefault();
            Scarecrows.log(Level.WARN, option.getId() + " is not present! Defaulting to " + optionDefault);
            if (optionDefault instanceof Boolean) {
                return new JsonPrimitive((Boolean) optionDefault);
            } else if (optionDefault instanceof Integer) {
                return new JsonPrimitive((Integer) optionDefault);
            } else if (optionDefault instanceof Enum<?>) {
                return new JsonPrimitive(String.valueOf(optionDefault));
            }

            return null;
        }
    }

    public static Screen createScreen(Screen parentScreen) {
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
                SCConfig.MISC.sadcrow.value
            )
                .setDefaultValue(true)
                .setSaveConsumer(value -> SCConfig.MISC.sadcrow.value = value)
                .setTooltip(new TranslatableText("config." + Scarecrows.MOD_ID + ".category.misc." + sadcrow + ".tooltip"))
                .build()
        );

        return builder.build();
    }
}
