package com.cookiee.cookieeserver.login;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

public class CustomHttpServletResponseWrapper extends HttpServletResponseWrapper {
    public CustomHttpServletResponseWrapper(HttpServletResponse response) {
        super(response);
    }
}
