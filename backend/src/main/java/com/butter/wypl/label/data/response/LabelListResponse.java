package com.butter.wypl.label.data.response;

import java.util.ArrayList;
import java.util.List;

import com.butter.wypl.label.domain.Label;

public record LabelListResponse(
	int labelCount,
	List<LabelResponse> labels
) {

	public static LabelListResponse from(List<Label> labels) {
		List<LabelResponse> labelResponseList = new ArrayList<>();

		for (Label label : labels) {
			labelResponseList.add(LabelResponse.from(label));
		}

		return new LabelListResponse(labels.size(), labelResponseList);
	}
}
