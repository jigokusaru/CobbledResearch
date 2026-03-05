package com.jigokusaru.cobbledresearch.util;

import com.cobblemon.mod.common.client.gui.pc.BoxStorageSlot;
import com.cobblemon.mod.common.client.gui.pc.PartyStorageSlot;
import com.cobblemon.mod.common.client.gui.pc.StorageSlot;
import com.cobblemon.mod.common.pokemon.Pokemon;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PcAddonHandler {
    private static boolean multiSelectActive = false;
    private static boolean dragging = false;
    private static final Map<UUID, Pokemon> selectedPokemon = new HashMap<>();
    private static final Map<UUID, Integer> selectionSourceIndices = new HashMap<>();
    private static String currentContainerId = null;

    public static boolean isMultiSelectActive() { return multiSelectActive; }
    public static boolean isDragging() { return dragging; }
    public static void setDragging(boolean state) { dragging = state; }
    public static String getCurrentContainerId() { return currentContainerId; }

    public static void toggleMultiSelect() {
        multiSelectActive = !multiSelectActive;
        if (!multiSelectActive) clear();
    }

    public static Map<UUID, Pokemon> getSelectedPokemon() { return selectedPokemon; }

    public static int getSelectionSourceIndex(UUID uuid) {
        return selectionSourceIndices.getOrDefault(uuid, -1);
    }

    public static boolean isBeingDragged(UUID uuid) {
        return dragging && selectedPokemon.containsKey(uuid);
    }

    public static boolean toggleSelection(Pokemon pokemon, String containerId, StorageSlot slot) {
        if (currentContainerId != null && !selectedPokemon.isEmpty() && !currentContainerId.equals(containerId)) {
            return false;
        }

        currentContainerId = containerId;
        UUID uuid = pokemon.getUuid();

        if (selectedPokemon.containsKey(uuid)) {
            selectedPokemon.remove(uuid);
            selectionSourceIndices.remove(uuid);
            if (selectedPokemon.isEmpty()) currentContainerId = null;
        } else {
            selectedPokemon.put(uuid, pokemon);
            int index = -1;
            if (slot instanceof PartyStorageSlot pSlot) index = pSlot.getPosition().getSlot();
            else if (slot instanceof BoxStorageSlot bSlot) index = bSlot.getPosition().getSlot();
            selectionSourceIndices.put(uuid, index);
        }
        return true;
    }

    public static void clear() {
        selectedPokemon.clear();
        selectionSourceIndices.clear();
        currentContainerId = null;
        dragging = false;
    }
}