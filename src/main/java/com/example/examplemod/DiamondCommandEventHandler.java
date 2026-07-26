package com.example.examplemod;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@EventBusSubscriber(modid = "examplemod")
public class DiamondCommandEventHandler {

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        dispatcher.register(
                Commands.literal("diamonds")
                        .executes(context -> {
                            CommandSourceStack source = context.getSource();

                            // Ensure the command is run by a player
                            try {
                                var player = source.getPlayerOrException();
                                ItemStack diamonds = new ItemStack(Items.DIAMOND, 64);

                                boolean added = player.getInventory().add(diamonds);
                                if (!added) {
                                    // Drop on ground if inventory is full
                                    player.drop(diamonds, false);
                                }

                                source.sendSuccess(() -> Component.literal("Gave you a stack of diamonds!"), true);
                                return 1;
                            } catch (Exception e) {
                                source.sendFailure(Component.literal("Only players can run this command."));
                                return 0;
                            }
                        })
        );
    }
}
