package com.example.examplemod.mbe01_block_simple;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * User: The Grey Ghost
 * Date: 24/12/2014
 *
 *  See MinecraftByExample class for more information
 */
public class StartupClientOnly
{
  /**
   * Tell the renderer this is a solid block
   * @param event
   */

  @SubscribeEvent
  public static void onClientSetupEvent(FMLClientSetupEvent event) {
    
    ItemBlockRenderTypes.setRenderLayer(StartupCommon.blockSimple, RenderType.solid());
  }
}
