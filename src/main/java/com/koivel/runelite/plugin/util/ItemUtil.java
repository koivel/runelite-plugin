package com.koivel.runelite.plugin.util;

import net.runelite.api.ItemComposition;
import net.runelite.api.ItemID;
import net.runelite.client.RuneLite;
import net.runelite.client.game.ItemManager;

public class ItemUtil {

    public static int getRealId(int itemId) {
        final ItemComposition itemComposition = getItemManager().getItemComposition(itemId);
        return itemComposition.getNote() != -1 ? itemComposition.getLinkedNoteId() : itemId;
    }

    public static int getPrice(int itemId) {
        if (itemId == ItemID.COINS_995) {
            return 1;
        }
        if (itemId == ItemID.PLATINUM_TOKEN) {
            return 1000;
        }
        return getItemManager().getItemPrice(itemId);
    }

    public static ItemManager getItemManager() {
        return RuneLite.getInjector().getInstance(ItemManager.class);
    }
}