package com.liskovsoft.smartyoutubetv2.common.misc;

/**
 * Pure math for the profile picker. No Android imports: unit-testable on the JVM.
 * The grid always has 3 columns; rows grow 1 -> 3 and then the grid scrolls.
 */
public final class ProfileGridLayout {
    public static final int COLUMNS = 3;
    public static final int MAX_ROWS = 3;

    private ProfileGridLayout() {
    }

    public static int rowsFor(int accounts) {
        if (accounts <= 0) {
            return 0;
        }

        return Math.min(MAX_ROWS, (accounts + COLUMNS - 1) / COLUMNS);
    }

    /**
     * Vertical padding used to center a short grid (1 or 2 rows) inside the screen.
     * Returns 0 when the grid is taller than the available space.
     */
    public static int topPaddingPx(int rows, int rowHeightPx, int gridTopPx, int screenHeightPx) {
        if (rows <= 0 || rowHeightPx <= 0) {
            return 0;
        }

        int free = screenHeightPx - (rows * rowHeightPx) - gridTopPx;

        return Math.max(0, free / 2);
    }
}
