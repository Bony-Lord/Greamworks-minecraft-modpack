/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.damagesource.DamageTypes
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EquipmentSlotGroup
 *  net.minecraft.world.entity.ai.attributes.Attribute
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier$Operation
 *  net.minecraft.world.entity.ai.attributes.Attributes
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.entity.projectile.AbstractArrow
 *  net.minecraft.world.entity.projectile.ThrownTrident
 *  net.minecraft.world.item.ItemStack
 *  net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent
 */
package works.gream.tweaks;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

public final class AbilityDamageScaling {
    private static final float REFERENCE_WEAPON_DAMAGE = 10.0f;
    private static final float MIN_ABILITY_RATIO = 0.25f;
    private static final float MAX_ABILITY_RATIO = 2.0f;

    private AbilityDamageScaling() {
    }

    public static void onIncomingDamage(LivingIncomingDamageEvent event) {
        Player attacker;
        DamageSource source;
        block7: {
            block6: {
                source = event.getSource();
                Entity entity = source.getEntity();
                if (!(entity instanceof Player)) break block6;
                attacker = (Player)entity;
                if (!source.is(DamageTypes.PLAYER_ATTACK) && !source.is(DamageTypes.MOB_ATTACK)) break block7;
            }
            return;
        }
        ItemStack weapon = AbilityDamageScaling.resolveWeapon(source, attacker);
        if (weapon.isEmpty()) {
            return;
        }
        if (source.getDirectEntity() instanceof AbstractArrow && !(source.getDirectEntity() instanceof ThrownTrident)) {
            return;
        }
        double weaponDamage = AbilityDamageScaling.attackDamage(weapon);
        if (weaponDamage <= 1.0) {
            return;
        }
        float abilityRatio = Math.clamp((float)(event.getOriginalAmount() / 10.0f), (float)0.25f, (float)2.0f);
        event.setAmount((float)weaponDamage * abilityRatio);
    }

    private static ItemStack resolveWeapon(DamageSource source, Player attacker) {
        AbstractArrow arrow;
        ItemStack firedFrom;
        Entity entity = source.getDirectEntity();
        if (entity instanceof AbstractArrow && (firedFrom = (arrow = (AbstractArrow)entity).getWeaponItem()) != null && !firedFrom.isEmpty()) {
            return firedFrom;
        }
        return attacker.getMainHandItem();
    }

    public static double attackDamage(ItemStack stack) {
        double[] values = new double[]{((Attribute)Attributes.ATTACK_DAMAGE.value()).getDefaultValue(), 0.0, 0.0, 1.0};
        stack.forEachModifier(EquipmentSlotGroup.MAINHAND, (attribute, modifier) -> {
            if (!attribute.equals((Object)Attributes.ATTACK_DAMAGE)) {
                return;
            }
            if (modifier.operation() == AttributeModifier.Operation.ADD_VALUE) {
                values[1] = values[1] + modifier.amount();
            } else if (modifier.operation() == AttributeModifier.Operation.ADD_MULTIPLIED_BASE) {
                values[2] = values[2] + modifier.amount();
            } else if (modifier.operation() == AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL) {
                values[3] = values[3] * (1.0 + modifier.amount());
            }
        });
        return (values[0] + values[1]) * (1.0 + values[2]) * values[3];
    }
}

