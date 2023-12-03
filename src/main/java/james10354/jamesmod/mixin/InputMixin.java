package james10354.jamesmod.mixin;

import james10354.jamesmod.util.IInput;
import net.minecraft.client.input.Input;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Input.class)
public abstract class InputMixin implements IInput {
    @Unique boolean sprint = false;

    @Override
    public boolean getSprint() {
        return this.sprint;
    }

    @Override
    public void setSprint(boolean sprint) {
        this.sprint = sprint;
    }
}
