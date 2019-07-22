package xyz.phanta.algane.client.gui;

import io.github.phantamanta44.libnine.client.gui.L9GuiContainer;
import net.minecraft.client.resources.I18n;
import xyz.phanta.algane.client.gui.component.GuiComponentSubmit;
import xyz.phanta.algane.constant.LangConst;
import xyz.phanta.algane.constant.ResConst;
import xyz.phanta.algane.inventory.ContainerGunWorkbench;

public class GuiGunWorkbench extends L9GuiContainer {

    private final ContainerGunWorkbench cont;
    private final GuiComponentSubmit submitBtn;

    public GuiGunWorkbench(ContainerGunWorkbench cont) {
        super(cont, ResConst.GUI_LASER_GUN_TABLE);
        this.cont = cont;
        this.submitBtn = new GuiComponentSubmit(80, 48, true, LangConst.TT_BTN_INSTALL, this::onSubmit);
        addComponent(submitBtn);
    }

    @Override
    public void drawForeground(float partialTicks, int mX, int mY) {
        super.drawForeground(partialTicks, mX, mY);
        drawContainerName(I18n.format(LangConst.GUI_LASER_GUN_TABLE));
    }

    private void onSubmit() {
        cont.attachPart();
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        submitBtn.setDisabled(cont.getAttachmentType() == null);
    }

}
