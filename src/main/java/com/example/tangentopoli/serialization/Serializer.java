package com.example.tangentopoli.serialization;

import com.example.tangentopoli.controller.BoardController;
import com.example.tangentopoli.model.Game;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Serializer implements Serializable {
    public static void serialize(Game game) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("game.ser"))) {
            oos.writeObject(game);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
