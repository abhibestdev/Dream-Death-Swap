package me.abhi.deathswap.game;

import me.abhi.deathswap.DeathSwapPlugin;
import me.abhi.deathswap.timer.Timer;
import me.abhi.deathswap.timer.event.TimerTickEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerLoginEvent;

public class GameListener implements Listener {

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        GameHandler gameHandler = DeathSwapPlugin.getInstance().getGameHandler();

        Player player = event.getPlayer();

        //Check if the game has started and the player is not participating
        if (gameHandler.isStarted() && !gameHandler.getPlayers().contains(player.getUniqueId())) {

            //Set gamemode to spectator
            player.setGameMode(GameMode.SPECTATOR);
            return;
        }

        //If player is not participating, add them to list of participants
        if (!gameHandler.getPlayers().contains(player.getUniqueId())) {
            gameHandler.getPlayers().add(player.getUniqueId());
            return;
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        GameHandler gameHandler = DeathSwapPlugin.getInstance().getGameHandler();

        Player player = (Player) event.getEntity();

        //If game hasn't started or the player is not participating, ignore
        if (!gameHandler.isStarted() || !gameHandler.getPlayers().contains(player.getUniqueId())) return;

        //Remove player from list of participants
        gameHandler.getPlayers().remove(player.getUniqueId());

        //Set player's gamemode to spectator and their health to max
        player.setHealth(player.getMaxHealth());
        player.setGameMode(GameMode.SPECTATOR);

        //Check if game can end
        gameHandler.checkIfGameCanEnd();
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        GameHandler gameHandler = DeathSwapPlugin.getInstance().getGameHandler();

        //Check if entity being attacked is a player
        if (event.getEntity() instanceof Player) {

            //Check if player is getting attacked by another player
            if (event.getDamager() instanceof Player) {

                //Cancel because we don't want other players to be able to straight up kill them
                event.setCancelled(true);
                return;
            }

            //Check if player is getting attacked by a projectile
            if (event.getDamager() instanceof Projectile) {
                Projectile projectile = (Projectile) event.getDamager();

                //Check if projectile was shot by a player
                if (projectile.getShooter() instanceof Player) {

                    //Cancel because we don't want other players to be able to straight up kill them
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }

    @EventHandler
    public void onFoodLevel(FoodLevelChangeEvent event) {
        GameHandler gameHandler = DeathSwapPlugin.getInstance().getGameHandler();

        //If game hasn't started, reset hunger
        if (!gameHandler.isStarted()) event.setFoodLevel(20);
    }

    @EventHandler
    public void onTimerTick(TimerTickEvent event) {
        Timer timer = event.getTimer();

        int time = timer.getTime();

        GameHandler gameHandler = DeathSwapPlugin.getInstance().getGameHandler();

        //Ignore if the game hasn't started
        if (!gameHandler.isStarted()) return;

        //Check if time is between 10 and 0
        if (time >= 10 && time > 0) {
            //Announce swap countdown in chat
            Bukkit.broadcastMessage(ChatColor.RED.toString() + ChatColor.BOLD + "Swapping in " + (time) + " second" + (time == 1 ? "" : "s") + "!");

            //Check if timer has reached 0
        } else if (time == 0) {

            //Swap players
            gameHandler.swap();

            //Reset timer back to 5 minutes
            timer.setTime(300);
            return;
        }
    }
}
