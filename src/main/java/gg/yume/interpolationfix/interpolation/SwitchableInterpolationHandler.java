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
package gg.yume.interpolationfix.interpolation;

import gg.yume.interpolationfix.config.InterpolationMode;
import net.minecraft.core.PositionAndRotation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InterpolationHandler;
import net.minecraft.world.entity.InterpolationTracker;
import net.minecraft.world.entity.LinearInterpolationHandler;
import net.minecraft.world.entity.PositionPath;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class SwitchableInterpolationHandler implements InterpolationHandler {

    private final @NotNull Entity entity;
    private final @NotNull InterpolationHandler vanilla;
    private final @NotNull InterpolationHandler classic;
    private @NotNull InterpolationMode interpolationMode;

    public SwitchableInterpolationHandler(@NotNull Entity entity, @NotNull InterpolationHandler vanilla, @NotNull InterpolationMode interpolationMode) {
        this.entity = entity;
        this.vanilla = vanilla;
        this.classic = LinearInterpolationHandler.create(entity, 3);
        this.interpolationMode = interpolationMode;
    }

    @Contract(pure = true)
    private @NotNull InterpolationHandler getActiveInterpolationHandler() {
        return switch (interpolationMode) {
            case VANILLA -> vanilla;
            case CLASSIC -> classic;
        };
    }

    public void setInterpolationMode(@NotNull InterpolationMode interpolationMode) {
        if (this.interpolationMode == interpolationMode) {
            return;
        }

        final var active = getActiveInterpolationHandler();
        PositionAndRotation target = active.target();
        active.cancel();

        this.interpolationMode = interpolationMode;
        if (target != null) {
            getActiveInterpolationHandler().interpolateTo(
                PositionPath.of(target.position()),
                target.yRot(),
                target.xRot(),
                true
            );
        }
    }

    @Override
    public @NotNull InterpolationTracker interpolationTracker() {
        return getActiveInterpolationHandler().interpolationTracker();
    }

    @Override
    public @Nullable PositionAndRotation target() {
        return getActiveInterpolationHandler().target();
    }

    @Override
    public boolean interpolateTo(@Nullable PositionPath position, float yRot, float xRot, boolean hasRotation) {
        return getActiveInterpolationHandler().interpolateTo(position, yRot, xRot, hasRotation);
    }

    @Override
    public void interpolate() {
        getActiveInterpolationHandler().interpolate();
    }

    @Override
    public void applyPredictedMovement(@NotNull Vec3 delta) {
        getActiveInterpolationHandler().applyPredictedMovement(delta);
    }

    @Override
    public boolean hasActiveInterpolation() {
        return getActiveInterpolationHandler().hasActiveInterpolation();
    }

    @Override
    public void cancel() {
        getActiveInterpolationHandler().cancel();
    }

}
