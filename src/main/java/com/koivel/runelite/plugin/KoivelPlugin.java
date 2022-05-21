package com.koivel.runelite.plugin;

import com.google.inject.Provides;
import com.koivel.runelite.plugin.tracker.DataService;
import com.koivel.runelite.plugin.tracker.TrackerService;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.LootManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import okhttp3.OkHttpClient;

import javax.inject.Inject;

@Slf4j
@PluginDescriptor(
        name = "Koivel"
)
public class KoivelPlugin extends Plugin {
    @Inject
    private Client client;

    @Inject
    private LootManager lootManager;

    @Inject
    private EventBus eventBus;

    @Inject
    private KoivelConfig config;
    
    @Inject
    private OkHttpClient httpClient;

    private TrackerService trackerService;

    @Override
    protected void startUp() throws Exception {
        DataService.reset();
        trackerService = new TrackerService(client, lootManager, eventBus, config, httpClient);
        trackerService.start();
        log.debug("Koivel started!");
    }

    @Override
    protected void shutDown() throws Exception {
        if (trackerService != null) trackerService.shutdown();
        log.debug("Koivel stopped!");
    }

    @Provides
    KoivelConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(KoivelConfig.class);
    }
}
