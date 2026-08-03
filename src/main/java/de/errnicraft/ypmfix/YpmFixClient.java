package dev.errnicraft.ypmfix;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.util.Identifier;

public class YpmFixClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Da die Mod umbenannt wurde, fangen wir das Paket direkt über die originale ID ab.
        // Das funktioniert immer, egal wie die .jar-Datei auf der Festplatte heißt!
        try {
            // Identifier-Erstellung kompatibel mit älteren und neueren Fabric-Versionen
            Identifier disclaimerPacketId = new Identifier("ypm", "show_disclaimer");
            
            ClientPlayNetworking.registerGlobalReceiver(disclaimerPacketId, (client, handler, buf, responseSender) -> {
                // Das Paket wird abgefangen und ins Leere geleitet -> Der Disclaimer blockiert!
            });
        } catch (Exception e) {
            // Verhindert Spielabstürze, falls die Registrierung fehlschlägt
            e.printStackTrace();
        }

        // Führt deine Befehle beim Weltbeitritt aus
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            if (client.getNetworkHandler() != null) {
                // Führt den SafeMode-Befehl aus (wichtig: ohne "/")
                client.getNetworkHandler().sendCommand("ypmconfig safeMode False");
            }
        });
    }
}
