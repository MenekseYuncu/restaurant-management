package org.violet.restaurantmanagement.common.exception;

import java.io.Serial;

public class RmaInvalidException extends RuntimeException {

  @Serial
  private static final long serialVersionUID = 621199513858865491L;

  public RmaInvalidException(String message) {
        super(message);
    }
}
