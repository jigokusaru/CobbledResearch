package com.jigokusaru.cobbledresearch;

import com.jigokusaru.cobbledresearch.config.ConfigHandler;
import dev.architectury.platform.Platform;

public final class CobbledResearch {
    public static final String MOD_ID = "cobbledresearch";

    public static void init() {
        ConfigHandler.loadConfig(Platform.getConfigFolder().resolve("cobbledresearch"));
    }
}