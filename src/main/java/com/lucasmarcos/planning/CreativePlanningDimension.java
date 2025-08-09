package com.lucasmarcos.planning;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(CreativePlanningDimension.MODID)
public class CreativePlanningDimension {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "creativeplanningdimension";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold Blocks which will all be registered under the "creativeplanningdimension" namespace
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    // Create a Deferred Register to hold Items which will all be registered under the "creativeplanningdimension" namespace
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredRegister<DimensionType> DIMENSION_TYPES = DeferredRegister.create(Registries.DIMENSION_TYPE, MODID);
    public static final ResourceKey<Level> PLANNING_DIMENSION_KEY = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(MODID, "planning_dimension"));
    public static final DeferredHolder<DimensionType, DimensionType> PLANNING_DIMENSION_TYPE = DIMENSION_TYPES.register("planning_dimension", () -> new DimensionType(0, true, false, false, true, 1, true, false, false, false, 0, 0, 0, false));

    public static final DeferredRegister<Biome> BIOMES = DeferredRegister.create(Registries.BIOME, MODID);
    public static final DeferredHolder<Biome, Biome> PLANNING_BIOME = BIOMES.register("planning_biome", PlanningBiome::create);

    

    public static final DeferredRegister<ChunkGenerator> CHUNK_GENERATORS = DeferredRegister.create(Registries.CHUNK_GENERATOR, MODID);
    public static final DeferredHolder<ChunkGenerator, ChunkGenerator> PLANNING_CHUNK_GENERATOR = CHUNK_GENERATORS.register("planning_chunk_generator", () -> new PlanningDimensionChunkGenerator(PLANNING_BIOME));

    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(Registries.PARTICLE_TYPE, MODID);
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> PLANNING_PARTICLE_TYPE = PARTICLE_TYPES.register("planning_particle", () -> new SimpleParticleType(true));

    // Creates a new Block with the id "creativeplanningdimension:example_block", combining the namespace and path
    public static final DeferredBlock<Block> PLANNING_PORTAL_BLOCK = BLOCKS.register("planning_portal_block", () -> new PlanningPortalBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(3.0f)));
    public static final DeferredItem<BlockItem> PLANNING_PORTAL_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("planning_portal_block", PLANNING_PORTAL_BLOCK);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> CREATIVE_PLANNING_DIMENSION_TAB = CREATIVE_MODE_TABS.register("creative_planning_dimension_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.creativeplanningdimension"))
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> PLANNING_PORTAL_BLOCK_ITEM.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(PLANNING_PORTAL_BLOCK_ITEM.get());
            }).build());

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public CreativePlanningDimension(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register the Deferred Register to the mod event bus so blocks get registered
        BLOCKS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so items get registered
        ITEMS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so tabs get registered
        CREATIVE_MODE_TABS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so dimension types get registered
        DIMENSION_TYPES.register(modEventBus);
        // Register the Deferred Register to the mod event bus so chunk generators get registered
        CHUNK_GENERATORS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so biomes get registered
        BIOMES.register(modEventBus);
        // Register the Deferred Register to the mod event bus so particle types get registered
        PARTICLE_TYPES.register(modEventBus);

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (CreativePlanningDimension) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");
    }

    // Add the planning portal block item to the creative tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            event.accept(PLANNING_PORTAL_BLOCK_ITEM);
        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }
}
