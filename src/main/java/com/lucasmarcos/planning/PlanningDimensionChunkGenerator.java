package com.lucasmarcos.planning;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.FixedBiomeSource;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.structure.StructureSet;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class PlanningDimensionChunkGenerator extends ChunkGenerator {

    public static final Codec<PlanningDimensionChunkGenerator> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(Registry.BIOME.CODEC.fieldOf("biome").forGetter(gen -> gen.biomeSource.getPrimaryBiome(BlockPos.ZERO, RandomState.EMPTY)))
                    .apply(instance, PlanningDimensionChunkGenerator::new));

    public PlanningDimensionChunkGenerator(Holder<Biome> biomeHolder) {
        super(new FixedBiomeSource(biomeHolder));
    }

    @Override
    protected Codec<? extends ChunkGenerator> codec() {
        return CODEC;
    }

    @Override
    public void buildSurface(WorldGenRegion p_223727_,
                             StructureManager p_223728_,
                             RandomState p_223729_,
                             ChunkAccess p_223730_) {
        // Do nothing, create an empty world
    }

    @Override
    public void applyCarvers(WorldGenRegion p_223719_,
                             long p_223720_,
                             RandomState p_223721_,
                             BiomeManager p_223722_,
                             StructureManager p_223723_,
                             ChunkAccess p_223724_,
                             GenerationStep.Carving p_223725_) {
        // Do nothing, create an empty world
    }

    @Override
    public void spawnOriginalMobs(WorldGenRegion p_223731_) {
        // Do nothing, create an empty world
    }

    @Override
    public int getGenDepth() {
        return 256;
    }

    @Override
    public CompletableFuture<ChunkAccess> fillFromNoise(Executor p_223732_,
                                                        Blender p_223733_,
                                                        RandomState p_223734_,
                                                        StructureManager p_223735_,
                                                        ChunkAccess p_223736_) {
        return CompletableFuture.completedFuture(p_223736_);
    }

    @Override
    public int getSeaLevel() {
        return 0;
    }

    @Override
    public int getMinY() {
        return 0;
    }

    @Override
    public int getBaseHeight(int p_223712_,
                             int p_223713_,
                             Heightmap.Types p_223714_,
                             LevelHeightAccessor p_223715_,
                             RandomState p_223716_) {
        return 0;
    }

    @Override
    public NoiseColumn getNoiseColumn(int p_223708_,
                                      int p_223709_,
                                      LevelHeightAccessor p_223710_,
                                      RandomState p_223711_) {
        return new NoiseColumn(0, new BlockState[0]);
    }

    @Override
    public void applyBiomeDecoration(WorldGenLevel p_223726_,
                                     ChunkAccess p_223727_,
                                     StructureManager p_223728_) {
        // Do nothing, create an empty world
    }

    @Override
    public void createStructures(RegistryAccess p_255601_,
                                 StructureManager p_256071_,
                                 ChunkAccess p_256054_,
                                 BiomeSource p_255997_,
                                 RandomState p_256072_) {
        // Do nothing, create an empty world
    }

    @Override
    public void createReferences(WorldGenRegion p_223717_,
                                 StructureManager p_223718_,
                                 ChunkAccess p_223719_) {
        // Do nothing, create an empty world
    }

    @Override
    public Holder<Biome> getBiome(BlockPos p_223704_,
                                  RandomState p_223705_,
                                  BiomeManager p_223706_) {
        return this.biomeSource.getPrimaryBiome(p_223704_, p_223705_);
    }

    @Override
    public void spawnMobs(WorldGenRegion p_223731_) {
        // Do nothing, create an empty world
    }

    @Override
    public List<HolderSet<StructureSet>> get  (Registry<StructureSet> p_255899_) {
        return List.of();
    }
}
