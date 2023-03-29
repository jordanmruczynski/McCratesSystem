package pl.jordii.mccratessystem.commands;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.*;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.units.qual.C;
import pl.jordii.mccratessystem.CratesRepository;
import pl.jordii.mccratessystem.McCratesSystem;
import pl.jordii.mccratessystem.database.MySQLCrateService;
import pl.jordii.mccratessystem.database.MySQLLocationService;
import pl.jordii.mccratessystem.database.MySQLUserService;
import pl.jordii.mccratessystem.database.model.Crate;
import pl.jordii.mccratessystem.database.model.ParticlesAnimationType;
import pl.jordii.mccratessystem.ui.CrateMenu;
import pl.jordii.mccratessystem.util.ColorNmFormatter;

import java.util.List;
import java.util.function.Predicate;


public class CrateCommand {

    private MySQLCrateService crateService = CratesRepository.getMySQLCrateService();
    private MySQLLocationService locationService = CratesRepository.getMySQLLocationService();
    private MySQLUserService userService = CratesRepository.getMySQLUserService();
    private Predicate<String> testIfCrateExists = crateName -> crateService.find(crateName) != null;
    private ArgumentSuggestions availableCratesSuggestion = ArgumentSuggestions.strings(CratesRepository.getCratesList().keySet());

    private CommandAPICommand openCrateMenu = new CommandAPICommand("openmenu")
            .withArguments(new StringArgument("crateName").replaceSuggestions(availableCratesSuggestion))
            .executes((sender, args) -> {
                new CrateMenu((Player) sender, CratesRepository.getCratesList().get((String) args[0]));
            });

    private CommandAPICommand forceOpenCrateMenu = new CommandAPICommand("forceopenmenu")
            .withArguments(new StringArgument("crateName").replaceSuggestions(availableCratesSuggestion))
            .withArguments(new PlayerArgument("player"))
            .executes((sender, args) -> {;
                new CrateMenu((Player) args[1], CratesRepository.getCratesList().get((String) args[0]));
            });

    private CommandAPICommand createCrate = new CommandAPICommand("create")
            .withArguments(new StringArgument("crateName"))
            .withArguments(new StringArgument("player"))
            .withArguments(new StringArgument("particleColor").replaceSuggestions(ArgumentSuggestions.strings(ColorNmFormatter.getColors())))
            .executes((sender, args) -> {
                final Player player = (Player) sender;
                if (!testIfCrateExists.test((String) args[0])) {
                    player.sendMessage("crate already exists");
                } else {
                    crateService.create(new Crate((String) args[0], (String) args[1], Lists.newArrayList(player.getInventory().getContents()), true, ParticlesAnimationType.BOUNDING_BOX, (String) args[2]));
                    player.sendMessage("crate has been created.");
                }
            });

    private CommandAPICommand locationAdd = new CommandAPICommand("add")
            .withArguments(new StringArgument("crateName").replaceSuggestions(availableCratesSuggestion))
            .executes((sender, args) -> {
                final Player player = (Player) sender;
                final Block block = player.getTargetBlockExact(2);
                if (block != null && block.getLocation() != null) {
                        locationService.save((String) args[0], block.getLocation());
                        player.sendMessage("location has been added.");

                } else {
                    player.sendMessage("you must look at the block.");
                }
            });

    private CommandAPICommand keysGive = new CommandAPICommand("give")
            .withArguments(new StringArgument("crateName").replaceSuggestions(availableCratesSuggestion))
            .withArguments(new IntegerArgument("amount", 1))
            .withArguments(new OfflinePlayerArgument("player"))
            .executes((sender, args) -> {
                Player player = (Player) args[2];
                userService.keys(player.getUniqueId(), (String) args[0]).thenAccept(keys -> {
                    if (keys <= 0) {
                        userService.delete(player.getUniqueId(), (String) args[0]);
                        userService.save(player.getUniqueId(), player.getName(), (String) args[0], (int) args[1]);
                    } else {
                        userService.update(player.getUniqueId(), player.getName(), (String) args[0], (keys + (int) args[1]));
                    }
                    sender.sendMessage("keys has been given.");
                });
            });

    private CommandAPICommand keysTake = new CommandAPICommand("take")
            .withArguments(new StringArgument("crateName").replaceSuggestions(availableCratesSuggestion))
            .withArguments(new IntegerArgument("amount", 1))
            .withArguments(new OfflinePlayerArgument("player"))
            .executes((sender, args) -> {
                Player player = (Player) args[2];
                    userService.keys(player.getUniqueId(), (String) args[0]).thenAccept(keys -> {
                        if (keys > 0) {
                            userService.update(player.getUniqueId(), player.getName(), (String) args[0], (keys - (int) args[1]));
                        } else {
                            sender.sendMessage("player has no keys.");
                        }
                        sender.sendMessage("keys has been taken.");
                    });
            });

    private CommandAPICommand keysSet = new CommandAPICommand("set")
            .withArguments(new StringArgument("crateName").replaceSuggestions(availableCratesSuggestion))
            .withArguments(new IntegerArgument("amount", 1))
            .withArguments(new OfflinePlayerArgument("player"))
            .executes((sender, args) -> {
                Player player = (Player) args[2];
                userService.update(player.getUniqueId(), player.getName(), (String) args[0], (int) args[1]);
                sender.sendMessage("keys has been set.");
            });

    private CommandAPICommand keysCheck = new CommandAPICommand("check")
            .withArguments(new OfflinePlayerArgument("player"))
            .executes((sender, args) -> {
                Player player = (Player) args[0];
                sender.sendMessage("");
                crateService.findAll().thenAccept(crates -> {
                    crates.forEach(crate -> {
                        userService.keys(player.getUniqueId(), crate.getCrateName()).thenAccept(keys -> {
                            sender.sendMessage("§bCrate: §f" + crate.getCrateName() + " §7| §bKeys: §f" + keys);
                        });
                    });
                    sender.sendMessage("");
                });
            });

    private CommandAPICommand locationList = new CommandAPICommand("list")
            .executes((sender, args) -> {
                final Player player = (Player) sender;
                player.sendMessage("");
                locationService.findAllWithIds().thenAccept(locations -> {
                    locations.forEach((loc, crates) -> {
                        crates.forEach((id, crate) -> {
                            TextComponent message = new TextComponent("§bID: §f" + id + " §7| §bCrate: §f" + crate + " §7| §bLocation: §7(§f" + loc.getBlockX() + " X §7| §f" + loc.getBlockY() + " Y §7| §f" + loc.getBlockZ() + " Z §7| §f" + loc.getWorld().getName() + "§7)");
                            message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§6Click, to teleport!").create()));
                            message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tp " + loc.getBlockX() + " " + ((loc.getBlockY())+1) + " " + loc.getBlockZ()));
                            player.spigot().sendMessage(message);
                        });
                    });
                }).whenComplete((aVoid, throwable) -> {
                    player.sendMessage("§7Click on the message to teleport to this location!");
                    player.sendMessage("");
                });

            });

    private CommandAPICommand locationRemove = new CommandAPICommand("remove")
            .withArguments(new IntegerArgument("id"))
            .executes((sender, args) -> {
                final Player player = (Player) sender;
                    try {
                        locationService.delete((Integer) args[0]);
                        player.sendMessage("location has been removed.");
                    } catch (Exception e) {
                        player.sendMessage("location not found.");
                    }

            });

    private CommandAPICommand editDisplayname = new CommandAPICommand("setdisplayname")
            .withArguments(new StringArgument("crateName").replaceSuggestions(availableCratesSuggestion))
            .withArguments(new TextArgument("displayName"))
            .executes((sender, args) -> {
                crateService.find((String) args[0]).thenAccept(crate -> {
                    if (crate != null) {
                        crate.setDisplayName((String) args[1]);
                        crateService.update(crate);
                        sender.sendMessage("display name has been set.");
                    } else {
                        sender.sendMessage("crate not found.");
                    }
                });
            });

    private CommandAPICommand editParticlesAnimation = new CommandAPICommand("setparticlesanimation")
            .withArguments(new StringArgument("crateName").replaceSuggestions(availableCratesSuggestion))
            .withArguments(new StringArgument("animationName").replaceSuggestions(ArgumentSuggestions.strings(ParticlesAnimationType.BOUNDING_BOX.getName())))
            .executes((sender, args) -> {
                crateService.find((String) args[0]).thenAccept(crate -> {
                    if (crate != null) {
                        crate.setParticlesAnimationType(ParticlesAnimationType.BOUNDING_BOX);
                        crateService.update(crate);
                        sender.sendMessage("particles animation type has been set.");
                    } else {
                        sender.sendMessage("crate not found.");
                    }
                });
            });

    private CommandAPICommand editParticlesColor = new CommandAPICommand("setparticlescolor")
            .withArguments(new StringArgument("crateName").replaceSuggestions(availableCratesSuggestion))
            .withArguments(new StringArgument("particlesColor").replaceSuggestions(ArgumentSuggestions.strings(ColorNmFormatter.getColors())))
            .executes((sender, args) -> {
                crateService.find((String) args[0]).thenAccept(crate -> {
                    if (crate != null) {
                        crate.setParticlesColor((String) args[1]);
                        crateService.update(crate);
                        sender.sendMessage("particles color has been set.");
                    } else {
                        sender.sendMessage("crate not found.");
                    }
                });
            });

    private CommandAPICommand setItems = new CommandAPICommand("set")
            .withArguments(new StringArgument("crateName").replaceSuggestions(availableCratesSuggestion))
            .executes((sender, args) -> {
                crateService.find((String) args[0]).thenAccept(crate -> {
                    if (crate != null) {
                        Player player = (Player) sender;
                        crate.setItems(Lists.newArrayList(player.getInventory().getContents()));
                        crateService.update(crate);
                        sender.sendMessage("items has been set.");
                    } else {
                        sender.sendMessage("crate not found.");
                    }
                });
            });

    private CommandAPICommand addItem = new CommandAPICommand("add")
            .withArguments(new StringArgument("crateName").replaceSuggestions(availableCratesSuggestion))
            .withArguments(new ItemStackArgument("item"))
            .executes((sender, args) -> {
                crateService.find((String) args[0]).thenAccept(crate -> {
                    if (crate != null) {
                        List<ItemStack> items = crate.getItems();
                        items.add((ItemStack) args[1]);
                        crate.setItems(items);
                        crateService.update(crate);
                        sender.sendMessage("item has been set.");
                    } else {
                        sender.sendMessage("crate not found.");
                    }
                });
            });

    private CommandAPICommand getItems = new CommandAPICommand("get")
            .withArguments(new StringArgument("crateName").replaceSuggestions(availableCratesSuggestion))
            .executes((sender, args) -> {
                crateService.find((String) args[0]).thenAccept(crate -> {
                    if (crate != null) {
                        Player player = (Player) sender;
                        Inventory inventory = Bukkit.createInventory(null, 54, "§bItemy skrzynki: §f" + crate.getCrateName());
                        crate.getItems().forEach(i -> inventory.addItem(i));
                        player.openInventory(inventory);
                    } else {
                        sender.sendMessage("crate not found.");
                    }
                });
            });

    private CommandAPICommand locationGroup = new CommandAPICommand("locations")
            .withSubcommands(locationAdd, locationList, locationRemove);

    private CommandAPICommand keysGroup = new CommandAPICommand("keys")
            .withSubcommands(keysGive, keysTake, keysSet, keysCheck);

    private CommandAPICommand editGroup = new CommandAPICommand("edit")
            .withSubcommands(editDisplayname, editParticlesAnimation, editParticlesColor);

    private CommandAPICommand itemsGroup = new CommandAPICommand("items")
            .withSubcommands(setItems, addItem, getItems);

    public CrateCommand() {
        new CommandAPICommand("crates")
                .withAliases("crate", "skrzynki", "premiumcase", "premiumcases", "magiczneskrzynki", "magiccrates")
                .withPermission("crates.admin")
                .withSubcommands(openCrateMenu, forceOpenCrateMenu, createCrate, locationGroup, keysGroup, editGroup, itemsGroup)
                .register();
    }
}
