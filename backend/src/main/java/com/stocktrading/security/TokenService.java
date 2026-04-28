package com.stocktrading.security;

import com.stocktrading.entity.User;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

@Service
public class TokenService {
    private final Map<String, TokenInfo> tokens = new ConcurrentHashMap<>();
    private final Duration ttl = Duration.ofHours(8);

    public String issueToken(User user) {
        String token = UUID.randomUUID().toString();
        tokens.put(token, new TokenInfo(user.getId(), Instant.now().plus(ttl)));
        return token;
    }

    public Long getUserId(String token) {
        TokenInfo info = tokens.get(token);
        if (info == null) {
            return null;
        }
        if (Instant.now().isAfter(info.expiresAt())) {
            tokens.remove(token);
            return null;
        }
        return info.userId();
    }

    public void revoke(String token) {
        tokens.remove(token);
    }

    private record TokenInfo(Long userId, Instant expiresAt) {
    }
}
