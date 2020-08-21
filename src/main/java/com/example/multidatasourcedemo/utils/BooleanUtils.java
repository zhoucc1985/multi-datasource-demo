package com.example.multidatasourcedemo.utils;

/**
 * @author zhoucc
 * @description 简化if 语句
 * @date 2020/8/21 10:53
 */
public class BooleanUtils<T> {

    public static void excuteIfTrue(Boolean expression , Consumer trueAction,  Consumer falseAction) {
            if (expression) {
                trueAction.accept();
            } else {
                falseAction.accept();
            }
    }

    public static void excuteIfTrue(Boolean expression , Consumer trueAction) {
        if (expression) {
            trueAction.accept();
        }
    }

    public static <T> T excuteIfTrue(Boolean expression, Supplier<? extends T>  trueAction) {
        if (expression) {
            return trueAction.get();
        }
        return null;
    }

    public static <T> T excuteIfTrue(Boolean expression, Supplier<? extends T>  trueAction, Supplier<? extends T>  falseAction) {
        if (expression) {
            return trueAction.get();
        } else {
            return falseAction.get();
        }
    }

    public static interface Consumer {
        void accept();
    }

    public static interface Supplier<T> {
        T get();
    }
}
