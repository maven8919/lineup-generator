package com.maven8919.lineupgenerator.view;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.maven8919.lineupgenerator.domain.Player;

@Service
public class PlayerToSummaryTranslator {

    public List<PlayerSummaryView> translatePlayer(List<Player> players) {
        List<PlayerSummaryView> result = new ArrayList<>();
        for (Player player : players) {
            result.add(translate(player));
        }
        return result;
    }

    private PlayerSummaryView translate(Player player) {
        PlayerSummaryView result = new PlayerSummaryView();
        result.setPlayerName(player.getPlayerName());
        result.setPosition(player.getPosition());
        result.setSalary(player.getSalary());
        result.setForecastedPoints(player.avarageMinutes() * player.getAvgPointsPerMinute());
        return result;
    }
    
}
