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

  // Conflict
  CANNOT_DELETE_LAST_ADMIN(409, "Cannot delete the last administrator");

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
