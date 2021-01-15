package me.abhi.deathswap.timer;

import lombok.Getter;
import lombok.Setter;
import me.abhi.deathswap.DeathSwapPlugin;
import me.abhi.deathswap.timer.event.TimerTickEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class Timer {

    @Setter @Getter private int time;
    private BukkitTask task;
    private boolean countdown;

    public Timer(int time, boolean countdown) {
        this.time = time;
        this.countdown = countdown;
    }

    public void start() {
        task = new BukkitRunnable() {
            public void run() {

                if (countdown) {
                    //Countdown time
                    time--;

                    //Cancel task if time is 0
                    if (time == 0) {
                        stop();
                    }
                    //Add time because we aren't counting down
                } else {
                    time++;
                }

                //Call TimerTickEvent everytime the timer updates
                TimerTickEvent timerTickEvent = new TimerTickEvent(Timer.this);
                timerTickEvent.call();
            }
        }.runTaskTimerAsynchronously(DeathSwapPlugin.getInstance(), 20L, 20L);
    }

    //Cancel task
    public void stop() {
        task.cancel();
    }

    public String getFormattedTime() {
        int seconds = time % 60;
        int minutes = (time / 60) % 60;

        return DeathSwapPlugin.getTimeFormat().format(minutes) + ":" + DeathSwapPlugin.getTimeFormat().format(seconds);
    }

}