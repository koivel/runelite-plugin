package com.koivel.runelite.plugin.tracker;

import java.util.ArrayList;

import com.koivel.runelite.plugin.modal.KEvent;
import com.koivel.runelite.plugin.modal.KEventSeries;

public class DataService {

    public static final Object LOCK = new Object();

    private static KEventSeries series;

    public static void addFrame(KEvent event) {
        synchronized (LOCK) {
            series.getEvents().add(event);
        }
    }

    public static KEventSeries getEventFrameGroup() {
        return series;
    }

    public static void reset() {
        KEventSeries eventGroup = new KEventSeries();
        eventGroup.setCollectionId("runescape");
        eventGroup.setEvents(new ArrayList<KEvent>());
        series = eventGroup;
    }
}
