package com.jigokusaru.cobbledresearch.config;

import java.util.ArrayList;
import java.util.List;

public class ShopConfig {
    public List<ShopCategory> categories = new ArrayList<>();

    public record ShopCategory(String name, List<ShopItem> items) {}
    public record ShopItem(String itemID, int price, String displayName) {}

    public ShopConfig() {
        List<ShopItem> ballItems = List.of(
                new ShopItem("minecraft:iron_ingot", 50, "Iron Ingot"),
                new ShopItem("cobblemon:poke_ball", 100, "Poke Ball")
        );
        categories.add(new ShopCategory("Basic Supplies", ballItems));
    }
}