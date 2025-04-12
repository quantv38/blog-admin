package com.doomdev.admin_blog.configs.security.identityservice;

import com.doomdev.admin_blog.components.ClientComponent;
import com.doomdev.admin_blog.configs.security.identityservice.requests.IntrospectRequest;
import com.doomdev.admin_blog.configs.security.identityservice.responses.IdentityDefaultResponse;
import com.doomdev.admin_blog.configs.security.identityservice.responses.IntrospectResponse;
import com.doomdev.admin_blog.configs.security.identityservice.responses.UserInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class IdentityServiceImpl implements IdentityService {
    private final IdentityClient identityClient;

    private final ClientComponent clientComponent;

    @Override
    public IntrospectResponse introspect(String token) {
        try {
            IntrospectRequest request = new IntrospectRequest(token);
            ResponseEntity<IdentityDefaultResponse<IntrospectResponse>> response = identityClient.introspect(request);

            return clientComponent.getResponseData(response);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public UserInfoResponse getUserInfo(String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);

            ResponseEntity<IdentityDefaultResponse<UserInfoResponse>> response = identityClient.getUserInfo(headers);

            return clientComponent.getResponseData(response);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }


}
