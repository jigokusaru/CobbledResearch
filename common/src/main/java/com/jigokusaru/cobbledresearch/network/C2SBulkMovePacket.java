package com.jigokusaru.cobbledresearch.network;

import dev.architectury.networking.NetworkManager;
import java.util.List;
import java.util.UUID;

public class C2SBulkMovePacket {

    public static void send(List<UUID> uuids, int targetBox) {
        // Now we just send the Payload object!
        NetworkManager.sendToServer(new BulkMovePayload(uuids, targetBox));
    }

    public static void register() {
        // Register the Sided Receiver using the Payload system
        NetworkManager.registerReceiver(NetworkManager.Side.C2S, BulkMovePayload.TYPE, BulkMovePayload.CODEC, (payload, context) -> {
            List<UUID> uuids = payload.uuids();
            int targetBox = payload.targetBox();

            context.queue(() -> {
                // Your server logic here
                System.out.println("Processing move for " + uuids.size() + " pokemon.");
            });
        });
    }
}