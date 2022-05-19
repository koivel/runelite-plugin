package com.koivel.runelite.plugin.tracker.trackers;

import com.koivel.runelite.plugin.modal.KEvent;
import com.koivel.runelite.plugin.tracker.Tracker;
import com.koivel.runelite.plugin.util.ItemUtil;

import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.api.widgets.WidgetID;
import net.runelite.client.eventbus.Subscribe;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class LootContainerTracker extends Tracker {

    private static final Set<Integer> theatreOfBloodRegions = new HashSet<>(Arrays.asList(12867, 14642));

    private boolean instancedEventLock = false;

    @Subscribe
    public void onWidgetLoaded(WidgetLoaded widgetLoaded) {
        ItemContainer itemContainer;
        String event;

        switch (widgetLoaded.getGroupId()) {
            case WidgetID.BARROWS_REWARD_GROUP_ID:
                event = "Barrows";
                itemContainer = getClient().getItemContainer(InventoryID.BARROWS_REWARD);
                break;
            case WidgetID.CHAMBERS_OF_XERIC_REWARD_GROUP_ID:
                if (instancedEventLock)
                    return;
                event = "Chambers of Xeric";
                itemContainer = getClient().getItemContainer(InventoryID.CHAMBERS_OF_XERIC_CHEST);
                instancedEventLock = true;
                break;
            case WidgetID.THEATRE_OF_BLOOD_GROUP_ID:
                if (instancedEventLock)
                    return;

                int regionID = WorldPoint
                        .fromLocalInstance(getClient(), getClient().getLocalPlayer().getLocalLocation()).getRegionID();
                if (!theatreOfBloodRegions.contains(regionID))
                    return;

                event = "Theatre of Blood";
                itemContainer = getClient().getItemContainer(InventoryID.THEATRE_OF_BLOOD_CHEST);
                instancedEventLock = true;
                break;
            case WidgetID.CLUE_SCROLL_REWARD_GROUP_ID:
                event = "Clue Scroll";
                itemContainer = getClient().getItemContainer(InventoryID.BARROWS_REWARD);
                break;
            case WidgetID.KINGDOM_GROUP_ID:
                event = "Kingdom of Miscellania";
                itemContainer = getClient().getItemContainer(InventoryID.KINGDOM_OF_MISCELLANIA);
                break;
            case WidgetID.FISHING_TRAWLER_REWARD_GROUP_ID:
                event = "Fishing Trawler";
                itemContainer = getClient().getItemContainer(InventoryID.FISHING_TRAWLER_REWARD);
                break;
            case WidgetID.DRIFT_NET_FISHING_REWARD_GROUP_ID:
                event = "Tempoross";
                itemContainer = getClient().getItemContainer(InventoryID.DRIFT_NET_FISHING_REWARD);
                break;
            default:
                return;

        }

        if (itemContainer == null)
            return;

        long now = System.currentTimeMillis();
        for (Item item : itemContainer.getItems()) {
            int itemId = ItemUtil.getRealId(item.getId());
            KEvent kEvent = KEvent.builder()
                    .groupId("loot/received")
                    .recordedAtEpochMs(now)
                    .build()
                    .tag("type", event.toLowerCase(Locale.ROOT))
                    .tag("itemId", String.valueOf(itemId))
                    .tag("itemDisplayName", getClient().getItemDefinition(itemId).getName())
                    .value("value", item.getQuantity())
                    .value("price", 0.0 + ItemUtil.getPrice(itemId));
            trackEvent(kEvent);
        }
    }
}
