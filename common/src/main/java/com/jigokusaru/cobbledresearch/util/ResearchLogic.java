package com.jigokusaru.cobbledresearch.util;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;

public class ResearchLogic {
    public static void playRejectSound(ServerPlayer player) {
        player.level().playSound(null, player.blockPosition(),
                SoundEvents.NOTE_BLOCK_BASS.value(), SoundSource.PLAYERS, 1.0f, 0.5f);
    }

    // Logic for filling boxes without caring about "shape"
    public static boolean hasSpaceInBox(int selectionSize, int boxCapacity, int occupiedCount) {
        return (boxCapacity - occupiedCount) >= selectionSize;
    }
}