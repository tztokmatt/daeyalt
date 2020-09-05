package scripts.daeyalt;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.util.abc.ABCUtil;
import org.tribot.api2007.*;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Painting;
import org.tribot.script.interfaces.Starting;
import scripts.daeyalt.util.AbcUtils;
import scripts.daeyalt.util.SpecUtils;

import java.awt.*;

@ScriptManifest(name = "Daeyalt", version = 0.1, category = "Mining", authors = {"TzTok-Matt"})
public class DaeyaltPlugin extends Script implements Painting, Starting {
    private static final double HOUR = 3600000.D;
    private static final int ID_DAEYALT_ACTIVE_OBJECT = 39095;
    private static final int ID_DAEYALT_ESSENCE_INV = 24706;
    private static final int OBJECT_FIND_DISTANCE = 30;

    private final ABCUtil abcUtil;
    private int essenceCountStart;
    private int miningXpStart;

    public DaeyaltPlugin() {
        abcUtil = new ABCUtil();
    }

    @Override
    public void onStart() {
        miningXpStart = Skills.getXP(Skills.SKILLS.MINING);
        essenceCountStart = Inventory.getCount(ID_DAEYALT_ESSENCE_INV);
    }

    @Override
    public void run() {
        while (true) {
            // Find the essence rock.
            final RSObject rock = findEssenceRock();
            if (rock == null) {
                // Unable to find a rock. Wait.
                sleep(3000, 5000);
                continue;
            }

            final RSTile rockStart = rock.getPosition();

            // Make sure that the rock is in a place where it can be clicked.
            if (!rock.isClickable()) {
                if (!rock.adjustCameraTo()) {
                    // Unable to adjust the camera in a way that shows the essence.
                    // TODO: Walk somewhere?
                    sleep(3000, 5000);
                    continue;
                }
            }

            AbcUtils.performAllTimedActions(abcUtil);

            if (shouldActivateSpecialAttack()) {
                SpecUtils.clickSpecOrb();
            }

            rock.click("Mine");

            Timing.waitCondition(() -> Player.getAnimation() == -1 || !rock.getPosition().equals(rockStart),
                    60_000);

            // Why not use abcUtil.generateReactionTime? Because the essence moves every 60 seconds, and also because
            // the last interaction was so long ago it causes the reaction times to be really long (>25 seconds).
            General.sleep(General.randomSD(2500, 1000));
        }
    }

    @Override
    public void onPaint(Graphics g) {
        final long runTimeMs = getRunningTime();
        final double runTimeHours = runTimeMs / HOUR;

        final int runXp = Skills.getXP(Skills.SKILLS.MINING) - miningXpStart;
        final long hourlyXp = Math.round(runXp / runTimeHours);

        final int runEss = Inventory.getCount(ID_DAEYALT_ESSENCE_INV) - essenceCountStart;
        final long hourlyEss = Math.round(runEss / runTimeHours);

        final FontMetrics fontMetrics = g.getFontMetrics();
        final int height = fontMetrics.getHeight();

        g.drawString(String.format("Runtime: %s", Timing.msToString(runTimeMs)), 5, 2 * height);
        g.drawString(String.format("Gained: %d XP (%d/hour)", runXp, hourlyXp), 5, 3 * height);
        g.drawString(String.format("Mined: %d ess (%d/hour)", runEss, hourlyEss), 5, 4 * height);
    }

    /**
     * Find the nearest active daeyalt essence rock.
     *
     * @return the essence rock, or {@code null} if one was not found
     */
    private RSObject findEssenceRock() {
        final RSObject[] arr = Objects.findNearest(OBJECT_FIND_DISTANCE, ID_DAEYALT_ACTIVE_OBJECT);
        if (arr.length == 0) {
            return null;
        }
        return arr[0];
    }

    /**
     * Check if the script should activate the special attack.
     *
     * @return {@code true} if the special attack should be activated
     */
    private boolean shouldActivateSpecialAttack() {
        return Equipment.isEquipped("Dragon pickaxe") && SpecUtils.isSpecFull();
    }
}
