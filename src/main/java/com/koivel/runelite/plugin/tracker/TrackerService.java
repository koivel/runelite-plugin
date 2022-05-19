package com.koivel.runelite.plugin.tracker;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.game.LootManager;
import okhttp3.OkHttpClient;

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

    private List<Tracker> trackers = Arrays.asList(new SkillTracker(), new LootContainerTracker(),
            new LootDropTracker(),
            new WriteTracker());

    public TrackerService(Client client, LootManager lootManager, EventBus eventBus, KoivelConfig config,
            OkHttpClient httpClient) {
        this.config = config;
        log.debug("Starting client with {} and {}", client, eventBus);
        this.client = client;
        this.lootManager = lootManager;
        this.eventBus = eventBus;
        this.httpClient = httpClient;
        for (Tracker tracker : trackers) {
            tracker.setTracketService(this);
        }
    }

    public void start() {
        for (Tracker tracker : trackers) {
            tracker.start();
        }
    }

    public void shutdown() {
        for (Tracker tracker : trackers) {
            tracker.shutdown();
        }
    }
}
