/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.dungeon_difficulty.logic.EntityDifficultyScalable
 *  net.dungeon_difficulty.logic.ItemScaling
 *  net.dungeon_difficulty.logic.PatternMatching$LocationData
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.item.ItemEntity
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.Level
 *  net.neoforged.neoforge.event.entity.living.LivingDropsEvent
 */
package works.gream.tweaks;

import net.dungeon_difficulty.logic.EntityDifficultyScalable;
import net.dungeon_difficulty.logic.ItemScaling;
import net.dungeon_difficulty.logic.PatternMatching;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import works.gream.tweaks.PowerLevelLootOffset;

public final class DungeonMobLootScaling {
    private DungeonMobLootScaling() {
    }

    public static void onLivingDrops(LivingDropsEvent event) {
        EntityDifficultyScalable scalable;
        ServerLevel level;
        LivingEntity entity;
        block7: {
            block6: {
                entity = event.getEntity();
                Level level2 = entity.level();
                if (!(level2 instanceof ServerLevel)) break block6;
                level = (ServerLevel)level2;
                if (entity instanceof EntityDifficultyScalable && (scalable = (EntityDifficultyScalable)entity).getScalingLevel() > 0) break block7;
            }
            return;
        }
        PatternMatching.LocationData location = scalable.getScalingLocationData();
        for (ItemEntity drop : event.getDrops()) {
            ItemStack stack = drop.getItem();
            if (stack.isEmpty() || ItemScaling.isScaled((ItemStack)stack)) continue;
            if (location != null) {
                ItemScaling.scale((ItemStack)stack, (ServerLevel)level, (ResourceLocation)entity.getLootTable().location(), (PatternMatching.LocationData)location);
            } else {
                ItemScaling.scale((ItemStack)stack, (int)scalable.getScalingLevel());
            }
            PowerLevelLootOffset.apply(stack, entity.getRandom(), level, location);
        }
    }
}

