package com.doomdev.admin_blog.configs.security.identityservice;

import com.doomdev.admin_blog.configs.security.identityservice.requests.IntrospectRequest;
import com.doomdev.admin_blog.configs.security.identityservice.responses.IdentityDefaultResponse;
import com.doomdev.admin_blog.configs.security.identityservice.responses.IntrospectResponse;
import com.doomdev.admin_blog.configs.security.identityservice.responses.UserInfoResponse;
import lombok.Getter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = IdentityClient.CLIENT_NAME, url = "${admin-blog.iam.url}")
public interface IdentityClient {
    String CLIENT_NAME = "IdentityClient";

    @PostMapping("/identity/auth/introspect")
    ResponseEntity<IdentityDefaultResponse<IntrospectResponse>> introspect(@RequestBody IntrospectRequest request);

    @GetMapping("/identity/users/userinfo")
    ResponseEntity<IdentityDefaultResponse<UserInfoResponse>> getUserInfo(@RequestHeader HttpHeaders headers);
}
