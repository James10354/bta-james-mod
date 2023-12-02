package james10354.jamesmod.mixin;

import james10354.jamesmod.util.IGuiOptionsPageBase;
import net.minecraft.client.gui.options.GuiOptionsPageBase;
import net.minecraft.client.option.GameSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(GuiOptionsPageBase.class)
public abstract class GuiOptionsPageBaseMixin implements IGuiOptionsPageBase {

    @Shadow
    GameSettings gameSettings;

    @Override
    public GameSettings getGameSettings() {
        return this.gameSettings;
    }

}
