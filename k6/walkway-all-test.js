import http from 'k6/http';
import {check, sleep} from 'k6';

const BASE_URL = "http://localhost:8080";
const VUSER_COUNT = 30; // 사용자 수를 줄여서 테스트 용이하게
const REQUESTS_PER_USER = 2;
const SLEEP_TIME = 1;

export let options = {
    stages: [
        {duration: '30s', target: VUSER_COUNT}, // N초 동안 N명의 유저
    ],
    thresholds: {
        'http_req_duration': ['p(95)<500'],
        'http_req_failed': ['rate<0.01'],
        'http_reqs': ['count > 0'], // 최소 1개 이상의 요청 성공
    },
};

function parseSetCookieHeader(headerStr, name) {
    const regex = new RegExp(`${name}=([^;]+)`);
    const match = headerStr.match(regex);
    return match ? match[1] : null;
}

// setup 함수: 테스트 시작 전 한 번만 실행되어 인증 쿠키를 얻습니다.
export function setup() {
    console.log("setup : Setting up the test: Initial login to get session cookie.");

    let loginUrl = `${BASE_URL}/dev/token`;
    let loginPayload = JSON.stringify({memberId: '1'});
    let loginParams = {
        headers: {
            'Content-Type': 'application/json',
        },
    };
    let loginRes = http.post(loginUrl, loginPayload, loginParams);

    check(loginRes, {
        'setup: login status is 200': (r) => r.status === 200,
        //'setup: login successful (Set-Cookie header received)': (r) => r.headers['Set-Cookie'] !== undefined,
    });

    // 모든 쿠키 로그
    console.log('setup : 📦 Received cookies from login:');
    // ✅ Set-Cookie 수동 파싱
    const setCookieHeader = loginRes.headers['Set-Cookie'];
    const accessToken = parseSetCookieHeader(setCookieHeader, 'accessToken');
    const refreshToken = parseSetCookieHeader(setCookieHeader, 'refreshToken');

    if (!accessToken || !refreshToken) {
        console.error('setup : ❌ 쿠키에서 accessToken 또는 refreshToken을 찾을 수 없습니다.');
    }

    console.log('setup : ✅ Parsed accessToken:', accessToken);
    console.log('setup : ✅ Parsed refreshToken:', refreshToken);

    return {
        accessToken,
        refreshToken,
    };
}


export default function (data) { // setup에서 반환된 data를 인자로 받습니다.
    const jar = http.cookieJar();
    jar.set(BASE_URL, 'accessToken', data.accessToken);
    jar.set(BASE_URL, 'refreshToken', data.refreshToken);

    // 요청을 반복 수행 (각 유저는 N번 요청)
    for (let i = 0; i < REQUESTS_PER_USER; i++) {
        let url = `${BASE_URL}/walkways/all`;
        const sortParam = 'liked';
        const sizeParam = '10';
        url = `${url}?sort=${sortParam}&size=${sizeParam}`;

        // 인증 헤더를 포함하여 요청 전송
        let res = http.get(url);

        check(res, {
            'is status 200': (r) => r.status === 200,
        });

        if (res.status !== 200) {
            console.error(`API request failed with status: ${res.status}, URL: ${url}, Body: ${res.body}`);
        }

        sleep(SLEEP_TIME);
    }
}

export function teardown(data) {
    console.log("Tearing down the test.");
    if (!data.loginSuccess) {
        console.error("Test was run without successful setup login.");
    }
}
