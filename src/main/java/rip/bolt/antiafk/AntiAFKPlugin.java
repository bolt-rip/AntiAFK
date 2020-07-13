package rip.bolt.antiafk;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class AntiAFKPlugin extends JavaPlugin implements Listener {

    private Map<Player, Long> lastMoveTimes;

    private static long AFK_DURATION = 5 * 60 * 1000;

    @Override
    public void onEnable() {
        lastMoveTimes = new HashMap<Player, Long>();

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {

            @Override
            public void run() {
                long time = System.currentTimeMillis();
                for (Player player : Bukkit.getOnlinePlayers()) {
                    long lastTime = lastMoveTimes.get(player);
                    if (lastTime + AFK_DURATION <= time)
                        player.kickPlayer("You have been idle for too long!");
                }
            }

        }, 20 * 60, 20 * 60);

        Bukkit.getPluginManager().registerEvents(this, this);
        System.out.println("[AntiAFK] AntiAFK is now enabled!");
    }

    @Override
    public void onDisable() {
        System.out.println("[AntiAFK] AntiAFK is now disabled!");
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        lastMoveTimes.put(event.getPlayer(), System.currentTimeMillis());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        lastMoveTimes.put(event.getPlayer(), System.currentTimeMillis());
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        lastMoveTimes.put(event.getPlayer(), System.currentTimeMillis());
    }

}
