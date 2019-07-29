package xyz.phanta.algane.client.sound;

import io.github.phantamanta44.libnine.client.sound.RepeatingSound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;

public class ChargingSound extends RepeatingSound {

    private int age = 0;

    public ChargingSound(ResourceLocation resource) {
        super(resource, 1F, 0F, SoundCategory.MASTER);
    }

    @Override
    public void update() {
        if (age < 60) {
            ++age;
        }
    }

    @Override
    public float getPitch() {
        return age == 60 ? 1F : (0.5F + 0.5F * age / 60F);
    }

}
