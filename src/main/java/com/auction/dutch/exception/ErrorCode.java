package com.auction.dutch.exception;

public enum ErrorCode {

  // User
  USER_NOT_FOUND(404, "User not found"),
  USERNAME_ALREADY_EXISTS(409, "Username already exists"),
  EMAIL_ALREADY_EXISTS(409, "Email already exists"),
  PHONE_ALREADY_EXISTS(409, "Phone number already exists"),
  USER_DELETED(409, "User has been deleted"),

  // Role
  ROLE_NOT_FOUND(404, "Role not found"),

  // Category
  CATEGORY_NOT_FOUND(404, "Category not found"),
  CATEGORY_CANNOT_BE_ITS_OWN_PARENT(400, "A category cannot be its own parent."),

  INVALID_CATEGORY_PARENT(400, "The specified parent category is invalid or does not exist."),

  // Attribute
  ATTRIBUTE_NOT_FOUND(404, "Attribute not found"),
  ATTRIBUTE_CODE_ALREADY_EXISTS(409, "Attribute code already exists"),
  ATTRIBUTE_NAME_ALREADY_EXISTS(409, "Attribute name already exists"),

  // Category attribute
  CATEGORY_ATTRIBUTE_NOT_FOUND(404, "Category attribute not found"),

  // Brand
  BRAND_NOT_FOUND(404, "Brand not found"),

  // Product
  PRODUCT_NOT_FOUND(404, "Product not found"),
  PRODUCT_CODE_ALREADY_EXISTS(409, "Product code already exists"),
  PRODUCT_OUT_OF_STOCK(400, "Product out of stock"),
  PRODUCT_INACTIVE(400, "Product is inactive"),

  // Product media
  PRODUCT_MEDIA_NOT_FOUND(404, "Product media not found"),
  MEDIA_NOT_ASSOCIATED_WITH_PRODUCT(400, "Some media do not belong to the product"),
  PRODUCT_MEDIA_COUNT_EXCEEDED(413, "Maximum number of media files exceeded"),
  INVALID_MEDIA_FILE(400, "Invalid media file format"),
  MEDIA_FILE_TOO_LARGE(413, "Media file size exceeds limit"),
  INVALID_MEDIA_REQUEST(400, "Media request data is missing or empty"),
  MEDIA_TYPE_MISMATCH(400, "Media type does not match the detected file type"),
  REORDER_STATE_CONFLICT(409,
      "The state transition request was rejected because it conflicts with the current resource state or was processed out of order"),
  INVALID_MEDIA_ORDER(422,
      "The requested media sequence is invalid. The number of media items do not match the current product state"),
  DUPLICATE_MEDIA_OPERATION(409,
      "The operation was rejected because a media file with identical attributes already exists for this product"),
  PRODUCT_MEDIA_REQUIRED(400, "Product must have at least one media item (image or video)."),

  // Auction
  AUCTION_NOT_FOUND(404, "Auction session not found"),
  AUCTION_NOT_STARTED(400, "Auction has not started yet"),
  AUCTION_ALREADY_ENDED(400, "Auction already ended"),
  AUCTION_ALREADY_SOLD(400, "Auction already sold"),

  // Wallet
  INSUFFICIENT_BALANCE(400, "Insufficient balance"),

  // Order
  ORDER_NOT_FOUND(404, "Order not found"),

  // Authentication
  UNAUTHORIZED(401, "Unauthorized"),
  INVALID_CREDENTIALS(401, "Invalid username or password"),

  // Authorization
  FORBIDDEN(403, "Access denied"),

  // Validation
  INVALID_REQUEST(400, "Invalid request"),

  // System
  INTERNAL_SERVER_ERROR(500, "Internal server error"),
  STORAGE_ERROR(500, "An error occurred while accessing the storage service"),

  // Conflict
  CANNOT_DELETE_LAST_ADMIN(409, "Cannot delete the last administrator"),

  // To many request
  TOO_MANY_PENDING_UPLOADS(429, "You have too many pending uploads. Please complete or wait for them to expire.");

  private final int status;
  private final String message;

  ErrorCode(int status, String message) {
    this.status = status;
    this.message = message;
  }

  public int getStatus() {
    return status;
  }

  public String getMessage() {
    return message;
  }
}
