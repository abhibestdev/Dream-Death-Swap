package me.abhi.deathswap.timer.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.abhi.deathswap.event.BaseEvent;
import me.abhi.deathswap.timer.Timer;

@AllArgsConstructor
public class TimerTickEvent extends BaseEvent {

    @Getter private Timer timer;
}
