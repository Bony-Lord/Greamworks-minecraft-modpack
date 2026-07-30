package works.gream.tweaks;

import com.mojang.logging.LogUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.event.EventHooks;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.fluids.FluidInteractionRegistry;
import org.slf4j.Logger;

/**
 * Cross-mod stone recipes driven by collisions between actual world fluids.
 * The source fluid is consumed; the adjacent reagent remains as a catalyst.
 */
public final class FluidStoneReactions {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final List<Reaction> REACTIONS = List.of(
        reaction("minecraft:lava", "chemica:distilled_water", "chemica:zelosite"),
        reaction("minecraft:lava", "chemica:brine", "chemica:fervorite"),
        reaction("minecraft:lava", "chemica:liquid_hydrogen", "minecraft:crying_obsidian"),
        reaction("minecraft:lava", "tfmg:cooling_fluid", "minecraft:blackstone"),

        reaction("tfmg:molten_slag", "minecraft:water", "create:scoria"),
        reaction("tfmg:molten_slag", "chemica:chromic_acid", "create:ochrum"),
        reaction("tfmg:molten_slag", "chemica:nitric_acid", "create:crimsite"),
        reaction("tfmg:molten_slag", "chemica:hydrofluoric_acid", "create:asurine"),
        reaction("tfmg:molten_slag", "chemica:sulfuric_nickel_solution", "create:veridium"),
        reaction("tfmg:molten_slag", "chemica:phosphoric_acid", "minecraft:calcite"),

        reaction("chemica:molten_glass", "tfmg:cooling_fluid", "create:scorchia"),
        reaction("chemica:brine", "tfmg:liquid_concrete", "create:limestone"),
        reaction("chemica:waste_slurry", "chemica:caustic_soda", "minecraft:tuff"),

        reaction("tfmg:liquid_concrete", "minecraft:water", "tfmg:concrete"),
        reaction("tfmg:liquid_asphalt", "tfmg:cooling_fluid", "tfmg:asphalt")
    );
    private static final Map<ResourceLocation, List<ResolvedReaction>> RESOLVED_BY_SOURCE =
        new HashMap<>();
    private static boolean registered;

    private FluidStoneReactions() {
    }

    public static synchronized void register() {
        if (registered) {
            return;
        }

        int registeredCount = 0;
        for (Reaction reaction : REACTIONS) {
            Fluid source = BuiltInRegistries.FLUID.getOptional(reaction.sourceFluid()).orElse(null);
            Fluid target = BuiltInRegistries.FLUID.getOptional(reaction.targetFluid()).orElse(null);
            Block result = BuiltInRegistries.BLOCK.getOptional(reaction.resultBlock()).orElse(null);
            if (source == null || target == null || result == null) {
                LOGGER.warn(
                    "Skipping fluid reaction {} + {} -> {} because a registry entry is missing",
                    reaction.sourceFluid(),
                    reaction.targetFluid(),
                    reaction.resultBlock()
                );
                continue;
            }

            FluidInteractionRegistry.addInteraction(
                source.getFluidType(),
                new FluidInteractionRegistry.InteractionInformation(
                    (level, currentPos, relativePos, currentState) ->
                        currentState.isSource()
                            && level.getFluidState(relativePos).getFluidType() == target.getFluidType(),
                    (level, currentPos, relativePos, currentState) ->
                        placeResult(level, currentPos, result.defaultBlockState())
                )
            );
            RESOLVED_BY_SOURCE
                .computeIfAbsent(reaction.sourceFluid(), ignored -> new ArrayList<>())
                .add(new ResolvedReaction(reaction.targetFluid(), result.defaultBlockState()));
            registeredCount++;
        }
        registered = true;
        LOGGER.info(
            "Registered {}/{} native fluid stone reactions",
            registeredCount,
            REACTIONS.size()
        );
    }

    /**
     * Some modded fluid blocks bypass LiquidBlock's NeoForge interaction hook.
     * Neighbor notifications provide a narrow fallback without scanning chunks.
     */
    public static void onNeighborNotify(BlockEvent.NeighborNotifyEvent event) {
        if (!(event.getLevel() instanceof Level level) || level.isClientSide()) {
            return;
        }

        tryReact(level, event.getPos());
        for (Direction direction : event.getNotifiedSides()) {
            tryReact(level, event.getPos().relative(direction));
        }
    }

    private static boolean tryReact(Level level, BlockPos sourcePos) {
        FluidState sourceState = level.getFluidState(sourcePos);
        if (!sourceState.isSource()) {
            return false;
        }

        List<ResolvedReaction> candidates = RESOLVED_BY_SOURCE.get(
            normalizedFluidId(sourceState)
        );
        if (candidates == null) {
            return false;
        }

        for (Direction direction : Direction.values()) {
            ResourceLocation adjacentFluid = normalizedFluidId(
                level.getFluidState(sourcePos.relative(direction))
            );
            for (ResolvedReaction candidate : candidates) {
                if (candidate.targetFluid().equals(adjacentFluid)) {
                    placeResult(level, sourcePos, candidate.result());
                    return true;
                }
            }
        }
        return false;
    }

    private static void placeResult(Level level, BlockPos sourcePos, BlockState result) {
        BlockState placed = EventHooks.fireFluidPlaceBlockEvent(
            level,
            sourcePos,
            sourcePos,
            result
        );
        level.setBlockAndUpdate(sourcePos, placed);
        level.levelEvent(1501, sourcePos, 0);
    }

    private static ResourceLocation normalizedFluidId(FluidState state) {
        if (state.isEmpty()) {
            return BuiltInRegistries.FLUID.getDefaultKey();
        }
        ResourceLocation id = BuiltInRegistries.FLUID.getKey(state.getType());
        return ResourceLocation.fromNamespaceAndPath(
            id.getNamespace(),
            id.getPath().replaceFirst("^flowing_", "")
        );
    }

    private static Reaction reaction(String source, String target, String result) {
        return new Reaction(
            ResourceLocation.parse(source),
            ResourceLocation.parse(target),
            ResourceLocation.parse(result)
        );
    }

    public static record Reaction(
        ResourceLocation sourceFluid,
        ResourceLocation targetFluid,
        ResourceLocation resultBlock
    ) {
    }

    private static record ResolvedReaction(
        ResourceLocation targetFluid,
        BlockState result
    ) {
    }

    public static List<Reaction> reactions() {
        return REACTIONS;
    }
}
