package com.nonglam.open_server.domain.post.dto.response;

import java.util.List;

public record SentimentPredictionResponse(
    List<Integer> predictions) {
}
