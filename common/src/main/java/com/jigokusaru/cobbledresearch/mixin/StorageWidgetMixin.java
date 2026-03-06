package com.jigokusaru.cobbledresearch.mixin;

import com.cobblemon.mod.common.api.storage.StorePosition;
import com.cobblemon.mod.common.api.storage.party.PartyPosition;
import com.cobblemon.mod.common.api.storage.pc.PCPosition;
import com.cobblemon.mod.common.client.gui.pc.StorageWidget;
import com.cobblemon.mod.common.net.messages.server.storage.party.ReleasePartyPokemonPacket;
import com.cobblemon.mod.common.net.messages.server.storage.pc.ReleasePCPokemonPacket;
import com.cobblemon.mod.common.CobblemonNetwork;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.jigokusaru.cobbledresearch.util.PcAddonHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.UUID;

@Mixin(value = StorageWidget.class, remap = false)
public abstract class StorageWidgetMixin {

    @Shadow public abstract void resetSelected();
    @Shadow private boolean displayConfirmRelease;
    @Shadow private StorePosition selectedPosition;

    /**
     * Forces the Release button to be visible if we have a multi-selection active.
     */
    @Inject(method = "canDeleteSelected", at = @At("HEAD"), cancellable = true)
    private void onCanDeleteSelected(CallbackInfoReturnable<Boolean> cir) {
        if (!PcAddonHandler.getSelectedPokemon().isEmpty()) {
            cir.setReturnValue(true);
        }
    }

    /**
     * Intercepts the click on the "Yes" confirmation button.
     * We provide a fake position if one isn't set, so the Kotlin 'return' check passes.
     */
    @Inject(method = "mouseClicked", at = @At("HEAD"))
    private void onPreMouseClicked(double pMouseX, double pMouseY, int pButton, CallbackInfoReturnable<Boolean> cir) {
        if (this.displayConfirmRelease && !PcAddonHandler.getSelectedPokemon().isEmpty()) {
            if (this.selectedPosition == null) {
                // Set a dummy position so the native 'val position = selectedPosition ?: return' check passes
                this.selectedPosition = new PartyPosition(0);
            }
        }
    }

    /**
     * Hijacks the actual data retrieval. When the 'Yes' button tries to get the Pokemon to release,
     * we trigger our mass-release loop and then tell the native code to stop.
     */
    @Inject(method = "getSelectedPokemon", at = @At("HEAD"), cancellable = true)
    private void onGetSelectedPokemon(CallbackInfoReturnable<Pokemon> cir) {
        if (!PcAddonHandler.getSelectedPokemon().isEmpty()) {
            performMassRelease();

            // Cleanup UI state
            this.displayConfirmRelease = false;
            this.resetSelected();

            // Return null to ensure the native packet-sending code (which only handles one) is skipped
            cir.setReturnValue(null);
        }
    }

    @Unique
    private void performMassRelease() {
        Map<UUID, Pokemon> selection = PcAddonHandler.getSelectedPokemon();
        String containerId = PcAddonHandler.getCurrentContainerId();

        for (Map.Entry<UUID, Pokemon> entry : selection.entrySet()) {
            UUID uuid = entry.getKey();
            int index = PcAddonHandler.getSelectionSourceIndex(uuid);

            if ("party".equals(containerId)) {
                CobblemonNetwork.sendToServer(new ReleasePartyPokemonPacket(uuid, new PartyPosition(index)));
            } else {
                int box = PcAddonHandler.getSelectionSourceBox(uuid);
                CobblemonNetwork.sendToServer(new ReleasePCPokemonPacket(uuid, new PCPosition(box, index)));
            }
        }
        // Selection is cleared after the loop
        PcAddonHandler.clear();
    }
}