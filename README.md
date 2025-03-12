# Gmail SMTP 설정
# Gmail 계정의 2단계 인증을 활성화한 후, 앱 비밀번호를 생성하여 사용하세요
# 앱 비밀번호 생성 방법: Google 계정 -> 보안 -> 2단계 인증 -> 앱 비밀번호
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=  # 본인의 Gmail 주소 입력 (예: your.email@gmail.com)
spring.mail.password=  # Gmail에서 생성한 앱 비밀번호 16자리 입력 (공백 없이)
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true


Google Calendar API 설정 방법
설정 단계

Google Cloud Console 접속

Google Cloud Console에 로그인


프로젝트 생성

상단 메뉴에서 "새 프로젝트" 선택
프로젝트 이름 입력 후 "만들기"


Calendar API 활성화

왼쪽 메뉴 → "API 및 서비스" → "라이브러리"
"Google Calendar API" 검색하여 선택 후 "활성화"


API 키 생성

왼쪽 메뉴 → "API 및 서비스" → "사용자 인증 정보"
"사용자 인증 정보 만들기" → "API 키" 선택
생성된 키를 복사 (google.api.api-key)


OAuth 클라이언트 ID 생성

"사용자 인증 정보 만들기" → "OAuth 클라이언트 ID"
애플리케이션 유형: "웹 애플리케이션" 선택
승인된 리디렉션 URI 추가 (예: http://localhost/calendar.go)
생성된 ID를 복사 (google.api.client-id)


OAuth 동의 화면 설정

왼쪽 메뉴 → "OAuth 동의 화면"
필수 정보 입력 후 캘린더 스코프 추가 (https://www.googleapis.com/auth/calendar)



애플리케이션 설정
요청 URL
캘린더 기능은 다음 URL로 접근합니다:
Copyhttp://localhost/calendar.go


애플리케이션에서 로그인합니다.
브라우저에서 http://localhost/calendar.go URL로 접속하면 캘린더 페이지로 이동합니다.
구글 계정으로 로그인하여 캘린더 권한을 부여합니다.
캘린더를 선택하고 일정을 추가/조회할 수 있습니다.

