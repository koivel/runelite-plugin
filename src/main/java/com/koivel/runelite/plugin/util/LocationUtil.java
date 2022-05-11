package com.koivel.runelite.plugin.util;

import com.koivel.runelite.plugin.modal.KEvent;
import com.koivel.runelite.plugin.modal.KEventLocation;

import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.api.coords.WorldPoint;

public class LocationUtil {

    public static void injectLocation(Client client, KEvent frame) {
        Player localPlayer = client.getLocalPlayer();
        if(localPlayer == null) return;
        WorldPoint worldLocation = localPlayer.getWorldLocation();
        if(worldLocation == null) return;

        int x = worldLocation.getX();
        int y = worldLocation.getY();

        frame.setLocation(new KEventLocation(x, y));
        frame.setPlane(worldLocation.getPlane());
    }
}
