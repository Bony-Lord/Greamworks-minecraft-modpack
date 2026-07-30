/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.critical_strike.api.CriticalDamageSource
 *  net.critical_strike.api.CriticalStrikeAttributes
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.damagesource.DamageTypes
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.ai.attributes.AttributeInstance
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier$Operation
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.entity.projectile.AbstractArrow
 *  net.neoforged.neoforge.event.entity.living.LivingDamageEvent$Post
 *  net.neoforged.neoforge.event.tick.PlayerTickEvent$Post
 */
package works.gream.tweaks;

import net.critical_strike.api.CriticalDamageSource;
import net.critical_strike.api.CriticalStrikeAttributes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import works.gream.tweaks.AbilityDamageScaling;

public final class CriticalChanceCombo {
    private static final ResourceLocation COMBO_MODIFIER = ResourceLocation.fromNamespaceAndPath((String)"greamworks_tweaks", (String)"critical_chance_combo");
    private static final String COMBO_STACKS = "greamworks_critical_combo_stacks";
    private static final String LAST_HIT = "greamworks_critical_combo_last_hit";
    private static final int MAX_STACKS = 12;
    private static final int TIMEOUT_TICKS = 60;
    private static final double CHANCE_PER_STACK = 0.05;

    private CriticalChanceCombo() {
    }

    public static void onDamagePost(LivingDamageEvent.Post event) {
        ServerPlayer attacker;
        DamageSource source;
        block5: {
            block4: {
                source = event.getSource();
                Entity entity = source.getEntity();
                if (!(entity instanceof ServerPlayer)) break block4;
                attacker = (ServerPlayer)entity;
                if (!(event.getNewDamage() <= 0.0f) && CriticalChanceCombo.isWeaponHit(source, attacker) && source instanceof CriticalDamageSource) break block5;
            }
            return;
        }
        CriticalDamageSource criticalSource = (CriticalDamageSource)source;
        if (criticalSource.rng_isCritical()) {
            CriticalChanceCombo.reset(attacker);
            return;
        }
        int stacks = Math.min(12, attacker.getPersistentData().getInt(COMBO_STACKS) + 1);
        attacker.getPersistentData().putInt(COMBO_STACKS, stacks);
        attacker.getPersistentData().putLong(LAST_HIT, attacker.level().getGameTime());
        CriticalChanceCombo.applyModifier(attacker, stacks);
    }

    public static void onPlayerTick(PlayerTickEvent.Post event) {
        ServerPlayer player;
        block5: {
            block4: {
                Player player2 = event.getEntity();
                if (!(player2 instanceof ServerPlayer)) break block4;
                player = (ServerPlayer)player2;
                if (player.tickCount % 10 == 0 && player.getPersistentData().getInt(COMBO_STACKS) > 0) break block5;
            }
            return;
        }
        long elapsed = player.level().getGameTime() - player.getPersistentData().getLong(LAST_HIT);
        if (elapsed > 60L) {
            CriticalChanceCombo.reset(player);
        }
    }

    private static boolean isWeaponHit(DamageSource source, ServerPlayer attacker) {
        Entity direct = source.getDirectEntity();
        if (direct == attacker) {
            return source.is(DamageTypes.PLAYER_ATTACK) && AbilityDamageScaling.attackDamage(attacker.getMainHandItem()) > 1.0;
        }
        return direct instanceof AbstractArrow;
    }

    private static void applyModifier(ServerPlayer player, int stacks) {
        AttributeInstance chance = player.getAttribute(CriticalStrikeAttributes.CHANCE.attributeEntry);
        if (chance == null) {
            return;
        }
        chance.removeModifier(COMBO_MODIFIER);
        if (stacks > 0) {
            chance.addTransientModifier(new AttributeModifier(COMBO_MODIFIER, (double)stacks * 0.05, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        }
    }

    private static void reset(ServerPlayer player) {
        player.getPersistentData().remove(COMBO_STACKS);
        player.getPersistentData().remove(LAST_HIT);
        CriticalChanceCombo.applyModifier(player, 0);
    }
}

