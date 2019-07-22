package xyz.phanta.algane.client.gui.component;

import io.github.phantamanta44.libnine.client.gui.component.GuiComponent;
import io.github.phantamanta44.libnine.util.render.TextureRegion;
import net.minecraft.client.resources.I18n;
import xyz.phanta.algane.constant.ResConst;

public class GuiComponentSubmit extends GuiComponent {

    private boolean disabled;
    private final String tooltip;
    private final Runnable callback;

    public GuiComponentSubmit(int x, int y, boolean disabled, String tooltip, Runnable callback) {
        super(x, y, 13, 13);
        this.disabled = disabled;
        this.tooltip = tooltip;
        this.callback = callback;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    @Override
    public void render(float partialTicks, int mX, int mY, boolean mouseOver) {
        getTexture(mouseOver).draw(x, y, width, height);
    }

    private TextureRegion getTexture(boolean mouseOver) {
        return disabled ? ResConst.GUI_COMP_SUBMIT_DISABLED
                : (mouseOver ? ResConst.GUI_COMP_SUBMIT_HOVERED : ResConst.GUI_COMP_SUBMIT_NORMAL);
    }

    @Override
    public boolean onClick(int mX, int mY, int button, boolean mouseOver) {
        if (mouseOver && !disabled && button == 0) {
            callback.run();
            playClickSound();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void renderTooltip(float partialTicks, int mX, int mY) {
        drawTooltip(I18n.format(tooltip), mX, mY);
    }

}
