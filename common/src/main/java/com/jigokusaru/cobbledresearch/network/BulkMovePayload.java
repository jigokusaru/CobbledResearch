package com.jigokusaru.cobbledresearch.network;

import com.jigokusaru.cobbledresearch.CobbledResearch;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record BulkMovePayload(List<UUID> uuids, int targetBox) implements CustomPacketPayload {
    public static final Type<BulkMovePayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(CobbledResearch.MOD_ID, "bulk_move"));

    public static final StreamCodec<FriendlyByteBuf, BulkMovePayload> CODEC = StreamCodec.ofMember(
            BulkMovePayload::write,
            BulkMovePayload::read
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void write(FriendlyByteBuf buf) {
        // Explicitly using the instance method writeUUID(UUID) inside a lambda
        buf.writeCollection(uuids, (b, uuid) -> b.writeUUID(uuid));
        buf.writeInt(targetBox);
    }

    public static BulkMovePayload read(FriendlyByteBuf buf) {
        return new BulkMovePayload(
                // Explicitly using the instance method readUUID() inside a lambda
                buf.readCollection(ArrayList::new, b -> b.readUUID()),
                buf.readInt()
        );
    }
}