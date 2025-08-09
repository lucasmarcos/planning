package com.lucasmarcos.planning;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class PlanningPortalBlock extends Block {

    public PlanningPortalBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide) {
            if (level instanceof ServerLevel serverLevel) {
                ServerLevel planningLevel = serverLevel.getServer().getLevel(CreativePlanningDimension.PLANNING_DIMENSION_KEY);
                if (planningLevel == null) {
                    CreativePlanningDimension.LOGGER.error("Planning dimension not found!");
                } else {
                    player.teleportTo(planningLevel, planningLevel.getSharedSpawnPos().getX(), planningLevel.getSharedSpawnPos().getY(), planningLevel.getSharedSpawnPos().getZ(), player.getYRot(), player.getXRot());
                }
            }
        }
        return InteractionResult.SUCCESS;
    }
}
