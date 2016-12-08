package bitmovers.elementaldimensions.dimensions.generators;

import bitmovers.elementaldimensions.mobs.EntitySpirit;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import mcjty.lib.compat.CompatChunkGenerator;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunkGenerator;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

import static bitmovers.elementaldimensions.dimensions.generators.tools.GeneratorTools.setBlockState;

public class SpiritChunkGenerator implements CompatChunkGenerator {

    private final World worldObj;
    private Random random;

    private List<Biome.SpawnListEntry> mobs = Lists.newArrayList(new Biome.SpawnListEntry(EntitySpirit.class, 100, 2, 2));

    public SpiritChunkGenerator(World worldObj) {
        this.worldObj = worldObj;
        long seed = 0x1fff; // @todo
        this.random = new Random((seed + 516) * 314);
    }

    private static void generate(int chunkX, int chunkZ, ChunkPrimer primer) {
        IBlockState baseBlock = Blocks.OBSIDIAN.getDefaultState();

        byte waterLevel = 60;
        int index = 0;
        for (int x = 0; x < 16; ++x) {
            for (int z = 0; z < 16; ++z) {
                int height = 0;
                while (height < 2) {
                    setBlockState(primer, index++, Blocks.BEDROCK.getDefaultState());
                    height++;
                }
                while (height < waterLevel) {
                    setBlockState(primer, index++, baseBlock);
                    height++;
                }
                while (height < 256) {
                    setBlockState(primer, index++, Blocks.AIR.getDefaultState());
                    height++;
                }
            }
        }
    }

    @Override
    public Chunk provideChunk(int x, int z) {
        ChunkPrimer chunkprimer = new ChunkPrimer();

        generate(x, z, chunkprimer);

        Chunk chunk = new Chunk(this.worldObj, chunkprimer, x, z);

        byte[] biomeArray = chunk.getBiomeArray();
        for (int i = 0; i < biomeArray.length; ++i) {
            biomeArray[i] = (byte) Biome.getIdForBiome(Biomes.PLAINS);
        }

        chunk.generateSkylightMap();
        return chunk;
    }

    @Override
    public void populate(int x, int z) {

    }

    @Override
    public boolean generateStructures(Chunk chunkIn, int x, int z) {
        return false;
    }

    @Override
    public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos) {
        if (creatureType == EnumCreatureType.MONSTER){
            return mobs;
        }
        return ImmutableList.of();

    }

    @Override
    public BlockPos clGetStrongholdGen(World worldIn, String structureName, BlockPos position) {
        return null;
    }

    @Override
    public void recreateStructures(Chunk chunkIn, int x, int z) {

    }
}
