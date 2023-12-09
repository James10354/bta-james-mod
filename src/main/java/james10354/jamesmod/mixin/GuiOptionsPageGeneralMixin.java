package james10354.jamesmod.mixin;

import james10354.jamesmod.util.IGameSettings;
import james10354.jamesmod.util.IGuiOptionsPageBase;
import net.minecraft.client.gui.options.GuiOptionsPageGeneral;
import net.minecraft.client.option.Option;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(GuiOptionsPageGeneral.class)
public abstract class GuiOptionsPageGeneralMixin implements IGuiOptionsPageBase {

    @ModifyArg(method = "<init>",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/options/GuiOptionsPageGeneral;addOptionsCategory(Ljava/lang/String;[Lnet/minecraft/client/option/Option;)V",
                    ordinal = 0),
            index = 1)
    private Option[] addOption(Option[] options) {
        Option[] newOptions = new Option[options.length + 1];
        for (int i = 0; i < options.length; i++) {
            newOptions[i] = options[i];
        }

        newOptions[options.length] = ((IGameSettings)this.getGameSettings()).getDynamicFoV();

        return newOptions;
    }
}
