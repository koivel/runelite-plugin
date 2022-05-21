package com.koivel.runelite.plugin.tracker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.koivel.runelite.plugin.modal.KEvent;
import com.koivel.runelite.plugin.modal.KEventSeries;

public class DataService {

    public static final Object LOCK = new Object();

    private static Map<String, KEventSeries> seriesByAccount = new HashMap<>();

    public static void addEvent(String accountHash, String accountDisplayName, KEvent event) {
        synchronized (LOCK) {
            KEventSeries series = seriesByAccount.computeIfAbsent(accountHash, (t) -> {
                KEventSeries newSeries = new KEventSeries();
                newSeries.setAccountId(accountHash);
                newSeries.setAccountDisplayName(accountDisplayName);
                newSeries.setCollectionId("runescape");
                newSeries.setEvents(new ArrayList<KEvent>());
                return newSeries;
            });
            series.getEvents().add(event);
        }
    }

    public static Collection<KEventSeries> getSeriesByAccount() {
        return seriesByAccount.values();
    }

    public static void reset() {
        seriesByAccount = new HashMap<>();
    }
}
