/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.tags.ItemTags
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.item.CrossbowItem
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.MaceItem
 *  net.minecraft.world.item.ProjectileWeaponItem
 *  net.minecraft.world.item.TridentItem
 *  net.neoforged.neoforge.event.entity.living.LivingDropsEvent
 */
package works.gream.tweaks;

import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MaceItem;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.TridentItem;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;

public final class VanillaBossLoot {
    private VanillaBossLoot() {
    }

    public static void onLivingDrops(LivingDropsEvent event) {
        if (event.getEntity().getType() != EntityType.ENDER_DRAGON && event.getEntity().getType() != EntityType.WITHER) {
            return;
        }
        event.getDrops().removeIf(drop -> VanillaBossLoot.isWeapon(drop.getItem()));
    }

    private static boolean isWeapon(ItemStack stack) {
        Item item = stack.getItem();
        return stack.is(ItemTags.SWORDS) || stack.is(ItemTags.AXES) || item instanceof ProjectileWeaponItem || item instanceof CrossbowItem || item instanceof TridentItem || item instanceof MaceItem;
    }
}

