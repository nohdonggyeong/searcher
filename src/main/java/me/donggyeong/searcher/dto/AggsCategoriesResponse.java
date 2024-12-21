package me.donggyeong.searcher.dto;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AggsCategoriesResponse {
	private Long total;
	private Integer size;
	private Map<String, Long> categories;
	private Long others;
}
