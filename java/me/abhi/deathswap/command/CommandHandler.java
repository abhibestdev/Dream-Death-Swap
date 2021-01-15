package me.abhi.deathswap.command;

import me.abhi.deathswap.DeathSwapPlugin;
import me.abhi.deathswap.command.framework.CommandFramework;

public class CommandHandler {

    private CommandFramework commandFramework;

    public CommandHandler() {
        //Initialize CommandFramework. This is how we register our commands.
        commandFramework = new CommandFramework(DeathSwapPlugin.getInstance());
    }

    public void registerCommand(Object o) {
        //Register command
        commandFramework.registerCommands(o);
    }
}
