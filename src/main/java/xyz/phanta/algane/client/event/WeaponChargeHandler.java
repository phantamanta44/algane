package xyz.phanta.algane.client.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import xyz.phanta.algane.client.sound.ChargingSound;
import xyz.phanta.algane.constant.ResConst;
import xyz.phanta.algane.init.AlganeCaps;
import xyz.phanta.algane.lasergun.LaserGun;
import xyz.phanta.algane.lasergun.core.base.LaserGunCore;
import xyz.phanta.algane.util.AlganeUtils;

import javax.annotation.Nullable;

public class WeaponChargeHandler {

    private final Minecraft mc = Minecraft.getMinecraft();
    @Nullable
    private ISound chargeSound;

    public void playChargeSound(SoundEvent sound) {
        stopChargeSound();
        chargeSound = new ChargingSound(sound.getSoundName());
        mc.getSoundHandler().playSound(chargeSound);
    }

    public void stopChargeSound() {
        if (chargeSound != null) {
            mc.getSoundHandler().stopSound(chargeSound);
        }
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END && event.player == mc.player && !event.player.isHandActive()) {
            stopChargeSound();
        }
    }

    @SubscribeEvent
    public void onHudRender(RenderGameOverlayEvent.Pre event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.CROSSHAIRS) {
            ItemStack stack = mc.player.getActiveItemStack();
            if (stack.hasCapability(AlganeCaps.LASER_GUN, null)) {
                LaserGun gun = AlganeUtils.getItemLaserGun(stack);
                AlganeUtils.getLaserCore(gun)
                        .filter(core -> core.getFiringParadigm() == LaserGunCore.FiringParadigm.CHARGE)
                        .ifPresent(core -> {
                            event.setCanceled(true);
                            ScaledResolution res = event.getResolution();
                            int screenW = res.getScaledWidth(), screenH = res.getScaledHeight();
                            GlStateManager.enableBlend();
                            ResConst.OVERLAY_CHARGE.draw(screenW / 2 - 16, screenH / 2 - 16, 32, 32);
                            int useTicks = mc.player.getItemInUseMaxCount();
                            if (useTicks < 60) {
                                double dx = Math.min(useTicks + event.getPartialTicks(), 60D) / 3.75D;
                                ResConst.OVERLAY_CHARGE.draw(screenW / 2F - dx, screenH / 2F - dx, dx * 2F, dx * 2F);
                            }
                        });
            }
        }
    }

}
