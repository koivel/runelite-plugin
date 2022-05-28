package com.koivel.runelite.plugin.tracker.trackers;

import net.runelite.api.Actor;
import net.runelite.api.Hitsplat;
import net.runelite.api.NPC;
import net.runelite.api.events.ActorDeath;
import net.runelite.api.events.HitsplatApplied;
import net.runelite.api.events.NpcDespawned;
import net.runelite.client.eventbus.Subscribe;

import java.util.HashSet;
import java.util.Set;

import com.koivel.runelite.plugin.modal.KEvent;
import com.koivel.runelite.plugin.tracker.Tracker;

public class KcTracker extends Tracker {
    private final Set<NPC> taggedNpcs = new HashSet<>();

    @Subscribe
    public void onHitsplatApplied(HitsplatApplied hitsplatApplied) {
        Actor actor = hitsplatApplied.getActor();
        if (actor instanceof NPC
                && hitsplatApplied.getHitsplat().getHitsplatType() == Hitsplat.HitsplatType.DAMAGE_ME) {
            taggedNpcs.add((NPC) actor);
        }
    }

    @Subscribe
    public void onActorDeath(ActorDeath actorDeath) {
        Actor actor = actorDeath.getActor();
        if (actor instanceof NPC && taggedNpcs.contains(actor)) {
            KEvent event = KEvent.builder()
                    .recordedAtEpochMs(System.currentTimeMillis())
                    .groupId("npc-kc").build()
                    .tag("npcName", actor.getName())
                    .value("value", 1);
            trackEvent(event);
        }
    }

    @Subscribe
    public void onNpcDespawned(NpcDespawned npcDespawned) {
        taggedNpcs.remove(npcDespawned.getNpc());
    }

    public String getName() {
        return "kc-tracker";
    }
}
