package com.doomdev.admin_blog.clients.responses;

public interface ClientResponse<T> {
    T getResponseData();

    boolean hasData();

    boolean isSuccess();
}
