package com.sgs.mylibrary.request_body;


import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.internal.Util;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

public class BinaryDataRequestBody extends RequestBody {

    MediaType mediaType;
    InputStream inputStream;

    public BinaryDataRequestBody(MediaType mediaType, InputStream inputStream) {
        this.mediaType = mediaType;
        this.inputStream = inputStream;
    }

    @Override
    public MediaType contentType() {
        return mediaType;
    }

    @Override
    public long contentLength() {
        try {
            return inputStream.available();
        } catch(IOException e) {
            return 0;
        }
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        Source source = null;
        try {
            source = Okio.source(inputStream);
            sink.writeAll(source);
        } finally {
            Util.closeQuietly(source);
        }
    }
}
