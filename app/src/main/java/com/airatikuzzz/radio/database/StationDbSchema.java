package com.airatikuzzz.radio.database;

/**
 * Created by maira on 02.07.2017.
 */

public class StationDbSchema {
    public static final class StationTable{
        public static final String NAME = "stations";
        public static final String NAME_All = "allstations";

        public static final class Cols{
            public static final String TITLE = "title";
            public static final String URL = "url";
            public static final String ICONURL = "iconurl";
            public static final String INFO = "info";
        }
    }
}
