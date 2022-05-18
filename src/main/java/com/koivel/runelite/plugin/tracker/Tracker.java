package com.koivel.runelite.plugin.tracker;

import net.runelite.api.Client;

public class Tracker {

    protected TrackerService trackerService;

    public void start(){
        trackerService.getEventBus().register(this);
    }

    public void shutdown(){
        trackerService.getEventBus().unregister(this);
    }

    protected Client getClient(){
        return trackerService.getClient();
    }

    protected int getTickCount(){
        return trackerService.getClient().getTickCount();
    }

    public void setTracketService(TrackerService trackerService) {
        this.trackerService = trackerService;
    }
}
