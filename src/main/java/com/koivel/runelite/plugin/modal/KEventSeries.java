package com.koivel.runelite.plugin.modal;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KEventSeries {
    private String collectionId;
    private String accountId;
    private String accountDisplayName;
    private List<KEvent> events;
}
