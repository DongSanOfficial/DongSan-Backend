package com.dongsan.core.domains.auth;

public record GetTokenRemaining(
        boolean accessTokenExpired,
        boolean refreshTokenExpired,
        String accessTokenRemaining,
        String refreshTokenRemaining
) {
    public GetTokenRemaining(long accessTokenRemainingTime, long refreshTokenRemainingTime){
        this(
                accessTokenRemainingTime == 0,
                refreshTokenRemainingTime == 0,
                formatTime(accessTokenRemainingTime),
                formatTime(refreshTokenRemainingTime)
        );
    }

    // 밀리초를 "HH:mm:ss" 형식으로 변환하는 메서드
    private static String formatTime(long millis) {
        long seconds = millis / 1000;
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        long remainingSeconds = seconds % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, remainingSeconds);
    }
}
