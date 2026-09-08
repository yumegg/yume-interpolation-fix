package gg.yume.interpolationfix.config;

import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.NotNull;

public final class InterpolationConfigs {

    private static final @NotNull InterpolationConfig CONFIG = InterpolationConfig.load(
        FabricLoader.getInstance().getConfigDir().resolve("yume-interpolation-fix.json")
    );

    public static @NotNull InterpolationConfig config() {
        return CONFIG;
    }

    private InterpolationConfigs() {
    }

}
