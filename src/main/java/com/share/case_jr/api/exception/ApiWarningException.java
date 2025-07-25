package com.share.case_jr.api.exception;

import java.util.List;

public class ApiWarningException extends WarningException {

  public ApiWarningException(String message) {
    super(message);
  }

  public ApiWarningException(List<String> errors) {
    super(errors);
  }
}
