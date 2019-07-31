package xyz.phanta.algane.client.gui;

import io.github.phantamanta44.libnine.client.gui.L9GuiContainer;
import io.github.phantamanta44.libnine.util.format.FormatUtils;
import io.github.phantamanta44.libnine.util.render.GuiUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.energy.IEnergyStorage;
import xyz.phanta.algane.constant.LangConst;
import xyz.phanta.algane.constant.ResConst;
import xyz.phanta.algane.inventory.ContainerLaserTurret;
import xyz.phanta.algane.item.block.ItemBlockLaserTurret;
import xyz.phanta.algane.lasergun.LaserGun;
import xyz.phanta.algane.util.AlganeUtils;
import xyz.phanta.algane.util.TooltipUtils;

public class GuiLaserTurret extends L9GuiContainer {

    private final ContainerLaserTurret cont;

    public GuiLaserTurret(ContainerLaserTurret cont) {
        super(cont, ResConst.GUI_LASER_TURRET.getTexture());
        this.cont = cont;
    }

    @Override
    public void drawForeground(float partialTicks, int mX, int mY) {
        super.drawForeground(partialTicks, mX, mY);
        LaserGun gun = cont.getGun();
        drawContainerName(getDisplayName(gun));
        IEnergyStorage energy = AlganeUtils.getLaserEnergy(gun).orElse(null);
        if (energy != null) {
            ResConst.GUI_LASER_TURRET_ENERGY.drawPartial(29, 28,
                    0F, 0F, energy.getEnergyStored() / (float)energy.getMaxEnergyStored(), 1F);
        }
        float heat = gun.getOverheat() / 100F;
        if (heat > 0) {
            if (gun.isHeatLocked()) {
                GlStateManager.color(1F, 1F, 1F, 0.75F + 0.25F * (float)Math.sin(System.currentTimeMillis() * 0.0125664D));
                ResConst.GUI_LASER_TURRET_HEAT_LOCK.drawPartial(29, 48, 0F, 0F, heat, 1F);
                GlStateManager.color(1F, 1F, 1F, 1F);
            } else {
                ResConst.GUI_LASER_TURRET_HEAT.drawPartial(29, 48, 0F, 0F, heat, 1F);
            }
        }
        if (GuiUtils.isMouseOver(29, 28, 118, 16, mX, mY)) {
            if (energy != null) {
                drawTooltip(TooltipUtils.formatFractionTooltip(LangConst.TT_ENERGY,
                        FormatUtils.formatSI(energy.getEnergyStored(), "FE"),
                        FormatUtils.formatSI(energy.getMaxEnergyStored(), "FE")), mX, mY);
            } else {
                drawTooltip(TextFormatting.RED + I18n.format(LangConst.STATUS_GUN_MISSING_ENERGY), mX, mY);
            }
        } else if (GuiUtils.isMouseOver(29, 48, 118, 3, mX, mY)) {
            drawTooltip(TooltipUtils.formatPercentTooltip(LangConst.TT_HEAT, heat), mX, mY);
        }
    }

    private static String getDisplayName(LaserGun gun) {
        return ItemBlockLaserTurret.getDisplayName(gun).orElse(I18n.format(LangConst.GUI_LASER_TURRET));
    }

}
