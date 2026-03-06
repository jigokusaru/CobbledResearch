package com.jigokusaru.cobbledresearch.util;

import net.minecraft.world.entity.player.Player;

public interface ResearchPointsProvider {
    long cobbledresearch$getPoints();
    void cobbledresearch$setPoints(long points);

    static ResearchPointsProvider from(Player player) {
        return (ResearchPointsProvider) player;
    }
}