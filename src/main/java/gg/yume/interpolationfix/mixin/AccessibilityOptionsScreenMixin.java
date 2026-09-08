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

import gg.yume.interpolationfix.client.InterpolationOptions;
import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.AccessibilityOptionsScreen;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AccessibilityOptionsScreen.class)
public abstract class AccessibilityOptionsScreenMixin extends OptionsSubScreen {

    protected AccessibilityOptionsScreenMixin(@NotNull Screen parent, @NotNull Options options, @NotNull Component title) {
        super(parent, options, title);
    }

    @Inject(method = "addOptions", at = @At("TAIL"))
    private void interpolationFix$addOption(@NotNull CallbackInfo callback) {
        var list = this.list;
        if (list != null) {
            list.addSmall(InterpolationOptions.create());
        }
    }

}
