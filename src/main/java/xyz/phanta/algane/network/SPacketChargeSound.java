package xyz.phanta.algane.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import xyz.phanta.algane.Algane;

import javax.annotation.Nullable;

public class SPacketChargeSound implements IMessage {

    private byte type;

    public SPacketChargeSound(byte type) {
        this.type = type;
    }

    public SPacketChargeSound() {
        // NO-OP
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        type = buf.readByte();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeByte(type);
    }

    public static class Handler implements IMessageHandler<SPacketChargeSound, IMessage> {

        @Nullable
        @Override
        public IMessage onMessage(SPacketChargeSound message, MessageContext ctx) {
            Minecraft mc = Minecraft.getMinecraft();
            //noinspection Convert2Lambda
            mc.addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    switch (message.type) {
                        case 0:
                            Algane.PROXY.stopChargeFx(mc.world, mc.player);
                            break;
                        case 1:
                            Algane.PROXY.playOrbChargeFx(mc.world, mc.player);
                            break;
                        case 2:
                            Algane.PROXY.playGaussChargeFx(mc.world, mc.player);
                            break;
                    }
                }
            });
            return null;
        }

    }

}
