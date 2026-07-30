/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.dungeon_difficulty.logic.ItemScaling
 *  net.minecraft.core.Holder
 *  net.minecraft.core.registries.BuiltInRegistries
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.tags.ItemTags
 *  net.minecraft.world.entity.EquipmentSlot
 *  net.minecraft.world.entity.EquipmentSlotGroup
 *  net.minecraft.world.entity.ai.attributes.Attribute
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier$Operation
 *  net.minecraft.world.entity.ai.attributes.Attributes
 *  net.minecraft.world.item.ArmorItem
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.ProjectileWeaponItem
 *  net.minecraft.world.item.TridentItem
 *  net.minecraft.world.item.component.ItemAttributeModifiers$Entry
 *  net.neoforged.neoforge.event.ItemAttributeModifierEvent
 */
package works.gream.tweaks;

import java.util.List;
import net.dungeon_difficulty.logic.ItemScaling;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.neoforged.neoforge.event.ItemAttributeModifierEvent;
import works.gream.tweaks.EquipmentBalance;

public final class PowerLevelScaling {
    private static final ResourceLocation ATTACK_ID = PowerLevelScaling.id("attack_damage");
    private static final ResourceLocation ARMOR_ID = PowerLevelScaling.id("armor");
    private static final ResourceLocation HEALTH_ID = PowerLevelScaling.id("max_health");
    private static final ResourceLocation MINING_ID = PowerLevelScaling.id("mining_efficiency");
    private static final ResourceLocation BREAK_SPEED_ID = PowerLevelScaling.id("block_break_speed");
    private static final ResourceLocation RANGED_DAMAGE_ID = PowerLevelScaling.id("ranged_damage");
    private static final ResourceLocation APOTHIC_ARROW_DAMAGE = ResourceLocation.fromNamespaceAndPath((String)"apothic_attributes", (String)"arrow_damage");

    private PowerLevelScaling() {
    }

    public static void markSmithingScaled(ItemStack stack) {
    }

    public static void onItemAttributes(ItemAttributeModifierEvent event) {
        boolean weapon;
        ItemStack stack = event.getItemStack();
        int level = ItemScaling.getScaleFactor((ItemStack)stack);
        if (level <= 0) {
            return;
        }
        Item item = stack.getItem();
        if (PowerLevelScaling.hasMaterializedDungeonScaling(stack) && !EquipmentBalance.hasBalance(item)) {
            return;
        }
        if (item instanceof ArmorItem) {
            ArmorItem armorItem = (ArmorItem)item;
            EquipmentSlotGroup slot = EquipmentSlotGroup.bySlot((EquipmentSlot)armorItem.getEquipmentSlot());
            PowerLevelScaling.scaleArmor(event, stack, level, slot);
            return;
        }
        if (stack.getItem() instanceof ProjectileWeaponItem) {
            PowerLevelScaling.addRangedDamage(event, level);
            return;
        }
        EquipmentSlotGroup armorSlot = PowerLevelScaling.firstSlotFor(event.getModifiers(), (Holder<Attribute>)Attributes.ARMOR);
        if (armorSlot != null && armorSlot != EquipmentSlotGroup.HAND && armorSlot != EquipmentSlotGroup.MAINHAND) {
            PowerLevelScaling.scaleArmor(event, stack, level, armorSlot);
            return;
        }
        boolean tool = stack.is(ItemTags.PICKAXES) || stack.is(ItemTags.AXES) || stack.is(ItemTags.SHOVELS) || stack.is(ItemTags.HOES);
        double currentDamage = PowerLevelScaling.addedValue(event.getModifiers(), (Holder<Attribute>)Attributes.ATTACK_DAMAGE, EquipmentSlotGroup.MAINHAND);
        if (currentDamage <= 0.0) {
            currentDamage = PowerLevelScaling.addedValue(event.getModifiers(), (Holder<Attribute>)Attributes.ATTACK_DAMAGE, EquipmentSlotGroup.HAND);
        }
        boolean bl = weapon = stack.is(ItemTags.SWORDS) || tool || currentDamage > 1.0;
        if (weapon) {
            double baseDamage = PowerLevelScaling.addedValue(stack.getItem().getDefaultAttributeModifiers().modifiers(), (Holder<Attribute>)Attributes.ATTACK_DAMAGE, EquipmentSlotGroup.MAINHAND);
            if (baseDamage <= 0.0) {
                baseDamage = PowerLevelScaling.addedValue(stack.getItem().getDefaultAttributeModifiers().modifiers(), (Holder<Attribute>)Attributes.ATTACK_DAMAGE, EquipmentSlotGroup.HAND);
            }
            if (baseDamage <= 0.0) {
                baseDamage = currentDamage;
            }
            if (baseDamage > 0.0) {
                PowerLevelScaling.addPositiveModifier(event, (Holder<Attribute>)Attributes.ATTACK_DAMAGE, ATTACK_ID, baseDamage * (1.0 + 0.12 * (double)level) - currentDamage, EquipmentSlotGroup.MAINHAND);
            }
        }
        if (stack.getItem() instanceof TridentItem) {
            PowerLevelScaling.addRangedDamage(event, level);
        }
        if (tool) {
            PowerLevelScaling.addPositiveModifier(event, (Holder<Attribute>)Attributes.MINING_EFFICIENCY, MINING_ID, 0.5 * (double)level, EquipmentSlotGroup.MAINHAND);
            event.addModifier(Attributes.BLOCK_BREAK_SPEED, new AttributeModifier(BREAK_SPEED_ID, 0.03 * (double)level, AttributeModifier.Operation.ADD_MULTIPLIED_BASE), EquipmentSlotGroup.MAINHAND);
        }
    }

    private static void addRangedDamage(ItemAttributeModifierEvent event, int level) {
        BuiltInRegistries.ATTRIBUTE.getHolder(APOTHIC_ARROW_DAMAGE).ifPresent(attribute -> event.addModifier((Holder)attribute, new AttributeModifier(RANGED_DAMAGE_ID, 0.12 * (double)level, AttributeModifier.Operation.ADD_MULTIPLIED_BASE), EquipmentSlotGroup.MAINHAND));
    }

    private static void scaleArmor(ItemAttributeModifierEvent event, ItemStack stack, int level, EquipmentSlotGroup slot) {
        double currentArmor = PowerLevelScaling.addedValue(event.getModifiers(), (Holder<Attribute>)Attributes.ARMOR, slot);
        double baseArmor = EquipmentBalance.baseArmor(stack.getItem());
        if (baseArmor <= 0.0) {
            baseArmor = PowerLevelScaling.addedValue(stack.getItem().getDefaultAttributeModifiers().modifiers(), (Holder<Attribute>)Attributes.ARMOR, slot);
        }
        if (baseArmor <= 0.0) {
            baseArmor = currentArmor;
        }
        PowerLevelScaling.addPositiveModifier(event, (Holder<Attribute>)Attributes.ARMOR, ARMOR_ID, baseArmor * (1.0 + 0.1 * (double)level) - currentArmor, slot);
        double currentHealth = PowerLevelScaling.addedValue(event.getModifiers(), (Holder<Attribute>)Attributes.MAX_HEALTH, slot);
        double baseHealth = PowerLevelScaling.addedValue(stack.getItem().getDefaultAttributeModifiers().modifiers(), (Holder<Attribute>)Attributes.MAX_HEALTH, slot);
        PowerLevelScaling.addPositiveModifier(event, (Holder<Attribute>)Attributes.MAX_HEALTH, HEALTH_ID, baseHealth + 0.5 * (double)level - currentHealth, slot);
    }

    private static EquipmentSlotGroup firstSlotFor(List<ItemAttributeModifiers.Entry> entries, Holder<Attribute> attribute) {
        return entries.stream().filter(entry -> entry.attribute().equals((Object)attribute)).map(ItemAttributeModifiers.Entry::slot).findFirst().orElse(null);
    }

    private static void addPositiveModifier(ItemAttributeModifierEvent event, Holder<Attribute> attribute, ResourceLocation id, double amount, EquipmentSlotGroup slot) {
        if (amount > 1.0E-4) {
            event.addModifier(attribute, new AttributeModifier(id, amount, AttributeModifier.Operation.ADD_VALUE), slot);
        }
    }

    private static double addedValue(List<ItemAttributeModifiers.Entry> entries, Holder<Attribute> attribute, EquipmentSlotGroup slot) {
        return entries.stream().filter(entry -> entry.attribute().equals((Object)attribute)).filter(entry -> entry.slot() == slot).map(ItemAttributeModifiers.Entry::modifier).filter(modifier -> modifier.operation() == AttributeModifier.Operation.ADD_VALUE).mapToDouble(AttributeModifier::amount).sum();
    }

    private static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath((String)"greamworks_tweaks", (String)("power_level/" + path));
    }

    private static boolean hasMaterializedDungeonScaling(ItemStack stack) {
        ItemAttributeModifiers current = stack.get(DataComponents.ATTRIBUTE_MODIFIERS);
        ItemAttributeModifiers defaults = stack.getItem().getDefaultAttributeModifiers();
        if (current == null) {
            return false;
        }
        for (ItemAttributeModifiers.Entry currentEntry : current.modifiers()) {
            if ("dungeon_difficulty".equals(currentEntry.modifier().id().getNamespace())) {
                return true;
            }
            for (ItemAttributeModifiers.Entry defaultEntry : defaults.modifiers()) {
                if (currentEntry.attribute().equals(defaultEntry.attribute())
                        && currentEntry.slot() == defaultEntry.slot()
                        && currentEntry.modifier().id().equals(defaultEntry.modifier().id())
                        && currentEntry.modifier().operation() == defaultEntry.modifier().operation()
                        && Math.abs(currentEntry.modifier().amount() - defaultEntry.modifier().amount()) > 1.0E-6) {
                    return true;
                }
            }
        }

        // Dungeon Difficulty can merge a weapon's HAND and MAINHAND entries while
        // retaining the original modifier IDs. Compare their combined attack value
        // so an already materialized weapon never receives our runtime layer again.
        double currentAttack = PowerLevelScaling.addedValue(
                current.modifiers(), (Holder<Attribute>)Attributes.ATTACK_DAMAGE, EquipmentSlotGroup.MAINHAND)
                + PowerLevelScaling.addedValue(
                current.modifiers(), (Holder<Attribute>)Attributes.ATTACK_DAMAGE, EquipmentSlotGroup.HAND);
        double defaultAttack = PowerLevelScaling.addedValue(
                defaults.modifiers(), (Holder<Attribute>)Attributes.ATTACK_DAMAGE, EquipmentSlotGroup.MAINHAND)
                + PowerLevelScaling.addedValue(
                defaults.modifiers(), (Holder<Attribute>)Attributes.ATTACK_DAMAGE, EquipmentSlotGroup.HAND);
        return Math.abs(currentAttack - defaultAttack) > 1.0E-6;
    }
}
