package pl.jordii.mccratessystem;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIConfig;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import pl.jordii.mccratessystem.commands.CrateCommand;
import pl.jordii.mccratessystem.config.DataLoader;
import pl.jordii.mccratessystem.database.MySQLCrateService;
import pl.jordii.mccratessystem.database.mysql.MySQLConnection;
import pl.jordii.mccratessystem.placeholderapihook.PlaceholderInject;

import java.util.logging.Logger;

public final class McCratesSystem extends JavaPlugin {

    private static McCratesSystem mcCratesSystem;
    private CratesRepository cratesRepository;

    private static MySQLConnection mySQLConnection;
    private static MySQLCrateService mySQLCrateService;
    private DataLoader dataLoader;

    @Override
    public void onEnable() {
        mcCratesSystem = this;
        dataLoader = new DataLoader();
        mySQLConnection = new MySQLConnection(getDataFolder() + "/mysqlCredentials.json");
        cratesRepository = new CratesRepository(mySQLConnection);

        CommandAPI.onLoad(new CommandAPIConfig().verboseOutput(true));
        CommandAPI.onEnable(this);
        Bukkit.getPluginManager().registerEvents(cratesRepository, this);
        new CrateCommand();
        new PlaceholderInject().register();
    }

    @Override
    public void onDisable() {
        mySQLConnection.closeConnection();
        CommandAPI.onDisable();
    }

    public static McCratesSystem getPluginInstance() {
        return mcCratesSystem;
    }

    public static Logger getPluginLogger() {
        return getPluginInstance().getLogger();
    }

    public static MySQLCrateService getMySQLCrateService() {
        return mySQLCrateService;
    }

}
