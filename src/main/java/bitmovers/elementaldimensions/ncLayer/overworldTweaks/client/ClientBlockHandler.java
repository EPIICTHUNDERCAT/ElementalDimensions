package bitmovers.elementaldimensions.ncLayer.overworldTweaks.client;

import bitmovers.elementaldimensions.init.BlockRegister;
import elec332.core.client.IIconRegistrar;
import elec332.core.client.model.ElecModelBakery;
import elec332.core.client.model.ElecQuadBakery;
import elec332.core.client.model.INoJsonBlock;
import elec332.core.client.model.template.ElecTemplateBakery;
import elec332.core.util.RegistryHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelRenderer;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by Elec332 on 4-8-2016.
 *
 * Since the blocks use vanilla textures, the models are loaded from here.
 */
public enum ClientBlockHandler implements INoJsonBlock {

    INSTANCE;

    private BlockRendererDispatcher blockRendererDispatcher;
    private IBakedModel missingModel;

    @Override
    public void registerModels(ElecQuadBakery elecQuadBakery, ElecModelBakery elecModelBakery, ElecTemplateBakery elecTemplateBakery) {

    }

    @Override
    public void registerTextures(IIconRegistrar iIconRegistrar) {

    }

    @Override
    @Nonnull
    public IBakedModel getBlockModel(IBlockState blockState) {
        Block block = blockState.getBlock();
        if (block == BlockRegister.silverFishStone){
            return getModelForState(Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, blockState.getValue(BlockStone.VARIANT)));
        }
        return missingModel;
    }

    @Override
    @SuppressWarnings("all")
    @Nonnull
    public IBakedModel getItemModel(ItemStack itemStack, World world, EntityLivingBase entityLivingBase) {
        Item item = itemStack.getItem();
        Block block = Block.getBlockFromItem(item);
        if (block == null){
            return missingModel;
        }
        if (block == BlockRegister.silverFishStone){ //An ItemBlock for this doesn't exist, but oh well
            return getModelForState(Blocks.STONE.getStateFromMeta(itemStack.getItemDamage()));
        }
        return missingModel;
    }

    @Nonnull
    public IBakedModel getModelForState(IBlockState state){
        return blockRendererDispatcher.getModelForState(state);
    }

    public ClientBlockHandler setFields(){
        blockRendererDispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
        missingModel = blockRendererDispatcher.blockModelShapes.modelManager.getMissingModel();
        return this;
    }

}
