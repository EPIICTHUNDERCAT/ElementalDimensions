package bitmovers.elementaldimensions.dimensions;

import bitmovers.elementaldimensions.mobs.EntityDirtZombieBoss;
import bitmovers.elementaldimensions.mobs.EntityGhost;
import bitmovers.elementaldimensions.mobs.EntityGhostBoss;
import bitmovers.elementaldimensions.util.Config;
import bitmovers.elementaldimensions.util.worldgen.WorldGenHelper;
import mcjty.lib.tools.WorldTools;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DimensionEvents {

    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent evt) {
        if (evt.phase == TickEvent.Phase.START) {
            return;
        }

        Dimensions dimension = Dimensions.findDimension(evt.world.provider.getDimension());
        if (dimension == null) {
            return;
        }
        switch (dimension) {
            case EARTH:
                handleEarth(evt);
                break;
            case WATER:
                break;
            case AIR:
                handleAir(evt);
                break;
            case SPIRIT:
                break;
            case FIRE:
                break;
            case OVERWORLD:
                break;
        }
    }

    private void handleAir(TickEvent.WorldTickEvent evt) {
        if (evt.world.playerEntities.isEmpty()) {
            // Temporary until we figure out why the code below sometimes gives a concurrent modificatino
            // exception when the player isn't there
            return;
        }
        List<EntityPlayerMP> playersToNotify = new ArrayList<>();
        for (Entity entity : evt.world.loadedEntityList) {
            if (entity instanceof EntityGhost || entity instanceof EntityGhostBoss) {
                // These are immune
                continue;
            }
            if (WorldGenHelper.areWeOutside(evt.world, entity.getPosition())) {
                if (entity instanceof EntityPlayer) {
                    playersToNotify.add((EntityPlayerMP)entity);
                } else {
                    entity.addVelocity(Config.Dimensions.windStrength, 0, 0);
                }
            }
        }
        for (EntityPlayerMP player : playersToNotify) {
            player.addVelocity(Config.Dimensions.windStrength, 0, 0);
            player.connection.sendPacket(new SPacketEntityVelocity(player));
            player.velocityChanged = false;
        }
    }

    private void handleEarth(TickEvent.WorldTickEvent evt) {
        long worldTime = evt.world.getWorldTime();
        if (Math.abs(worldTime-18000) < 500) {
            // It is around midnight now
            int count = evt.world.countEntities(EntityDirtZombieBoss.class);
            if (count == 0) {
                BlockPos pos = findAveragePlayerPos(evt.world);
                if (pos != null) {
                    EntityDirtZombieBoss boss = new EntityDirtZombieBoss(evt.world);
                    boss.setPosition(pos.getX(), pos.getY(), pos.getZ());
                    WorldTools.spawnEntity(evt.world, boss);
                }
            }
        }
    }

    private static Random random = new Random();

    private static BlockPos findAveragePlayerPos(World world) {
        AxisAlignedBB b = null;
        for (EntityPlayer entity : world.playerEntities) {
            BlockPos position = entity.getPosition();
            if (b == null) {
                b = new AxisAlignedBB(position);
            } else {
                b = b.union(new AxisAlignedBB(position));
            }
        }
        if (b == null) {
            return null;
        }
        BlockPos center = new BlockPos((b.maxX+b.minX) / 2 + random.nextInt(50) - 25, (b.maxY+b.minY) / 2, (b.maxZ+b.minZ) / 2 + random.nextInt(50) - 25);
        return world.getTopSolidOrLiquidBlock(center);
    }


}
