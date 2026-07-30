/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  artifacts.entity.MimicEntity
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.core.registries.BuiltInRegistries
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.InteractionResult
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.MobSpawnType
 *  net.minecraft.world.entity.ai.attributes.Attributes
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.ServerLevelAccessor
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.properties.BlockStateProperties
 *  net.minecraft.world.level.block.state.properties.Property
 *  net.neoforged.neoforge.event.entity.player.PlayerInteractEvent$RightClickBlock
 *  noobanidus.mods.lootr.common.api.data.blockentity.ILootrBlockEntity
 */
package works.gream.tweaks;

import artifacts.entity.MimicEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import noobanidus.mods.lootr.common.api.data.blockentity.ILootrBlockEntity;

final class LootrMimics {
    private static final ResourceLocation MIMIC_ID = ResourceLocation.fromNamespaceAndPath((String)"artifacts", (String)"mimic");
    private static final int BASE_PROBABILITY_256 = 8;
    private static final double LUCK_STEP = -0.75;
    private static final int MAX_PROBABILITY_256 = 64;

    private LootrMimics() {
    }

    static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        ILootrBlockEntity lootr;
        ServerLevel level;
        Level level2;
        block9: {
            block8: {
                level2 = event.getLevel();
                if (!(level2 instanceof ServerLevel)) break block8;
                level = (ServerLevel)level2;
                if (event.getEntity() instanceof ServerPlayer) break block9;
            }
            return;
        }
        ServerPlayer player = (ServerPlayer)event.getEntity();
        BlockPos pos = event.getPos();
        BlockState state = level.getBlockState(pos);
        if (!LootrMimics.isLootrChest(state)) {
            return;
        }
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof ILootrBlockEntity) || (lootr = (ILootrBlockEntity)blockEntity).hasBeenOpened()) {
            return;
        }
        if (!LootrMimics.isSelected(level, pos, player)) {
            return;
        }
        EntityType type = (EntityType)BuiltInRegistries.ENTITY_TYPE.get(MIMIC_ID);
        Entity created = type.create((Level)level);
        if (!(created instanceof MimicEntity)) {
            return;
        }
        MimicEntity mimic = (MimicEntity)created;
        Direction facing = state.hasProperty((Property)BlockStateProperties.HORIZONTAL_FACING) ? (Direction)state.getValue((Property)BlockStateProperties.HORIZONTAL_FACING) : Direction.NORTH;
        mimic.setDormant(true);
        mimic.setFacing(facing);
        mimic.setPos((double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5);
        mimic.finalizeSpawn((ServerLevelAccessor)level, level.getCurrentDifficultyAt(pos), MobSpawnType.STRUCTURE, null);
        if (!level.addFreshEntity((Entity)mimic)) {
            return;
        }
        level.removeBlock(pos, false);
        mimic.setTarget((LivingEntity)player);
        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.SUCCESS);
    }

    private static boolean isLootrChest(BlockState state) {
        ResourceLocation id = BuiltInRegistries.BLOCK.getKey(state.getBlock());
        if (!id.getNamespace().equals("lootr")) {
            return false;
        }
        return id.getPath().equals("lootr_chest") || id.getPath().equals("lootr_trapped_chest");
    }

    private static boolean isSelected(ServerLevel level, BlockPos pos, ServerPlayer player) {
        int probability256 = LootrMimics.probability256(player);
        if (probability256 <= 0) {
            return false;
        }
        long dimensionSalt = (long)level.dimension().location().hashCode() * -7046029254386353131L;
        int seed = (int)(level.getSeed() ^ dimensionSalt);
        return LootrMimics.hash8(pos.getX(), pos.getY(), pos.getZ(), seed) < probability256;
    }

    private static int probability256(ServerPlayer player) {
        double luck = player.getAttributeValue(Attributes.LUCK);
        double scaled = 16.0 * (1.0 + luck * -0.75);
        return Math.max(0, Math.min(127, (int)Math.round(scaled)));
    }

    private static int hash8(int x, int y, int z, int seed) {
        int hash = seed;
        hash ^= x * 73856093;
        hash ^= y * 19349663;
        hash ^= z * 83492791;
        hash ^= hash >>> 13;
        hash *= 1540483477;
        hash ^= hash >>> 15;
        return hash & 0xFF;
    }
}
