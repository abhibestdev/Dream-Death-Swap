package me.abhi.deathswap.game;

import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;
import me.abhi.deathswap.DeathSwapPlugin;
import me.abhi.deathswap.command.CommandHandler;
import me.abhi.deathswap.game.command.GameCommand;
import me.abhi.deathswap.timer.Timer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.function.Consumer;

public class GameHandler {

    @Getter private List<UUID> players;
    @Setter @Getter private boolean started;
    @Setter @Getter private Timer timer;

    public GameHandler() {
        //Initialize players to an empty list
        players = new ArrayList<>();

        //Register commands
        CommandHandler commandHandler = DeathSwapPlugin.getInstance().getCommandHandler();
        commandHandler.registerCommand(new GameCommand());

        //Register listeners
        Bukkit.getPluginManager().registerEvents(new GameListener(), DeathSwapPlugin.getInstance());
    }

    //Method to swap player locations. Wrote it in a way that it works with more than just 2 players
    public void swap() {
        Map<UUID, UUID> toSwapMap = Maps.newHashMap();

        gamePlayerAction(player -> {
            //List of players we can swap the player with
            List<UUID> acceptableSwappees = new ArrayList<>(players);

            //Remove player from list of people they can swap with
            acceptableSwappees.remove(player.getUniqueId());

            toSwapMap.keySet().forEach(uuid -> {
                UUID swappingWith = toSwapMap.get(uuid);

                //Remove players that are already being swapped with
                acceptableSwappees.remove(swappingWith);
            });

            //Shuffle players we can swap with
            Collections.shuffle(acceptableSwappees);

            //If there is less than one player we can swap with, ignore
            if (acceptableSwappees.size() <= 0) return;

            //Get first UUID in the list
            UUID toSwapWith = acceptableSwappees.get(0);

            //Save the player they will be swapping with
            toSwapMap.put(player.getUniqueId(), toSwapWith);
        });

        Map<UUID, Location> locationMap = Maps.newHashMap();

        //Save each player's location in locationMap
        gamePlayerAction(player -> locationMap.put(player.getUniqueId(), player.getLocation().clone()));

        toSwapMap.keySet().forEach(uuid -> {
            Player player = Bukkit.getPlayer(uuid);

            //If player doesn't have someone to swap with, ignore
            if (!toSwapMap.containsKey(uuid)) return;

            Player toSwapWith = Bukkit.getPlayer(toSwapMap.get(uuid));

            //If both player's are online and they have saved locations
            if (player != null && toSwapMap != null && locationMap.containsKey(uuid) && locationMap.containsKey(toSwapWith.getUniqueId())) {

                //Swap locations
                player.teleport(locationMap.get(toSwapWith.getUniqueId()));
                toSwapWith.teleport(locationMap.get(uuid));
            }
        });
    }

    public void checkIfGameCanEnd() {
        //If there isn't just one alive player, ignore
        if (players.size() != 1) return;

        UUID uuid = players.get(0);
        Player winner = Bukkit.getPlayer(uuid);

        //Game is no longer started
        setStarted(false);

        //Stop the timer
        timer.stop();
        setTimer(null);

        Bukkit.broadcastMessage(ChatColor.GREEN.toString() + ChatColor.BOLD + winner.getName() + " has won Death Swap!");
        return;
    }

    public void gamePlayerAction(Consumer<? super Player> action) {
        players.forEach(uuid -> {
            Player player = Bukkit.getPlayer(uuid);

            //Run action on all online players
            if (player != null) action.accept(player);
        });
    }
}
