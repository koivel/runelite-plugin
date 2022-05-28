package com.koivel.runelite.plugin.tracker.trackers.write;

import net.runelite.api.events.GameTick;
import net.runelite.client.eventbus.Subscribe;

import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

import com.koivel.runelite.plugin.modal.KEventSeries;
import com.koivel.runelite.plugin.tracker.DataService;
import com.koivel.runelite.plugin.tracker.Tracker;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WriteTracker extends Tracker {

    private long lastWriteMs = System.currentTimeMillis();

    private WriteHandler writeHandler;

    @Override
    public void start() {
        super.start();
        this.writeHandler = new WriteHandler(trackerService.getHttpClient(),
                () -> trackerService.getConfig());

    }

    @Subscribe
    public void onGameTick(GameTick gameTick) {
        long now = System.currentTimeMillis();
        if (now - lastWriteMs < TimeUnit.MINUTES.toMillis(1))
            return;

        synchronized (DataService.LOCK) {
            Collection<KEventSeries> seriesByAccount = DataService.getSeriesByAccount();
            for (KEventSeries series : seriesByAccount) {
                if (series.getEvents().size() > 0) {
                    try {
                        writeHandler.write(series, new WriteCallbackHandler(writeHandler, series));
                        log.debug("wrote {} events", series.getEvents().size());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            DataService.reset();
        }

        lastWriteMs = now;
    }

    public String getName() {
        return "write-tracker";
    }
}
