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
package gg.yume.interpolationfix.mixin;

import gg.yume.interpolationfix.config.InterpolationConfigs;
import gg.yume.interpolationfix.interpolation.SwitchableInterpolationHandler;
import net.minecraft.world.entity.InterpolationHandler;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Inject(method = "createInterpolationHandler", at = @At("RETURN"), cancellable = true)
    private void interpolationFix$createEntityInterpolation(@NotNull CallbackInfoReturnable<@NotNull InterpolationHandler> callback) {
        final var living = (LivingEntity) (Object) this;
        if (living.level().isClientSide()) {
            callback.setReturnValue(new SwitchableInterpolationHandler(
                living,
                callback.getReturnValue(),
                InterpolationConfigs.config().getInterpolationMode()
            ));
        }
    }

}
