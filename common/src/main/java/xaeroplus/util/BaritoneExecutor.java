package xaeroplus.util;

import baritone.api.BaritoneAPI;
import baritone.api.event.events.PathEvent;
import baritone.api.event.listener.AbstractBaritoneListener;
import baritone.api.pathing.goals.GoalXZ;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import xaeroplus.Globals;

import static net.minecraft.world.level.Level.NETHER;
import static net.minecraft.world.level.Level.OVERWORLD;

// avoid classloading this unless baritone is actually present
// otherwise game crashes
public final class BaritoneExecutor extends AbstractBaritoneListener {
    
    private static Runnable onGoalReachedCallback;

    private BaritoneExecutor() {
        // Registering Baritone's event listener
        BaritoneAPI.getProvider().getPrimaryBaritone().getGameEventHandler().registerEventListener(this);
    }

    public static GoalXZ getBaritoneGoalXZ(int x, int z) {
        ResourceKey<Level> customDim = Globals.getCurrentDimensionId();
        ResourceKey<Level> actualDim = ChunkUtils.getActualDimension();
        double customDimDiv = 1.0;
        if (customDim != actualDim) {
            if (customDim == NETHER && actualDim == OVERWORLD) {
                customDimDiv = 8;
            } else if (customDim == OVERWORLD && actualDim == NETHER) {
                customDimDiv = 0.125;
            }
        }
        int goalX = (int) (x * customDimDiv);
        int goalZ = (int) (z * customDimDiv);
        return new GoalXZ(goalX, goalZ);
    }

    public static void goal(int x, int z) {
        if (!BaritoneHelper.isBaritonePresent()) return;
        BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess().setGoal(getBaritoneGoalXZ(x, z));
    }

    public static void path(int x, int z) {
        if (!BaritoneHelper.isBaritonePresent()) return;
        BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess().setGoalAndPath(getBaritoneGoalXZ(x, z));
    }

    public static void elytra(int x, int z) {
        if (!BaritoneHelper.isBaritonePresent()) return;
        if (!BaritoneHelper.isBaritoneElytraPresent()) return;
        BaritoneAPI.getProvider().getPrimaryBaritone().getElytraProcess().pathTo(getBaritoneGoalXZ(x, z));
    }

    public static void gridx(int startx, int startz){
        BaritoneAPI.getProvider().getPrimaryBaritone().getElytraProcess().pathTo(getBaritoneGoalXZ(startx - 10000, startz));
    }

    public static void gridz(int startx, int startz){
        BaritoneAPI.getProvider().getPrimaryBaritone().getElytraProcess().pathTo(getBaritoneGoalXZ(startx, startz - 10000));
    }

    // New method to handle when a goal is reached
    public static void onGoalReached(Runnable callback) {
        if (!BaritoneHelper.isBaritonePresent()) return;
        onGoalReachedCallback = callback;
    }

    // This is the event listener method that is called when Baritone reaches its goal
    @Override
    public void onPathEvent(PathEvent event) {
        if (event.getType() == PathEvent.Type.PATH_COMPLETE && onGoalReachedCallback != null) {
            onGoalReachedCallback.run();  // Trigger the callback once the goal is reached
            onGoalReachedCallback = null; // Reset the callback after running
        }
    }
}
