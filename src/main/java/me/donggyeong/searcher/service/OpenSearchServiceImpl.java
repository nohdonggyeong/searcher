package me.donggyeong.searcher.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch.core.SearchRequest;
import org.opensearch.client.opensearch.core.SearchResponse;
import org.opensearch.client.opensearch.core.search.Hit;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.RequiredArgsConstructor;
import me.donggyeong.searcher.enums.ErrorCode;
import me.donggyeong.searcher.exception.CustomException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class OpenSearchServiceImpl implements OpenSearchService {

	private static final Logger LOGGER = LoggerFactory.getLogger(OpenSearchServiceImpl.class);

	private final OpenSearchClient openSearchClient;
	private final ObjectMapper objectMapper;

	private static final String PREFIX_ALIAS = "alias_for_";

	@Override
	@Transactional(readOnly = true)
	public List<Map<String, Object>> search(String target, String query, Integer from, Integer size) {
		if (StringUtils.isEmpty(target)) {
			throw new CustomException(ErrorCode.INVALID_TARGET);
		}

		try {
			List<Map<String, Object>> responseList = new ArrayList<>();

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
							.fields("*")
						))
					.from(from)
					.size(size)
					.build();
			}

			SearchResponse<ObjectNode> searchResponse = openSearchClient.search(searchRequest, ObjectNode.class);

			for (Hit<ObjectNode> hit : searchResponse.hits().hits()) {
				responseList.add(objectMapper.convertValue(hit.source(), Map.class));
			}
			return responseList;

		} catch (Exception e) {
			LOGGER.error("Error during OpenSearch operation", e);
			throw new CustomException(ErrorCode.OPENSEARCH_SEARCH_OPERATION_FAILED);
		}
	}
}
