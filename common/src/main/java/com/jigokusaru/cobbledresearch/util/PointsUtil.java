package com.jigokusaru.cobbledresearch.util;

import net.minecraft.world.entity.player.Player;

public class PointsUtil {

    public static long getPoints(Player player) {
        return ResearchPointsProvider.from(player).cobbledresearch$getPoints();
    }

    public static void addPoints(Player player, long amount) {
        ResearchPointsProvider provider = ResearchPointsProvider.from(player);
        provider.cobbledresearch$setPoints(provider.cobbledresearch$getPoints() + amount);
    }

    public static boolean takePoints(Player player, long amount) {
        ResearchPointsProvider provider = ResearchPointsProvider.from(player);
        long current = provider.cobbledresearch$getPoints();
        if (current >= amount) {
            provider.cobbledresearch$setPoints(current - amount);
            return true;
        }
        return false;
    }
}