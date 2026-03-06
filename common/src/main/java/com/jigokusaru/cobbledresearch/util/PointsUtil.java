package com.jigokusaru.cobbledresearch.util;

import com.jigokusaru.cobbledresearch.registration.ModAttachments;
import net.minecraft.world.entity.player.Player;

public class PointsUtil {

    public static long getPoints(Player player) {
        return player.getAttached(ModAttachments.POINTS).getPoints();
    }

    public static void addPoints(Player player, long amount) {
        ResearchPoints data = player.getAttached(ModAttachments.POINTS);
        data.setPoints(data.getPoints() + amount);
        // We tell Architectury the data changed so it saves/syncs
        player.setAttached(ModAttachments.POINTS, data);
    }

    public static boolean takePoints(Player player, long amount) {
        ResearchPoints data = player.getAttached(ModAttachments.POINTS);
        if (data.getPoints() >= amount) {
            data.setPoints(data.getPoints() - amount);
            player.setAttached(ModAttachments.POINTS, data);
            return true;
        }
        return false;
    }
}