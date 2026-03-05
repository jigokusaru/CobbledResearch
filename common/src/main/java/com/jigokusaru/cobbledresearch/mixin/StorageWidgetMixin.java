package com.jigokusaru.cobbledresearch.mixin;

import com.cobblemon.mod.common.client.gui.pc.StorageWidget;
import com.cobblemon.mod.common.net.messages.server.storage.pc.ReleasePCPokemonPacket;
import com.cobblemon.mod.common.CobblemonNetwork;
import com.jigokusaru.cobbledresearch.util.PcAddonHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(value = StorageWidget.class, remap = false)
public abstract class StorageWidgetMixin {

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void onMouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        if (PcAddonHandler.isMultiSelectActive()) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "canDeleteSelected", at = @At("HEAD"), cancellable = true)
    private void onCanDeleteSelected(CallbackInfoReturnable<Boolean> cir) {
        if (PcAddonHandler.isMultiSelectActive() && !PcAddonHandler.getSelectedPokemon().isEmpty()) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "resetSelected", at = @At("HEAD"))
    private void onResetSelected(CallbackInfo ci) {
        if (PcAddonHandler.isMultiSelectActive() && !PcAddonHandler.getSelectedPokemon().isEmpty()) {
            for (UUID uuid : PcAddonHandler.getSelectedPokemon().keySet()) {
                CobblemonNetwork.sendToServer(new ReleasePCPokemonPacket(uuid, null));
            }
            PcAddonHandler.clear();
        }
    }
}