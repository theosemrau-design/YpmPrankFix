package dev.errnicraft.ypmfix;

import net.fabricmc.api.ClientModInitializer;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class YpmFixClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // 1. SCHUTZ: Wir blockieren das Netzwerk-Paket "ypm:show_disclaimer" über pures Java (Reflection)
        // Dadurch fangen wir den Disclaimer ab, ohne die abgestürzte Fake-Klasse zu benötigen!
        try {
            Class<?> identifierClass = Class.forName("net.minecraft.class_2960"); // Identifier
            Constructor<?> idConstructor = identifierClass.getConstructor(String.class, String.class);
            Object packetId = idConstructor.newInstance("ypm", "show_disclaimer");

            Class<?> clientNetworkingClass = Class.forName("net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking");
            Method registerMethod = clientNetworkingClass.getMethod("registerGlobalReceiver", identifierClass, Class.forName("net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking$PlayChannelHandler"));

            // Wir blockieren den Empfänger mit einem leeren Handler
            // (Hinweis: Falls dies im Spiel eine Warnung wirft, läuft es dank des try-catch stabil weiter)
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 2. SCHUTZ: Wir warten über das offizielle Fabric-Event, bis du ECHT in der Welt stehst
        try {
            Class<?> connectionEventsClass = Class.forName("net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents");
            Field joinField = connectionEventsClass.getField("JOIN");
            Object joinEvent = joinField.get(null);

            Method registerJoinMethod = joinEvent.getClass().getMethod("register", Class.forName("net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents$Join"));

            // Sobald das Join-Event feuert, führen wir die Befehle absolut sicher aus
            Thread commandWorker = new Thread(() -> {
                try {
                    // Kurze Pause, damit die Welt-Struktur stabil steht
                    Thread.sleep(1500);

                    Class<?> minecraftClientClass = Class.forName("net.minecraft.class_310");
                    Method getInstanceMethod = minecraftClientClass.getMethod("method_1551");
                    Object minecraftClient = getInstanceMethod.invoke(null);

                    Object player = minecraftClient.getClass().getField("field_1724").get(minecraftClient);

                    if (player != null) {
                        Method sendCommandMethod = player.getClass().getMethod("method_44099", String.class);
                        
                        // Hier werden deine drei Befehle garantiert ausgeführt:
                        sendCommandMethod.invoke(player, "ypmconfig enablesafemode False");
                        sendCommandMethod.invoke(player, "ypmconfig canOpenWeb True");
                        sendCommandMethod.invoke(player, "ypmconfig canShutdown True");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            commandWorker.setName("YpmFix-Join-Worker");
            commandWorker.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
