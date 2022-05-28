package com.koivel.runelite.plugin.tracker.trackers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.koivel.runelite.plugin.modal.KEvent;
import com.koivel.runelite.plugin.tracker.Tracker;
import com.koivel.runelite.plugin.util.ItemUtil;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.client.eventbus.Subscribe;

@Slf4j
public class ItemContainerTracker extends Tracker {

    private Map<Integer, String> trackedInventories = new HashMap<Integer, String>();

    private Map<Integer, ContainerCache> containerCache = new HashMap<Integer, ContainerCache>();

    private int gameTick = 0;
    private long gameTickBasedNow = 0;

    @Override
    public void start() {
        super.start();
        this.trackedInventories.put(InventoryID.BANK.getId(), "Bank");
        this.trackedInventories.put(InventoryID.INVENTORY.getId(), "Inventory");
        this.trackedInventories.put(InventoryID.EQUIPMENT.getId(), "Equipment");
    }

    @Subscribe
    public void onItemContainerChanged(ItemContainerChanged event) {
        if (trackedInventories.containsKey(event.getContainerId())) {
            int currentTick = getClient().getTickCount();
            if (currentTick != gameTick) {
                gameTick = currentTick;
                gameTickBasedNow = System.currentTimeMillis();
            }

            track(event.getContainerId(), event.getItemContainer());
        }
    }

    private void track(int contaienrId, ItemContainer container) {
        ContainerCache previousCache = containerCache.get(contaienrId);
        ContainerCache currentCache = buildCache(contaienrId, container);

        if (previousCache == null
                || previousCache.getItemCache().hashCode() != currentCache.getItemCache().hashCode()) {

            long totalValue = 0;
            for (Entry<Integer, Integer> entry : currentCache.getItemCache().entrySet()) {
                int itemId = entry.getKey();
                int currentQuantity = entry.getValue();

                long price = ItemUtil.getPrice(itemId);
                totalValue += price * currentQuantity;

                int previousQuantity = previousCache == null ? -1
                        : previousCache.getItemCache().getOrDefault(itemId, -1);

                if (previousQuantity != currentQuantity) {
                    trackItemEvent(contaienrId, itemId, currentQuantity, price);
                    log.debug("item change in {}, {} : {} -> {} @ {} Gp", contaienrId, itemId, previousQuantity,
                            currentQuantity, price);
                }
            }

            if (previousCache != null) {
                for (Entry<Integer, Integer> entry : previousCache.getItemCache().entrySet()) {
                    int itemId = entry.getKey();
                    long price = ItemUtil.getPrice(itemId);
                    int previousQuantity = entry.getValue();
                    int currentQuantity = currentCache.getItemCache().getOrDefault(itemId, 0);

                    if (previousQuantity > 0 && currentQuantity == 0) {
                        trackItemEvent(contaienrId, itemId, currentQuantity, price);
                        log.debug("item change in {}, {} : {} -> {} @ {} Gp", contaienrId, itemId, previousQuantity,
                                currentQuantity, price);
                    }
                }
            }

            KEvent event = KEvent.builder().recordedAtEpochMs(gameTickBasedNow).groupId("item-container-total-value")
                    .build()
                    .value("value", totalValue).tag("containerId", String.valueOf(contaienrId));
            trackEvent(event);

            log.debug("total value for {} : {}", contaienrId, totalValue);
            containerCache.put(contaienrId, currentCache);
        }
    }

    private void trackItemEvent(int containerId, int itemId, int quanitity, long price) {
        KEvent event = KEvent.builder().recordedAtEpochMs(gameTickBasedNow)
                .groupId("item-container-contents").build()
                .tag("itemId", String.valueOf(itemId))
                .tag("containerId", String.valueOf(containerId))
                .tag("containerName", this.trackedInventories.get(containerId))
                .tag("itemDisplayName", getClient().getItemDefinition(itemId).getName())
                .value("value", quanitity)
                .value("itemPrice", price)
                .value("totalValue", (long) (quanitity * price));
        trackEvent(event);
    }

    private ContainerCache buildCache(int contaienrId, ItemContainer container) {
        ContainerCache cache = new ContainerCache(contaienrId, new HashMap<>());
        for (Item item : container.getItems()) {
            if (item == null || item.getId() < 1) {
                continue;
            }
            if (contaienrId == InventoryID.BANK.getId()
                    && getClient().getItemDefinition(item.getId()).getPlaceholderTemplateId() != -1) {
                continue;
            }
            int itemId = ItemUtil.getRealId(item.getId());
            cache.getItemCache().compute(itemId, (k, v) -> {
                return (v == null ? 0 : v) + item.getQuantity();
            });
        }
        return cache;
    }

    @Data
    @AllArgsConstructor
    private class ContainerCache {
        private int containerId;
        private Map<Integer, Integer> itemCache;
    }

    public String getName() {
        return "account-value-tracker";
    }
}
