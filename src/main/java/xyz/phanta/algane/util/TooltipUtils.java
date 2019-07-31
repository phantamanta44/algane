package xyz.phanta.algane.util;

import io.github.phantamanta44.libnine.util.format.FormatUtils;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;
import xyz.phanta.algane.constant.LangConst;
import xyz.phanta.algane.lasergun.LaserGun;
import xyz.phanta.algane.lasergun.LaserGunModifier;

import java.util.List;

public class TooltipUtils {

    public static String formatFractionTooltip(String labelKey, String nom, String denom) {
        return formatKvTooltip(labelKey, I18n.format(LangConst.TT_FRACTION, nom, denom));
    }

    public static String formatPercentTooltip(String labelKey, float value) {
        return formatKvTooltip(labelKey, FormatUtils.formatPercentage(value));
    }

    public static String formatDecimalTooltip(String labelKey, float value) {
        return formatKvTooltip(labelKey, String.format("%.1f", value));
    }

    public static String formatKvTooltip(String labelKey, String value) {
        return TextFormatting.GRAY + I18n.format(labelKey, TextFormatting.AQUA + value);
    }

    public static void handleLaserGunTooltip(LaserGun gun, List<String> tooltip) {
        AlganeUtils.getLaserEnergy(gun).ifPresent(energy -> tooltip.add(TooltipUtils.formatFractionTooltip(LangConst.TT_ENERGY,
                FormatUtils.formatSI(energy.getEnergyStored(), "FE"), FormatUtils.formatSI(energy.getMaxEnergyStored(), "FE"))));
        LaserGunModifier mods = AlganeUtils.computeTotalMods(gun);
        tooltip.add(TooltipUtils.formatPercentTooltip(LangConst.TT_STAT_DAMAGE, AlganeUtils.getDamageMultiplier(mods)));
        tooltip.add(TooltipUtils.formatPercentTooltip(LangConst.TT_STAT_ENERGY, AlganeUtils.getEnergyMultiplier(mods)));
        tooltip.add(TooltipUtils.formatPercentTooltip(LangConst.TT_STAT_HEAT, AlganeUtils.getHeatMultiplier(mods)));
    }

}
