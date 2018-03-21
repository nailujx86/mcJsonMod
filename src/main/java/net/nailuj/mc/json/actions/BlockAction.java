/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.nailuj.mc.json.actions;

import net.minecraft.block.Block;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

/**
 *
 * @author Julian Blazek
 */
public class BlockAction implements ActionInterface {

    private boolean relative = true;
    private int x;
    private int y;
    private int z;
    private String blockName;

    @Override
    public void processAction(MinecraftServer server, ICommandSender sender) {
        BlockPos blockPosition = new BlockPos(x, y, z);
        if (relative) {
            blockPosition = blockPosition.add(sender.getPosition());
        }
        sender.getEntityWorld().setBlockState(blockPosition, Block.getBlockFromName(blockName).getDefaultState(), 3);
    }

}
