package scripts.daeyalt.util;

import org.tribot.api.util.abc.ABCUtil;

/**
 * Helpers for using {@link ABCUtil}.
 */
public class AbcUtils {
    /**
     * Perform all of ABC2's timed actions.
     *
     * @param abcUtil script's {@code ABCUtil} instance
     */
    public static void performAllTimedActions(ABCUtil abcUtil) {
        if (abcUtil.shouldCheckTabs()) {
            abcUtil.checkTabs();
        }

        if (abcUtil.shouldCheckXP()) {
            abcUtil.checkXP();
        }

        if (abcUtil.shouldExamineEntity()) {
            abcUtil.examineEntity();
        }

        if (abcUtil.shouldLeaveGame()) {
            abcUtil.leaveGame();
        }

        if (abcUtil.shouldMoveMouse()) {
            abcUtil.moveMouse();
        }

        if (abcUtil.shouldPickupMouse()) {
            abcUtil.pickupMouse();
        }

        if (abcUtil.shouldRightClick()) {
            abcUtil.rightClick();
        }

        if (abcUtil.shouldRotateCamera()) {
            abcUtil.rotateCamera();
        }

    }

    private AbcUtils() {
    }
}
