package com.koivel.runelite.plugin.tracker.trackers;

import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.NpcLootReceived;
import net.runelite.client.events.PlayerLootReceived;
import net.runelite.client.game.ItemStack;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.koivel.runelite.plugin.modal.KEvent;
import com.koivel.runelite.plugin.tracker.Tracker;
import com.koivel.runelite.plugin.util.ItemUtil;
import com.koivel.runelite.plugin.util.RegionUtil;

public class LootTracker extends Tracker {

    private static final Set<Integer> SOUL_WARS_REGIONS = new HashSet<>(Arrays.asList(8493, 8749, 9005));
    private static final Set<Integer> LAST_MAN_STANDING_REGIONS = new HashSet<>(Arrays.asList(13658,
            13659,
            13660,
            13914,
            13915,
            13916,
            13918,
            13919,
            13920,
            14174,
            14175,
            14176,
            14430,
            14431,
            14432));

    @Subscribe
    public void onNpcLootReceived(NpcLootReceived event) {
        long now = System.currentTimeMillis();
        for (ItemStack item : event.getItems()) {
            int itemId = ItemUtil.getRealId(item.getId());
            KEvent kEvent = KEvent.builder()
                    .type("ValueChange")
                    .groupId("loot/npc/received")
                    .value(item.getQuantity())
                    .score(0.0 + ItemUtil.getPrice(itemId))
                    .displayText(getClient().getItemDefinition(itemId).getName())
                    .recordedAtEpochMs(now)
                    .build()
                    .tag(0, event.getNpc().getName())
                    .tag(1, String.valueOf(itemId));

            trackEvent(kEvent);
        }
    }

    @Subscribe
    public void onPlayerLootReceived(PlayerLootReceived event) {
        if (RegionUtil.isPlayerWithinMapRegion(getClient(), LAST_MAN_STANDING_REGIONS)
                || RegionUtil.isPlayerWithinMapRegion(getClient(), SOUL_WARS_REGIONS)) {
            return;
        }

        long now = System.currentTimeMillis();
        for (ItemStack item : event.getItems()) {
            int itemId = ItemUtil.getRealId(item.getId());
            KEvent kEvent = KEvent.builder()
                    .type("ValueChange")
                    .groupId("loot/player/received")
                    .value(item.getQuantity())
                    .score(0.0 + ItemUtil.getPrice(itemId))
                    .displayText(getClient().getItemDefinition(itemId).getName())
                    .recordedAtEpochMs(now)
                    .build()
                    .tag(0, event.getPlayer().getName())
                    .tag(1, String.valueOf(itemId));

            trackEvent(kEvent);
        }
    }
}