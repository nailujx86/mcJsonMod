/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.nailuj.mc.json.actions;

import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

/**
 *
 * @author Julian Blazek
 */
public interface ActionInterface {
    public void processAction(MinecraftServer server, ICommandSender sender);
}
