package com.koivel.runelite.plugin.modal;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class KEventWriteReq {
    private List<KEventSeries> eventSeries;
}
