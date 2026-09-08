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
package gg.yume.interpolationfix;

import gg.yume.interpolationfix.config.InterpolationConfigs;
import net.fabricmc.api.ClientModInitializer;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class InterpolationFixClient implements ClientModInitializer {

    private static final @NotNull Logger LOGGER = LoggerFactory.getLogger(InterpolationFixClient.class);

    @Override
    public void onInitializeClient() {
        LOGGER.info(
            "Entity interpolation: {}",
            switch (InterpolationConfigs.config().getInterpolationMode()) {
                case CLASSIC -> "26.2 (classic)";
                case VANILLA -> "26.3 (default)";
            }
        );
    }

}
