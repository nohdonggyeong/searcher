package me.donggyeong.searcher.service;

import me.donggyeong.searcher.dto.SearchHitsResponse;

public interface OpenSearchService {
	SearchHitsResponse search(String target, String query, Integer from, Integer size);

}
