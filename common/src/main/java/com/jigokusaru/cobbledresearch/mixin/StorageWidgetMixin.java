package com.jigokusaru.cobbledresearch.mixin;

import com.cobblemon.mod.common.client.gui.pc.StorageWidget;
import com.jigokusaru.cobbledresearch.util.PcAddonHandler;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = StorageWidget.class, remap = false)
public abstract class StorageWidgetMixin {

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void onMouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        if (PcAddonHandler.isMultiSelectActive()) {
            // Restore: Let shift-clicks pass to detect Party selection
            if (Screen.hasShiftDown()) return;

            // Block default PC drag/swap behavior
            cir.setReturnValue(false);
        }
    }
}