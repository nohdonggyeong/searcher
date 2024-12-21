package me.donggyeong.searcher.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import me.donggyeong.searcher.dto.AggsCategoriesResponse;
import me.donggyeong.searcher.dto.DocsResponse;
import me.donggyeong.searcher.service.OpenSearchService;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class OpenSearchController {
	private final OpenSearchService openSearchService;

	@GetMapping("/docs")
	public ResponseEntity<DocsResponse> searchDocs(
	 	@RequestParam(value = "target") String target,
		@RequestParam(value = "query", required = false) String query,
		@RequestParam(value = "from", required = false, defaultValue = "0") Integer from,
		@RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
		DocsResponse docsResponse = openSearchService.searchDocs(target, query, from, size);
		return ResponseEntity.status(HttpStatus.OK).body(docsResponse);
	}

	@GetMapping("/aggs/categories")
	public ResponseEntity<AggsCategoriesResponse> getAggsCategories(
		@RequestParam(value = "target") String target,
		@RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
		AggsCategoriesResponse aggsCategoriesResponse = openSearchService.getAggsCategories(target, size);
		return ResponseEntity.status(HttpStatus.OK).body(aggsCategoriesResponse);
	}
}
