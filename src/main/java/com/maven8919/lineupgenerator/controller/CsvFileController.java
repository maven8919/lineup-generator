package com.maven8919.lineupgenerator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.maven8919.lineupgenerator.service.PlayerListerService;

@Controller
public class CsvFileController {

    private static final String PLAYERS_MODEL_ATTRIBUTE_NAME = "players";
    private static final String CSV_FILE_REQUEST_MAPPING = "/csvFile";
    private static final String FILE_FORM_NAME = "playersCsv";
    private static final String PLAYERS_LOGICAL_VIEW_NAME = PLAYERS_MODEL_ATTRIBUTE_NAME;
    
    @Autowired
    private PlayerListerService playerListerService;

    @RequestMapping(value=CSV_FILE_REQUEST_MAPPING, method=RequestMethod.POST)
    public String csvFile(@RequestParam(FILE_FORM_NAME) MultipartFile file, Model model) {
        model.addAttribute(PLAYERS_MODEL_ATTRIBUTE_NAME, playerListerService.listPlayers(file));
        return PLAYERS_LOGICAL_VIEW_NAME;
    }
    
}
