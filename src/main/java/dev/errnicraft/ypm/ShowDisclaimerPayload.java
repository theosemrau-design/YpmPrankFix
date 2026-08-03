package dev.errnicraft.ypm;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record ShowDisclaimerPayload() implements CustomPayload {
    public static final CustomPayload.Id<ShowDisclaimerPayload> ID = new CustomPayload.Id<>(Identifier.of("ypm", "show_disclaimer"));
    public static final PacketCodec<RegistryByteBuf, ShowDisclaimerPayload> CODEC = PacketCodec.unit(new ShowDisclaimerPayload());

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static class Companion {
        public static CustomPayload.Id<ShowDisclaimerPayload> getID() {
            return ID;
        }
        public static PacketCodec<RegistryByteBuf, ShowDisclaimerPayload> getCODEC() {
            return CODEC;
        }
    }
}
