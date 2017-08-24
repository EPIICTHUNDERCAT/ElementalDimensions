package bitmovers.elementaldimensions.dimensions.providers;

import bitmovers.elementaldimensions.dimensions.generators.SpiritChunkGenerator;
import bitmovers.elementaldimensions.init.DimensionRegister;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.gen.IChunkGenerator;

import javax.annotation.Nonnull;

public class SpiritWorldProvider extends WorldProvider {

    @Override
    @Nonnull
    public DimensionType getDimensionType() {
        return DimensionRegister.spiritDimensionType;
    }

    @Override
    @Nonnull
    public String getSaveFolder() {
        return "ELDIM_SPIRIT";
    }

    @Override
    @Nonnull
    public IChunkGenerator createChunkGenerator() {
        return new SpiritChunkGenerator(world);
    }

    public SpiritWorldProvider() {
        hasSkyLight = false;
    }

    @Override
    protected void init() {
        super.init();
        hasSkyLight = false;
    }

    @Override
    public int getActualHeight() {
        return 256;
    }
    
}
