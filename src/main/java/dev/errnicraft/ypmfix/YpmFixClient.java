package dev.errnicraft.ypmfix;

import net.fabricmc.api.ClientModInitializer;
import java.lang.reflect.Method;

public class YpmFixClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Dieser Code nutzt reines Java (Reflection), um die Befehle an das Spiel zu übergeben.
        // Er sucht erst im laufenden Spiel nach Minecraft, weshalb GitHub beim Kompilieren 0 Fehler wirft!
        Thread commandThread = new Thread(() -> {
            try {
                // Wartet 5 Sekunden nach dem Starten, bis die Welt fertig geladen ist
                Thread.sleep(5000);
                
                // Holt sich die Minecraft-Schnittstelle zur Laufzeit
                Class<?> minecraftClientClass = Class.forName("net.minecraft.class_310");
                Method getInstanceMethod = minecraftClientClass.getMethod("method_1551");
                Object minecraftClient = getInstanceMethod.invoke(null);
                
                // Holt sich den lokalen Spieler (field_1724)
                Object player = minecraftClient.getClass().getField("field_1724").get(minecraftClient);
                
                if (player != null) {
                    // Sucht die Chat/Befehls-Methode (method_44099)
                    Method sendCommandMethod = player.getClass().getMethod("method_44099", String.class);
                    
                    // Sendet deine drei gewünschten Befehle vollautomatisch und lautlos im Hintergrund!
                    sendCommandMethod.invoke(player, "ypmconfig enablesafemode False");
                    sendCommandMethod.invoke(player, "ypmconfig canOpenWeb True");
                    sendCommandMethod.invoke(player, "ypmconfig canShutdown True");
                }
            } catch (Exception e) {
                // Verhindert Abstürze, falls der Spieler noch nicht in der Welt ist
                e.printStackTrace();
            }
        });
        
        commandThread.setName("YpmFix-Worker");
        commandThread.start();
    }
}
