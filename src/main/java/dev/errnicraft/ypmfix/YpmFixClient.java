package dev.errnicraft.ypmfix;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.loader.api.FabricLoader;
import dev.errnicraft.ypm.ShowDisclaimerPayload;

public class YpmFixClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        if (!FabricLoader.getInstance().isModLoaded("ypm")) {
            return;
        }

        // Registriert das Fake-Paket im System, damit Minecraft 1.21 es akzeptiert
        PayloadTypeRegistry.playS2C().register(ShowDisclaimerPayload.ID, ShowDisclaimerPayload.CODEC);

        // Fängt den Kanal ab und leitet ihn ins Leere (Disclaimer blockiert!)
        ClientPlayNetworking.registerGlobalReceiver(ShowDisclaimerPayload.ID, (payload, context) -> {
            // Absichtlich leer – der Disclaimer blockiert!
        });

        // Führt alle gewünschten Befehle beim Weltbeitritt vollautomatisch aus
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            if (client.getNetworkHandler() != null) {
                // Befehl 1: safeMode ausschalten
                client.getNetworkHandler().sendCommand("ypmconfig safeMode False");
                
                // Befehl 2: canOpenWeb einschalten
                client.getNetworkHandler().sendCommand("ypmconfig canOpenWeb True");
                
                // Befehl 3: canShutdown einschalten
                client.getNetworkHandler().sendCommand("ypmconfig canShutdown True");
            }
        });
    }
}
