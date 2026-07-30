/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.world.item.crafting.RecipeSerializer
 *  net.neoforged.bus.api.EventPriority
 *  net.neoforged.bus.api.IEventBus
 *  net.neoforged.fml.common.Mod
 *  net.neoforged.neoforge.common.NeoForge
 *  net.neoforged.neoforge.common.loot.IGlobalLootModifier
 *  net.neoforged.neoforge.registries.DeferredHolder
 *  net.neoforged.neoforge.registries.DeferredRegister
 *  net.neoforged.neoforge.registries.NeoForgeRegistries$Keys
 */
package works.gream.tweaks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import works.gream.tweaks.AbilityDamageScaling;
import works.gream.tweaks.CriticalChanceCombo;
import works.gream.tweaks.DungeonMobLootScaling;
import works.gream.tweaks.EquipmentBalance;
import works.gream.tweaks.LootrMimics;
import works.gream.tweaks.PowerLevelScaling;
import works.gream.tweaks.PowerLevelSmithingRecipe;
import works.gream.tweaks.UniversalChestGearLootModifier;
import works.gream.tweaks.VanillaBossLoot;

@Mod(value="greamworks_tweaks")
public final class GreamworksTweaks {
    public static final String MOD_ID = "greamworks_tweaks";
    private static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create((ResourceKey)Registries.RECIPE_SERIALIZER, (String)"greamworks_tweaks");
    private static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> LOOT_MODIFIERS = DeferredRegister.create((ResourceKey)NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, (String)"greamworks_tweaks");
    public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<UniversalChestGearLootModifier>> UNIVERSAL_CHEST_GEAR = LOOT_MODIFIERS.register("universal_chest_gear", () -> UniversalChestGearLootModifier.CODEC);
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<PowerLevelSmithingRecipe>> POWER_LEVEL_SMITHING = RECIPE_SERIALIZERS.register("power_level_smithing", PowerLevelSmithingRecipe.Serializer::new);

    public GreamworksTweaks(IEventBus modBus) {
        RECIPE_SERIALIZERS.register(modBus);
        LOOT_MODIFIERS.register(modBus);
        modBus.addListener(GreamworksTweaks::onCommonSetup);
        NeoForge.EVENT_BUS.addListener(EventPriority.HIGHEST, LootrMimics::onRightClickBlock);
        NeoForge.EVENT_BUS.addListener(EquipmentBalance::onItemAttributes);
        NeoForge.EVENT_BUS.addListener(EventPriority.LOWEST, PowerLevelScaling::onItemAttributes);
        NeoForge.EVENT_BUS.addListener(AbilityDamageScaling::onIncomingDamage);
        NeoForge.EVENT_BUS.addListener(CriticalChanceCombo::onDamagePost);
        NeoForge.EVENT_BUS.addListener(CriticalChanceCombo::onPlayerTick);
        NeoForge.EVENT_BUS.addListener(VanillaBossLoot::onLivingDrops);
        NeoForge.EVENT_BUS.addListener(EventPriority.LOWEST, DungeonMobLootScaling::onLivingDrops);
        NeoForge.EVENT_BUS.addListener(FluidStoneReactions::onNeighborNotify);
    }

    private static void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(FluidStoneReactions::register);
    }
}
