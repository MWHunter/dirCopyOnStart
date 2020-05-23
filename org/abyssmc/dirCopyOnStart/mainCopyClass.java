package org.abyssmc.dirCopyOnStart;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class mainCopyClass extends JavaPlugin {
    @Override
    public void onEnable() {
        File deleteConfig = new File(this.getDataFolder() + File.separator + "delete.yml");
        FileConfiguration deleteFileConfig = YamlConfiguration.loadConfiguration(deleteConfig);

        // Make the plugin folder
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }

        // Load the files
        try {
            if (!deleteConfig.exists()) {
                deleteConfig.createNewFile();
                deleteFileConfig = YamlConfiguration.loadConfiguration(deleteConfig); // The file didn't exist yet, so we must load it again now that it exists

                List<String> filesToDelete = new ArrayList<>();
                filesToDelete.add("exampleMainDirectoryToDelete/");
                filesToDelete.add("logs/2020-5-23-01100101.tar.gz");
                deleteFileConfig.set("paths", filesToDelete);
                deleteFileConfig.save(deleteConfig);
            }

        } catch (Exception createException) {
            createException.printStackTrace();
            getLogger().log(Level.SEVERE, "Exception while creating /plugins/dirCopyOnStart/delete.yml, which does not exist!");
        }

        // Delete the folders and files
        List<String> deleteFilesAndDirs = deleteFileConfig.getStringList("paths");
        for (String directory : deleteFilesAndDirs) {
            FileUtils.deleteQuietly(new File(directory));
        }

        // Copy the folders and files
        try {
            File copyTheseFolderToMainDirectory = new File(getDataFolder() + File.separator + "CopyToServerFolder");
            if (!copyTheseFolderToMainDirectory.exists()) {
                copyTheseFolderToMainDirectory.mkdir();
            }
            FileUtils.copyDirectory(copyTheseFolderToMainDirectory, Bukkit.getWorldContainer());

        } catch (Exception copyException) {
            copyException.printStackTrace();
            getLogger().log(Level.SEVERE, "Exception while copying the /plugins/dirCopyOnStart/CopyToServerFolder/ files and folders to the main server directory");
        }
    }
}
