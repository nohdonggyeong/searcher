package me.donggyeong.searcher.service;

import me.donggyeong.searcher.dto.AggsCategoriesResponse;
import me.donggyeong.searcher.dto.DocsResponse;

public interface OpenSearchService {
	DocsResponse searchDocs(String target, String query, Integer from, Integer size);
	AggsCategoriesResponse getAggsCategories(String target, Integer size);
}
