package com.jigokusaru.cobbledresearch.mixin;

import com.cobblemon.mod.common.client.gui.pc.StorageSlot;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.jigokusaru.cobbledresearch.util.PcAddonHandler;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = StorageSlot.class, remap = false)
public abstract class StorageSlotMixin {
    @Shadow public abstract Pokemon getPokemon();

    // Changed target from "render" to "renderWidget" based on your provided source
    @Inject(method = "renderWidget", at = @At("HEAD"), cancellable = true)
    private void onRenderWidget(GuiGraphics context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        Pokemon p = getPokemon();
        // If this specific Pokemon is being dragged by our Multi-Select, stop the render here
        if (p != null && PcAddonHandler.isBeingDragged(p.getUuid())) {
            ci.cancel();
        }
    }
}