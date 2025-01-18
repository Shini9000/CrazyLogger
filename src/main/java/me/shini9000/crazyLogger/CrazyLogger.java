package me.shini9000.crazyLogger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public class CrazyLogger extends JavaPlugin implements Listener {

    private int counter;

    @Override
    public void onEnable() {
        Bukkit.getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new Logger(this), this);
        registerConfig();
    }

    private void registerConfig() {
        saveDefaultConfig();
    }



    // THIS IS USELESS THERE IS NO MENU WITH THIS NAME
    // OLD CODE????
//    @EventHandler
//    public void inventoryClick(InventoryClickEvent e) {
//        if (e.getView().getTitle().equalsIgnoreCase(ChatColor.RED + "CrazyLogger")) {
//            //e.setCancelled(true);
//            Bukkit.getServer().broadcastMessage("click event worked");
//
//            if (e.getCurrentItem() != null && e.getCurrentItem().getType().equals(Material.COAL)) {
//                e.getWhoClicked().closeInventory();
//
//                Inventory gui2 = getServer().createInventory(null, 54, ChatColor.RED + "CrazyLogger Page 2");
//                ItemStack coal = new ItemStack(Material.COAL, 1);
//                gui2.addItem(coal);
//                e.getWhoClicked().openInventory(gui2);
//
//                counter = 0;
//                Player player = (Player) e.getWhoClicked();
//                ConfigurationSection configSection = getConfig().getConfigurationSection("Data." + player.getName().toLowerCase() + "." + "info");
//
//                Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
//                    if (configSection != null) {
//                        List<ItemStack> itemsToAdd = new ArrayList<>();
//                        for (String key : configSection.getKeys(false)) {
//                            counter++;
//                            if (counter > 64) {
//                                String format1 = configSection.getString(key);
//                                String format2 = format1.replace("Crate:", "C:");
//                                String format3 = format2.replace("Reward:", "R:");
//
//                                ItemStack book = new ItemStack(Material.BOOK, 1);
//                                ItemMeta itemM = book.getItemMeta();
//                                itemM.setLore(Arrays.asList(format3));
//                                itemM.setDisplayName(key);
//
//                                book.setItemMeta(itemM);
//                                itemsToAdd.add(book);
//                            }
//                        }
//
//                        Bukkit.getScheduler().runTask(this, () -> {
//                            for (ItemStack item : itemsToAdd) {
//                                gui2.addItem(item);
//                            }
//                        });
//                    }
//                });
//            }
//        }
//    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("crazylogger") && sender instanceof Player) {
            if (!sender.hasPermission("crazylogger.lookup")) {
                sender.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
                return true;
            }

            if (args.length < 1) {
                sender.sendMessage(ChatColor.RED + "Please provide a player name.");
                return true;
            }

            String playerName = args[0].toLowerCase();
            int page = 1;
            if (args.length > 1) {
                try {
                    page = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    sender.sendMessage(ChatColor.RED + "Invalid page number.");
                    return true;
                }
            }

            ConfigurationSection configSection = getConfig().getConfigurationSection("Data." + playerName + "." + "info");
            if (configSection == null || configSection.getKeys(false).size() == 0) {
                sender.sendMessage(ChatColor.RED + "Could not find that person in the data.");
                return true;
            }

            int pageSize = 45;
            int totalEntries = configSection.getKeys(false).size();
            int totalPages = (int) Math.ceil((double) totalEntries / pageSize);

            if (page > totalPages) {
                sender.sendMessage(ChatColor.RED + "Page " + page + " does not exist.");
                return true;
            }

            Inventory gui = getServer().createInventory(null, 54, ChatColor.LIGHT_PURPLE + "CrazyLogger Page " + page);
            int startIndex = (page - 1) * pageSize;
            int endIndex = startIndex + pageSize;
            int currentIndex = 0;

            for (String key : configSection.getKeys(false)) {
                if (currentIndex >= startIndex && currentIndex < endIndex) {
                    String format = configSection.getString(key).replace("Crate:", "C:").replace("Reward:", "R:");
                    ItemStack book = new ItemStack(Material.BOOK, 1);
                    ItemMeta itemMeta = book.getItemMeta();
                    itemMeta.setLore(Arrays.asList(format));
                    itemMeta.setDisplayName(key);
                    book.setItemMeta(itemMeta);
                    gui.addItem(book);
                }
                if (currentIndex >= endIndex) {
                    break;
                }
                currentIndex++;
            }

            // Add navigation items. Will figure this out later
            //if (page > 1) {
            // Previous page shit here
            //}
            //if (page < totalPages) {
            // Work on the next page shit here
            //}

            sender.sendMessage(ChatColor.BLUE + playerName + "'s Data - Page " + page + " of " + totalPages);
            ((Player) sender).openInventory(gui);

            return true;
        }
        return false;
    }
}