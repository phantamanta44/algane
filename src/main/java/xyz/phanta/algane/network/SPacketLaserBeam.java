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
import java.util.UUID;

@SuppressWarnings("NullableProblems")
public class SPacketLaserBeam implements IMessage {

    private Vec3d from, to;
    private int colour, radius;
    @Nullable
    private UUID ownerId;
    @Nullable
    private EnumHand hand;

    public SPacketLaserBeam(Vec3d from, Vec3d to, int colour, int radius, @Nullable UUID ownerId, @Nullable EnumHand hand) {
        this.from = from;
        this.to = to;
        this.colour = colour;
        this.radius = radius;
        this.ownerId = ownerId;
        this.hand = hand;
    }

    public SPacketLaserBeam() {
        // NO-OP
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public void fromBytes(ByteBuf buf) {
        from = new Vec3d(buf.readFloat(), buf.readFloat(), buf.readFloat());
        to = new Vec3d(buf.readFloat(), buf.readFloat(), buf.readFloat());
        colour = buf.readInt();
        radius = buf.readInt();
        switch (buf.readByte()) {
            case 1:
                hand = EnumHand.MAIN_HAND;
                ownerId = new UUID(buf.readLong(), buf.readLong());
                break;
            case 2:
                hand = EnumHand.OFF_HAND;
                ownerId = new UUID(buf.readLong(), buf.readLong());
                break;
            default:
                hand = null;
                ownerId = null;
                break;
        }
    }

    @SuppressWarnings("DuplicatedCode")
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
        if (ownerId != null) {
            buf.writeLong(ownerId.getMostSignificantBits());
            buf.writeLong(ownerId.getLeastSignificantBits());
        }
    }

    public static class Handler implements IMessageHandler<SPacketLaserBeam, IMessage> {

        @Nullable
        @Override
        public IMessage onMessage(SPacketLaserBeam message, MessageContext ctx) {
            Minecraft mc = Minecraft.getMinecraft();
            mc.addScheduledTask(() -> Algane.PROXY.spawnParticleLaserBeam(
                    mc.world, message.from, message.to, message.colour, message.radius, message.ownerId, message.hand));
            return null;
        }

    }

}
