## Power of two

![스크린샷 2024-02-11 오후 4 52 35](https://github.com/jylee2033/book-study/assets/85793553/7dd98694-cee7-424f-93b4-4aebf269c36d)

## Latency numbers every programmer should know

![스크린샷 2024-02-11 오후 4 58 26](https://github.com/jylee2033/book-study/assets/85793553/7b09fd7a-a971-4c1c-b87e-d8a41d8ae78b)

![스크린샷 2024-02-11 오후 5 00 29](https://github.com/jylee2033/book-study/assets/85793553/c7dca6d9-6cc7-4324-91d7-92aa891d2915)

### 결론
- 메모리는 빠르지만 디스크는 느림 (디스크 탐색 피해야 함)
- simple compression algorithms은 빠름
- 인터넷을 통해 데이터를 보내기 전에 압축
- 데이터 센터는 보통 다른 지역에 있으며 데이터를 보내는 데 시간이 걸림


## Availability numbers

![스크린샷 2024-02-11 오후 5 15 37](https://github.com/jylee2033/book-study/assets/85793553/06d4664a-79e4-4bfd-b3c3-183b52e1b039)


## Example: Estimate Twitter QPS and storage requirements

### 가정:
- 월간 사용자 300 million(3억)명
- 사용자의 50%가 매일 트위터 사용
- 사용자는 하루에 평균 2개의 트윗을 함
- 트윗의 10%가 미디어 포함
- 데이터는 5년 동안 저장됨

### 추정:
- QPS (초당 쿼리):
  - DAU (일일 활성 사용자) = 300 million * 50% = 150 million (1억 5천만)
  - 트윗 QPS = 150 million * 2 트윗 / 24시간 / 3600초 = ~3500
  - 최대 QPS = 2 * QPS = ~7000

- 미디어 저장공간
    - 평균 트윗 크기:
      - tweet_id : 64 bytes
      - text : 140 bytes
      - media : 1 MB
    - 미디어 저장소 : 150 million * 2 * 10% * 1 MB = 하루 30 TB
    - 5년간 미디어 저장소 : 30TB * 365일 * 5년 = ~55 PB


## Tips

- Back-of-the-envelope estimation은 process에 관한 것
- 문제 해결이 결과보다 더 중요함
- 반올림과 근사값 사용
- assumptions 기록
- 단위 표시 (ex. 5 MB)
- QPS, peak (최대) QPS, storage, cache, 서버 수 등 계산 연습
