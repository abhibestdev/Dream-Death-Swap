package me.abhi.deathswap.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;

@AllArgsConstructor
public class PlayerEvent extends BaseEvent {

    @Getter private Player player;
}
