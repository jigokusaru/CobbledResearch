package com.jigokusaru.cobbledresearch.registration;

import com.jigokusaru.cobbledresearch.util.ResearchPoints;
import dev.architectury.registry.registries.AttachmentRegistry;
import dev.architectury.registry.registries.AttachmentType;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public class ModAttachments {
    // In 1.21, we use AttachmentRegistry.create()
    public static final Supplier<AttachmentType<ResearchPoints>> POINTS = AttachmentRegistry.create(
            ResourceLocation.fromNamespaceAndPath("cobbledresearch", "points"),
            builder -> builder.initializer(ResearchPoints::empty).persistent(ResearchPoints.CODEC).copyOnDeath()
    );

    public static void init() {
        // This just triggers the static block above
    }
}