package com.cvs.opencv;

import org.opencv.core.Core;

/**
 * Created by gregor.horvat on 30. 01. 2017.
 */
public class Constants {

    public enum BorderType {
//        BORDER_CONSTANT(Core.BORDER_CONSTANT),
        BORDER_REPLICATE(Core.BORDER_REPLICATE),
        BORDER_REFLECT(Core.BORDER_REFLECT),
        BORDER_WRAP(Core.BORDER_WRAP),
        BORDER_DEFAULT(Core.BORDER_DEFAULT),
        BORDER_REFLECT_101(Core.BORDER_REFLECT_101),
        BORDER_TRANSPARENT(Core.BORDER_TRANSPARENT),
        BORDER_ISOLATED(Core.BORDER_ISOLATED);

        int type;
        BorderType(int type) {
            this.type = type;
        }

        public static String[] names() {
            Constants.BorderType[] values = Constants.BorderType.values();
            String[] names = new String[values.length];
            for (int i = 0; i < values.length; i++) {
                names[i] = values[i].name();
            }
            return names;
        }
    }
}
