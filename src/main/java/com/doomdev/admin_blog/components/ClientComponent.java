package com.doomdev.admin_blog.components;

import com.doomdev.admin_blog.clients.responses.ClientResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Slf4j
@RequiredArgsConstructor
public class ClientComponent {

    public <T> T getResponseData(ResponseEntity<? extends ClientResponse<T>> responseEntity) {
        if (Objects.isNull(responseEntity)) {
            return null;
        }

        if (Objects.isNull(responseEntity.getBody())) {
            return null;
        }

        if (responseEntity.getBody().hasData()) {
            return responseEntity.getBody().getResponseData();
        }

        return null;
    }
}
