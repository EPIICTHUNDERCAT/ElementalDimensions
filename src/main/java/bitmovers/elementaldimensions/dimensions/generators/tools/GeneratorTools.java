package bitmovers.elementaldimensions.dimensions.generators.tools;

import net.minecraft.block.state.IBlockState;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraftforge.fml.common.registry.GameData;

public class GeneratorTools {

    public static void setBlockState(ChunkPrimer primer, int index, IBlockState state) {
        primer.data[index] = (char)GameData.getBlockStateIDMap().get(state);
    }

    public static IBlockState getBlockState(ChunkPrimer primer, int index) {
        return GameData.getBlockStateIDMap().getByValue(primer.data[index]);
    }

}
