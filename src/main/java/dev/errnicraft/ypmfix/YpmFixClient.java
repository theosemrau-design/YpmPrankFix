package dev.errnicraft.ypmfix;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;

public class YpmFixClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Die Mod schaltet sich nur ein, wenn die Haupt-Mod geladen ist
        if (!FabricLoader.getInstance().isModLoaded("ypm")) {
            return;
        }

        // Fängt das Paket über die simulierte Klasse ab und blockiert den Disclaimer komplett
        ClientPlayNetworking.registerGlobalReceiver(dev.errnicraft.ypm.ShowDisclaimerPayload.Companion.getID(), (payload, context) -> {
            // Absichtlich leer – das Paket verpufft wirkungslos im Hintergrund
        });

        // Führt den SafeMode-Befehl beim Einloggen in eine Welt vollautomatisch aus
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            if (client.getNetworkHandler() != null) {
                // Sendet den Befehl unsichtbar an den Server (ohne "/")
                client.getNetworkHandler().sendCommand("ypmconfig safeMode False");
            }
        });
    }
}
