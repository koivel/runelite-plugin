package com.koivel.runelite.plugin.tracker;

import com.koivel.runelite.plugin.modal.KEvent;
import com.koivel.runelite.plugin.util.LocationUtil;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;

@Slf4j
public abstract class Tracker {

    protected TrackerService trackerService;

    public void start() {
        trackerService.getEventBus().register(this);
    }

    public void shutdown() {
        trackerService.getEventBus().unregister(this);
    }

    protected Client getClient() {
        return trackerService.getClient();
    }

    protected int getTickCount() {
        return trackerService.getClient().getTickCount();
    }

    public abstract String getName();

    protected void trackEvent(KEvent event) {
        log.debug("tracking event : {}", event);
        LocationUtil.injectLocation(getClient(), event);
        DataService.addEvent(String.valueOf(getClient().getAccountHash()), getClient().getLocalPlayer().getName(),
                event);
    }

    public void setTracketService(TrackerService trackerService) {
        this.trackerService = trackerService;
    }
}
