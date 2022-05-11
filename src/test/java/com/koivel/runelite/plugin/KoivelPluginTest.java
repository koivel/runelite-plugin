package com.koivel.runelite.plugin;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class KoivelPluginTest {
    public static void main(String[] args) throws Exception {
        ExternalPluginManager.loadBuiltin(KoivelPlugin.class);
        RuneLite.main(args);
    }
}