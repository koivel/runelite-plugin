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
import com.koivel.runelite.plugin.tracker.Tracker;

public class SkillTracker extends Tracker {

    private long lastWrite = 0;

    private long cacheAccountHash = 0;
    private Map<Skill, Integer> xpCache = new HashMap<>();
    private Map<Skill, Integer> levelCache = new HashMap<>();

    private void executeSkillCheck() {
        long now = System.currentTimeMillis();
        for (Skill skill : Skill.values()) {
            int currentXp = getClient().getSkillExperience(skill);
            int previousXp = xpCache.getOrDefault(skill, 0);
            if (currentXp > previousXp) {
                xpCache.put(skill, currentXp);
                KEvent event = KEvent.builder()
                        .type("ValueCurrent")
                        .groupId("total_xp")
                        .value(currentXp)
                        .recordedAtEpochMs(now)
                        .build()
                        .tag(0, skill.getName().toLowerCase(Locale.ROOT));
                this.trackEvent(event);
            }

            int currentLevel = getClient().getRealSkillLevel(skill);
            int previousLevel = levelCache.getOrDefault(skill, skill == Skill.HITPOINTS ? 10 : 1);
            if (currentLevel > previousLevel) {
                levelCache.put(skill, currentXp);
                KEvent event = KEvent.builder()
                        .type("ValueCurrent")
                        .groupId("total_level")
                        .value(currentLevel)
                        .recordedAtEpochMs(now)
                        .build()
                        .tag(0, skill.getName().toLowerCase(Locale.ROOT));
                this.trackEvent(event);
            }
        }
    }

    @Subscribe
    public void onGameTick(GameTick gameTick) {
        if (getClient().getGameState() != GameState.LOGGED_IN) {
            return;
        }

        long now = System.currentTimeMillis();
        if (now - lastWrite < TimeUnit.SECONDS.toMillis(15))
            return;
        lastWrite = now;

        long accountHash = getClient().getAccountHash();
        if (cacheAccountHash != accountHash) {
            xpCache.clear();
            levelCache.clear();
        }
        this.cacheAccountHash = accountHash;
        executeSkillCheck();
    }
}
