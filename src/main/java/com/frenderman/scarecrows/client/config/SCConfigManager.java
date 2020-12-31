package com.frenderman.scarecrows.client.config;

import com.frenderman.scarecrows.Scarecrows;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;

public class SCConfigManager {
    private static final File FILE = FabricLoader.getInstance().getConfigDir().toFile().toPath().resolve(Scarecrows.MOD_ID + ".json").toFile();

    public static void load() {
        try {
            String json = new String(Files.readAllBytes(FILE.toPath()));
            if (!json.equals("")) {
                JsonObject jsonObject = (JsonObject) new JsonParser().parse(json);

                SCConfig.MiscGroup MISC = SCConfig.MISC;
                MISC.sadcrow.value = jsonObject.getAsJsonPrimitive(MISC.sadcrow.getId()).getAsBoolean();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void save() {
        JsonObject jsonObject = new JsonObject();

        SCConfig.MiscGroup MISC = SCConfig.MISC;
        jsonObject.addProperty(MISC.sadcrow.getId(), MISC.sadcrow.getBoolean());

        try (PrintWriter out = new PrintWriter(FILE)) {
            out.println(jsonObject.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
