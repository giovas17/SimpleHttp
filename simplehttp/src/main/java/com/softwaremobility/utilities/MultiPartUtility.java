package com.softwaremobility.utilities;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;

public class MultiPartUtility {
    private final String boundary;
    private static final String LINE_END = "\r\n";
    private HttpURLConnection httpConn;
    private String charset;
    private OutputStream outputStream;
    private PrintWriter writer;


    public MultiPartUtility(HttpURLConnection httpConn, String charset)
            throws IOException {
        this.charset = charset;
        boundary = "******";
        this.httpConn = httpConn;
        httpConn.setUseCaches(false);
        httpConn.setDoOutput(true);
        httpConn.setDoInput(true);
        httpConn.setRequestProperty("Content-Type",
                "multipart/form-data; boundary=" + boundary);
        outputStream = httpConn.getOutputStream();
        writer = new PrintWriter(new OutputStreamWriter(outputStream, charset),
                true);
    }

    public void addTextPart(String name, String value) {
        writer.append("--").
                append(boundary).
                append(LINE_END).
                append("Content-Disposition: form-data; name=\"").
                append(name).append("\"").
                append(LINE_END).
                append("Content-Type: text/plain; charset=").
                append(charset).append(
                LINE_END);
        writer.append(LINE_END);
        writer.append(value).append(LINE_END);
        writer.flush();
    }

    public void addPhoto(String fieldName, byte[] fileBytes)
            throws IOException {
        writer.append("--")
                .append(boundary)
                .append(LINE_END)
                .append("Content-Disposition: form-data; name=\"")
                .append(fieldName)
                .append("\"; filename=\"")
                .append("imagen1.jpg")
                .append("\"")
                .append(LINE_END)
                .append("Content-Type: image/jpeg")
                .append(LINE_END)
                .append("Content-Transfer-Encoding: BINARY")
                .append(LINE_END)
                .append(LINE_END);
        writer.flush();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(fileBytes);
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        outputStream.flush();
        inputStream.close();

        writer.append(LINE_END);
        writer.flush();
    }

    public HttpURLConnection finish() throws IOException {
        writer.append(LINE_END).flush();
        writer.append("--")
                .append(boundary)
                .append("--")
                .append(LINE_END)
                .close();

        return this.httpConn;
    }
}