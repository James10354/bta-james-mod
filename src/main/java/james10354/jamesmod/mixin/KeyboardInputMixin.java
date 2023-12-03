package james10354.jamesmod.mixin;

import james10354.jamesmod.util.IGameSettings;
import org.checkerframework.common.value.qual.IntVal;
import org.objectweb.asm.Opcodes;
import net.minecraft.client.input.KeyboardInput;
import net.minecraft.client.option.GameSettings;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(KeyboardInput.class)
public abstract class KeyboardInputMixin implements IGameSettings {

    @Shadow @Final private boolean[] keys = new boolean[12];

    @Shadow @Final private GameSettings gameSettings;

    @Inject(method = "setKey", at = @At(value = "FIELD", target = "Lnet/minecraft/client/input/KeyboardInput;keys:[Z", shift = At.Shift.BY, by = -5), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD, remap = false)
    private void setKeySprint(int keyCode, boolean isPressed, CallbackInfo ci, byte inputIndex) {

        if (((IGameSettings)this.gameSettings).getKeySprint().isKey(keyCode)) {
            inputIndex = 11;
        }

        if (inputIndex >= 0) {
            System.out.println(inputIndex);
            this.keys[inputIndex] = isPressed;
        }
        ci.cancel();
    }

    @ModifyConstant(method = "releaseAllKeys", constant = @Constant(intValue = 11), remap = false)
    private int newKeyCount(int constant) {
        return this.keys.length;
    }
}
