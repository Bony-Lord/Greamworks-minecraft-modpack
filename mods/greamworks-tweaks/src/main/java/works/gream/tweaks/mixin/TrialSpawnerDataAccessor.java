/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.level.SpawnData
 *  net.minecraft.world.level.block.entity.trialspawner.TrialSpawnerData
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Accessor
 */
package works.gream.tweaks.mixin;

import java.util.Optional;
import net.minecraft.world.level.SpawnData;
import net.minecraft.world.level.block.entity.trialspawner.TrialSpawnerData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={TrialSpawnerData.class})
public interface TrialSpawnerDataAccessor {
    @Accessor(value="nextSpawnData")
    public Optional<SpawnData> greamworks$getNextSpawnData();
}

