package com.varunmishra.foregroundservice;

/**
 * Created by varun on 10/24/17.
 */

public class Constants {
    public interface ACTION {
        public static String STARTFOREGROUND_ACTION = "com.varunmishra.foregroundservice.action.startforeground";
        public static String STOPFOREGROUND_ACTION = "com.varunmishra.foregroundservice.action.stopforeground";
    }

    public interface NOTIFICATION_ID {
        public static int FOREGROUND_SERVICE = 101;
    }
}
