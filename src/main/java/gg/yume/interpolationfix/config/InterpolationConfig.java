package gg.yume.interpolationfix.config;

import java.io.IOException;
import java.nio.file.Path;

import gg.yume.interpolationfix.interpolation.SwitchableInterpolationHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class InterpolationConfig {

    private static final @NotNull Logger LOGGER = LoggerFactory.getLogger(InterpolationConfig.class);
    private static final @NotNull SystemToast.SystemToastId SAVE_FAILURE = new SystemToast.SystemToastId();

    @Contract("_ -> new")
    public static @NotNull InterpolationConfig load(@NotNull Path path) {
        return new InterpolationConfig(InterpolationConfigData.load(path));
    }

    private final @NotNull InterpolationConfigData data;

    private InterpolationConfig(@NotNull InterpolationConfigData data) {
        this.data = data;
    }

    public @NotNull InterpolationMode getInterpolationMode() {
        return data.getInterpolationMode();
    }

    public void setInterpolationMode(@NotNull InterpolationMode interpolationMode) {
        final var previousMode = data.getInterpolationMode();
        if (previousMode == interpolationMode) {
            return;
        }
        data.setInterpolationMode(interpolationMode);
        LOGGER.info("Changed interpolation mode: {} -> {}", previousMode, interpolationMode);

        final Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level != null) {
            for (final Entity entity : minecraft.level.entitiesForRendering()) {
                if (entity.getInterpolation() instanceof SwitchableInterpolationHandler interpolation) {
                    interpolation.setInterpolationMode(interpolationMode);
                }
            }
        }

        try {
            data.save();
        } catch (IOException exception) {
            LOGGER.error("Could not save interpolation settings; the change only applies to this session", exception);
            SystemToast.addOrUpdate(
                minecraft.gui.toastManager(),
                SAVE_FAILURE,
                Component.translatable("options.yume-interpolation-fix.saveFailure"),
                null
            );
        }
    }

}
