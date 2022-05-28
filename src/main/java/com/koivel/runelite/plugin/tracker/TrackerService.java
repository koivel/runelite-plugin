package com.koivel.runelite.plugin.tracker;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.game.LootManager;
import okhttp3.OkHttpClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.koivel.runelite.plugin.KoivelConfig;
import com.koivel.runelite.plugin.tracker.trackers.*;
import com.koivel.runelite.plugin.tracker.trackers.write.WriteTracker;

@Getter
@Slf4j
public class TrackerService {

    private Client client;
    private LootManager lootManager;
    private EventBus eventBus;
    private KoivelConfig config;
    private OkHttpClient httpClient;

    private List<Tracker> trackers = Arrays.asList(
            new SkillTracker(),
            new LootContainerTracker(),
            new LootDropTracker(),
            new ItemContainerTracker(),
            new KcTracker(),
            new WriteTracker());

    private List<Tracker> enabledTrackers = new ArrayList<>();

    public TrackerService(Client client, LootManager lootManager, EventBus eventBus, KoivelConfig config,
            OkHttpClient httpClient) {
        this.config = config;
        log.info("Starting client with {} and {}", client, eventBus);
        this.client = client;
        this.lootManager = lootManager;
        this.eventBus = eventBus;
        this.httpClient = httpClient;

        String[] disabledTrackers = config.disabledTrackers().split(",");
        for (Tracker tracker : trackers) {
            tracker.setTracketService(this);

            boolean enabled = true;
            for (String disabledTracker : disabledTrackers) {
                if (disabledTracker.trim().equalsIgnoreCase(tracker.getName())) {
                    enabled = false;
                }
            }

            if (enabled) {
                enabledTrackers.add(tracker);
            }
        }
    }

    public void start() {
        for (Tracker tracker : enabledTrackers) {
            tracker.start();
        }
    }

    public void shutdown() {
        for (Tracker tracker : enabledTrackers) {
            tracker.shutdown();
        }
    }
}
