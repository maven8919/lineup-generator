package com.maven8919.lineupgenerator.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

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

import com.maven8919.lineupgenerator.domain.Player;
import com.maven8919.lineupgenerator.domain.PlayerGameStat;
import com.maven8919.lineupgenerator.domain.Position;

@Service
public class PlayerListerService {

    public List<PlayerGameStat> listPlayers(MultipartFile file) {
        List<PlayerGameStat> playerGameStats = parseCsv(file);
        

        return playerGameStats;
    }

    private List<PlayerGameStat> parseCsv(MultipartFile file) {
        List<PlayerGameStat> playerGameStats = new ArrayList<>();
        ICsvMapReader mapReader = null;
        try {
            mapReader = new CsvMapReader(new InputStreamReader(file.getInputStream()), CsvPreference.STANDARD_PREFERENCE);
            final String[] header = mapReader.getHeader(true);
            final CellProcessor[] processors = getProcessors();
            Map<String, Object> players;
            while ((players = mapReader.read(header,processors)) != null) {
                Player player = mapRowToPlayer(players);
                PlayerGameStat playerGameStat = getStatsFromPlayer(player);
                if (playerGameStat != null) {
                    playerGameStats.add(playerGameStat);
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
        return playerGameStats;
    }

    private PlayerGameStat getStatsFromPlayer(Player player) {
        String playerName = player.getPlayerName();
        String playerId = getPlayerId(playerName);
        return new PlayerGameStat(player, null, null, 0, null, null, null, null, null);
    }

    private String getPlayerId(String playerName) {
        // TODO add additional property file
        // http://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#boot-features-external-config-application-property-files
        String id = null;
        Properties prop = new Properties();
        try (InputStream input = new FileInputStream("player_keys.properties")){
            prop.load(input);
            id = prop.getProperty(playerName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return id;
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
