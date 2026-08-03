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

        // Fängt den Kanal ab und leitet ihn ins Leere
        ClientPlayNetworking.registerGlobalReceiver(ShowDisclaimerPayload.ID, (payload, context) -> {
            // Absichtlich leer – der Disclaimer blockiert!
        });

        // Führt den safeMode-Befehl beim Weltbeitritt aus
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            if (client.getNetworkHandler() != null) {
                client.getNetworkHandler().sendCommand("ypmconfig safeMode False");
            }
        });
    }
}
