package james10354.jamesmod.util;

import net.minecraft.core.world.World;

public interface IBlock {
    void onBlockDestroyedByExplosion(World world, int x, int y, int z, boolean isFireExplosion);
}
