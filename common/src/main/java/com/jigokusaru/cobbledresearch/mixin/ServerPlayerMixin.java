package com.jigokusaru.cobbledresearch.mixin;

import com.jigokusaru.cobbledresearch.util.ResearchPointsProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class ServerPlayerMixin implements ResearchPointsProvider {
    @Unique
    private long cobbledresearch$points = 0;

    @Override
    public long cobbledresearch$getPoints() {
        return this.cobbledresearch$points;
    }

    @Override
    public void cobbledresearch$setPoints(long points) {
        this.cobbledresearch$points = Math.max(0, points);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void onSave(CompoundTag nbt, CallbackInfo ci) {
        nbt.putLong("CobbledResearchPoints", this.cobbledresearch$points);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void onLoad(CompoundTag nbt, CallbackInfo ci) {
        if (nbt.contains("CobbledResearchPoints")) {
            this.cobbledresearch$points = nbt.getLong("CobbledResearchPoints");
        }
    }
}