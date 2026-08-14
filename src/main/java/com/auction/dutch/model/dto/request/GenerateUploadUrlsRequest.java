package com.auction.dutch.model.dto.request;

import java.util.List;

import com.auction.dutch.model.dto.internal.UploadRequestItem;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

public record GenerateUploadUrlsRequest(@NotEmpty @Valid List<UploadRequestItem> items) {

}
