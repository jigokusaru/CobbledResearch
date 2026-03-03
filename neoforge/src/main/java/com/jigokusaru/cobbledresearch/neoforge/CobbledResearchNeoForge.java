package com.jigokusaru.cobbledresearch.neoforge;

import com.jigokusaru.cobbledresearch.CobbledResearch;
import net.neoforged.fml.common.Mod;

@Mod(CobbledResearch.MOD_ID)
public final class CobbledResearchNeoForge {
    public CobbledResearchNeoForge() {
        // Run our common setup.
        CobbledResearch.init();
    }
}
