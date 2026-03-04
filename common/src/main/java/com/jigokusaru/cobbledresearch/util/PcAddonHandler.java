package com.jigokusaru.cobbledresearch.util;

import com.cobblemon.mod.common.pokemon.Pokemon;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PcAddonHandler {
    private static boolean multiSelectActive = false;
    private static boolean dragging = false;
    private static final Map<UUID, Pokemon> selectedPokemon = new HashMap<>();
    private static String currentContainerId = null;

    public static boolean isMultiSelectActive() { return multiSelectActive; }
    public static boolean isDragging() { return dragging; }
    public static void setDragging(boolean state) { dragging = state; }

    public static void toggleMultiSelect() {
        multiSelectActive = !multiSelectActive;
        if (!multiSelectActive) clear();
    }

    public static Map<UUID, Pokemon> getSelectedPokemon() { return selectedPokemon; }

    // New helper to tell the UI to hide these Pokemon while dragging
    public static boolean isBeingDragged(UUID uuid) {
        return dragging && selectedPokemon.containsKey(uuid);
    }

    public static void toggleSelection(Pokemon pokemon, String containerId) {
        if (currentContainerId != null && !currentContainerId.equals(containerId)) {
            selectedPokemon.clear();
        }
        currentContainerId = containerId;
        if (selectedPokemon.containsKey(pokemon.getUuid())) {
            selectedPokemon.remove(pokemon.getUuid());
        } else {
            selectedPokemon.put(pokemon.getUuid(), pokemon);
        }
    }

    public static void clear() {
        selectedPokemon.clear();
        currentContainerId = null;
        dragging = false;
    }
}