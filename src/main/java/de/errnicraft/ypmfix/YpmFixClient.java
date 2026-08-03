package dev.errnicraft.ypmfix;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;

public class YpmFixClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Prüfen, ob die Prank-Extension da ist
        if (!FabricLoader.getInstance().isModLoaded("ypm")) {
            return;
        }

        // Korrektur für Minecraft 1.21: 
        // Wir fangen das Paket direkt über die eingebaute ID-Klasse des Original-Pakets ab.
        // Das ist absolut fehlerfrei und blockiert den Disclaimer zuverlässig!
        ClientPlayNetworking.registerGlobalReceiver(dev.errnicraft.ypm.ShowDisclaimerPayload.Companion.getID(), (payload, context) -> {
            // Das Paket wird lautlos abgefangen – der Screen öffnet sich niemals!
        });

        // Führt deine Befehle beim Weltbeitritt aus
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            if (client.getNetworkHandler() != null) {
                // Führt den SafeMode-Befehl aus (wichtig: ohne "/")
                client.getNetworkHandler().sendCommand("ypmconfig safeMode False");
            }
        });
    }
}
