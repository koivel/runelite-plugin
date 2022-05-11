package com.koivel.runelite.plugin.tracker.trackers.write;

import com.google.gson.Gson;
import com.koivel.runelite.plugin.KoivelConfig;
import com.koivel.runelite.plugin.modal.KEventSeries;
import com.koivel.runelite.plugin.modal.KEventWriteReq;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.function.Supplier;

@Slf4j
public class WriteHandler {

    private final MediaType json = MediaType.parse("application/json; charset=utf-8");
    private final OkHttpClient httpClient = new OkHttpClient();
    private final Gson gson = new Gson();

    private Supplier<KoivelConfig> configSupplier;

    public WriteHandler(Supplier<KoivelConfig> configSupplier) {
        this.configSupplier = configSupplier;
    }

    public void write(KEventSeries eventGroup, WriteCallbackHandler callback) throws IOException {
        KEventWriteReq reqBody = new KEventWriteReq(Arrays.asList(eventGroup));
        RequestBody bodyJson = RequestBody.create(
                json,
                gson.toJson(reqBody));

        Request request = new Request.Builder()
                .url(getHost() + "/event-api/events/write")
                .addHeader("Authorization", "Bearer " + configSupplier.get().refreshKey())
                .post(bodyJson)
                .build();

        httpClient.newCall(request).enqueue(callback);
    }

    private String getHost() {
        return configSupplier.get().devMode() ? "http://localhost:3333/api/v1" : "https://api.koivel.com/api/v1";
    }

}
