package me.donggyeong.searcher.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class OpenSearchController {
	// private final OpenSearchService openSearchService;
	//
	// @PostMapping
	// public ResponseEntity<List<Map<String, Object>>> search(
	// 	@RequestParam(value = "query") String query,
	// 	@RequestParam(value = "from", required = false, defaultValue = "0") int from,
	// 	@RequestParam(value = "size", required = false, defaultValue = "10") int size
	// ) {
	// 	List<Map<String, Object>> documents = openSearchService.search(query, from, size);
	// 	return ResponseEntity.status(HttpStatus.OK).body(documents);
	// }
}
