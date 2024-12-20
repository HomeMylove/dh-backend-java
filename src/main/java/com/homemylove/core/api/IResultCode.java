package com.homemylove.core.api;

import java.io.Serializable;

public interface IResultCode extends Serializable {
    String getMessage();
    int getCode();
}

