package com.umc.commonplant.global.exception;

public class MethodNotAllowedException extends RuntimeException{
    private ErrorResponseStatus status;

    public MethodNotAllowedException(ErrorResponseStatus status) {
        this.status = status;
    }
    public ErrorResponseStatus getStatus() {
        return this.status;
    }
}