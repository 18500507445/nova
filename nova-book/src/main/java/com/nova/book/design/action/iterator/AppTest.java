package com.nova.book.design.action.iterator;


/**
 * @description:
 * @author: wzh
 * @date: 2023/4/8 23:19
 */
class AppTest {

    public static void main(String[] args) {
        String[] arr = new String[]{"a", "b", "c"};
        ArrayCollection<String> collection = ArrayCollection.of(arr);
        for (Object o : collection) {
            System.out.println("o = " + o);
        }

    }
}
