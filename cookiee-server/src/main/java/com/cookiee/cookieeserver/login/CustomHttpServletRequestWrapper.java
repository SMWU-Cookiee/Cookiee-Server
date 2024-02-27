package com.cookiee.cookieeserver.login;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

public class CustomHttpServletRequestWrapper extends HttpServletRequestWrapper {
    public CustomHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }
}
