package com.auction.dutch.model.dto.response;

import java.util.List;

public record GenerateUploadUrlsResponse(

        List<UploadUrlItemResponse> items

) {
}
