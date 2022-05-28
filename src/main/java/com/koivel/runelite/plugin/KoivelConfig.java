package com.koivel.runelite.plugin;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("koivel-configs")
public interface KoivelConfig extends Config {
    @ConfigItem(keyName = "refreshKey", name = "API Key", description = "The private key to send data to your Koivel profile.", secret = true, position = 0)
    default String refreshKey() {
        return "";
    }

    @ConfigItem(keyName = "disabledTrackers", name = "Disabled Trackers", description = "Comma separated list of trackers to disable.", position = 1)
    default String disabledTrackers() {
        return "";
    }

    @ConfigItem(keyName = "devMode", name = "Local mode (testing only)", position = 2, description = "Sends data to localhost instead of server do not use unless you know what you are doing.")
    default boolean devMode() {
        return false;
    }
}
