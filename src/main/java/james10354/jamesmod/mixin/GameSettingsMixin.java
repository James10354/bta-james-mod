package james10354.jamesmod.mixin;

import com.b100.utils.ReflectUtils;
import james10354.jamesmod.util.IGameSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.option.GameSettings;
import net.minecraft.client.option.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;

@Mixin(GameSettings.class)
public abstract class GameSettingsMixin implements IGameSettings {

    @Shadow public KeyBinding[] keys;

    @Shadow public abstract void loadOptions();

    @Shadow public abstract void saveOptions();

    @Unique public KeyBinding keySprint;

    @Override
    public KeyBinding getKeySprint() { return this.keySprint; }

    @Inject(method = "<init>", at = @At(value = "RETURN"))
    private void extraSettings(Minecraft minecraft, File file, CallbackInfo ci) {
        this.keySprint = new KeyBinding("key.keySprint", 29);
        this.keys = (KeyBinding[]) ReflectUtils.getAllObjects(GameSettings.class, KeyBinding.class, ((GameSettings)(Object)this));
        this.loadOptions();
        this.saveOptions();
    }
}
