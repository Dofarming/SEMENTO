package com.dfg.semento.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dfg.semento.document.LogDocument;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TestController {

	@GetMapping("/test")
	public ResponseEntity<String> test(){
		return ResponseEntity.ok("test success");
	}
}
