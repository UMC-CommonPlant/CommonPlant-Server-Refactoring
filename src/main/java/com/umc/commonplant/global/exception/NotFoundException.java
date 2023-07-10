package com.umc.commonplant.global.exception;

public class NotFoundException extends RuntimeException{
    private ErrorResponseStatus status;

    public NotFoundException(ErrorResponseStatus status) {
        this.status = status;
    }
    public ErrorResponseStatus getStatus() {
        return this.status;
    }

}