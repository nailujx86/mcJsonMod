/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.nailuj.mc.json;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Julian Blazek
 */
@Mod(modid = MainMod.MODID, version = MainMod.VERSION)
public class MainMod {

    public static final String MODID = "jsonmod";
    public static final String VERSION = "0.1";
    private static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event) {
    }
    
    @EventHandler
    public void serverStart(FMLServerStartingEvent event) { 
        event.registerServerCommand(new JsonCommand());     
    }

}
