package com.maven8919.lineupgenerator.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {
	
	private static final String INDEX_REQUEST_MAPPING = "/";
    private static final String INDEX_LOGICAL_VIEW_NAME = "index";

	@RequestMapping(INDEX_REQUEST_MAPPING)
	public String index() {
		return INDEX_LOGICAL_VIEW_NAME;
	}
	
}
