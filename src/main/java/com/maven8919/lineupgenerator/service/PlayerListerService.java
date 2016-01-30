package com.maven8919.lineupgenerator.service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.ojalgo.optimisation.Expression;
import org.ojalgo.optimisation.ExpressionsBasedModel;
import org.ojalgo.optimisation.Optimisation;
import org.ojalgo.optimisation.Variable;
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
import com.maven8919.lineupgenerator.domain.Position;
import com.maven8919.lineupgenerator.domain.ReboundStats;
import com.maven8919.lineupgenerator.domain.StatLine;
import com.maven8919.lineupgenerator.domain.Team;
import com.maven8919.lineupgenerator.domain.ThreePointStats;

@Service
public class PlayerListerService {

    private static final int BUDGET = 200;
    
    private List<Player> players;

    @Autowired
    private Environment env;
    
    public List<Player> listPlayers(MultipartFile file) {
        parseCsv(file);
        return players;
    }

	public List<Player> generateStarters() {
    	List<Player> starters = new ArrayList<Player>();
    	
    	ExpressionsBasedModel model = new ExpressionsBasedModel();
    	for (Player player : players) {
    		model.addVariable(Variable.make(player.getPlayerName()).lower(0).upper(1).weight(player.avarageMinutes() * player.getAvgPointsPerMinute()).integer(true));
    	}
    	
    	Expression pointGuardCount = model.addExpression("Point guard count").lower(1).upper(3);
    	setExpression(pointGuardCount, Position.PG);
    	
    	Expression shootingGuardCount = model.addExpression("Shooting guard count").lower(1).upper(3);
    	setExpression(shootingGuardCount, Position.SG);
    	
    	Expression smallForwardCount = model.addExpression("Small forward count").lower(1).upper(3);
    	setExpression(smallForwardCount, Position.SF);
    	
    	Expression powerForwardCount = model.addExpression("Power forward count").lower(1).upper(3);
    	setExpression(powerForwardCount, Position.PF);
    	
    	Expression centerCount = model.addExpression("Center count").lower(1).upper(2);
    	setExpression(centerCount, Position.C);
    	
    	Expression salaryMax = model.addExpression("Salary").lower(1).upper(BUDGET);
    	Expression max8Players = model.addExpression("Max 8 players").lower(8).upper(8);
    	for (Player player : players) {
    		salaryMax.set(players.indexOf(player), player.getSalary());
    		max8Players.set(players.indexOf(player), 1);
    	}
    	
    	Optimisation.Result result = model.maximise();
    	for (int i = 0; i < players.size(); i++) {
    		if (0.9 < result.doubleValue(i)) {
    			starters.add(players.get(i));
    		}
    	}
    	
    	return starters;
	}

	private void setExpression(Expression expression, Position position) {
		for (Player player : players) {
    		if (position == player.getPosition()) {
    			expression.set(players.indexOf(player), 1);
    		}
    	}
	}

	private void parseCsv(MultipartFile file) {
        players = new ArrayList<>();
        ICsvMapReader mapReader = null;
        try {
            mapReader = new CsvMapReader(new InputStreamReader(file.getInputStream()), CsvPreference.STANDARD_PREFERENCE);
            final String[] header = mapReader.getHeader(true);
            final CellProcessor[] processors = getProcessors();
            Map<String, Object> playersMap;
            while ((playersMap = mapReader.read(header, processors)) != null) {
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

    private Player mapRowToPlayer(Map<String, Object> playersMap) {
        String firstName = convertToLowerCaseAndRemoveSpecialCharacters((String) playersMap.get("First Name"));
        String lastName = convertToLowerCaseAndRemoveSpecialCharacters((String) playersMap.get("Last Name"));
        String playerName = firstName + "-" + lastName;
        int salary = (int) playersMap.get("Salary");
        Position position = parsePosition((String) playersMap.get("Position"));
        Player player = new Player(playerName, position, salary);
        return player;
    }

    private void getStatsForPlayer(Player player) {
        String playerName = player.getPlayerName();
        String playerId = env.getProperty(playerName);
        if (playerId != null) {
            getGameStatsForPlayer(playerId, player);
        }
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

    private void getGameStatsForPlayer(String playerId, Player player) {
        List<Elements> playerStatTableRows = getPlayerStatRows(playerId);
        for (Elements playerStatTableRow : playerStatTableRows) {
            if (playerStatTableRow.size() >= 22) {
                player.addPlayerGameStat(getGameStat(playerStatTableRow));
            }
        }
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

}
