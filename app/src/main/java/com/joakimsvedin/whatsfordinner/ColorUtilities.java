package com.joakimsvedin.whatsfordinner;

import android.graphics.Color;

public class ColorUtilities {

    public static int getColorForRowPos( int p ) {
        if(p % 2 == 1) {
            // set BG-color for ListView regular row/item
            return Color.parseColor("#E0E0E0");
        } else {
            // set BG-color for alternative row/item
            return Color.parseColor("#FFFFFF");
        }

    }
}
