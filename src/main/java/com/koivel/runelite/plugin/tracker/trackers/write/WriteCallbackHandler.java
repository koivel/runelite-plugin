package com.koivel.runelite.plugin.tracker.trackers.write;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import java.io.IOException;

import com.koivel.runelite.plugin.modal.KEventSeries;

@Slf4j
public class WriteCallbackHandler implements Callback {

    private final WriteHandler handler;
    private final KEventSeries eventFrameGroup;
    private int retries = 0;

    public WriteCallbackHandler(WriteHandler handler, KEventSeries eventFrameGroup) {
        this.handler = handler;
        this.eventFrameGroup = eventFrameGroup;
    }

    @Override
    public void onFailure(Call call, IOException e) {
        log.error("Failed to write Koivel events.", e);
    }

    @SneakyThrows
    @Override
    public void onResponse(Call call, Response response) throws IOException {
        log.debug("Got write response {} on attempt {}", response.code(), retries);
        retries++;

        try  {
            int responseCode = response.code();

            if (responseCode != 200) {
                String message = response.message();
                System.out.println(message);
                if (retries <= 3) {
                    handler.write(eventFrameGroup, this);
                    Thread.sleep(retries * 1000L);
                }
            }
        } finally {
            response.close();
        }
    }
}
