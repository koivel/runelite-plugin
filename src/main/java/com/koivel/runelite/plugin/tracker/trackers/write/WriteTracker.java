package com.koivel.runelite.plugin.tracker.trackers.write;

import net.runelite.api.Player;
import net.runelite.api.events.GameTick;
import net.runelite.client.eventbus.Subscribe;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.koivel.runelite.plugin.modal.KEventSeries;
import com.koivel.runelite.plugin.tracker.DataService;
import com.koivel.runelite.plugin.tracker.Tracker;

public class WriteTracker extends Tracker {

    private long lastWriteMs = System.currentTimeMillis();

    private final WriteHandler writeHandler = new WriteHandler(trackerService.getHttpClient(),
            () -> trackerService.getConfig());

    @Override
    public void start() {
        super.start();
    }

    @Subscribe
    public void onGameTick(GameTick gameTick) {
        long now = System.currentTimeMillis();
        if (now - lastWriteMs < TimeUnit.MINUTES.toMillis(1))
            return;

        synchronized (DataService.LOCK) {
            KEventSeries eventFrameGroup = DataService.getEventFrameGroup();
            eventFrameGroup.setAccountId("" + getClient().getAccountHash());

            Player player = getClient().getLocalPlayer();
            String playerName = player == null ? null : player.getName();
            eventFrameGroup.setAccountDisplayName(playerName);

            if (eventFrameGroup.getEvents().size() > 0) {
                try {
                    writeHandler.write(eventFrameGroup, new WriteCallbackHandler(writeHandler, eventFrameGroup));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            DataService.reset();
        }

        lastWriteMs = now;
    }

}
