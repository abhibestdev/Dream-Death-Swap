package me.abhi.deathswap.game.command;

import me.abhi.deathswap.DeathSwapPlugin;
import me.abhi.deathswap.command.framework.Command;
import me.abhi.deathswap.command.framework.CommandArgs;
import me.abhi.deathswap.game.GameHandler;
import me.abhi.deathswap.timer.Timer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class GameCommand {

    @Command(name = "game", permission = "op")
    public void game(CommandArgs args) {

        //Send game help
        args.getSender().sendMessage(new String[]{
                ChatColor.RED + "Game Help:",
                ChatColor.RED + " * /game start - Start game",
                ChatColor.RED + " * /game end - Force end game",
        });
    }

    @Command(name = "game.start", permission = "op")
    public void gameStart(CommandArgs args) {
        GameHandler gameHandler = DeathSwapPlugin.getInstance().getGameHandler();

        //Check if game is already started
        if (gameHandler.isStarted()) {
            args.getSender().sendMessage(ChatColor.RED + "The game is already started!");
            return;
        }

        //Set timer to 5 minutes
        Timer timer = new Timer(300, true);

        //Save timer object to GameHandler
        gameHandler.setTimer(timer);

        //Start timer
        timer.start();

        //Mark game started
        gameHandler.setStarted(true);

        Bukkit.broadcastMessage(ChatColor.GREEN.toString() + ChatColor.BOLD + "Death Swap has started! Good luck!");
        return;
    }

    @Command(name = "game.end", permission = "op")
    public void gameEnd(CommandArgs args) {
        GameHandler gameHandler = DeathSwapPlugin.getInstance().getGameHandler();

        //Check if game isn't started
        if (!gameHandler.isStarted()) {
            args.getSender().sendMessage(ChatColor.RED + "The game hasn't started!");
            return;
        }
        //Set game as not started
        gameHandler.setStarted(false);

        //Cancel timer and set it to null
        gameHandler.getTimer().stop();
        gameHandler.setTimer(null);

        args.getSender().sendMessage(ChatColor.RED + "Death Swap has been force ended!");
        return;
    }
}
