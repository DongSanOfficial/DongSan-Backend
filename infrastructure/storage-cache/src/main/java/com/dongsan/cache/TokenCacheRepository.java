package com.dongsan.cache;

import com.dongsan.core.domains.auth.TokenRepository;
import java.util.concurrent.TimeUnit;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TokenCacheRepository implements TokenRepository {
    private final StringRedisTemplate stringRedisTemplate;

    public TokenCacheRepository(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    /**
     * refresh token 저장
     * 키가 존재하는 경우에는 값을 업데이트하고, 존재하지 않으면 새로운 키-값 쌍을 생성한다.
     * 만료기간 : 15일
     * @param memberId      key
     * @param refreshToken  value
     */
    @Override
    public void saveRefreshToken(Long memberId, String refreshToken) {
        String key = generateRefreshTokenKey(memberId);
        stringRedisTemplate.opsForValue().set(key, refreshToken, 15, TimeUnit.DAYS);
    }

    /**
     * refresh token 삭제
     * @param memberId  key
     */
    @Override
    public void deleteRefreshToken(Long memberId){
        String key = generateRefreshTokenKey(memberId);
        stringRedisTemplate.delete(key);
    }

    /**
     * key(memberId) 에 해당하는 Refresh token 조회
     * @param memberId  Key
     * @return  value (Refresh token, 없으면 Null)
     */
    @Override
    public String getRefreshToken(Long memberId){
        String key = generateRefreshTokenKey(memberId);
        return stringRedisTemplate.opsForValue().get(key);
    }

    /**
     * 현재 value(refresh token) 과 비교할 refresh token이 동일한지 확인
     * @param memberId      memberId
     * @param refreshToken  비교할 Refresh token
     * @return      동일하면 true, 아니면 false, 키가 없으면 false
     */
    @Override
    public boolean compareRefreshToken(Long memberId, String refreshToken){
        String key = generateRefreshTokenKey(memberId);
        String value = stringRedisTemplate.opsForValue().get(key);
        return value != null && value.equals(refreshToken);
    }

    private String generateRefreshTokenKey(Long memberId){
        return "refreshToken:" + memberId;
    }

}

