package com.jigokusaru.cobbledresearch.config;

import java.util.HashMap;
import java.util.Map;

public class PointsConfig {
    public int basePointsPerRelease = 10;

    // Multipliers (0.5 = +50%, 1.0 = +100%)
    public float shinyMultiplier = 1.0f;
    public float legendaryMultiplier = 5.0f;
    public float hiddenAbilityMultiplier = 0.5f;
    public float perfectIvMultiplier = 1.0f; // For 6IVs

    // Per-Species base overrides (Optional)
    public Map<String, Integer> speciesOverrides = new HashMap<>();

    public PointsConfig() {
        speciesOverrides.put("cobblemon:mew", 1000);
    }
}