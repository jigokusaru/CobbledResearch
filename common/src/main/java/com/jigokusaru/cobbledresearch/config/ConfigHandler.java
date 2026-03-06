package com.jigokusaru.cobbledresearch.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public class ConfigHandler {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static GeneralConfig GENERAL = new GeneralConfig();
    public static PointsConfig POINTS = new PointsConfig();
    public static ShopConfig SHOP = new ShopConfig();

    public static void loadConfig(Path configDir) {
        File dir = configDir.toFile();
        if (!dir.exists()) dir.mkdirs();

        GENERAL = loadFile(new File(dir, "config.json"), GeneralConfig.class, new GeneralConfig());
        POINTS = loadFile(new File(dir, "points.json"), PointsConfig.class, new PointsConfig());
        SHOP = loadFile(new File(dir, "shop.json"), ShopConfig.class, new ShopConfig());
    }

    private static <T> T loadFile(File file, Class<T> clazz, T defaultValue) {
        if (!file.exists()) {
            saveFile(file, defaultValue);
            return defaultValue;
        }
        try (FileReader reader = new FileReader(file)) {
            return GSON.fromJson(reader, clazz);
        } catch (IOException e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    private static void saveFile(File file, Object config) {
        try (FileWriter writer = new FileWriter(file)) {
            GSON.toJson(config, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}