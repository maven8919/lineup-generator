package com.maven8919.lineupgenerator.service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ParseDouble;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvMapReader;
import org.supercsv.io.ICsvMapReader;
import org.supercsv.prefs.CsvPreference;

import com.maven8919.lineupgenerator.domain.FieldGoalStats;
import com.maven8919.lineupgenerator.domain.FreeThrowStats;
import com.maven8919.lineupgenerator.domain.Location;
import com.maven8919.lineupgenerator.domain.MiscStats;
import com.maven8919.lineupgenerator.domain.Player;
import com.maven8919.lineupgenerator.domain.StatLine;
import com.maven8919.lineupgenerator.domain.Position;
import com.maven8919.lineupgenerator.domain.ReboundStats;
import com.maven8919.lineupgenerator.domain.Team;
import com.maven8919.lineupgenerator.domain.ThreePointStats;

@Service
public class PlayerListerService {
    
    @Autowired
    private Environment env;
    
    private List<Player> allRelevantPlayersBasedOnMinutes;
    private List<Player> pointGuards;
    private List<Player> shootingGuards;
    private List<Player> smallForwards;
    private List<Player> powerForwards;
    private List<Player> centers;
    private List<Player> guards;
    private List<Player> forwards;

    public List<Player> listPlayers(MultipartFile file) {
        List<Player> players = parseCsv(file);
        return players;
    }
    
    public List<Player> generateStarters(List<Player> players) {
        allRelevantPlayersBasedOnMinutes = filterPlayersByMinutes(players);
        pointGuards = getPlayersByPosition(allRelevantPlayersBasedOnMinutes, Arrays.asList(Position.PG));
        shootingGuards = getPlayersByPosition(allRelevantPlayersBasedOnMinutes, Arrays.asList(Position.SG));
        smallForwards = getPlayersByPosition(allRelevantPlayersBasedOnMinutes, Arrays.asList(Position.SF));
        powerForwards = getPlayersByPosition(allRelevantPlayersBasedOnMinutes, Arrays.asList(Position.PF));
        centers = getPlayersByPosition(allRelevantPlayersBasedOnMinutes, Arrays.asList(Position.C));
        guards = getPlayersByPosition(allRelevantPlayersBasedOnMinutes, Arrays.asList(Position.PG, Position.SG));
        forwards = getPlayersByPosition(allRelevantPlayersBasedOnMinutes, Arrays.asList(Position.SF, Position.PF));
        sortAllPlayers();
        return generateStartersFromSortedLists();
    }

    private List<Player> generateStartersFromSortedLists() {
        int count = 0;
        List<Player> result = null;
        Double maxValue = Double.MIN_VALUE;
        for (Player pg : pointGuards) {
            for (Player sg : shootingGuards) {
                for (Player sf : smallForwards) {
                    for (Player pf : powerForwards) {
                        for (Player c : centers) {
                            for (Player g : guards) {
                                if (g.equals(pg) || g.equals(sg)) continue;
                                for (Player f : forwards) {
                                    if (f.equals(sf) || f.equals(pf)) continue;
                                    for (Player any : allRelevantPlayersBasedOnMinutes) {
                                        count++;
                                        if (count == 100000) {
                                            System.out.println("Still running");
                                            count = 0;
                                        }
                                        if (any.equals(pg) || any.equals(sg) || any.equals(sf) || any.equals(pf) || any.equals(c) || any.equals(g) || any.equals(f)) continue;
                                        int totalSalary = totalSalary(pg, sg, sf, pf, c, g, f, any);
                                        double totalValue = totalValue(pg, sg, sf, pf, c, g, f, any);
                                        if (totalSalary <= 200 && totalValue > maxValue) {
                                            result = Arrays.asList(pg, sg, sf, pf, c, g, f, any);
                                            maxValue = totalValue(pg, sg, sf, pf, c, g, f, any);
                                            System.out.println("Current best lineup: " + pg.getPlayerName() + "-" + sg.getPlayerName() + "-" + g.getPlayerName() + "-" +
                                                    sf.getPlayerName() + "-" + pf.getPlayerName() + "-" + f.getPlayerName() + "-" +
                                                    c.getPlayerName() + "-" + any.getPlayerName() +
                                                    " with salary: " + totalSalary + " and total value of: " + totalValue);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return result;
    }
    
    private int totalSalary(Player pg, Player sg, Player sf, Player pf, Player c, Player g, Player f, Player any) {
        return pg.getSalary() + sg.getSalary() + f.getSalary() + sf.getSalary() +
                pf.getSalary() + c.getSalary() + g.getSalary() + any.getSalary();
    }

    private Double totalValue(Player pg, Player sg, Player sf, Player pf, Player c, Player g, Player f, Player any) {
        return (pg.avarageMinutes() * pg.getAvgPointsPerMinute()) + 
                (sg.avarageMinutes() * sg.getAvgPointsPerMinute()) +
                (sf.avarageMinutes() * sf.getAvgPointsPerMinute()) +
                (pf.avarageMinutes() * pf.getAvgPointsPerMinute()) + 
                (c.avarageMinutes() * c.getAvgPointsPerMinute()) +
                (g.avarageMinutes() * g.getAvgPointsPerMinute()) +
                (f.avarageMinutes() * f.getAvgPointsPerMinute()) +
                (any.avarageMinutes() * any.getAvgPointsPerMinute());
    }

    private void sortAllPlayers() {
        sortAndReverse(allRelevantPlayersBasedOnMinutes);
        sortAndReverse(pointGuards);
        sortAndReverse(shootingGuards);
        sortAndReverse(smallForwards);
        sortAndReverse(powerForwards);
        sortAndReverse(centers);
        sortAndReverse(guards);
        sortAndReverse(forwards);
    }

    private void sortAndReverse(List<Player> listToSortAndReverse) {
        Collections.sort(listToSortAndReverse);
        Collections.reverse(listToSortAndReverse);
    }

    private List<Player> getPlayersByPosition(List<Player> allRelevantPlayersBasedOnMinutes, List<Position> positionFilters) {
        List<Player> result = new ArrayList<>();
        for (Player player : allRelevantPlayersBasedOnMinutes) {
            Position position = player.getPosition();
            if (positionFilters.contains(position)) {
                result.add(player);
            }
        }
        return result;
    }

    private List<Player> filterPlayersByMinutes(List<Player> players) {
        List<Player> result = new ArrayList<>();
        for (Player player : players) {
            if (player.avarageMinutes() >= 20.0 ) {
                result.add(player);
            }
        }
        return result;
    }

    private List<Player> parseCsv(MultipartFile file) {
        List<Player> players = new ArrayList<>();
        ICsvMapReader mapReader = null;
        try {
            mapReader = new CsvMapReader(new InputStreamReader(file.getInputStream()), CsvPreference.STANDARD_PREFERENCE);
            final String[] header = mapReader.getHeader(true);
            final CellProcessor[] processors = getProcessors();
            Map<String, Object> playersMap;
            while ((playersMap = mapReader.read(header,processors)) != null) {
                Player player = mapRowToPlayer(playersMap);
                if (!"INJ".equals((String) playersMap.get("Injury Status")) && !"O".equals((String) playersMap.get("Injury Status"))) {
                    getStatsForPlayer(player);
                    players.add(player);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (mapReader != null) {
                try {
                    mapReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return players;
    }

    private void getStatsForPlayer(Player player) {
        String playerName = player.getPlayerName();
        String playerId = env.getProperty(playerName);
        if (playerId != null) {
            getGameStatsForPlayer(playerId, player);
        }
    }

    private void getGameStatsForPlayer(String playerId, Player player) {
        List<Elements> playerStatTableRows = getPlayerStatRows(playerId);
        for (Elements playerStatTableRow : playerStatTableRows) {
            if (playerStatTableRow.size() >= 22) {
                player.addPlayerGameStat(getGameStat(playerStatTableRow));
            }
        }
    }

    private StatLine getGameStat(Elements playerStatTableRow) {
        Team opponent = getOpponentTeamFromStatRow(playerStatTableRow);
        Location location = getLocationFromStatRow(playerStatTableRow);
        int minutes = getMinutesFromStatRow(playerStatTableRow);
        FieldGoalStats fieldGoalStats = getFieldGoalStatsFromStatRow(playerStatTableRow);
        ThreePointStats threePointStats = getThreePointStatsFromStatRow(playerStatTableRow);
        FreeThrowStats freeThrowStats = getFreeThrowStatsFromStatRow(playerStatTableRow);
        ReboundStats reboundStats = getReboundStatsFromStatRow(playerStatTableRow);
        MiscStats miscStats = getMiscStatsFromStatRow(playerStatTableRow);
        return new StatLine(opponent, location, minutes, fieldGoalStats, threePointStats, freeThrowStats, reboundStats, miscStats);
    }

    private Location getLocationFromStatRow(Elements playerStatTableRow) {
        return Location.getLocationFromString(playerStatTableRow.get(1).text());
    }

    private Team getOpponentTeamFromStatRow(Elements playerStatTableRow) {
        return Team.getTeamFromString(playerStatTableRow.get(1).text());
    }

    private int getMinutesFromStatRow(Elements playerStatTableRow) {
        return Integer.parseInt(playerStatTableRow.get(3).text());
    }
    
    private FieldGoalStats getFieldGoalStatsFromStatRow(Elements playerStatTableRow) {
        int fieldGoalsMade = Integer.parseInt(playerStatTableRow.get(4).text());
        int fieldGoalsAttempted = Integer.parseInt(playerStatTableRow.get(5).text());
        return new FieldGoalStats(fieldGoalsMade, fieldGoalsAttempted);
    }
    
    private ThreePointStats getThreePointStatsFromStatRow(Elements playerStatTableRow) {
        int threePointsMade = Integer.parseInt(playerStatTableRow.get(7).text());
        int threePointsAttempted = Integer.parseInt(playerStatTableRow.get(8).text());
        return new ThreePointStats(threePointsMade, threePointsAttempted);
    }
    
    private FreeThrowStats getFreeThrowStatsFromStatRow(Elements playerStatTableRow) {
        int freeThrowsMade = Integer.parseInt(playerStatTableRow.get(10).text());
        int freeThrowsAttempted = Integer.parseInt(playerStatTableRow.get(11).text());
        return new FreeThrowStats(freeThrowsMade, freeThrowsAttempted);
    }
    
    private ReboundStats getReboundStatsFromStatRow(Elements playerStatTableRow) {
        int offensiveRebounds = Integer.parseInt(playerStatTableRow.get(13).text());
        int defensiveRebounds = Integer.parseInt(playerStatTableRow.get(14).text());
        return new ReboundStats(offensiveRebounds, defensiveRebounds);
    }
    
    private MiscStats getMiscStatsFromStatRow(Elements playerStatTableRow) {
        int assists = Integer.parseInt(playerStatTableRow.get(16).text());
        int turnovers = Integer.parseInt(playerStatTableRow.get(17).text());
        int steals = Integer.parseInt(playerStatTableRow.get(18).text());
        int blocks = Integer.parseInt(playerStatTableRow.get(19).text());
        int points = Integer.parseInt(playerStatTableRow.get(21).text());
        return new MiscStats(assists, turnovers, steals, blocks, points);
    }
    
    private List<Elements> getPlayerStatRows(String playerId) {
        List<Elements> result = new ArrayList<>();
        try {
            Document doc = Jsoup.connect("http://www.rotoworld.com/player/nba/" + playerId).get();
            Elements statsElement = doc.getElementsByClass("statstable");
            Element element = statsElement.get(3);
            Elements rowElements = element.getElementsByTag("tr");
            for (int i = 3; i < rowElements.size(); i++) {
                Element currElement = rowElements.get(i);
                result.add(currElement.getElementsByTag("td"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private Player mapRowToPlayer(Map<String, Object> players) {
        String firstName = convertToLowerCaseAndRemoveSpecialCharacters((String) players.get("First Name"));
        String lastName = convertToLowerCaseAndRemoveSpecialCharacters((String) players.get("Last Name"));
        String playerName = firstName + "-" + lastName;
        int salary = (int) players.get("Salary");
        Position position = parsePosition((String) players.get("Position"));
        Player player = new Player(playerName, position, salary);
        return player;
    }

    private static CellProcessor[] getProcessors() {

        final CellProcessor[] processors = new CellProcessor[]{new Optional(), // Id
            new NotNull(), // firstName
            new NotNull(), // lastName
            new NotNull(), // position
            new Optional(), // team
            new Optional(), // opponent
            new Optional(), // game
            new Optional(), // time
            new NotNull(new ParseInt()), // salary
            new NotNull(new ParseDouble()), // fantasy points per game
            new NotNull() // injury status
        };

        return processors;
    }

    private String convertToLowerCaseAndRemoveSpecialCharacters(String string) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < string.length(); i++) {
            char ch = string.charAt(i);
            if (Character.isWhitespace(ch) || ch == '-') {
                sb.append('-');
            } else if (Character.isLetter(ch)) {
                sb.append(Character.toLowerCase(ch));
            }
        }
        return sb.toString();
    }
    
    private Position parsePosition(String positionString) {
        Position result = null;
        if ("PG".equals(positionString)) {
            result = Position.PG;
        } else if ("SG".equals(positionString)) {
            result = Position.SG;
        } else if ("SF".equals(positionString)) {
            result = Position.SF;
        } else if ("PF".equals(positionString)) {
            result = Position.PF;
        } else {
            result = Position.C;
        }
        return result;
    }

    
    
}
