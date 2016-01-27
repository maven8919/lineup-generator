package com.maven8919.lineupgenerator.domain;

public enum Team {
    HAWKS,NETS,CELTICS,
    HORNETS, BULLS, CAVS,
    MAVS, NUGGETS, PISTONS,
    WARRIORS, ROCKETS, PACERS,
    CLIPPERS, LAKERS, GRIZZLIES,
    HEAT, WOLVES, BUCKS,
    PELICANS, KNICKS, THUNDER,
    MAGIC, _76ERS, SUNS,
    BLAZERS, KINGS, SPURS,
    RAPTORS, JAZZ, WIZARDS;

    public static Team getTeamFromString(String opponent) {
        Team result = null;
        String opponentWithoutLocationIndicator = opponent.replaceAll("@", "");
        if ("ATL".equals(opponentWithoutLocationIndicator)) {
            result = HAWKS;
        } else if ("BKN".equals(opponentWithoutLocationIndicator)) {
            result = NETS;
        } else if ("BOS".equals(opponentWithoutLocationIndicator)) {
            result = CELTICS;
        } else if ("CHA".equals(opponentWithoutLocationIndicator)) {
            result = HORNETS;
        } else if ("CHI".equals(opponentWithoutLocationIndicator)) {
            result = BULLS;
        } else if ("CLE".equals(opponentWithoutLocationIndicator)) {
            result = CAVS;
        } else if ("DAL".equals(opponentWithoutLocationIndicator)) {
            result = MAVS;
        } else if ("DEN".equals(opponentWithoutLocationIndicator)) {
            result = NUGGETS;
        } else if ("DET".equals(opponentWithoutLocationIndicator)) {
            result = PISTONS;
        } else if ("GS".equals(opponentWithoutLocationIndicator)) {
            result = WARRIORS;
        } else if ("HOU".equals(opponentWithoutLocationIndicator)) {
            result = ROCKETS;
        } else if ("IND".equals(opponentWithoutLocationIndicator)) {
            result = PACERS;
        } else if ("LAC".equals(opponentWithoutLocationIndicator)) {
            result = CLIPPERS;
        } else if ("LAK".equals(opponentWithoutLocationIndicator)) {
            result = LAKERS;
        } else if ("MEM".equals(opponentWithoutLocationIndicator)) {
            result = GRIZZLIES;
        } else if ("MIA".equals(opponentWithoutLocationIndicator)) {
            result = HEAT;
        } else if ("MIN".equals(opponentWithoutLocationIndicator)) {
            result = WOLVES;
        } else if ("NO".equals(opponentWithoutLocationIndicator)) {
            result = PELICANS;
        } else if ("NY".equals(opponentWithoutLocationIndicator)) {
            result = KNICKS;
        } else if ("OKC".equals(opponentWithoutLocationIndicator)) {
            result = THUNDER;
        } else if ("ORL".equals(opponentWithoutLocationIndicator)) {
            result = MAGIC;
        } else if ("PHI".equals(opponentWithoutLocationIndicator)) {
            result = _76ERS;
        } else if ("PHO".equals(opponentWithoutLocationIndicator)) {
            result = SUNS;
        } else if ("POR".equals(opponentWithoutLocationIndicator)) {
            result = BLAZERS;
        } else if ("SAC".equals(opponentWithoutLocationIndicator)) {
            result = KINGS;
        } else if ("SA".equals(opponentWithoutLocationIndicator)) {
            result = SPURS;
        } else if ("TOR".equals(opponentWithoutLocationIndicator)) {
            result = RAPTORS;
        } else if ("UTA".equals(opponentWithoutLocationIndicator)) {
            result = JAZZ;
        } else if ("WAS".equals(opponentWithoutLocationIndicator)) {
            result = WIZARDS;
        } else {
            result = BUCKS;
        }
        return result;
    }
}
