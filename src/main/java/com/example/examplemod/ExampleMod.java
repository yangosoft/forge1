package com.example.examplemod;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraftforge.eventbus.api.IEventBus;
import java.util.stream.Collectors;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import com.example.examplemod.mbe01_block_simple.BlockSimple;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("examplemod")
public class ExampleMod {
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();
    public static IEventBus MOD_EVENT_BUS;

    public static String MODID = "examplemod";
    public static BlockSimple blockSimple = new BlockSimple(); // this holds the unique instance of your block
    public static BlockItem itemBlockSimple; // this holds the unique instance of the ItemBlock corresponding to your
                                             // block

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);

    // public static final RegistryObject<Block> ROCK_BLOCK =
    // BLOCKS.register("rock", () -> new
    // Block(BlockBehaviour.Properties.of(Material.STONE)));

    public static final RegistryObject<Block> ROCK_MB = BLOCKS.register("mbe01_block_simple_registry_name", () -> {
        return blockSimple;
    });

    public ExampleMod() {

        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        MOD_EVENT_BUS = FMLJavaModLoadingContext.get().getModEventBus();

    }

    private void setup(final FMLCommonSetupEvent event) {
        // some preinit code
        LOGGER.info("HELLO FROM PREINIT");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        // some example code to dispatch IMC to another mod
        InterModComms.sendTo("examplemod", "helloworld", () -> {
            LOGGER.info("Hello world from the MDK");
            return "Hello world";
        });
    }

    private void processIMC(final InterModProcessEvent event) {
        // some example code to receive and process InterModComms from other mods
        LOGGER.info("Got IMC {}",
                event.getIMCStream().map(m -> m.messageSupplier().get()).collect(Collectors.toList()));
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically subscribe events on the
    // contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            // register a new block here
            LOGGER.info("HELLO from Register Block");

            // blockSimple = (BlockSimple)(new BlockSimple().setRegistryName("examplemod",
            // "mbe01_block_simple_registry_name"));

            // blockRegistryEvent.getRegistry().register(blockSimple);

            LOGGER.info("DONE!");
        }

        @SubscribeEvent
        public static void onItemsRegistration(final RegistryEvent.Register<Item> itemRegisterEvent) {
            // We need to create a BlockItem so the player can carry this block in their
            // hand and it can appear in the inventory
            final int MAXIMUM_STACK_SIZE = 20; // player can only hold 20 of this block in their hand at once

            Item.Properties itemSimpleProperties = new Item.Properties().stacksTo(MAXIMUM_STACK_SIZE)
                    .tab(CreativeModeTab.TAB_BUILDING_BLOCKS);

            itemBlockSimple = new BlockItem(blockSimple, itemSimpleProperties);
            itemBlockSimple.setRegistryName(blockSimple.getRegistryName());
            itemRegisterEvent.getRegistry().register(itemBlockSimple);
        }
    }

    /**
     * Register common events for both dedicated servers and clients. This method is
     * safe to call directly.
     */
    public void registerCommonEvents(IEventBus eventBus) {
        LOGGER.info("Registring mbe01");
        eventBus.register(com.example.examplemod.mbe01_block_simple.StartupCommon.class);
    }

    public static void registerClientOnlyEvents() {
        MOD_EVENT_BUS.register(com.example.examplemod.mbe01_block_simple.StartupClientOnly.class);
    }

    @SubscribeEvent
    public static void onCommonSetupEvent(FMLCommonSetupEvent event) {
        // not actually required for this example....
    }
}
