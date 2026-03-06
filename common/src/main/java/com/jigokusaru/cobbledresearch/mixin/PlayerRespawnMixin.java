package com.jigokusaru.cobbledresearch.mixin;

import com.jigokusaru.cobbledresearch.util.ResearchPointsProvider;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class PlayerRespawnMixin {
    @Inject(method = "restoreFrom", at = @At("TAIL"))
    private void onRestore(ServerPlayer oldPlayer, boolean alive, CallbackInfo ci) {
        long oldPoints = ResearchPointsProvider.from(oldPlayer).cobbledresearch$getPoints();
        ResearchPointsProvider.from((ServerPlayer) (Object) this).cobbledresearch$setPoints(oldPoints);
    }
}