package com.sgs.mylibrary.log_interface;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface File {

    String read(String filename) throws IOException;

    String read(InputStream inputStream) throws IOException;

    void write(String filename, String content) throws IOException;

    void write(OutputStream outputStream, String content) throws IOException;
}
