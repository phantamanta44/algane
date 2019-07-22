package xyz.phanta.algane.client.gui;

import io.github.phantamanta44.libnine.client.gui.L9GuiContainer;
import io.github.phantamanta44.libnine.util.format.FormatUtils;
import io.github.phantamanta44.libnine.util.render.GuiUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import xyz.phanta.algane.client.gui.component.GuiComponentSubmit;
import xyz.phanta.algane.constant.LangConst;
import xyz.phanta.algane.constant.ResConst;
import xyz.phanta.algane.inventory.ContainerModWorkbench;
import xyz.phanta.algane.lasergun.LaserGunModifier;
import xyz.phanta.algane.util.AlganeUtils;

import javax.annotation.Nullable;

public class GuiModWorkbench extends L9GuiContainer {

    private final ContainerModWorkbench cont;
    private final GuiComponentSubmit submitBtn;
    @Nullable
    private String statDamage = null, statEnergy = null, statHeat = null;

    public GuiModWorkbench(ContainerModWorkbench cont) {
        super(cont, ResConst.GUI_MODIFIER_TABLE);
        this.cont = cont;
        this.submitBtn = new GuiComponentSubmit(94, 57, true, this::onSubmit);
        addComponent(submitBtn);
    }

    @Override
    public void drawForeground(float partialTicks, int mX, int mY) {
        super.drawForeground(partialTicks, mX, mY);
        drawContainerName(I18n.format(LangConst.GUI_MODIFIER_TABLE));
        drawStat(statDamage, 18);
        drawStat(statEnergy, 29);
        drawStat(statHeat, 40);
        if (GuiUtils.isMouseOver(39, 17, 98, 9, mX, mY)) {
            drawTooltip(I18n.format(LangConst.MISC_STAT_DAMAGE), mX, mY);
        } else if (GuiUtils.isMouseOver(39, 28, 98, 9, mX, mY)) {
            drawTooltip(I18n.format(LangConst.MISC_STAT_ENERGY), mX, mY);
        } else if (GuiUtils.isMouseOver(39, 39, 98, 9, mX, mY)) {
            drawTooltip(I18n.format(LangConst.MISC_STAT_HEAT), mX, mY);
        }
        GlStateManager.color(1F, 1F, 1F, 1F);
        GlStateManager.disableLighting();
    }

    private void drawStat(@Nullable String stat, int y) {
        if (stat != null) {
            fontRenderer.drawStringWithShadow(stat, 50, y, 0x43DAE3);
        }
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        LaserGunModifier stats = cont.getCurrentStats();
        if (stats != null) {
            statDamage = FormatUtils.formatPercentage(AlganeUtils.getDamageMultiplier(stats));
            statEnergy = FormatUtils.formatPercentage(AlganeUtils.getEnergyMultiplier(stats));
            statHeat = FormatUtils.formatPercentage(AlganeUtils.getHeatMultiplier(stats));
        } else {
            statDamage = statEnergy = statHeat = null;
        }
        submitBtn.setDisabled(!cont.isOperable());
    }

    private void onSubmit() {
        cont.combine();
    }

}
