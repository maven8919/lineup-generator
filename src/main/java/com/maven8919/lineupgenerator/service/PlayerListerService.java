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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import net.sf.javailp.Linear;
import net.sf.javailp.OptType;
import net.sf.javailp.Problem;
import net.sf.javailp.Result;
import net.sf.javailp.Solver;
import net.sf.javailp.SolverFactory;
import net.sf.javailp.SolverFactoryLpSolve;

@Service
public class PlayerListerService {

	private static final Logger log = LoggerFactory.getLogger(PlayerListerService.class);
    private static final int BUDGET = 200;
    
    private List<Player> players;

    @Autowired
    private Environment env;
    
    public List<Player> listPlayers(MultipartFile file) {
        parseCsv(file);
        return players;
    }
    
    public List<Player> generateStarters() {
    	double[] salaries = getPlayerSalaries();
    	double[] pointGuards = getPositionConstraints(Position.PG);
    	double[] shootingGuards = getPositionConstraints(Position.SG);
    	double[] smallForwards = getPositionConstraints(Position.SF);
    	double[] powerForwards = getPositionConstraints(Position.PF);
    	double[] centers = getPositionConstraints(Position.C);
    	double[] max8 = getMax8();
    	
    	List<Variable> variables = new ArrayList<>();
    	for (Player player : players) {
    		Variable variable = Variable.make(player.getPlayerName()).lower(0).upper(1).weight(player.getSalary()).integer(true);
    		variables.add(variable);
    	}
    	
    	ExpressionsBasedModel model = new ExpressionsBasedModel();
    	for (Variable variable : variables) {
    		model.addVariable(variable);
    	}
    	
    	Expression pointGuardCount = model.addExpression("Point guard count").lower(1).upper(3);
    	for (Player player : players) {
    		if (Position.PG == player.getPosition()) {
    			pointGuardCount.set(players.indexOf(player), 1);
    		}
    	}
    	
    	Expression shootingGuardCount = model.addExpression("Shooting guard count").lower(1).upper(3);
    	for (Player player : players) {
    		if (Position.SG == player.getPosition()) {
    			shootingGuardCount.set(players.indexOf(player), 1);
    		}
    	}
    	
    	Expression smallForwardCount = model.addExpression("Small forward count").lower(1).upper(3);
    	for (Player player : players) {
    		if (Position.SF == player.getPosition()) {
    			smallForwardCount.set(players.indexOf(player), 1);
    		}
    	}
    	
    	Expression powerForwardCount = model.addExpression("Power forward count").lower(1).upper(3);
    	for (Player player : players) {
    		if (Position.PF == player.getPosition()) {
    			powerForwardCount.set(players.indexOf(player), 1);
    		}
    	}
    	
    	Expression centerCount = model.addExpression("Center count").lower(1).upper(3);
    	for (Player player : players) {
    		if (Position.C == player.getPosition()) {
    			centerCount.set(players.indexOf(player), 1);
    		}
    	}
    	
    	Optimisation.Result result = model.maximise();
    	log.info(result.toString());
    	
//    	LinearObjectiveFunction func = new LinearObjectiveFunction(salaries, 0);
//    	Collection<LinearConstraint> constraints = new ArrayList<>();
//    	constraints.add(new LinearConstraint(pointGuards, Relationship.GEQ, 1));
//    	constraints.add(new LinearConstraint(pointGuards, Relationship.LEQ, 3));
//    	constraints.add(new LinearConstraint(shootingGuards, Relationship.GEQ, 1));
//    	constraints.add(new LinearConstraint(shootingGuards, Relationship.LEQ, 3));
//    	constraints.add(new LinearConstraint(smallForwards, Relationship.GEQ, 1));
//    	constraints.add(new LinearConstraint(smallForwards, Relationship.LEQ, 3));
//    	constraints.add(new LinearConstraint(powerForwards, Relationship.GEQ, 1));
//    	constraints.add(new LinearConstraint(powerForwards, Relationship.LEQ, 3));
//    	constraints.add(new LinearConstraint(centers, Relationship.GEQ, 1));
//    	constraints.add(new LinearConstraint(centers, Relationship.LEQ, 2));
//    	constraints.add(new LinearConstraint(salaries, Relationship.LEQ, BUDGET));
//    	for (int i = 0; i < players.size(); i++) {
//    		double[] _1playerJustOnceConstraint = create1PlayerConstraintAtSpecificIndex(i);
//    		constraints.add(new LinearConstraint(_1playerJustOnceConstraint, Relationship.LEQ, 1));
//    	}
//    	constraints.add(new LinearConstraint(max8, Relationship.EQ, 8));
    	
//    	SimplexSolver solver = new SimplexSolver();
//    	PointValuePair solution = solver.optimize(new MaxIter(Integer.MAX_VALUE), func, new LinearConstraintSet(constraints),
//    			GoalType.MAXIMIZE, new NonNegativeConstraint(true));
//    	
//    	System.out.println(solution.getValue());
//    	double[] sol = solution.getPoint();
//    	for (int i = 0; i < sol.length; i++) {
//    		System.out.println(sol[i]);
//    	}
    	
    	return null;
	}

	private double[] getPlayerSalaries() {
    	double[] result = new double[players.size()];
    	for (int i = 0; i < players.size(); i++) {
    		result[i] = players.get(i).getSalary();
    	}
		return result;
	}

	private double[] getPositionConstraints(Position position) {
		double[] result = new double[players.size()];
		for (int i = 0; i < players.size(); i++)
			if (position == players.get(i).getPosition()) {
				result[i] = 1;
			} else {
				result[i] = 0;
		}
		return result;
	}
	
	private double[] getMax8() {
		double[] result = new double[players.size()];
		for (int i = 0; i < result.length; i++) {
			result[i] = 1;
		}
		return result;
	}
	
	private double[] create1PlayerConstraintAtSpecificIndex(int index) {
		double[] result = new double[players.size()];
		for (int i = 0; i < players.size(); i++) {
			result[i] = 0;
		}
		result[index] = 1;
		return result;
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
