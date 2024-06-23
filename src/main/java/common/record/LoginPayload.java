package common.record;

public record LoginPayload(String jwtToken, String refreshToken, int uid, String email) {
}
