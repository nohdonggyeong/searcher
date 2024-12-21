package me.donggyeong.searcher.dto;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class DocsResponse {
	private Long total;
	private Integer from;
	private Integer size;
	private List<Map<String, Object>> documents;
}
