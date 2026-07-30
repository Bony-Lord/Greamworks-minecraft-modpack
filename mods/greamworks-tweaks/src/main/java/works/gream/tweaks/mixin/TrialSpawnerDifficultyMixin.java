/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.dungeon_difficulty.config.Config$SpawnerModifier
 *  net.dungeon_difficulty.logic.Difficulty
 *  net.dungeon_difficulty.logic.ItemScaling
 *  net.dungeon_difficulty.logic.PatternMatching
 *  net.dungeon_difficulty.logic.PatternMatching$EntityData
 *  net.dungeon_difficulty.logic.PatternMatching$LocationData
 *  net.dungeon_difficulty.logic.PatternMatching$SpawnerScaleResult
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Holder
 *  net.minecraft.core.registries.BuiltInRegistries
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.monster.Enemy
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.SpawnData
 *  net.minecraft.world.level.block.entity.trialspawner.TrialSpawner
 *  net.minecraft.world.level.block.entity.trialspawner.TrialSpawnerConfig
 *  net.minecraft.world.level.block.entity.trialspawner.TrialSpawnerData
 *  net.minecraft.world.level.storage.loot.LootTable
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.ModifyArg
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package works.gream.tweaks.mixin;

import java.util.IdentityHashMap;
import java.util.Map;
import net.dungeon_difficulty.config.Config;
import net.dungeon_difficulty.logic.Difficulty;
import net.dungeon_difficulty.logic.ItemScaling;
import net.dungeon_difficulty.logic.PatternMatching;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SpawnData;
import net.minecraft.world.level.block.entity.trialspawner.TrialSpawner;
import net.minecraft.world.level.block.entity.trialspawner.TrialSpawnerConfig;
import net.minecraft.world.level.block.entity.trialspawner.TrialSpawnerData;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import works.gream.tweaks.PowerLevelLootOffset;
import works.gream.tweaks.mixin.TrialSpawnerDataAccessor;

@Mixin(value={TrialSpawner.class})
public abstract class TrialSpawnerDifficultyMixin {
    @Shadow
    @Final
    private TrialSpawnerData data;
    @Unique
    private final Map<TrialSpawnerConfig, ScaledSettings> greamworks$scaledConfigs = new IdentityHashMap<TrialSpawnerConfig, ScaledSettings>();
    @Unique
    private TrialSpawnerConfig greamworks$activeBaseConfig;
    @Unique
    private ScaledSettings greamworks$activeSettings;
    @Unique
    private ServerLevel greamworks$rewardLevel;
    @Unique
    private PatternMatching.LocationData greamworks$rewardLocation;
    @Unique
    private ResourceLocation greamworks$rewardLootTable;

    @Inject(method={"tickServer"}, at={@At(value="HEAD")})
    private void greamworks$beginScaledTick(ServerLevel level, BlockPos pos, boolean ominous, CallbackInfo ci) {
        TrialSpawnerConfig base;
        TrialSpawner spawner = (TrialSpawner)(Object)this;
        this.greamworks$activeBaseConfig = base = spawner.getConfig();
        this.greamworks$activeSettings = this.greamworks$scaledConfigs.computeIfAbsent(base, ignored -> this.greamworks$scaleConfig(base, spawner.getRequiredPlayerRange(), level, pos));
    }

    @Inject(method={"tickServer"}, at={@At(value="RETURN")})
    private void greamworks$endScaledTick(ServerLevel level, BlockPos pos, boolean ominous, CallbackInfo ci) {
        this.greamworks$activeBaseConfig = null;
        this.greamworks$activeSettings = null;
    }

    @Inject(method={"getConfig"}, at={@At(value="RETURN")}, cancellable=true)
    private void greamworks$useScaledConfig(CallbackInfoReturnable<TrialSpawnerConfig> cir) {
        if (cir.getReturnValue() == this.greamworks$activeBaseConfig && this.greamworks$activeSettings != null) {
            cir.setReturnValue(this.greamworks$activeSettings.config());
        }
    }

    @Inject(method={"getRequiredPlayerRange"}, at={@At(value="RETURN")}, cancellable=true)
    private void greamworks$useScaledPlayerRange(CallbackInfoReturnable<Integer> cir) {
        if (this.greamworks$activeSettings != null) {
            cir.setReturnValue(this.greamworks$activeSettings.requiredPlayerRange());
        }
    }

    @Unique
    private ScaledSettings greamworks$scaleConfig(TrialSpawnerConfig base, int baseRequiredPlayerRange, ServerLevel level, BlockPos pos) {
        PatternMatching.EntityData entityData = this.greamworks$entityData(level);
        PatternMatching.SpawnerScaleResult result = PatternMatching.getModifiersForSpawner((PatternMatching.LocationData)PatternMatching.LocationData.create((ServerLevel)level, (BlockPos)pos), (PatternMatching.EntityData)entityData, (ServerLevel)level);
        if (result.level() <= 0 || result.modifiers().isEmpty()) {
            return new ScaledSettings(base, baseRequiredPlayerRange);
        }
        float totalMobs = 0.0f;
        float spawnDelay = 0.0f;
        float requiredPlayerRange = 0.0f;
        for (Config.SpawnerModifier modifier : result.modifiers()) {
            totalMobs += (float)result.level() * modifier.spawn_count_multiplier;
            spawnDelay += (float)result.level() * (modifier.min_spawn_delay_multiplier + modifier.max_spawn_delay_multiplier) * 0.5f;
            requiredPlayerRange += (float)result.level() * modifier.required_player_range_multiplier;
        }
        TrialSpawnerConfig scaledConfig = new TrialSpawnerConfig(base.spawnRange(), TrialSpawnerDifficultyMixin.greamworks$clamp(base.totalMobs() * (1.0f + totalMobs), 1.0f, 64.0f), base.simultaneousMobs(), TrialSpawnerDifficultyMixin.greamworks$clamp(base.totalMobsAddedPerPlayer() * (1.0f + totalMobs), 0.0f, 16.0f), base.simultaneousMobsAddedPerPlayer(), TrialSpawnerDifficultyMixin.greamworks$clamp(Math.round((float)base.ticksBetweenSpawn() * (1.0f + spawnDelay)), 10, 1200), base.spawnPotentialsDefinition(), base.lootTablesToEject(), base.itemsToDropWhenOminous());
        int scaledPlayerRange = TrialSpawnerDifficultyMixin.greamworks$clamp(Math.round((float)baseRequiredPlayerRange * (1.0f + requiredPlayerRange)), 1, 128);
        return new ScaledSettings(scaledConfig, scaledPlayerRange);
    }

    @Inject(method={"ejectReward"}, at={@At(value="HEAD")})
    private void greamworks$beginRewardScaling(ServerLevel level, BlockPos pos, ResourceKey<LootTable> lootTable, CallbackInfo ci) {
        this.greamworks$rewardLevel = level;
        this.greamworks$rewardLocation = PatternMatching.LocationData.create((ServerLevel)level, (BlockPos)pos);
        this.greamworks$rewardLootTable = lootTable.location();
    }

    @ModifyArg(method={"ejectReward"}, at=@At(value="INVOKE", target="Lnet/minecraft/core/dispenser/DefaultDispenseItemBehavior;spawnItem(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;ILnet/minecraft/core/Direction;Lnet/minecraft/world/phys/Position;)V"), index=1)
    private ItemStack greamworks$scaleReward(ItemStack original) {
        if (this.greamworks$rewardLevel == null || this.greamworks$rewardLocation == null || original.isEmpty()) {
            return original;
        }
        ItemStack scaled = original.copy();
        ItemScaling.scale((ItemStack)scaled, (ServerLevel)this.greamworks$rewardLevel, (ResourceLocation)this.greamworks$rewardLootTable, (PatternMatching.LocationData)this.greamworks$rewardLocation);
        PowerLevelLootOffset.apply(scaled, this.greamworks$rewardLevel.random, this.greamworks$rewardLevel, this.greamworks$rewardLocation);
        Difficulty difficulty = PatternMatching.getDifficulty((PatternMatching.LocationData)this.greamworks$rewardLocation, (ServerLevel)this.greamworks$rewardLevel);
        if (difficulty != null && difficulty.isValid() && difficulty.allowsLootScaling() && scaled.getMaxStackSize() > 1) {
            float bonus = Math.min(0.2f, (float)Math.max(0, difficulty.rewardLevel()) * 6.5E-4f);
            float expectedExtra = (float)scaled.getCount() * bonus;
            int extra = (int)expectedExtra;
            if (this.greamworks$rewardLevel.random.nextFloat() < expectedExtra - (float)extra) {
                ++extra;
            }
            scaled.grow(Math.min(extra, scaled.getMaxStackSize() - scaled.getCount()));
        }
        return scaled;
    }

    @Inject(method={"ejectReward"}, at={@At(value="RETURN")})
    private void greamworks$endRewardScaling(ServerLevel level, BlockPos pos, ResourceKey<LootTable> lootTable, CallbackInfo ci) {
        this.greamworks$rewardLevel = null;
        this.greamworks$rewardLocation = null;
        this.greamworks$rewardLootTable = null;
    }

    @Unique
    private PatternMatching.EntityData greamworks$entityData(ServerLevel level) {
        SpawnData spawnData = ((TrialSpawnerDataAccessor)this.data).greamworks$getNextSpawnData().orElse(null);
        if (spawnData == null) {
            return new PatternMatching.EntityData(null, false);
        }
        ResourceLocation id = ResourceLocation.tryParse((String)spawnData.getEntityToSpawn().getString("id"));
        Holder holder = id == null ? null : (Holder)BuiltInRegistries.ENTITY_TYPE.getHolder(id).orElse(null);
        boolean hostile = false;
        if (holder != null) {
            Entity probe = ((EntityType)holder.value()).create((Level)level);
            hostile = probe instanceof Enemy;
        }
        return new PatternMatching.EntityData(holder, hostile);
    }

    @Unique
    private static int greamworks$clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    @Unique
    private static float greamworks$clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }

    @Unique
    private record ScaledSettings(TrialSpawnerConfig config, int requiredPlayerRange) {
    }
}
