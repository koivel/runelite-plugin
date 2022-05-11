package com.koivel.runelite.plugin.modal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class KEvent {
    private String groupId;
    private String type;
    private double value;

    private Double score;
    private KEventTags tags;
    private String displayText;
    private Long recordedAtEpochMs;
    private KEventLocation location;
    private Integer plane;

    public KEvent tag(int index, String value) {
        if (tags == null) {
            tags = new KEventTags();
        }

        switch (index) {
            case 0:
                tags.setTag0(value);
                break;
            case 1:
                tags.setTag1(value);
                break;
            case 2:
                tags.setTag2(value);
                break;
            case 3:
                tags.setTag3(value);
                break;
            case 4:
                tags.setTag4(value);
                break;
        }
        return this;
    }
}
