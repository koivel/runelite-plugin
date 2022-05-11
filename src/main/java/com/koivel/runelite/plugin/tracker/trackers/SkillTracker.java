package com.koivel.runelite.plugin.tracker.trackers;

import net.runelite.api.GameState;
import net.runelite.api.Skill;
import net.runelite.api.events.GameTick;
import net.runelite.client.eventbus.Subscribe;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.koivel.runelite.plugin.modal.KEvent;
import com.koivel.runelite.plugin.tracker.DataService;
import com.koivel.runelite.plugin.tracker.Tracker;
import com.koivel.runelite.plugin.util.LocationUtil;

public class SkillTracker extends Tracker {

    private long lastWrite = 0;
    private Map<Skill, Integer> cache = new HashMap<>();

    @Subscribe
    public void onGameTick(GameTick gameTick) {
        if (getClient().getGameState() != GameState.LOGGED_IN)
            return;

        long now = System.currentTimeMillis();
        if (now - lastWrite < TimeUnit.SECONDS.toMillis(15))
            return;
        lastWrite = now;

        for (Skill skill : Skill.values()) {
            int currentXp = getClient().getSkillExperience(skill);
            int previousXp = cache.getOrDefault(skill, 0);

            if (currentXp == previousXp)
                continue;
            cache.put(skill, currentXp);
            
            KEvent frame = KEvent.builder()
                    .type("ValueCurrent")
                    .groupId("total_xp")
                    .value(getClient().getSkillExperience(skill))
                    .recordedAtEpochMs(now)
                    .build()
                    .tag(0, skill.getName().toLowerCase(Locale.ROOT));
            LocationUtil.injectLocation(getClient(), frame);

            DataService.addFrame(frame);
        }
    }
}
