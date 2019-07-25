package xyz.phanta.algane.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import xyz.phanta.algane.Algane;

import javax.annotation.Nullable;

@SuppressWarnings("NullableProblems")
public class SPacketLaserBeam implements IMessage {

    Vec3d from, to;
    int colour, radius;
    @Nullable
    EnumHand hand;

    public SPacketLaserBeam(Vec3d from, Vec3d to, int colour, int radius, @Nullable EnumHand hand) {
        this.from = from;
        this.to = to;
        this.colour = colour;
        this.radius = radius;
        this.hand = hand;
    }

    public SPacketLaserBeam() {
        // NO-OP
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        from = new Vec3d(buf.readFloat(), buf.readFloat(), buf.readFloat());
        to = new Vec3d(buf.readFloat(), buf.readFloat(), buf.readFloat());
        colour = buf.readInt();
        radius = buf.readInt();
        switch (buf.readByte()) {
            case 1:
                hand = EnumHand.MAIN_HAND;
                break;
            case 2:
                hand = EnumHand.OFF_HAND;
                break;
            default:
                hand = null;
                break;
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeFloat((float)from.x);
        buf.writeFloat((float)from.y);
        buf.writeFloat((float)from.z);
        buf.writeFloat((float)to.x);
        buf.writeFloat((float)to.y);
        buf.writeFloat((float)to.z);
        buf.writeInt(colour);
        buf.writeInt(radius);
        buf.writeByte(hand == null ? 0 : (hand == EnumHand.MAIN_HAND ? 1 : 2));
    }

    public static class Handler implements IMessageHandler<SPacketLaserBeam, IMessage> {

        @Nullable
        @Override
        public IMessage onMessage(SPacketLaserBeam message, MessageContext ctx) {
            Minecraft mc = Minecraft.getMinecraft();
            mc.addScheduledTask(() -> Algane.PROXY.spawnParticleLaserBeam(
                    mc.world, message.from, message.to, message.colour, message.radius, message.hand));
            return null;
        }

    }

}
