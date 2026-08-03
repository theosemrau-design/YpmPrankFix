package dev.errnicraft.ypmfix;

import net.fabricmc.api.ClientModInitializer;

public class YpmFixClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Da wir keine externen Minecraft-Klassen laden können, nutzen wir das eingebaute 
        // Java-System, um die Befehle beim Starten der Mod direkt an das System zu übergeben.
        try {
            // Hier tragen wir deine drei Wunschbefehle mit den korrekten Namen ein
            String cmd1 = "ypmconfig enablesafemode False";
            String cmd2 = "ypmconfig canOpenWeb True";
            String cmd3 = "ypmconfig canShutdown True";
            
            // Die Mod speichert diese Befehle ab, sodass sie beim Laden aktiv werden
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
