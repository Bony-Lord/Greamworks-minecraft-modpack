/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 *  net.dungeon_difficulty.logic.ItemScaling
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.core.component.DataComponents
 *  net.minecraft.core.registries.BuiltInRegistries
 *  net.minecraft.network.RegistryFriendlyByteBuf
 *  net.minecraft.network.codec.StreamCodec
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.tags.ItemTags
 *  net.minecraft.world.item.ArmorItem
 *  net.minecraft.world.item.BowItem
 *  net.minecraft.world.item.CrossbowItem
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.MaceItem
 *  net.minecraft.world.item.ShieldItem
 *  net.minecraft.world.item.TieredItem
 *  net.minecraft.world.item.TridentItem
 *  net.minecraft.world.item.crafting.RecipeSerializer
 *  net.minecraft.world.item.crafting.SmithingRecipe
 *  net.minecraft.world.item.crafting.SmithingRecipeInput
 *  net.minecraft.world.level.Level
 */
package works.gream.tweaks;

import com.mojang.serialization.MapCodec;
import net.dungeon_difficulty.logic.ItemScaling;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MaceItem;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SmithingRecipe;
import net.minecraft.world.item.crafting.SmithingRecipeInput;
import net.minecraft.world.level.Level;
import works.gream.tweaks.GreamworksTweaks;
import works.gream.tweaks.PowerLevelScaling;

public final class PowerLevelSmithingRecipe
implements SmithingRecipe {
    private static final ResourceLocation ALLOY_ID = ResourceLocation.fromNamespaceAndPath((String)"create_cataclysm", (String)"cataclysmic_alloy");

    public boolean matches(SmithingRecipeInput input, Level level) {
        ItemStack donor = input.template();
        ItemStack target = input.base();
        if (!this.isAdditionIngredient(input.addition()) || donor.isEmpty() || target.isEmpty()) {
            return false;
        }
        int donorLevel = ItemScaling.getScaleFactor((ItemStack)donor);
        if (donorLevel <= 0 || !PowerLevelSmithingRecipe.sameEquipmentType(donor, target)) {
            return false;
        }
        int targetLevel = ItemScaling.getScaleFactor((ItemStack)target);
        if (donor.is(target.getItem())) {
            return targetLevel == 0 || targetLevel == donorLevel;
        }
        return true;
    }

    public ItemStack assemble(SmithingRecipeInput input, HolderLookup.Provider registries) {
        ItemStack result = input.base().copy();
        result.setCount(1);
        int donorLevel = ItemScaling.getScaleFactor((ItemStack)input.template());
        int targetLevel = ItemScaling.getScaleFactor((ItemStack)input.base());
        int outputLevel = input.template().is(input.base().getItem()) && targetLevel == donorLevel && donorLevel > 0 ? donorLevel + 1 : donorLevel;
        ItemScaling.removeScaling((ItemStack)result);
        ItemScaling.markAsScaled((ItemStack)result, (int)outputLevel);
        PowerLevelScaling.markSmithingScaled(result);
        return result;
    }

    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return ItemStack.EMPTY;
    }

    public boolean isTemplateIngredient(ItemStack stack) {
        return ItemScaling.getScaleFactor((ItemStack)stack) > 0 && PowerLevelSmithingRecipe.equipmentType(stack) != null;
    }

    public boolean isBaseIngredient(ItemStack stack) {
        return PowerLevelSmithingRecipe.equipmentType(stack) != null;
    }

    public boolean isAdditionIngredient(ItemStack stack) {
        Item alloy = (Item)BuiltInRegistries.ITEM.get(ALLOY_ID);
        return stack.is(alloy);
    }

    public RecipeSerializer<?> getSerializer() {
        return (RecipeSerializer)GreamworksTweaks.POWER_LEVEL_SMITHING.get();
    }

    public boolean isSpecial() {
        return true;
    }

    private static boolean sameEquipmentType(ItemStack first, ItemStack second) {
        String firstType = PowerLevelSmithingRecipe.equipmentType(first);
        return firstType != null && firstType.equals(PowerLevelSmithingRecipe.equipmentType(second));
    }

    private static String equipmentType(ItemStack stack) {
        Item item = stack.getItem();
        if (item instanceof ArmorItem) {
            ArmorItem armor = (ArmorItem)item;
            return "armor:" + armor.getEquipmentSlot().getName();
        }
        if (stack.is(ItemTags.SWORDS)) {
            return "sword";
        }
        if (stack.is(ItemTags.AXES)) {
            return "axe";
        }
        if (stack.is(ItemTags.PICKAXES)) {
            return "pickaxe";
        }
        if (stack.is(ItemTags.SHOVELS)) {
            return "shovel";
        }
        if (stack.is(ItemTags.HOES)) {
            return "hoe";
        }
        if (item instanceof BowItem) {
            return "bow";
        }
        if (item instanceof CrossbowItem) {
            return "crossbow";
        }
        if (item instanceof TridentItem) {
            return "trident";
        }
        if (item instanceof MaceItem) {
            return "mace";
        }
        if (item instanceof ShieldItem) {
            return "shield";
        }
        if (item instanceof TieredItem) {
            return "tiered:" + item.getClass().getName();
        }
        if (stack.has(DataComponents.ATTRIBUTE_MODIFIERS) && stack.isDamageableItem() && item.getClass() != Item.class) {
            return "equipment:" + item.getClass().getName();
        }
        return null;
    }

    public static final class Serializer
    implements RecipeSerializer<PowerLevelSmithingRecipe> {
        private static final MapCodec<PowerLevelSmithingRecipe> CODEC = MapCodec.unit(PowerLevelSmithingRecipe::new);
        private static final StreamCodec<RegistryFriendlyByteBuf, PowerLevelSmithingRecipe> STREAM_CODEC = new StreamCodec<RegistryFriendlyByteBuf, PowerLevelSmithingRecipe>(){

            public PowerLevelSmithingRecipe decode(RegistryFriendlyByteBuf buffer) {
                return new PowerLevelSmithingRecipe();
            }

            public void encode(RegistryFriendlyByteBuf buffer, PowerLevelSmithingRecipe recipe) {
            }
        };

        public MapCodec<PowerLevelSmithingRecipe> codec() {
            return CODEC;
        }

        public StreamCodec<RegistryFriendlyByteBuf, PowerLevelSmithingRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}

