package com.liskovsoft.smartyoutubetv2.common.misc;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ProfileGridLayoutTest {
    @Test
    public void rowsFollowThreeColumnGrid() {
        assertEquals(0, ProfileGridLayout.rowsFor(0));
        assertEquals(1, ProfileGridLayout.rowsFor(1));
        assertEquals(1, ProfileGridLayout.rowsFor(3));
        assertEquals(2, ProfileGridLayout.rowsFor(4));
        assertEquals(2, ProfileGridLayout.rowsFor(6));
        assertEquals(3, ProfileGridLayout.rowsFor(7));
        assertEquals(3, ProfileGridLayout.rowsFor(9));
        assertEquals(3, ProfileGridLayout.rowsFor(12)); // clamped, scroll handles the rest
    }

    @Test
    public void topPaddingCentersShortGridsAndNeverGoesNegative() {
        assertEquals(0, ProfileGridLayout.topPaddingPx(3, 600, 200, 1080)); // overflow -> 0
        assertEquals(390, ProfileGridLayout.topPaddingPx(1, 300, 0, 1080)); // (1080 - 300) / 2
        assertTrue(ProfileGridLayout.topPaddingPx(2, 600, 100, 1080) >= 0);
        assertEquals(0, ProfileGridLayout.topPaddingPx(0, 300, 0, 1080)); // no accounts
    }
}
