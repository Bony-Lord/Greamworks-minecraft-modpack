/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.registries.BuiltInRegistries
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.EquipmentSlot
 *  net.minecraft.world.entity.EquipmentSlotGroup
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier$Operation
 *  net.minecraft.world.entity.ai.attributes.Attributes
 *  net.minecraft.world.item.ArmorItem
 *  net.minecraft.world.item.Item
 *  net.neoforged.neoforge.event.ItemAttributeModifierEvent
 */
package works.gream.tweaks;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.event.ItemAttributeModifierEvent;

public final class EquipmentBalance {
    private static final Map<String, ArmorStats> ARMOR = new HashMap<String, ArmorStats>();

    private EquipmentBalance() {
    }

    public static void onItemAttributes(ItemAttributeModifierEvent itemAttributeModifierEvent) {
        Item item = itemAttributeModifierEvent.getItemStack().getItem();
        String string = BuiltInRegistries.ITEM.getKey(item).toString();
        ArmorStats armorStats = ARMOR.get(string);
        if (armorStats == null || !(item instanceof ArmorItem)) {
            return;
        }
        ArmorItem armorItem = (ArmorItem)item;
        EquipmentSlotGroup equipmentSlotGroup = EquipmentSlotGroup.bySlot((EquipmentSlot)armorItem.getEquipmentSlot());
        ResourceLocation resourceLocation = ResourceLocation.withDefaultNamespace((String)("armor." + armorItem.getType().getName()));
        itemAttributeModifierEvent.replaceModifier(Attributes.ARMOR, new AttributeModifier(resourceLocation, armorStats.armor, AttributeModifier.Operation.ADD_VALUE), equipmentSlotGroup);
        itemAttributeModifierEvent.replaceModifier(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(resourceLocation, armorStats.toughness, AttributeModifier.Operation.ADD_VALUE), equipmentSlotGroup);
        if (armorStats.knockbackResistance > 0.0) {
            itemAttributeModifierEvent.replaceModifier(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(resourceLocation, armorStats.knockbackResistance, AttributeModifier.Operation.ADD_VALUE), equipmentSlotGroup);
        } else {
            itemAttributeModifierEvent.removeModifier(Attributes.KNOCKBACK_RESISTANCE, resourceLocation);
        }
    }

    static double baseArmor(Item item) {
        ArmorStats armorStats = ARMOR.get(BuiltInRegistries.ITEM.getKey(item).toString());
        return armorStats == null ? 0.0 : armorStats.armor;
    }

    static boolean hasBalance(Item item) {
        return ARMOR.containsKey(BuiltInRegistries.ITEM.getKey(item).toString());
    }

    private static void set(String string, double d, double d2, double d3, double d4, double d5, double d6) {
        EquipmentBalance.put(string + "_helmet", d, d5, d6);
        EquipmentBalance.put(string + "_chestplate", d2, d5, d6);
        EquipmentBalance.put(string + "_leggings", d3, d5, d6);
        EquipmentBalance.put(string + "_boots", d4, d5, d6);
    }

    private static void put(String string, double d, double d2, double d3) {
        ARMOR.put(string, new ArmorStats(d, d2, d3));
    }

    static {
        EquipmentBalance.set("deeperdarker:resonarium", 6.0, 12.0, 10.0, 4.0, 2.0, 0.0);
        EquipmentBalance.set("deeperdarker:warden", 8.5, 17.0, 13.6, 6.8, 6.8, 0.085);
        EquipmentBalance.set("block_factorys_bosses:knight", 12.0, 24.0, 20.0, 10.0, 12.0, 0.2);
        EquipmentBalance.put("block_factorys_bosses:dragon_skull", 7.5, 4.5, 0.075);
        EquipmentBalance.put("block_factorys_bosses:dragon_bones_chestplate", 15.0, 4.5, 0.075);
        EquipmentBalance.put("block_factorys_bosses:dragon_bones_leggings", 12.0, 4.5, 0.075);
        EquipmentBalance.put("block_factorys_bosses:dragon_bones_boots", 6.0, 4.5, 0.075);
        EquipmentBalance.set("born_in_chaos_v1:nightmare_mantleofthe_night", 4.0, 8.0, 6.0, 3.0, 3.0, 0.03);
        EquipmentBalance.set("born_in_chaos_v1:dark_metal_armor", 8.5, 17.0, 13.6, 6.8, 6.8, 0.102);
        EquipmentBalance.put("born_in_chaos_v1:spiny_shell_armor_helmet", 5.0, 3.0, 0.04);
        EquipmentBalance.put("born_in_chaos_v1:spiny_shell_armor_chestplate", 9.0, 3.0, 0.04);
        EquipmentBalance.put("born_in_chaos_v1:lord_pumpkinheads_hat_helmet", 4.0, 2.0, 0.02);
        EquipmentBalance.put("greamworks:unused_missionary_hat_armor", 90.0, 60.0, 0.0);
        EquipmentBalance.put("born_in_chaos_v1:spiritual_guide_sombrero_helmet", 3.0, 2.0, 0.0);
        EquipmentBalance.put("born_in_chaos_v1:killer_rabbit_ears_helmet", 2.0, 1.0, 0.0);
        EquipmentBalance.set("cataclysm:cursium", 6.0, 12.0, 10.0, 5.0, 6.0, 0.1);
        EquipmentBalance.set("cataclysm:ignitium", 8.0, 15.0, 12.0, 6.0, 8.0, 0.15);
        EquipmentBalance.put("cataclysm:ignitium_elytra_chestplate", 12.0, 7.0, 0.12);
        EquipmentBalance.put("cataclysm:bone_reptile_helmet", 5.0, 3.0, 0.04);
        EquipmentBalance.put("cataclysm:bone_reptile_chestplate", 10.0, 3.0, 0.04);
        EquipmentBalance.put("cataclysm:monstrous_helm", 7.0, 4.0, 0.1);
        EquipmentBalance.put("cataclysm:bloom_stone_pauldrons", 12.0, 5.0, 0.08);
    }

    private record ArmorStats(double armor, double toughness, double knockbackResistance) {
    }
}
