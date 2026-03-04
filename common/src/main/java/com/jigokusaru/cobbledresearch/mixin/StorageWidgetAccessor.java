package com.jigokusaru.cobbledresearch.mixin;

import com.cobblemon.mod.common.client.gui.pc.BoxStorageSlot;
import com.cobblemon.mod.common.client.gui.pc.PartyStorageSlot;
import com.cobblemon.mod.common.client.gui.pc.StorageWidget;
import com.cobblemon.mod.common.client.gui.pc.GrabbedStorageSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.ArrayList;

@Mixin(value = StorageWidget.class, remap = false)
public interface StorageWidgetAccessor {
    @Accessor("boxSlots")
    ArrayList<BoxStorageSlot> getBoxSlots();

    @Accessor("partySlots")
    ArrayList<PartyStorageSlot> getPartySlots();

    @Accessor("grabbedSlot")
    void setGrabbedSlot(GrabbedStorageSlot grabbedSlot);
}