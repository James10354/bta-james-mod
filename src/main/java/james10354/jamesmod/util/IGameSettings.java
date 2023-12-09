package james10354.jamesmod.util;

import net.minecraft.client.option.BooleanOption;
import net.minecraft.client.option.KeyBinding;

public interface IGameSettings {
    KeyBinding getKeySprint();

    BooleanOption getDynamicFoV();
}
