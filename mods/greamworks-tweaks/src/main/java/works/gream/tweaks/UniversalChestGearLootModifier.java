/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.MapCodec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 *  com.mojang.serialization.codecs.RecordCodecBuilder$Instance
 *  it.unimi.dsi.fastutil.objects.ObjectArrayList
 *  net.dungeon_difficulty.logic.ItemScaling
 *  net.dungeon_difficulty.logic.PatternMatching$LocationData
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Position
 *  net.minecraft.core.registries.BuiltInRegistries
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Items
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.storage.loot.LootContext
 *  net.minecraft.world.level.storage.loot.parameters.LootContextParams
 *  net.minecraft.world.level.storage.loot.predicates.LootItemCondition
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.neoforge.common.loot.IGlobalLootModifier
 *  net.neoforged.neoforge.common.loot.LootModifier
 */
package works.gream.tweaks;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import net.dungeon_difficulty.logic.ItemScaling;
import net.dungeon_difficulty.logic.PatternMatching;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import works.gream.tweaks.PowerLevelLootOffset;

public final class UniversalChestGearLootModifier
extends LootModifier {
    public static final MapCodec<UniversalChestGearLootModifier> CODEC = RecordCodecBuilder.mapCodec(
            instance -> codecStart(instance).apply(instance, UniversalChestGearLootModifier::new)
    );
    private static final List<String> LOW_TIER = List.of("minecraft:leather_helmet", "minecraft:leather_chestplate", "minecraft:leather_leggings", "minecraft:leather_boots", "minecraft:wooden_sword", "minecraft:wooden_axe", "minecraft:wooden_pickaxe", "minecraft:wooden_shovel", "minecraft:wooden_hoe", "minecraft:bow");
    private static final List<String> COPPER_TIER = List.of("minecraft:chainmail_helmet", "minecraft:chainmail_chestplate", "minecraft:chainmail_leggings", "minecraft:chainmail_boots", "minecraft:golden_helmet", "minecraft:golden_chestplate", "minecraft:golden_leggings", "minecraft:golden_boots", "minecraft:golden_sword", "minecraft:golden_axe", "minecraft:golden_pickaxe", "minecraft:golden_shovel", "minecraft:golden_hoe", "minecraft:stone_sword", "minecraft:stone_axe", "minecraft:stone_pickaxe", "minecraft:stone_shovel", "minecraft:stone_hoe", "minecraft:crossbow", "minecraft:shield", "create_sa:copper_helmet", "create_sa:copper_chestplate", "create_sa:copper_leggings", "create_sa:copper_boots", "create_sa:copper_sword", "create_sa:copper_axe", "create_sa:copper_pickaxe", "create_sa:copper_shovel", "create_sa:copper_hoe");
    private static final List<String> IRON_TIER = List.of("minecraft:iron_helmet", "minecraft:iron_chestplate", "minecraft:iron_leggings", "minecraft:iron_boots", "minecraft:iron_sword", "minecraft:iron_axe", "minecraft:iron_pickaxe", "minecraft:iron_shovel", "minecraft:iron_hoe", "create_sa:zinc_helmet", "create_sa:zinc_chestplate", "create_sa:zinc_leggings", "create_sa:zinc_boots", "create_sa:zinc_sword", "create_sa:zinc_axe", "create_sa:zinc_pickaxe", "create_sa:zinc_shovel", "create_sa:zinc_hoe");
    private static final List<String> DIAMOND_TIER = List.of("minecraft:diamond_helmet", "minecraft:diamond_chestplate", "minecraft:diamond_leggings", "minecraft:diamond_boots", "minecraft:diamond_sword", "minecraft:diamond_axe", "minecraft:diamond_pickaxe", "minecraft:diamond_shovel", "minecraft:diamond_hoe");
    private static final List<SupplyEntry> COMMON_SUPPLIES = List.of(
        new SupplyEntry("minecraft:torch", 2, 8),
        new SupplyEntry("minecraft:arrow", 2, 8),
        new SupplyEntry("minecraft:coal", 1, 4),
        new SupplyEntry("minecraft:string", 1, 4),
        new SupplyEntry("minecraft:bone", 1, 4),
        new SupplyEntry("minecraft:leather", 1, 3),
        new SupplyEntry("minecraft:flint", 1, 3),
        new SupplyEntry("minecraft:paper", 1, 4),
        new SupplyEntry("minecraft:stick", 2, 6),
        new SupplyEntry("minecraft:glass_bottle", 1, 3),
        new SupplyEntry("minecraft:sweet_berries", 2, 6),
        new SupplyEntry("minecraft:glow_berries", 1, 4),
        new SupplyEntry("minecraft:apple", 1, 2),
        new SupplyEntry("minecraft:raw_chicken", 1, 2),
        new SupplyEntry("minecraft:raw_cod", 1, 3),
        new SupplyEntry("minecraft:raw_salmon", 1, 2),
        new SupplyEntry("minecraft:potato", 1, 4),
        new SupplyEntry("minecraft:dried_kelp", 2, 6),
        new SupplyEntry("minecraft:rotten_flesh", 2, 5),
        new SupplyEntry("minecraft:gunpowder", 1, 2),
        new SupplyEntry("minecraft:iron_nugget", 2, 5),
        new SupplyEntry("minecraft:copper_ingot", 1, 2),
        new SupplyEntry("minecraft:raw_copper", 1, 2),
        new SupplyEntry("minecraft:redstone", 1, 3),
        new SupplyEntry("minecraft:lapis_lazuli", 1, 3),
        new SupplyEntry("create:andesite_alloy", 1, 2),
        new SupplyEntry("create:zinc_nugget", 1, 3),
        new SupplyEntry("create:shaft", 1, 2),
        new SupplyEntry("create:cogwheel", 1, 1),
        new SupplyEntry("farmersdelight:rope", 1, 3),
        new SupplyEntry("farmersdelight:canvas", 1, 2),
        new SupplyEntry("silentgear:template_board", 1, 3)
    );
    private static final List<String> FIELD_GEAR = List.of(
        "minecraft:stone_sword",
        "minecraft:stone_axe",
        "minecraft:stone_pickaxe",
        "minecraft:stone_shovel",
        "minecraft:stone_hoe",
        "sticknstone:wooden_longsword",
        "sticknstone:wooden_rapier",
        "sticknstone:wooden_spear",
        "sticknstone:wooden_cutlass",
        "sticknstone:wooden_chakram",
        "sticknstone:stone_longsword",
        "sticknstone:stone_rapier",
        "sticknstone:stone_spear",
        "sticknstone:stone_glaive",
        "sticknstone:stone_cutlass",
        "sticknstone:stone_chakram"
    );
    private static final List<String> SILENT_TEMPLATES = List.of(
        "silentgear:sword_template",
        "silentgear:pickaxe_template",
        "silentgear:axe_template",
        "silentgear:shovel_template",
        "silentgear:knife_template",
        "silentgear:dagger_template",
        "silentgear:spear_template",
        "silentgear:shield_template"
    );

    private UniversalChestGearLootModifier(LootItemCondition[] conditions) {
        super(conditions);
    }

    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        List<String> tier;
        float roll;
        String path = context.getQueriedLootTableId().getPath();
        if (!path.startsWith("chests/") && !path.contains("/chests/")) {
            return generatedLoot;
        }
        if (context.getRandom().nextFloat() < 0.6f) {
            UniversalChestGearLootModifier.addRandomSupply(generatedLoot, context);
        }
        if (context.getRandom().nextFloat() < 0.22f) {
            UniversalChestGearLootModifier.addUnscaledItem(generatedLoot, context, FIELD_GEAR, 1, 1);
        }
        if (context.getRandom().nextFloat() < 0.08f) {
            UniversalChestGearLootModifier.addUnscaledItem(generatedLoot, context, SILENT_TEMPLATES, 1, 1);
        }
        tier = (roll = context.getRandom().nextFloat()) < 0.01f ? DIAMOND_TIER : (roll < 0.06f ? IRON_TIER : (roll < 0.16f ? COPPER_TIER : (roll < 0.31f ? LOW_TIER : null)));
        if (tier != null) {
            UniversalChestGearLootModifier.addScaledEquipment(generatedLoot, context, tier);
        }
        return generatedLoot;
    }

    private static void offsetPowerLevels(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        Vec3 origin = (Vec3)context.getParamOrNull(LootContextParams.ORIGIN);
        BlockPos position = origin == null ? null : BlockPos.containing((Position)origin);
        PatternMatching.LocationData location = position == null ? null : PatternMatching.LocationData.create((ServerLevel)context.getLevel(), (BlockPos)position);
        for (ItemStack stack : generatedLoot) {
            PowerLevelLootOffset.apply(stack, context.getRandom(), context.getLevel(), location);
        }
    }

    private static void addScaledEquipment(ObjectArrayList<ItemStack> generatedLoot, LootContext context, List<String> tier) {
        int start = context.getRandom().nextInt(tier.size());
        for (int offset = 0; offset < tier.size(); ++offset) {
            ResourceLocation id = ResourceLocation.parse((String)tier.get((start + offset) % tier.size()));
            Item item = BuiltInRegistries.ITEM.getOptional(id).orElse(Items.AIR);
            if (item == Items.AIR) continue;
            ItemStack stack = new ItemStack((ItemLike)item);
            Vec3 origin = (Vec3)context.getParamOrNull(LootContextParams.ORIGIN);
            BlockPos position = origin == null ? null : BlockPos.containing((Position)origin);
            ItemScaling.scale((ItemStack)stack, (ServerLevel)context.getLevel(), (BlockPos)position, (ResourceLocation)context.getQueriedLootTableId());
            generatedLoot.add(stack);
            break;
        }
    }

    private static void addRandomSupply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        int start = context.getRandom().nextInt(COMMON_SUPPLIES.size());
        for (int offset = 0; offset < COMMON_SUPPLIES.size(); ++offset) {
            SupplyEntry entry = COMMON_SUPPLIES.get((start + offset) % COMMON_SUPPLIES.size());
            Item item = BuiltInRegistries.ITEM.getOptional(ResourceLocation.parse((String)entry.itemId())).orElse(Items.AIR);
            if (item == Items.AIR) continue;
            int count = entry.minCount() + context.getRandom().nextInt(entry.maxCount() - entry.minCount() + 1);
            generatedLoot.add(new ItemStack((ItemLike)item, count));
            break;
        }
    }

    private static void addUnscaledItem(
        ObjectArrayList<ItemStack> generatedLoot,
        LootContext context,
        List<String> candidates,
        int minCount,
        int maxCount
    ) {
        int start = context.getRandom().nextInt(candidates.size());
        for (int offset = 0; offset < candidates.size(); ++offset) {
            ResourceLocation id = ResourceLocation.parse(candidates.get((start + offset) % candidates.size()));
            Item item = BuiltInRegistries.ITEM.getOptional(id).orElse(Items.AIR);
            if (item == Items.AIR) {
                continue;
            }
            int count = minCount + context.getRandom().nextInt(maxCount - minCount + 1);
            generatedLoot.add(new ItemStack(item, count));
            return;
        }
    }

    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }

    private record SupplyEntry(String itemId, int minCount, int maxCount) {
    }
}
