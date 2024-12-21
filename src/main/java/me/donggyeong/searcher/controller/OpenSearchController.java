package me.donggyeong.searcher.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import me.donggyeong.searcher.service.OpenSearchService;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class OpenSearchController {
	private final OpenSearchService openSearchService;

	@GetMapping
	public ResponseEntity<List<Map<String, Object>>> search(
	 	@RequestParam(value = "target") String target,
		@RequestParam(value = "query", required = false) String query,
		@RequestParam(value = "from", required = false, defaultValue = "0") Integer from,
		@RequestParam(value = "size", required = false, defaultValue = "10") Integer size
	) {
		List<Map<String, Object>> documents = openSearchService.search(target, query, from, size);
		return ResponseEntity.status(HttpStatus.OK).body(documents);
	}
}
