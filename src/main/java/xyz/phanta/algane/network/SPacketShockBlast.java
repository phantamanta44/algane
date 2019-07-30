package xyz.phanta.algane.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import xyz.phanta.algane.Algane;

import javax.annotation.Nullable;

@SuppressWarnings("NullableProblems")
public class SPacketShockBlast implements IMessage {

    private Vec3d pos;
    private float radius;
    private float intensity;
    private int colour;

    public SPacketShockBlast(Vec3d pos, float radius, float intensity, int colour) {
        this.pos = pos;
        this.radius = radius;
        this.intensity = intensity;
        this.colour = colour;
    }

    public SPacketShockBlast() {
        // NO-OP
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        pos = new Vec3d(buf.readFloat(), buf.readFloat(), buf.readFloat());
        radius = buf.readFloat();
        intensity = buf.readFloat();
        colour = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeFloat((float)pos.x);
        buf.writeFloat((float)pos.y);
        buf.writeFloat((float)pos.z);
        buf.writeFloat(radius);
        buf.writeFloat(intensity);
        buf.writeInt(colour);
    }

    public static class Handler implements IMessageHandler<SPacketShockBlast, IMessage> {

        @Nullable
        @Override
        public IMessage onMessage(SPacketShockBlast message, MessageContext ctx) {
            Minecraft mc = Minecraft.getMinecraft();
            mc.addScheduledTask(() -> Algane.PROXY.spawnParticleShockBlast(
                    mc.world, message.pos, message.radius, message.intensity, message.colour));
            return null;
        }

    }

}
