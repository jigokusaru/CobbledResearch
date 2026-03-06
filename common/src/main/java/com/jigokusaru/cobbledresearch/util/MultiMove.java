package com.jigokusaru.cobbledresearch.util;

import java.util.UUID;

/**
 * A simple data holder for multi-drag operations to avoid Mixin inner-class constraints.
 */
public record MultiMove(UUID uuid, int srcBox, int srcIdx, int destIdx) {}