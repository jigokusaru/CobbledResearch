package com.jigokusaru.cobbledresearch.mixin;

import com.cobblemon.mod.common.client.gui.pc.StorageSlot;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.jigokusaru.cobbledresearch.util.PcAddonHandler;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = StorageSlot.class, remap = false)
public abstract class StorageSlotMixin {

    @Inject(method = "renderWidget", at = @At("HEAD"), cancellable = true)
    private void onRenderWidget(GuiGraphics context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        StorageSlot slot = (StorageSlot) (Object) this;
        Pokemon p = slot.getPokemon();

        // Hide the sprite if it's currently being multi-dragged
        if (p != null && PcAddonHandler.isBeingDragged(p.getUuid())) {
            ci.cancel();
        }
    }
}