package com.jigokusaru.cobbledresearch.util;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PcAddonHandler {
    private static boolean multiSelectActive = false;
    private static final Set<UUID> selectedPokemon = new HashSet<>();

    public static boolean isMultiSelectActive() { return multiSelectActive; }

    public static void toggleMultiSelect() {
        multiSelectActive = !multiSelectActive;
        if (!multiSelectActive) selectedPokemon.clear();
    }

    public static void toggleSelection(UUID uuid) {
        if (selectedPokemon.contains(uuid)) {
            selectedPokemon.remove(uuid);
        } else {
            selectedPokemon.add(uuid);
        }
    }

    public static Set<UUID> getSelected() { return selectedPokemon; }
    public static void clear() { selectedPokemon.clear(); }
}