package com.excelToDatabase.excelToDatabase.exception;

public class FileStorageException extends RuntimeException {

    private static final long serialVersionUID=1L;

    public FileStorageException(String msg){
        super(msg);
    }
    public FileStorageException(String msg, Throwable cause){
        super(msg, cause);
    }
}
