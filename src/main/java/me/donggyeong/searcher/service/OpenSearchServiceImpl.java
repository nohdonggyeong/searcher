package me.donggyeong.searcher.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch._types.SortOrder;
import org.opensearch.client.opensearch.core.SearchRequest;
import org.opensearch.client.opensearch.core.SearchResponse;
import org.opensearch.client.opensearch.core.search.Hit;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.RequiredArgsConstructor;
import me.donggyeong.searcher.dto.AggsCategoriesResponse;
import me.donggyeong.searcher.dto.DocsResponse;
import me.donggyeong.searcher.enums.ErrorCode;
import me.donggyeong.searcher.exception.CustomException;

@Service
@RequiredArgsConstructor
public class OpenSearchServiceImpl implements OpenSearchService {
	private final OpenSearchClient openSearchClient;
	private final ObjectMapper objectMapper;

	private static final String PREFIX_ALIAS = "alias_for_";

	private static final String FIELD_ALL = "*";
	private static final String FIELD_CATEGORY_KEYWORD = "author.keyword";

	private static final String KEY_TOTAL_COUNT = "total_count";
	private static final String KEY_CATEGORY_COUNTS = "category_counts";
	
	private static final String COUNT_BY_ID = "_id";
	private static final String SORT_BY_COUNT = "_count";
	private static final String SORT_BY_KEY = "_key";

	@Override
	@Transactional(readOnly = true)
	public DocsResponse searchDocs(String target, String query, Integer from, Integer size) {
		if (StringUtils.isEmpty(target)) {
			throw new CustomException(ErrorCode.INVALID_TARGET);
		}

		try {
			SearchRequest searchRequest;
			if (StringUtils.isEmpty(query)) {
				searchRequest = new SearchRequest.Builder()
					.index(PREFIX_ALIAS + target)
					.from(from)
					.size(size)
					.build();
			} else {
				searchRequest = new SearchRequest.Builder()
					.index(PREFIX_ALIAS + target)
					.query(q -> q
						.multiMatch(mm -> mm
							.query(query)
							.fields(FIELD_ALL)
						))
					.from(from)
					.size(size)
					.build();
			}
			SearchResponse<ObjectNode> searchResponse = openSearchClient.search(searchRequest, ObjectNode.class);

			List<Map<String, Object>> documents = new ArrayList<>();
			for (Hit<ObjectNode> hit : searchResponse.hits().hits()) {
				documents.add(objectMapper.convertValue(hit.source(), Map.class));
			}

			return new DocsResponse(searchResponse.hits().total().value(), from, size, documents);
		} catch (Exception e) {
			throw new CustomException(ErrorCode.OPENSEARCH_SEARCH_OPERATION_FAILED);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public AggsCategoriesResponse getAggsCategories(String target, Integer size) {
		if (StringUtils.isEmpty(target)) {
			throw new CustomException(ErrorCode.INVALID_TARGET);
		}

		try {
			SearchRequest searchRequest = new SearchRequest.Builder()
				.index(PREFIX_ALIAS + target)
				.size(0)	// No need to return documents, only aggregations
				.aggregations(KEY_TOTAL_COUNT, agg -> agg
					.valueCount(vc -> vc
						.field(COUNT_BY_ID)))
				.aggregations(KEY_CATEGORY_COUNTS, agg -> agg
					.terms(t -> t
						.field(FIELD_CATEGORY_KEYWORD)
						.size(size)
						.order(List.of(
							Map.of(SORT_BY_COUNT, SortOrder.Desc),
							Map.of(SORT_BY_KEY, SortOrder.Asc)
						))
					)
				)
				.build();
			SearchResponse<ObjectNode> searchResponse = openSearchClient.search(searchRequest, ObjectNode.class);

			long totalCounts = (long) searchResponse.aggregations().get(KEY_TOTAL_COUNT).valueCount().value();
			Map<String, Long> categoryCounts = new LinkedHashMap<>();
			searchResponse.aggregations().get(KEY_CATEGORY_COUNTS).sterms().buckets().array().forEach(bucket -> {
				categoryCounts.put(bucket.key(), bucket.docCount());
			});

			return new AggsCategoriesResponse(totalCounts, size, categoryCounts, searchResponse.aggregations().get(
				KEY_CATEGORY_COUNTS).sterms().sumOtherDocCount());
		} catch (Exception e) {
			throw new CustomException(ErrorCode.OPENSEARCH_AGG_OPERATION_FAILED);
		}
	}
}
