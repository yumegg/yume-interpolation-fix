/*
 * yume-interpolation-fix by yume.gg
 *
 * To the extent possible under law, the person who associated CC0 with
 * yume-interpolation-fix has waived all copyright and related or neighboring rights
 * to yume-interpolation-fix.
 *
 * You should have received a copy of the CC0 legalcode along with this
 * work.  If not, see <http://creativecommons.org/publicdomain/zero/1.0/>.
 */
package gg.yume.interpolationfix.client;

import gg.yume.interpolationfix.config.InterpolationConfigs;
import gg.yume.interpolationfix.config.InterpolationMode;
import net.minecraft.client.OptionInstance;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public final class InterpolationOptions {

    public static @NotNull OptionInstance<@NotNull Boolean> create() {
        final OptionInstance<Boolean> option = OptionInstance.createBoolean(
            "options.yume-interpolation-fix.entityInterpolation",
            OptionInstance.cachedConstantTooltip(
                Component.translatable("options.yume-interpolation-fix.entityInterpolation.tooltip")
            ),
            (_, useClassic) -> useClassic
                ? Component.translatable("options.yume-interpolation-fix.classic")
                : Component.translatable("options.yume-interpolation-fix.vanilla"),
            false,
            useClassic -> {
                InterpolationConfigs.config().setInterpolationMode(
                    useClassic ? InterpolationMode.CLASSIC : InterpolationMode.VANILLA
                );
            }
        );
        option.set(switch (InterpolationConfigs.config().getInterpolationMode()) {
            case CLASSIC -> true;
            case VANILLA -> false;
        });
        return option;
    }

    private InterpolationOptions() {
    }

}
