package com.koivel.runelite.plugin.tracker;

import com.koivel.runelite.plugin.modal.KEvent;
import com.koivel.runelite.plugin.util.LocationUtil;

import net.runelite.api.Client;

public class Tracker {

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

    protected void trackEvent(KEvent event) {
        LocationUtil.injectLocation(getClient(), event);
        DataService.addFrame(event);
    }

    public void setTracketService(TrackerService trackerService) {
        this.trackerService = trackerService;
    }
}
