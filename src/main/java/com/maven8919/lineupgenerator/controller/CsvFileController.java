package com.maven8919.lineupgenerator.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.maven8919.lineupgenerator.domain.Player;
import com.maven8919.lineupgenerator.service.PlayerListerService;
import com.maven8919.lineupgenerator.view.PlayerSummaryView;
import com.maven8919.lineupgenerator.view.PlayerToSummaryTranslator;

@Controller
public class CsvFileController {

    private static final String PLAYERS_MODEL_ATTRIBUTE_NAME = "players";
    private static final String CSV_FILE_REQUEST_MAPPING = "/csvFile";
    private static final String FILE_FORM_NAME = "playersCsv";
    private static final String PLAYERS_LOGICAL_VIEW_NAME = PLAYERS_MODEL_ATTRIBUTE_NAME;
    
    @Autowired
    private PlayerListerService playerListerService;
    @Autowired
    private PlayerToSummaryTranslator playerToSummaryTranslator;

    @ModelAttribute(PLAYERS_MODEL_ATTRIBUTE_NAME)
    public List<PlayerSummaryView> players(@RequestParam(FILE_FORM_NAME) MultipartFile file) {
        List<Player> players = playerListerService.listPlayers(file);
//        List<Player> starters = playerListerService.generateStarters(players);
        return playerToSummaryTranslator.translatePlayer(players);
    }
    
    @RequestMapping(value=CSV_FILE_REQUEST_MAPPING, method=RequestMethod.POST)
    public String csvFile(@RequestParam(FILE_FORM_NAME) MultipartFile file, Model model) {
        return PLAYERS_LOGICAL_VIEW_NAME;
    }
    
}
