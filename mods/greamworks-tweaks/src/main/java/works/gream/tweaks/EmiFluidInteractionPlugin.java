package works.gream.tweaks;

import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.recipe.EmiWorldInteractionRecipe;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

/**
 * Mirrors the authoritative FluidInteractionRegistry table in EMI's
 * World Interaction category. This is display-only and cannot create a
 * second gameplay recipe.
 */
@EmiEntrypoint
public final class EmiFluidInteractionPlugin implements EmiPlugin {
    @Override
    public void register(EmiRegistry registry) {
        for (FluidStoneReactions.Reaction reaction : FluidStoneReactions.reactions()) {
            Fluid source = BuiltInRegistries.FLUID.getOptional(reaction.sourceFluid()).orElse(null);
            Fluid target = BuiltInRegistries.FLUID.getOptional(reaction.targetFluid()).orElse(null);
            Block result = BuiltInRegistries.BLOCK.getOptional(reaction.resultBlock()).orElse(null);
            if (source == null || target == null || result == null) {
                continue;
            }

            registry.addRecipe(
                EmiWorldInteractionRecipe.builder()
                    .id(reactionId(reaction))
                    .leftInput(EmiStack.of(source, 1000))
                    .rightInput(EmiStack.of(target, 1000), true)
                    .output(EmiStack.of(result))
                    .supportsRecipeTree(false)
                    .build()
            );
        }
    }

    private static net.minecraft.resources.ResourceLocation reactionId(
        FluidStoneReactions.Reaction reaction
    ) {
        String path = reaction.sourceFluid().getPath()
            + "_with_"
            + reaction.targetFluid().getNamespace()
            + "_"
            + reaction.targetFluid().getPath();
        return net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(
            GreamworksTweaks.MOD_ID,
            "fluid_interaction/" + path
        );
    }
}
