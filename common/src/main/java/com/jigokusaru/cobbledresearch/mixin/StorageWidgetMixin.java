package com.jigokusaru.cobbledresearch.mixin;

import com.cobblemon.mod.common.client.gui.pc.StorageWidget;
import com.jigokusaru.cobbledresearch.util.PcAddonHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = StorageWidget.class, remap = false)
public abstract class StorageWidgetMixin {

    /**
     * This intercepts the click BEFORE the PC can do anything.
     * If Multi-Select is on, we tell the PC "Don't touch this click."
     */
    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void onMouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        if (PcAddonHandler.isMultiSelectActive()) {
            // We return false here so the StorageWidget thinks the click wasn't for it.
            // This prevents all native swapping and grabbing logic.
            cir.setReturnValue(false);
        }
    }
}