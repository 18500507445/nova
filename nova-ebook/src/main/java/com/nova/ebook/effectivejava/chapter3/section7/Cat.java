package com.nova.ebook.effectivejava.chapter3.section7;

/**
 * @description:
 * @author: wzh
 * @date: 2023/2/15 19:01
 */
interface Cat {

    public static final String colour = "default";

    String getColour(String name);

    void catchMouse();

}

enum CatColour {

    BLUE,

    BLACK;
}

class BlueCat implements Cat {

    public static final String colour = "blue";

    @Override
    public String getColour(String name) {
        return CatColour.BLUE.name();
    }

    @Override
    public void catchMouse() {

    }
}

class BlackCat implements Cat {

    public static final String colour = "black";

    @Override
    public String getColour(String name) {
        return CatColour.BLACK.name();
    }

    @Override
    public void catchMouse() {

    }
}
