package xyz.phanta.algane.util;

import io.github.phantamanta44.libnine.util.math.LinAlUtils;
import io.github.phantamanta44.libnine.util.tuple.IPair;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.*;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

// Some code adapted from Project Crossbow, under MIT
// https://github.com/phantamanta44/ProjectCrossbow/blob/1.12.2/src/main/java/io/github/phantamanta44/pcrossbow/util/PhysicsUtils.java
public class LasingUtils {

    public static Vec3d laseEntity(World world, Vec3d origin, Vec3d dir, float range,
                                   Predicate<Entity> filter, Consumer<Entity> hitCallback) {
        Vec3d endPos = origin.add(dir.scale(range));
        RayTraceResult trace = traceLaser(world, origin, endPos, LasingUtils::isOpaque);
        if (trace != null) {
            endPos = trace.hitVec;
        }
        Optional<Entity> hitOpt = getFirstEntityOnLine(Entity.class, filter, world, origin, endPos);
        if (hitOpt.isPresent()) {
            Entity hit = hitOpt.get();
            hitCallback.accept(hit);
            Vec3d hitPos = hit.getPositionVector();
            Vec3d toEntity = LinAlUtils.castOntoPlane(origin, dir, hitPos, dir);
            if (toEntity != null) {
                endPos = toEntity;
            } else {
                endPos = hitPos;
            }
        }
        return endPos;
    }

    public static <E extends Entity> Optional<E> getFirstEntityOnLine(Class<E> entityType, Predicate<Entity> filter,
                                                                      World world, Vec3d from, Vec3d to) {
        return getEntitiesOnLine(world, from, to).stream()
                .filter(entityType::isInstance)
                .map(entityType::cast)
                .filter(filter)
                .map(e -> IPair.of(e, e.getDistanceSq(from.x, from.y, from.z)))
                .min(Comparator.comparing(IPair::getB))
                .map(IPair::getA);
    }

    public static List<Entity> getEntitiesOnLine(World world, Vec3d from, Vec3d to) {
        return world.getEntitiesInAABBexcluding(null, new AxisAlignedBB(from.x, from.y, from.z, to.x, to.y, to.z), null);
    }

    @Nullable
    public static RayTraceResult traceLaser(World world, Vec3d initialPos, Vec3d end, Predicate<IBlockState> hitTest) {
        if (!Double.isNaN(initialPos.x) && !Double.isNaN(initialPos.y) && !Double.isNaN(initialPos.z)) {
            if (!Double.isNaN(end.x) && !Double.isNaN(end.y) && !Double.isNaN(end.z)) {
                int xf = MathHelper.floor(end.x);
                int yf = MathHelper.floor(end.y);
                int zf = MathHelper.floor(end.z);
                int x = MathHelper.floor(initialPos.x);
                int y = MathHelper.floor(initialPos.y);
                int z = MathHelper.floor(initialPos.z);
                BlockPos blockPos = new BlockPos(x, y, z);
                RayTraceResult collision;
                IBlockState si = world.getBlockState(blockPos);
                collision = si.collisionRayTrace(world, blockPos, initialPos, end);
                if (si.getCollisionBoundingBox(world, blockPos) != null && hitTest.test(si)) {
                    //noinspection ConstantConditions
                    if (collision != null) {
                        collision.hitVec = collision.hitVec.subtract(end.subtract(initialPos).normalize());
                        return collision;
                    }
                }
                Vec3d pos = initialPos;
                int n = 200;
                while (n-- >= 0) {
                    if (Double.isNaN(pos.x) || Double.isNaN(pos.y) || Double.isNaN(pos.z)
                            || (x == xf && y == yf && z == zf)) {
                        return null;
                    }
                    boolean blockChangesX = true;
                    boolean blockChangesY = true;
                    boolean blockChangesZ = true;
                    double resultFaceBoundX = 999D;
                    double resultFaceBoundY = 999D;
                    double resultFaceBoundZ = 999D;
                    if (xf > x) {
                        resultFaceBoundX = x + 1D;
                    } else if (xf < x) {
                        resultFaceBoundX = x;
                    } else {
                        blockChangesX = false;
                    }
                    if (yf > y) {
                        resultFaceBoundY = y + 1D;
                    } else if (yf < y) {
                        resultFaceBoundY = y;
                    } else {
                        blockChangesY = false;
                    }
                    if (zf > z) {
                        resultFaceBoundZ = z + 1D;
                    } else if (zf < z) {
                        resultFaceBoundZ = z;
                    } else {
                        blockChangesZ = false;
                    }
                    double fractionInBlockX = 999.0D;
                    double fractionInBlockY = 999.0D;
                    double fractionInBlockZ = 999.0D;
                    double remainingX = end.x - pos.x;
                    double remainingY = end.y - pos.y;
                    double remainingZ = end.z - pos.z;
                    if (blockChangesX) fractionInBlockX = (resultFaceBoundX - pos.x) / remainingX;
                    if (blockChangesY) fractionInBlockY = (resultFaceBoundY - pos.y) / remainingY;
                    if (blockChangesZ) fractionInBlockZ = (resultFaceBoundZ - pos.z) / remainingZ;
                    if (fractionInBlockX == -0.0D) fractionInBlockX = -1.0E-4D;
                    if (fractionInBlockY == -0.0D) fractionInBlockY = -1.0E-4D;
                    if (fractionInBlockZ == -0.0D) fractionInBlockZ = -1.0E-4D;
                    EnumFacing dirTravelled;
                    if (fractionInBlockX < fractionInBlockY && fractionInBlockX < fractionInBlockZ) {
                        dirTravelled = xf > x ? EnumFacing.WEST : EnumFacing.EAST;
                        pos = new Vec3d(resultFaceBoundX, pos.y + remainingY * fractionInBlockX, pos.z + remainingZ * fractionInBlockX);
                    } else if (fractionInBlockY < fractionInBlockZ) {
                        dirTravelled = yf > y ? EnumFacing.DOWN : EnumFacing.UP;
                        pos = new Vec3d(pos.x + remainingX * fractionInBlockY, resultFaceBoundY, pos.z + remainingZ * fractionInBlockY);
                    } else {
                        dirTravelled = zf > z ? EnumFacing.NORTH : EnumFacing.SOUTH;
                        pos = new Vec3d(pos.x + remainingX * fractionInBlockZ, pos.y + remainingY * fractionInBlockZ, resultFaceBoundZ);
                    }
                    x = MathHelper.floor(pos.x) - (dirTravelled == EnumFacing.EAST ? 1 : 0);
                    y = MathHelper.floor(pos.y) - (dirTravelled == EnumFacing.UP ? 1 : 0);
                    z = MathHelper.floor(pos.z) - (dirTravelled == EnumFacing.SOUTH ? 1 : 0);
                    blockPos = new BlockPos(x, y, z);
                    IBlockState currentState = world.getBlockState(blockPos);
                    collision = currentState.collisionRayTrace(world, blockPos, pos, end);
                    //noinspection ConstantConditions
                    if (collision != null
                            && (currentState.getCollisionBoundingBox(world, blockPos) != null && hitTest.test(currentState))) {
                        return collision;
                    }
                }
                return null;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public static boolean isOpaque(IBlockState state) {
        return state.isOpaqueCube() && state.isFullCube() && isImpassible(state);
    }

    public static boolean isImpassible(IBlockState state) {
        return state.getBlock().canCollideCheck(state, false);
    }

}
