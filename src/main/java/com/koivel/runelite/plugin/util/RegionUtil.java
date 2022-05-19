package com.koivel.runelite.plugin.util;

import net.runelite.api.Client;
import java.util.Set;

public class RegionUtil {

    public static boolean isPlayerWithinMapRegion(Client client, Set<Integer> definedMapRegions) {
        int[] mapRegions = client.getMapRegions();
        for (int region : mapRegions) {
            if (definedMapRegions.contains(region)) {
                return true;
            }
        }
        return false;
    }
}