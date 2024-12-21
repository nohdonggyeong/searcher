package me.donggyeong.searcher.service;

import java.util.List;
import java.util.Map;

public interface OpenSearchService {
	List<Map<String, Object>> search(String target, String query, Integer from, Integer size);
}
