package me.shini9000.crazyLogger;
import com.badbones69.crazycrates.api.events.PlayerPrizeEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger implements Listener {
    private CrazyLogger plugin;
    public Logger(CrazyLogger plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void prizeLogger(PlayerPrizeEvent e) {
        String p = e.getPlayer().getName().toLowerCase();
        String crate = e.getCrateName();
        String prize = e.getPrize().getDisplayItem(e.getCrate()).getItemMeta().getDisplayName();

        if (e.getPlayer().isOp() || e.getPlayer().hasPermission("crazylogger.exempt")) {
        } else {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            int counter = plugin.getConfig().getInt("Data." + p + ".total") + 1;
            plugin.getConfig().set("Data." + p + ".total", counter);
            plugin.getConfig().set("Data." + p + ".info." + counter, dateFormat.format(date) + " Crate: " + crate + " Reward: " + prize);
            plugin.saveConfig();
        }
    }
}