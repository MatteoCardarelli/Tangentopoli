package com.example.tangentopoli.serialization;

import com.example.tangentopoli.engine.GameEngine;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameSaveManager {

    private static final String SAVE_DIRECTORY = "saves/";

    public List<String> getSavedGames() {
        List<String> savedGames = new ArrayList<>();
        File directory = new File(SAVE_DIRECTORY);

        if (directory.exists() && directory.isDirectory()) {
            File[] saveFiles = directory.listFiles((dir, name) -> name.endsWith(".ser"));
            if (saveFiles != null) {
                for (File file : saveFiles) {
                    savedGames.add(file.getName());
                }
            }
        }

        return savedGames;
    }
}
