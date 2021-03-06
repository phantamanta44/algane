package xyz.phanta.algane.client.event;

import io.github.phantamanta44.libnine.util.format.FormatUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.phanta.algane.client.AlganeClientConfig;
import xyz.phanta.algane.constant.ResConst;
import xyz.phanta.algane.init.AlganeCaps;
import xyz.phanta.algane.lasergun.LaserGun;
import xyz.phanta.algane.util.AlganeUtils;

public class WeaponOverlayRenderer {

    private final Minecraft mc = Minecraft.getMinecraft();

    @SubscribeEvent(receiveCanceled = true)
    public void onHudRender(RenderGameOverlayEvent.Post event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR) {
            GlStateManager.enableBlend();
            ItemStack stack = mc.player.getHeldItemMainhand();
            LaserGun gun = null;
            if (stack.hasCapability(AlganeCaps.LASER_GUN, null)) {
                gun = stack.getCapability(AlganeCaps.LASER_GUN, null);
            } else {
                stack = mc.player.getHeldItemOffhand();
                if (stack.hasCapability(AlganeCaps.LASER_GUN, null)) {
                    gun = stack.getCapability(AlganeCaps.LASER_GUN, null);
                }
            }
            if (gun != null) {
                ScaledResolution res = event.getResolution();
                int offX = AlganeClientConfig.hudOffsetX, offY = AlganeClientConfig.hudOffsetY;
                int yEnd = ((res.getScaledWidth() / 2 - 91 - offX) > 256)
                        ? (res.getScaledHeight() - offY) : (res.getScaledHeight() - 22 - offY);
                ResConst.OVERLAY_WEAPON_BG.draw(offX, yEnd - 32);
                float energy = AlganeUtils.getLaserEnergy(gun).map(energyCell -> {
                    float stored = energyCell.getEnergyStored();
                    ResConst.OVERLAY_WEAPON_ENERGY.drawPartial(offX + 6, yEnd - 26,
                            0F, 0F, stored / (float)energyCell.getMaxEnergyStored(), 1F);
                    return stored;
                }).orElse(0F);
                float heat = gun.getOverheat();
                if (heat > 0) {
                    if (gun.isHeatLocked()) {
                        GlStateManager.color(1F, 1F, 1F, 0.75F + 0.25F * (float)Math.sin(System.currentTimeMillis() * 0.0125664D));
                        ResConst.OVERLAY_WEAPON_HEAT_LOCK.drawPartial(offX + 129, yEnd - 26, 0F, 0F, heat / 100F, 1F);
                    } else {
                        ResConst.OVERLAY_WEAPON_HEAT.drawPartial(offX + 129, yEnd - 26, 0F, 0F, heat / 100F, 1F);
                    }
                }
                mc.fontRenderer.drawStringWithShadow((int)Math.floor(heat) + "%", offX + 260, yEnd - 29, 0xFF6565);
                mc.fontRenderer.drawStringWithShadow(FormatUtils.formatSI(energy, "FE"), offX + 260, yEnd - 14, 0x6565FF);
                GlStateManager.color(1F, 1F, 1F, 1F);
            }
        }
    }

}
