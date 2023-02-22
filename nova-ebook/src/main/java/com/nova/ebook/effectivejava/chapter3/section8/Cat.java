package com.nova.ebook.effectivejava.chapter3.section8;

/**
 * @description: 通俗解释就是少写if else，多用继承和接口
 * @author: wzh
 * @date: 2023/2/15 19:01
 */
class Cat {

    void catchMouse() {
        if ("blue".equals(CatColour.BLUE.getName())) {

        } else if ("black".equals(CatColour.BLACK.getName())) {

        }
    }

}

enum CatColour {

    BLUE("blue"),

    BLACK("black");

    private final String name;

    public String getName() {
        return name;
    }

    CatColour(String name) {
        this.name = name;
    }
}

class BlueCat extends Cat {

    @Override
    public void catchMouse() {

    }
}

class BlackCat extends Cat {

    @Override
    public void catchMouse() {

    }
}
