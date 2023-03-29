package pl.jordii.mccratessystem.config;

import pl.jordii.mccratessystem.McCratesSystem;
import pl.jordii.mccratessystem.util.FileCopy;

import java.awt.*;
import java.io.*;

public class DataLoader {

    private final McCratesSystem mcCratesSystem = McCratesSystem.getPluginInstance();
    private final String mysqlConfigFileName = "mysqlCredentials.json";
    private File mysqlConfigFile;

    public DataLoader() {
        setup();
    }

    private void setup() {
        mysqlConfigFile = new File(mcCratesSystem.getDataFolder(), mysqlConfigFileName);

        if (!mcCratesSystem.getDataFolder().exists()) {
            mcCratesSystem.getDataFolder().mkdir();
        }

        if (!mysqlConfigFile.exists()) {
            FileCopy.createFileFromResource(mysqlConfigFileName, mysqlConfigFile);
        }
    }
}