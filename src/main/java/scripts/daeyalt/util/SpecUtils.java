/*
 * Spec Utils
 *
 * Revision: 4
 * License: Unlicense (see https://unlicense.org for more details)
 * TRiBot Forum Thread: https://tribot.org/forums/topic/82925-snippet-special-attack/
 * Original author: TzTok-Matt, with contributions from Optimus
 */
package scripts.daeyalt.util;

import org.tribot.api2007.Game;
import org.tribot.api2007.GameTab;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.types.RSInterface;

/**
 * Helpers related to special attacks.
 */
public class SpecUtils {
    private static final int INTERFACEID_ATTACK_TAB = 593;
    private static final int INTERFACEID_MINIMAP_AREA = 160;
    private static final int INTERFACEID_SPEC_BAR = 40;
    private static final int INTERFACEID_SPEC_ORB = 30;
    private static final int SETTING_SPEC_LEVEL = 300;
    private static final int SETTING_SPEC_ACTIVE = 301;
    private static final int SPEC_MAX = 1000;

    /**
     * Get the player's special attack level.
     *
     * @return the player's special attack level, in tenths of a percent
     */
    public static int getSpecLevel() {
        return Game.getSetting(SETTING_SPEC_LEVEL);
    }

    /**
     * Check if the player's special attack is active/primed.
     *
     * @return {@code true} if the special attack is enabled, {@code false} otherwise
     */
    public static boolean getSpecActive() {
        return Game.getSetting(SETTING_SPEC_ACTIVE) == 1;
    }

    /**
     * Check if the player's special attack meter is full.
     *
     * @return {@code true} if the player's special attack meter is full, {@code false} otherwise
     */
    public static boolean isSpecFull() {
        return getSpecLevel() == SPEC_MAX;
    }

    /**
     * Switch to the attack tab, and click the special attack bar to (hopefully) activate the special attack.
     *
     * @return {@code true} if the spec bar was successfully clicked, {@code false} otherwise
     */
    public static boolean clickSpecBar() {
        if (!GameTab.TABS.COMBAT.open()) {
            return false;
        }

        final RSInterface specBar = Interfaces.get(INTERFACEID_ATTACK_TAB, INTERFACEID_SPEC_BAR);
        if (specBar == null) {
            return false;
        }

        return specBar.click("Use");
    }

    /**
     * Click the special attack orb to (hopefully) activate the special attack.
     *
     * @return {@code true} if the spec orb was successfully clicked, {@code false} otherwise
     */
    public static boolean clickSpecOrb() {
        final RSInterface specOrb = Interfaces.get(INTERFACEID_MINIMAP_AREA, INTERFACEID_SPEC_ORB);
        if (specOrb == null) {
            return false;
        }

        return specOrb.click("Use");
    }

    // Prevent instantiation
    private SpecUtils() {
    }
}

