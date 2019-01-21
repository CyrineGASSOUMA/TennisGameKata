package com.sg.kata.tennisgame.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
public class MsgUtils {
    public String getCurrentClassAndMethod(Class clazz) {
        String currentMethod  = new Object() {}.getClass().getEnclosingMethod().getName();
        return "[" + clazz.getSimpleName() + "] [" + currentMethod + "] : ";
    }

    public void logInfoMsg(Class clazz, String msg) {
        Logger logger = LoggerFactory.getLogger(clazz);

        logger.info(getCurrentClassAndMethod(clazz) + msg);
    }
    public void logWarnMsg(Class clazz, String msg) {
        Logger logger = LoggerFactory.getLogger(clazz);

        logger.warn(getCurrentClassAndMethod(clazz) + msg);
    }
    public void logErrorMsg(Class clazz, String msg) {
        Logger logger = LoggerFactory.getLogger(clazz);

        logger.error(getCurrentClassAndMethod(clazz) + msg);
    }
}
