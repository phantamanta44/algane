package xyz.phanta.algane.util;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.*;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.stream.Stream;

// Some code adapted from Project Crossbow, under MIT
// https://github.com/phantamanta44/ProjectCrossbow/blob/1.12.2/src/main/java/io/github/phantamanta44/pcrossbow/util/PhysicsUtils.java
public class LasingUtils {

    public static Stream<EntityLivingBase> laseEntities(World world, Vec3d from, Vec3d to) {
        RayTraceResult trace = traceLaser(world, from, to);
        Vec3d end = trace != null ? trace.hitVec : to;
        return world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(from, end)).stream()
                .filter(e -> intersectsLine(e.getEntityBoundingBox(), from, end));
    }

    private static boolean intersectsLine(AxisAlignedBB prism, Vec3d lineMin, Vec3d lineMax) {
        double x1 = lineMin.x, x2 = lineMax.x, dx = x2 - x1;
        double y1 = lineMin.y, y2 = lineMax.y, dy = y2 - y1;
        double z1 = lineMin.z, z2 = lineMax.z, dz = z2 - z1;
        double dydx = dy / dx, dzdx = dz / dx, dzdy = dz / dy;
        double inter1, inter2;
        // project to xy plane
        inter1 = (prism.minX - x1) * dydx + y1;
        inter2 = (prism.maxX - x1) * dydx + y1;
        if (Math.min(inter1, inter2) > prism.maxY || Math.max(inter1, inter2) < prism.minY) return false;
        // project to xz plane
        inter1 = (prism.minX - x1) * dzdx + z1;
        inter2 = (prism.maxX - x1) * dzdx + z1;
        if (Math.min(inter1, inter2) > prism.maxZ || Math.max(inter1, inter2) < prism.minZ) return false;
        // project to yz plane
        inter1 = (prism.minY - y1) * dzdy + z1;
        inter2 = (prism.maxY - y1) * dzdy + z1;
        return !(Math.min(inter1, inter2) > prism.maxZ || Math.max(inter1, inter2) < prism.minZ);
    }

    @Nullable
    private static RayTraceResult traceLaser(World world, Vec3d initialPos, Vec3d end) {
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
                if (si.getCollisionBoundingBox(world, blockPos) != null && isObstructed(si)) {
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
                            && (currentState.getCollisionBoundingBox(world, blockPos) != null || isObstructed(currentState))) {
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

    private static boolean isObstructed(IBlockState state) {
        return state.isOpaqueCube() && state.getBlock().canCollideCheck(state, false);
    }

}
