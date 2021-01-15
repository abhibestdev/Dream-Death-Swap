package me.abhi.deathswap;

import lombok.Getter;
import me.abhi.deathswap.command.CommandHandler;
import me.abhi.deathswap.game.GameHandler;
import org.bukkit.plugin.java.JavaPlugin;

import java.text.DecimalFormat;

public class DeathSwapPlugin extends JavaPlugin {

    @Getter private static DeathSwapPlugin instance;
    @Getter private static DecimalFormat timeFormat;

    @Getter private CommandHandler commandHandler;
    @Getter private GameHandler gameHandler;

    @Override
    public void onEnable() {
        instance = this;
        timeFormat = new DecimalFormat("00");

        //Register handlers
        registerHandlers();
    }

    private void registerHandlers() {
        commandHandler = new CommandHandler();
        gameHandler = new GameHandler();
    }
}
