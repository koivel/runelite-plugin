package com.koivel.runelite.plugin.modal;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@Builder
@ToString
public class KEvent {
    private String groupId;

    private Map<String, String> tags;
    private Map<String, Number> values;

    private Long recordedAtEpochMs;
    private KEventLocation location;
    private Integer plane;

    public KEvent tag(String key, String value) {
        if (tags == null) {
            tags = new HashMap<>();
        }
        tags.put(key, value);
        return this;
    }

    public KEvent value(String key, double value) {
        if (values == null) {
            values = new HashMap<>();
        }
        values.put(key, value);
        return this;
    }
}
