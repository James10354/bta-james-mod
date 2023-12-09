package james10354.jamesmod.mixin;

import james10354.jamesmod.util.IGameSettings;
import james10354.jamesmod.util.IGuiOptionsPageBase;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.options.GuiOptionsPageBase;
import net.minecraft.client.gui.options.GuiOptionsPageControls;
import net.minecraft.client.gui.options.GuiOptionsPageOptionBase;
import net.minecraft.client.option.GameSettings;
import net.minecraft.client.option.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(GuiOptionsPageControls.class)
public abstract class GuiOptionsPageControlsMixin implements IGuiOptionsPageBase, IGameSettings {

    @ModifyArg(method = "<init>",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/options/GuiOptionsPageControls;addKeyBindingsCategory(Ljava/lang/String;[Lnet/minecraft/client/option/KeyBinding;)V",
                    ordinal = 0),
            index = 1)
    private KeyBinding[] addKeyBind(KeyBinding[] bindings) {
        KeyBinding[] newBindings = new KeyBinding[bindings.length + 1];
        for (int i = 0; i < bindings.length; i++) {
            newBindings[i] = bindings[i];
        }
        newBindings[bindings.length] = ((IGameSettings)this.getGameSettings()).getKeySprint();

        return newBindings;
    }
}
