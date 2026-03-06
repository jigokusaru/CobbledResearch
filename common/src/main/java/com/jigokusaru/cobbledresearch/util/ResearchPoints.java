package com.jigokusaru.cobbledresearch.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record ResearchPoints(long points) {
    public static final Codec<ResearchPoints> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.LONG.fieldOf("points").forGetter(ResearchPoints::points)
            ).apply(instance, ResearchPoints::new)
    );

    public static ResearchPoints empty() {
        return new ResearchPoints(0);
    }
}