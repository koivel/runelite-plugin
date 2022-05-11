package com.koivel.runelite.plugin;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("koivel-configs")
public interface KoivelConfig extends Config {
    @ConfigItem(
            keyName = "refreshKey",
            name = "Secret refresh key.",
            description = "The private key to send data to your Koivel profile,",
            secret = true
    )
    default String refreshKey() {
        return "";
    }

    @ConfigItem(
            keyName = "devMode",
            name = "Dev Mode",
            description = "Sends data to localhost instead of server do not use unless you know what you are doing."
    )
    default boolean devMode() {
        return false;
    }
}
