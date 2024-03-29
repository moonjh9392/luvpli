# 러플리 (Luvpli)

![로고](https://user-images.githubusercontent.com/74370531/205530048-516ae0ad-9cd6-4683-ab1e-104afa0682f6.png)

### 🔗 배포 : https://luvpli.link

### 🔗 노션 : [https://www.notion.so/b6743b64483b462795231baf9c295432?p=787a146451974abfbb7a566db423a74d&pm=c](https://light-eyebrow-b69.notion.site/playlist-2a71599f4f3d4f059f04e7b30190bc5c?pvs=4)

<br/>
<hr/>
<br/>

## 🐻 팀명 : 하리보싱싱 

<br/>

## ✨ 팀원 소개

| <img src="https://user-images.githubusercontent.com/74370531/205542590-a7976816-5e16-4fcd-a8b4-f9fd6f9c5b7e.jpeg" width=130> | <img src="https://user-images.githubusercontent.com/74370531/205542607-04f9d2d8-a721-4614-a31c-8a62975c2174.jpeg" width=130> | <img src="https://user-images.githubusercontent.com/74370531/205542615-6f4d5247-7d2f-4035-8a93-cb1378bf187d.jpeg" width=130> | <img src="https://user-images.githubusercontent.com/74370531/205542630-46f55c38-4dac-4499-a5ea-386f7e335d56.jpg" width=130> | <img src="https://user-images.githubusercontent.com/74370531/205542638-68c63bd6-7059-4c2d-b621-4e1ba38f6f15.jpg" width=130 > | <img src="https://user-images.githubusercontent.com/74370531/205542643-1bae174d-94ce-42f4-a3cf-0ab4db8302f3.jpg" width=130> | 
| :------: | :------: | :------: | :------: | :------: | :------: |
| 문지훈 | 송준모 | 홍유진 | 노영석 | 김아리 | 정경은 |
| 👑 FE | FE | FE | 👑 BE | BE | BE |
|[@moonjh9392](https://github.com/moonjh9392)|[@merrychrisml](https://github.com/merrychrisml)|[@yujinyny](https://github.com/yujinyny)|[@Youngseoki](https://github.com/Youngseoki)|[@lielocks](https://github.com/lielocks)|[@bbororo](https://github.com/bbororo)

<br/>

## ✅ 맡은 역할


- FE
  - 문지훈 : 유튜브 API를 이용한 playlist CRUD 및 마이페이지 관리

  - 송준모 : 방 CRUD, Stomp를 통한 웹 소켓 통신(실시간 채팅), 방에서 playlist 관리

  - 홍유진 : 소셜(Google) 로그인 및 회원정보 수정, 메인(홈, 랭킹, 검색) 기능 담당
- BE
  - 노영석 : 회원 CRUD, 랭킹, 팔로우, 검색

  - 김아리 : 방 CRUD, 웹소켓 Stomp 통신 실시간 채팅 구현

  - 정경은 : playlist CRUD

<br/>

## 🎧 서비스 소개 : 플레이리스트 공유 플랫폼

러플리는 자신이 좋아하는 음악들로 구성한 플레이리스트를 생성해 공유할 수 있는 플레이리스트 공유 플랫폼입니다. 러플리는 접근성이 좋은 유튜브를 활용하여 개인이 플레이리스트를 생성하고, 서로 공유하고, 자신의 플레이리스트를 사람들과 같이 들으며 실시간 소통할 수 있는 서비스입니다. 

러플리에서는 채팅방에서 플레이리스트를 같이 들으며 실시간으로 소통할 수 있고, 서로의 플레이리스트를 북마크하거나 좋아요를 누르는 방식, 맘에 드는 DJ를 팔로우하는 것으로 상호작용이 가능합니다. 

자신의 플레이리스트가 좋아요를 많이 받거나, 자신을 팔로우 한 사람이 많아질수록 러플리 랭킹에 반영이 되고 인기 DJ가 될 수 있습니다!

<br/>

## 구현 핵심 기능

<details>
<summary><b>1. 로그인 토큰 관리</b></summary>
<div markdown="1">

  ```jsx
//액세스 토큰 : 30분 
//리프레시 토큰 : 3일

//로그인시 액세스 토큰,리프레시 토큰 param 으로 받아서 localStorage에 저장

localStorage.setItem('accessToken', accessToken);
localStorage.setItem('refreshToken', refreshToken);

//헤더에 엑세스 토큰 세팅
instance.defaults.headers.Authorization = accessToken;

//만료 로직
//액세스 토큰 만료시 => 서버에서 401 코드를 받음 리프레시 토큰을 담아 재발급 api 요청
//재발급 요청에서 리프레시 토큰 만료 확인 => 서버에서 404 코드 받음 => 로그아웃 
instance.interceptors.response.use(
	(response) => {
		return response;
	},

	async (error) => {
		// 액세스 토큰 만료 => 새 액세스 토큰 발급(연장)
		if (error.response.status === 401) {
			instance
				.post(
					`/api/members/refresh`,
					{},
					{
						headers: {
							RefreshToken: localStorage.getItem('refreshToken'),
						},
					},
				)
				.then((res) => {
					localStorage.setItem('accessToken', res.headers.authorization);

					window.alert('로그인이 연장되었습니다. 새로고침됩니다.');
					window.location.reload();
				})
				.catch((err) => {
					// 리프레시 토큰 만료 => 로그아웃
					if (err.response.status === 404) {
						window.alert('로그인이 만료되었습니다. 홈으로 이동됩니다.');
						window.location.href = '/logout';
					}
				});
		}

		return Promise.reject(error);
	},
);
```

- 쿠키에 토큰을 저장하는 방법도 생각 해봤지만 웹스토리지의 여러가지 이점으로 인해 로컬스토리지에 저장하기로 결정

## Web Storage

- 쿠키와 달리 자동 전송의 위험성이 없음
- **오리진(Origin)**(도메인,프로토콜,포트) 단위로 접근이 제한되는 특성 덕분에 **CSRF로 부터 안전**
- 쿠키보다 **큰 저쟝 용량 지원**(모바일 2.5MB, 데스크탑 5~10MB)
- 서버가 HTTP 헤더를 통해 스토리지 객체를 조작할 수 없음(웹 스토리지 객체 조작은 JavaScript 내에서만 수행)
- 오직 **문자형(string)** 데이터 타입만 지원
- **로컬 스토리지(Local Storage)** 와 **세션 스토리지(Session Storage)** 가 있으며, 같은 Storage 객체를 상속하기 때문에 메서드가 동일  
  
</div>
</details>

<details>
<summary><b>2. 소켓통신을 이용한 채팅방</b></summary>
<div markdown="1">
  
  채팅을 위해 Stomp사용

![image](https://user-images.githubusercontent.com/45509511/207283679-7f6944e0-f963-4ed8-b00c-a5a81b735800.png)

Stomp는 위의 사진과 같은 구조를 하고있음

- connect : 소켓을 연결
- subcribe(구독) : 서버로부터 받는 응답을 처리
- activate(활성화) : 데이터를 send 하기 위해 소켓을 활성화 activate 하면 connect도 같이됨
- publish(send) : 데이터를 서버로 보냄

문제점

- activate 상태에서 event가 발생하면 connect의 연결상태가 false로 바뀌어 event가 발생할때마다 activate를 해줘야함
- 이 증상으로 인하여 서버의 용량이 작으면(EC2 프리티어 사용함) 과부하가 걸려 페이지가 아예 먹통이 되버린다.
  
![image](https://user-images.githubusercontent.com/45509511/207283582-c9880a97-26fd-4416-91b9-74eec72aed52.png)

해결방법

- connect의 연결상태가 false여도 최초에 생성한 subcribe는 살아있어서 서버로 부터 응답을 받을수 있는것을 발견
- event가 발생할때 마다 activate 하지않고 데이터를 보낼때만 activate하는 것으로 변경
- 여기서 다시 <span style="color:red"> **문제** </span> 가 발생 activate ⇒ publish 하는데 activate 되는동안 publish가 먼저 전송되어 연결이 활성화 되어있지 않다는 오류가 발생
- publish하기전에 activate가 될수있는 시간을 벌기위해 publish를 settimeout으로 감싸주어 지연시간을 생성하여 해결함
  
</div>
</details>

<details>
<summary><b>3. 유튜브 API를 이용한 영상 재생</b></summary>
<div markdown="1">

- 영상의 url에서 videoId를 추출하여 유튜브 api에 해당 영상의 데이터 요청
- 받아온 데이터를 DB에 저장 후 여러가지 용도로 사용함

```tsx
https://www.googleapis.com/youtube/v3/videos?id=${id}&key=${process.env.REACT_APP_YOUTUBE_KEY}
//위의 주소로 get 요청을 하면 해당영상의 정보를 받아올 수 있음
//id : 예를 들면 https://www.youtube.com/watch?v=D6cEVIJNlp8 wathc?v={여기가 id}

getYouTubeMusic(videoId)
				.then((res) => {
					if (res.items[0]?.snippet) {
						result = true;
						musicInfo.videoId = videoId;
						musicInfo.url = url;
						musicInfo.channelTitle = res.items[0].snippet.channelTitle;
						musicInfo.title = res.items[0].snippet.title;
						if (res.items[0].snippet.thumbnails.maxres) {
							musicInfo.thumbnail = res.items[0].snippet.thumbnails.maxres.url;
						} else {
							musicInfo.thumbnail = res.items[0].snippet.thumbnails.medium.url;
						}
					}
				})
//url : 영상의 유튜브 주소
//videoId : player를 재생시킬때 필요한 id
//channelTitle : 채널 아이디
//title : 영상 제목
//thumbnail : 썸네일

//데이터들을 저장하여 화면에 보여줄때나 플레이어를 재생 시킬때 사용
```

- react-youtube를 이용하여 영상 재생

```tsx
<YouTube
								videoId={videoId}
								opts={opts}
								onReady={onReady}
								onPlay={onPlay}
							/>

//videoId : 재생될 영상의 videoId
//opts : player의 default 옵션
//onReady : 영상이 준비되었을때 실행할 메소드
//onPlay : 영상이 play 되었을때 실행할 메소드
```

- 화면에서는 player의 크기를 0으로 하여 보이지 않게하고 소리만 들리게 만들어 영상을 보는것이 아닌 음악감상을 하는 느낌을 주게 만들었다.
</div>
</details>

<details>
<summary><b>4. 배포 자동화</b></summary>
<div markdown="1">

- githubAction 으로 dev branch에 push 하면 자동으로 AWS S3에 빌드/배포 되도록 만듬

```yaml
# .github/workflows/main.yml
name: client
on:
  push:
    branches:
      - dev
env:
  REACT_APP_STACK_SERVER: ${{ secrets.REACT_APP_STACK_SERVER }}
  REACT_APP_YOUTUBE_KEY: ${{ secrets.REACT_APP_YOUTUBE_KEY }}
  REACT_APP_STACK_WS_SERVER: ${{ secrets.REACT_APP_STACK_WS_SERVER}}
  REACT_APP_ADMIN_EMAIL_01: ${{secrets.REACT_APP_ADMIN_EMAIL_01}}
  REACT_APP_ADMIN_EMAIL_02: ${{secrets.REACT_APP_ADMIN_EMAIL_02}}
  REACT_APP_ADMIN_EMAIL_03: ${{secrets.REACT_APP_ADMIN_EMAIL_03}}
jobs:
  build:
    runs-on: ubuntu-20.04
    steps:
      - name: Checkout source code.
        uses: actions/checkout@v2

      - name: Install dependencies
        run: npm install
        working-directory: ./client
      - name: Build
        run: npm run build
        env:
          CI: ""
        working-directory: ./client
      - name: SHOW AWS CLI VERSION
        env:
          AWS_ACCESS_KEY_ID: ${{ secrets.AWS_ACCESS_KEY_ID }}
          AWS_SECRET_ACCESS_KEY: ${{ secrets.AWS_SECRET_ACCESS_KEY }}
          AWS_EC2_METADATA_DISABLED: true
        run: |
          aws --version
      - name: Sync Bucket
        env:
          AWS_ACCESS_KEY_ID: ${{ secrets.AWS_ACCESS_KEY_ID }}
          AWS_SECRET_ACCESS_KEY: ${{ secrets.AWS_SECRET_ACCESS_KEY }}
          AWS_EC2_METADATA_DISABLED: true
        run: |
          aws s3 sync \
            --region ap-northeast-2 \
            build s3://luvpli \
            --delete
        working-directory: ./client
```

- github에 repository에 올라가면 안되는 중요한 정보들은 로컬에서는 env를 만들어 사용하였으나 action에서는 적용되지 않기 때문에 secret key를 만들어 사용하였다.
- 배포 순서 : github Action ⇒ AWS S3(http) ⇒ AWS CloudFront(https) ⇒ AWS Route53(도메인 변경)
- Action ⇒ S3는 자동으로 배포되지만 CloudFront는 일정시간마다 재배포되기 때문에 push한 즉시 반영결과를 보고싶다면 CloudFront의 무효화 기능을 이용해야했다.
</div>
</details>

<br/>

## 🚀 기술 스택

### Cloud
<img src="https://img.shields.io/badge/Amazon S3-569A31?style=for-the-badge&logo=Amazon S3&logoColor=white"/> <img src="https://img.shields.io/badge/Amazon RDS-527FFF?style=for-the-badge&logo=Amazon RDS&logoColor=white"/> <img src="https://img.shields.io/badge/Amazon EC2-FF9900?style=for-the-badge&logo=Amazon EC2&logoColor=white"/>

### Web
<img src="https://img.shields.io/badge/Stomp-black?style=for-the-badge&logoColor=white"/>

### Front-end
<img src="https://img.shields.io/badge/TypeScript-3178C6?style=for-the-badge&logo=TypeScript&logoColor=white"/> <img src="https://img.shields.io/badge/React-20232a?style=for-the-badge&logo=React&logoColor=61DAFB"/> <img src="https://img.shields.io/badge/React Router-CA4245?style=for-the-badge&logo=React Router&logoColor=white"/> <img src="https://img.shields.io/badge/Redux Toolkit-764ABC?style=for-the-badge&logo=redux&logoColor=white"/> <img src="https://img.shields.io/badge/Styled Components-DB7093?style=for-the-badge&logo=Styled Components&logoColor=white"/> <img src="https://img.shields.io/badge/axios-5A29E4?style=for-the-badge&logo=axios&logoColor=white"/>

### Back-end
<img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=for-the-badge&logo=Spring Boot&logoColor=white"/> <img src="https://img.shields.io/badge/Spring Security-6DB33F?style=for-the-badge&logo=Spring Security&logoColor=white"/> <img src="https://img.shields.io/badge/Spring Data JPA-6DB33F?style=for-the-badge&logoColor=white"/> <img src="https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens&logoColor=white"/> <img src="https://img.shields.io/badge/OAUTH 2.0-black?style=for-the-badge&&logoColor=white"/> <img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=MySQL&logoColor=white"/>

### Code Management
<img src="https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=Git&logoColor=white"/> <img src="https://img.shields.io/badge/GitHub-black?style=for-the-badge&logo=GitHub&logoColor=white"/>

### Formatting
<img src="https://img.shields.io/badge/ESLint-4B32C3?style=for-the-badge&logo=ESLint&logoColor=white"/> <img src="https://img.shields.io/badge/Prettier-F7B93E?style=for-the-badge&logo=Prettier&logoColor=black"/>

<br/>

## 🖥️ 아키텍처

![아키텍처](https://user-images.githubusercontent.com/74370531/205537012-98c79bf2-4115-4996-a228-945ebf5e4623.png)

<br/>

## ⏩ 기능 시연

<details>
<summary><b>메인(로그인)</b></summary>
<div markdown="1">

![로그인](https://user-images.githubusercontent.com/74370531/205856932-0ba8e78b-ea5a-4bdf-8326-d9af2aaabd6a.gif)

- 러플리 사이트에 접속하면 헤더 오른쪽 상단에 로그인 버튼을 눌러 소셜 로그인을 할 수 있습니다.

- 로그인 된 상태에서 헤더 상단의 본인 정보를 클릭하면 마이페이지로 가거나 로그아웃을 할 수 있습니다.
</div>
</details>

<details>
<summary><b>메인(room/playlist 리스트)</b></summary>
<div markdown="1">

![메인](https://user-images.githubusercontent.com/74370531/205857913-0ed5147e-cd55-41fb-8fbe-a598a6702ad0.gif)

- 메인페이지는 방 메인페이지와 플레이리스트 메인페이지로 나뉘어져 있습니다.

- 방 메인페이지의 경우 현재 생성되어있는 모든 방을 보여주는 전체 리스트와 랭킹에 등재되어 있는 인기 DJ의 방을 보여주는 인기 DJ 방송 리스트, 그리고 가장 많은 인원이 접속해있는 방을 보여주는 가장 많은 청취자가 있는 방송 리스트로 나뉘어져 있습니다.

- 로그인 한 상태라면 방 메인페이지에서 방 생성과 방 접속이 가능합니다.

- 메인페이지의 경우 사용자 참여 및 컨텐츠 탐색에 용이한 무한스크롤로 페이지네이션을 대체했습니다.

- 플레이리스트 메인페이지의 경우 가장 많은 좋아요를 받은 플레이리스트 목록과 랭킹에 등재된 인기 DJ의 플레이리스트 목록, 그리고 전체 플레이리스트 목록으로 구성되어 있습니다.

- 로그인 한 상태라면 플레이리스트 메인페이지에서 플레이리스트 생성과 플레이리스트 상세 조회가 가능합니다.
</div>
</details>

<details>
<summary><b>랭킹</b></summary>
<div markdown="1">

![랭킹](https://user-images.githubusercontent.com/74370531/205857115-d5f1856a-edbd-421e-a964-f5fe4c82f585.gif)

- 러플리에서는 플레이리스트 좋아요와 DJ를 팔로우한 팔로워 수를 합산하여 스코어를 책정하고 높은 순서대로 랭킹에 등재됩니다. 랭킹의 경우 일정시간마다 갱신됩니다.

- 러플리에서는 스코어 점수에 따라 등급을 부여받습니다. 등급은 해당 유저 페이지에서 조회 가능합니다.

<img src="https://user-images.githubusercontent.com/74370531/205537682-3adadbb5-c1d5-4ef3-9345-1be16ea4485e.png" style="width: 300px"/>
</div>
</details>

<details>
<summary><b>검색</b></summary>
<div markdown="1">

![검색](https://user-images.githubusercontent.com/74370531/205857144-2f5a4436-696e-4527-929b-28e68569b8ce.gif)

- 검색 페이지에서는 유저, 플레이리스트, 방을 대분류로 선택하여 검색할 수 있습니다.

- 방의 경우 제목, 장르, 방장명을 소분류로 설정, 플레이리스트의 경우 플레이리스트 제목, 장르, 플레이리스트 생성 유저를 소분류로 설정, 유저의 경우 유저명을 소분류로 설정해 검색합니다.

- 검색 했을 때 해당 방 / 플레이리스트의 장르 태그를 클릭할 경우 해당 장르 태그를 가진 모든 방 / 플레이리스트를 조회합니다.
</div>
</details>

<details>
<summary><b>유저(마이) 페이지</b></summary>
<div markdown="1">

![my1](https://user-images.githubusercontent.com/45509511/205621702-51271264-4ead-4bef-8e79-83680a0af941.gif)

![my2](https://user-images.githubusercontent.com/45509511/205621717-02734795-9f49-471c-84e5-84a4f2decf5c.gif)

- 유저의 페이지에는 닉네임, 자기소개, 유저의 등급을 확인할 수 있습니다. 등급은 랭킹에서 책정되는 스코어에 따라 나뉩니다.

- 본인의 페이지일 경우 닉네임과 자기소개 변경이 가능합니다. 여기서 닉네임은 중복된 이름을 허용하지 않습니다.

- 유저 페이지에서는 해당 유저의 플레이리스트 목록, 해당 유저가 북마크한 플레이리스트 목록, 팔로우 한 DJ를 확인할 수 있습니다. 본인의 페이지일 경우 플레이리스트 생성이 가능하고 생성되어 있는 플레이리스트 수정 및 삭제가 가능합니다. 또한 북마크한 플레이리스트를 해제하거나 팔로우한 DJ를 언팔로우할 수 있습니다.
</div>
</details>

<details>
<summary><b>playlist 생성</b></summary>
<div markdown="1">

![create](https://user-images.githubusercontent.com/45509511/205621776-91feb88c-8000-4476-8b07-dbeb6aa08aa5.gif)

- 플레이리스트는 자신만 볼 수 있게 비공개 설정이 가능합니다.

- 플레이리스트 제목을 설정하고 카테고리를 설정한 후 유튜브 URL을 입력해 추가 버튼을 누르면 플레이리스트 노래 목록에 추가됩니다. 플레이리스트 제목의 경우 20자 이내의 제목 설정이 가능합니다.
</div>
</details>

<details>
<summary><b>playlist 수정</b></summary>
<div markdown="1">

![modify](https://user-images.githubusercontent.com/45509511/205621809-750f5727-4568-4bf5-8093-80e10a956f50.gif)

- 플레이리스트 수정 페이지에서는 플레이리스트 제목, 카테고리, 플레이리스트 노래 목록을 수정할 수 있습니다. 플레이리스트 노래 목록이 2개 이상일 경우 순서를 바꾸고 싶은 노래를 드래그하면 노래의 순서를 원하는 곳으로 이동시킬 수 있습니다.
</div>
</details>

<details>
<summary><b>playlist 삭제</b></summary>
<div markdown="1">

![delete](https://user-images.githubusercontent.com/45509511/205621826-0e9e5257-8d31-49ca-a790-109927bffea3.gif)

- 플레이리스트 상세 페이지에서 삭제 버튼을 누르면 플레이리스트가 삭제됩니다.
</div>
</details><details>
<summary><b>playlist 상세</b></summary>
<div markdown="1">

![detail](https://user-images.githubusercontent.com/45509511/205621905-463530da-fd0a-45b2-81ee-a504eaaf8feb.gif)

- 플레이리스트 상세 페이지에서는 해당 플레이리스트를 좋아요하거나 북마크할 수 있습니다. 본인의 플레이리스트일 경우 수정 및 삭제가 가능하고 플레이리스트 노래 목록에 있는 노래를 클릭하면 해당 유튜브 URL로 이동합니다.
</div>
</details>

<details>
<summary><b>room 생성/수정</b></summary>
<div markdown="1">

<p align="center">
<img src="https://user-images.githubusercontent.com/80391881/205555014-c9317b69-cde0-47d4-a7a1-128947e279ef.gif" style="width: 1000px"/>
</p>

- 방 메인페이지에서 로그인한 상태로 방 만들기 버튼을 클릭할 경우 방 만들기 모달이 나타납니다. 방 제목의 경우 20자 이내로 작성해야 하고 비밀번호를 설정할 수 있습니다. 플레이리스트 추가 버튼을 눌러 해당 방의 플레이리스트를 설정합니다. 설정할 수 있는 플레이리스트는 본인의 플레이리스트 혹은 북마크한 플레이리스트를 선택할 수 있습니다.

- 방에 들어간 이후 방을 만든 사람은 방장 권한을 받습니다. 방장은 오른쪽 하단 채팅방에 참여한 인원 리스트에 방장 아이콘을 부여받습니다. 방장은 방 수정을 할 수 있습니다. 방 수정은 방 제목만 가능합니다.
</div>
</details>

<details>
<summary><b>room 입장/퇴장</b></summary>
<div markdown="1">

<p align="center">
<img src="https://user-images.githubusercontent.com/80391881/205556051-e626f1be-91bc-4e76-8df4-d807ac65b28e.gif" style="width: 1000px"/>
</p>

- 러플리의 유저는 로그인 한 후 메인페이지에 있는 방을 클릭하면 방 입장이 가능합니다. 방의 비밀번호가 존재할 경우 비밀번호를 입력해야 합니다. 방에 입장하면 플레이리스트 안내 모달이 나타납니다.

- 방 나가기를 클릭하면 방을 나갈 수 있습니다. 방에 본인밖에 없을 때 방을 나가게 되면 해당 방은 삭제됩니다.(운영자의 방은 삭제되지 않습니다.)
</div>
</details>

<details>
<summary><b>room 플레이리스트 재생/채팅</b></summary>
<div markdown="1">

<p align="center">
<img src="https://user-images.githubusercontent.com/80391881/205557849-c3e67c13-30d0-4e1e-9edd-a2042b208a99.gif" style="width: 1000px"/>
</p>

- 방에 들어간 이후 오른쪽에 있는 플레이리스트 재생 버튼을 누르면 플레이리스트에 있는 노래가 재생됩니다. 현재 재생되고 있는 노래는 플레이리스트 노래 제목 옆에 스테레오 아이콘으로 표시됩니다.

- 노래가 끝나면 다음 노래가 자동 재생 됩니다.

- 오른쪽 화살표 버튼을 누르면 다음 곡 재생, 왼쪽 화살표 버튼을 누르면 이전 곡 재생이 됩니다. 볼륨 버튼을 클릭하면 음소거를 할 수 있습니다.

- 같은 방에 있는 인원들은 실시간 채팅이 가능합니다. 오른쪽 하단에는 채팅방에 참여한 인원이 표시됩니다. 표시된 인원을 팔로우하거나 표시된 인원의 유저 페이지로 이동이 가능합니다.
</div>
</details>

<br/>

## 📝 커밋 컨벤션

| Emogi | 유형 | 설명 |
| :------: | :------: | :------: |
| ✨ | feat | 기능 생성 |
| 🐛 | fix | 에러 수정 |
| 🚀 | build | 배포 |
| 🎉 | init | 프로젝트 시작 |
| 💄 | style | 스타일 수정 |
| ♻️ | refactor | 리팩토링 |
| 🔨 | chore | 짜잘한 수정 |
| 🔥 | remove | 코드/파일 삭제 |
| ✅ | test | 테스트 추가/수정 |
