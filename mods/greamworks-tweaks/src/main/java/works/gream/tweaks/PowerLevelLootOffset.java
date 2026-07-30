/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.dungeon_difficulty.logic.Difficulty
 *  net.dungeon_difficulty.logic.ItemScaling
 *  net.dungeon_difficulty.logic.PatternMatching
 *  net.dungeon_difficulty.logic.PatternMatching$LocationData
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.item.ItemStack
 */
package works.gream.tweaks;

import net.dungeon_difficulty.logic.Difficulty;
import net.dungeon_difficulty.logic.ItemScaling;
import net.dungeon_difficulty.logic.PatternMatching;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;

public final class PowerLevelLootOffset {
    private static final int DEFAULT_SHIFT = -2;
    private static final int HEROIC_SHIFT = 7;

    private PowerLevelLootOffset() {
    }

    public static void apply(ItemStack stack, RandomSource random, ServerLevel level, PatternMatching.LocationData location) {
        int generatedLevel = ItemScaling.getScaleFactor((ItemStack)stack);
        if (generatedLevel <= 0) {
            return;
        }
        int shift = PowerLevelLootOffset.isHeroic(level, location) ? 7 : -2;
        int targetLevel = Math.max(1, generatedLevel + shift + random.nextInt(5) - 2);
        if (targetLevel != generatedLevel) {
            ItemScaling.rescale((ItemStack)stack, (int)targetLevel);
        }
    }

    private static boolean isHeroic(ServerLevel level, PatternMatching.LocationData location) {
        if (location == null) {
            return false;
        }
        Difficulty difficulty = PatternMatching.getDifficulty((PatternMatching.LocationData)location, (ServerLevel)level);
        return difficulty != null && difficulty.type() != null && "heroic".equals(difficulty.type().name);
    }
}

