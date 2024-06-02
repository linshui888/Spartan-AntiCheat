package com.vagdedes.spartan.utils.math;

import com.vagdedes.spartan.abstraction.player.SpartanPlayer;
import com.vagdedes.spartan.abstraction.world.SpartanBlock;
import com.vagdedes.spartan.abstraction.world.SpartanLocation;
import com.vagdedes.spartan.functionality.server.SpartanBukkit;
import com.vagdedes.spartan.utils.minecraft.mcp.AxisAlignedBB;
import com.vagdedes.spartan.utils.minecraft.mcp.MathHelper;
import com.vagdedes.spartan.utils.minecraft.mcp.MovingObjectPosition;
import com.vagdedes.spartan.utils.minecraft.mcp.Vec3;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.util.Vector;

import java.util.Set;

public class RayUtils {

    private static final boolean[] BOOLEANS = {true, false};

    public static double scaleVal(double value, double scale) {
        double scale2 = Math.pow(10, scale);
        return Math.ceil(value * scale2) / scale2;
    }

    public static boolean validRayLines(RayLine a, RayLine b, double radi) {
        double angleA = Math.atan2(a.z(), a.x());
        double angleB = Math.atan2(b.z(), b.x());

        double angleDiff = Math.abs(angleA - angleB);

        if (angleDiff > Math.PI) {
            angleDiff = 2 * Math.PI - angleDiff;
        }

        return angleDiff <= Math.toRadians(radi);
    }

    public static double calculateRayLines(RayLine a, RayLine b) {
        double angleA = Math.atan2(a.z(), a.x());
        double angleB = Math.atan2(b.z(), b.x());

        double angleDiff = Math.abs(angleA - angleB);

        if (angleDiff > Math.PI) {
            angleDiff = 2 * Math.PI - angleDiff;
        }

        return Math.toDegrees(angleDiff);
    }

    public static double calculateRayLine(RayLine a) {
        return Math.atan2(a.z(), a.x());
    }

    public static float masterCast(float num) {
        return sinusoidOnlyMin(castTo360(num));
    }

    public static float castTo360(float num) {
        float value = Math.abs((num + 360) % 360 - 180);
        // int wrapShitMath = (int) Math.floor(value / 360);
        return value;
    }

    public static float castTo360WithLimit(float num) {
        float value = Math.abs((num + 360) % 360 - 180);
        int wrapShitMath = (int) Math.floor(value / 360);
        return value - (wrapShitMath * 360);
    }

    public static float sinusoidOnlyMin(float num) {
        int wrapShitMath = (int) Math.floor(num / 180);
        return num - (wrapShitMath * 180);
    }

    public static float set360Limit(float num) {
        int wrapShitMath = (int) Math.floor(num / 360);
        return num - (wrapShitMath * 360);
    }


    @SafeVarargs
    public static boolean onBlock(SpartanPlayer player, SpartanLocation location, Set<Material>... sets) {
        World world = player.getWorld();
        Vector playerLocation = location.toVector(),
                min = playerLocation.clone().add(new Vector(-0.3, -0.5, -0.3)),
                max = playerLocation.clone().add(new Vector(0.3, 0.1, 0.3));

        for (int x = min.getBlockX(); x <= max.getBlockX(); x++) {
            for (int y = min.getBlockY(); y <= max.getBlockY(); y++) {
                for (int z = min.getBlockZ(); z <= max.getBlockZ(); z++) {
                    SpartanBlock block = new SpartanLocation(world, null, x, y, z, 0.0f, 0.0f).getBlock();

                    for (Set<Material> set : sets) {
                        if (set.contains(block.material)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public static boolean onBlock(SpartanPlayer player, SpartanLocation location, Set<Material> set) {
        World world = player.getWorld();
        Vector playerLocation = location.toVector(),
                min = playerLocation.clone().add(new Vector(-0.3, -0.5, -0.3)),
                max = playerLocation.clone().add(new Vector(0.3, 0.1, 0.3));

        for (int x = min.getBlockX(); x <= max.getBlockX(); x++) {
            for (int y = min.getBlockY(); y <= max.getBlockY(); y++) {
                for (int z = min.getBlockZ(); z <= max.getBlockZ(); z++) {
                    SpartanBlock block = new SpartanLocation(world, null, x, y, z, 0.0f, 0.0f).getBlock();

                    if (set.contains(block.material)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean onBlock(SpartanPlayer player, SpartanLocation location, Material material) {
        World world = player.getWorld();
        Vector playerLocation = location.toVector(),
                min = playerLocation.clone().add(new Vector(-0.3, -0.5, -0.3)),
                max = playerLocation.clone().add(new Vector(0.3, 0.1, 0.3));

        for (int x = min.getBlockX(); x <= max.getBlockX(); x++) {
            for (int y = min.getBlockY(); y <= max.getBlockY(); y++) {
                for (int z = min.getBlockZ(); z <= max.getBlockZ(); z++) {
                    SpartanBlock block = new SpartanLocation(world, null, x, y, z, 0.0f, 0.0f).getBlock();

                    if (block.material == material) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean onSolidBlock(SpartanPlayer player, SpartanLocation location) {
        World world = player.getWorld();
        Vector playerLocation = location.toVector(),
                min = playerLocation.clone().add(new Vector(-0.3, -0.5, -0.3)),
                max = playerLocation.clone().add(new Vector(0.3, 0.1, 0.3));

        for (int x = min.getBlockX(); x <= max.getBlockX(); x++) {
            for (int y = min.getBlockY(); y <= max.getBlockY(); y++) {
                for (int z = min.getBlockZ(); z <= max.getBlockZ(); z++) {
                    SpartanBlock block = new SpartanLocation(world, null, x, y, z, 0.0f, 0.0f).getBlock();

                    if (block.material.isSolid()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static float[] getRotation(Player player1, Entity entity2) {
        Vector direction = entity2.getLocation().toVector().subtract(player1.getLocation().toVector());
        float yaw = (float) Math.toDegrees(Math.atan2(direction.getX(), direction.getZ())),
                pitch = (float) Math.toDegrees(Math.asin(-direction.getY()));
        return new float[]{castTo360(yaw), pitch};
    }

    public static boolean inHitbox(SpartanPlayer player, Entity target, float size) {
        SpartanLocation location = player.movement.getLocation();
        boolean intersection = false;
        boolean exempt;

        if (target instanceof Player) {
            SpartanPlayer targetSpartan = SpartanBukkit.getPlayer(target.getUniqueId());
            assert targetSpartan != null;
            double targetX = targetSpartan.movement.getLocation().getX();
            double targetY = targetSpartan.movement.getLocation().getY();
            double targetZ = targetSpartan.movement.getLocation().getZ();

            AxisAlignedBB boundingBox = new AxisAlignedBB(
                    targetX - size, targetY - 0.1F, targetZ - size,
                    targetX + size, targetY + 1.9F, targetZ + size
            );
            // boundingBox = boundingBox.expand(0.04, 0.03, 0.04);
            intersection = isIntersection(player, location, intersection, boundingBox);
            exempt = target.isInsideVehicle();
        } else {
            double targetX = target.getLocation().getX();
            double targetY = target.getLocation().getY();
            double targetZ = target.getLocation().getZ();

            AxisAlignedBB boundingBox = new AxisAlignedBB(
                    targetX - size, targetY - 0.1F, targetZ - size,
                    targetX + size, targetY + 1.9F, targetZ + size
            );
            // boundingBox = boundingBox.expand(0.04, 0.03, 0.04);
            intersection = isIntersection(player, location, intersection, boundingBox);
            exempt = target.isInsideVehicle() || !(target instanceof Villager || target instanceof Zombie || target instanceof Skeleton || target instanceof Creeper);
        }
        return intersection || exempt;
    }

    private static boolean isIntersection(SpartanPlayer player, SpartanLocation location, boolean intersection, AxisAlignedBB boundingBox) {
        for (final boolean rotation : BOOLEANS) {
            for (final boolean sneak : BOOLEANS) {
                final float yaw = location.getYaw();
                final float pitch = location.getPitch();
                final MovingObjectPosition result = rayCast(yaw, pitch, sneak, boundingBox, player);
                intersection |= result != null && result.hitVec != null;
            }
        }
        return intersection;
    }

    private static MovingObjectPosition rayCast(final float yaw, final float pitch, final boolean sneak, final AxisAlignedBB bb, SpartanPlayer player) {
        SpartanLocation position = player.movement.getLocation();
        double lastX = position.getX(),
                lastY = position.getY(),
                lastZ = position.getZ();
        Vec3 vec3 = new Vec3(lastX, lastY + getEyeHeight(sneak, player), lastZ),
                vec31 = getVectorForRotation(pitch, yaw),
                vec32 = vec3.add(new Vec3(vec31.xCoord * 4.5D, vec31.yCoord * 4.5D, vec31.zCoord * 4.5D));
        return bb.calculateIntercept(vec3, vec32);
    }

    private static Vec3 getVectorForRotation(final float pitch, final float yaw) {
        float f = MathHelper.cos(-yaw * 0.017453292F - (float) Math.PI),
                f1 = MathHelper.sin(-yaw * 0.017453292F - (float) Math.PI),
                f2 = -MathHelper.cos(-pitch * 0.017453292F),
                f3 = MathHelper.sin(-pitch * 0.017453292F);
        return new Vec3(f1 * f2, f3, f * f2);
    }

    public static float getEyeHeight(final boolean sneak, SpartanPlayer player) {
        float f2 = 1.62F;

        if (player.isSleeping()) {
            f2 = 0.2F;
        }
        if (sneak) {
            f2 -= 0.08F;
        }
        return f2;
    }

    public static double getOnlyScale(double value) {
        return value - Math.floor(value);
    }

    public static float bruteforceRayTrace(Player player, Entity target) {
        SpartanPlayer spartanPlayer = SpartanBukkit.getPlayer(player);
        float bruteForce = 0.01F,
                bruteForceStart = 0.01F;
        boolean checked = false;

        for (int i = 0; i < 40; i++) {
            if (inHitbox(spartanPlayer, target, bruteForceStart)) {
                bruteForce = bruteForceStart;
                checked = true;
            } else {
                bruteForceStart += 0.01F;
            }
        }
        return (checked) ? bruteForce : 0.4F;
    }

    public static float bruteforceRayTrace(SpartanPlayer spartanPlayer, Entity target) {
        float bruteForce = 0.01F;
        float bruteForceStart = 0.01F;
        boolean checked = false;

        for (int i = 0; i < 60; i++) {
            if (inHitbox(spartanPlayer, target, bruteForceStart)) {
                bruteForce = bruteForceStart;
                checked = true;
            } else {
                bruteForceStart += 0.01F;
            }
        }
        return (checked) ? bruteForce : 0.6F;
    }
}