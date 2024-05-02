# 프로젝트 GitFlow 브랜치 전략 가이드
이 README는 우리 프로젝트에서 GitFlow 브랜치 전략을 어떻게 구현하고 사용해야 하는지에 대한 상세한 지침을 제공합니다. 모든 개발자는 이 가이드를 따라 일관된 브랜치 관리와 협업을 수행해야 합니다.
## 주의! 모든 커밋메시지 앞에는 issue 넘버를 붙인다
## tag 에 붙이는 버전은 1.0.0 으로 통일한다 (추후 수정 필요하면 공지할 예정)
## 커밋메세지는 영어로 통일

## 브랜치 구조 및 용도
### 1. main
- 목적: 안정적인 릴리즈 버전 유지.
- 사용: 릴리즈된 버전의 코드 저장소. 최종 사용자에게 배포되는 코드.
### 2. develop
- 목적: 다음 릴리즈 준비를 위한 개발 진행.
- 사용: 모든 개발이 이루어지는 기본 브랜치. 이곳에서 분기된 feature 브랜치가 병합되고, 준비된 코드는 release 브랜치로 이동.
### 3. feature
- 목적: 새 기능 개발, 기존 기능의 개선 및 버그 수정.
- 사용: develop 브랜치에서 분기하여 사용. 각 기능 또는 버그 수정에 대해 별도의 브랜치를 생성.
### 4. release
- 목적: 다음 버전 릴리즈 준비.
- 사용: 릴리즈를 앞두고 최종 테스트와 버그 수정을 위해 사용. develop 브랜치에서 분기하며, 완료 후 main으로 병합 및 태그 지정.
### 5. hotfix
- 목적: 릴리즈된 버전에서 발견된 긴급한 문제 해결.
- 사용: main 브랜치에서 직접 분기하여 긴급 수정 후 main과 develop에 병합.

  
## 상세 작업 절차
### 새 기능 개발
1. 브랜치 생성:
```bash
Copy code
git checkout develop
git pull origin develop
git checkout -b feature/기능명
```

2. 개발 작업 수행 후 커밋:

```bash
Copy code
git add .
git commit -m "새 기능: 기능명에 대한 설명"
```

3. 개발 브랜치로 병합 요청 (Pull Request):
- develop 브랜치로 Pull Request 생성.
- 동료의 코드 리뷰 후 병합 승인.

## 릴리즈 준비
1. 릴리즈 브랜치 생성 및 준비:
```bash
Copy code
git checkout develop
git pull origin develop
git checkout -b release/버전
```

2. 릴리즈 최종 확인 및 버그 수정:
- 모든 팀원이 테스트 참여.
- 필요한 모든 수정 사항 커밋.

3. 릴리즈 완료:
```bash
Copy code
git checkout main
git merge release/버전
git tag -a v버전 -m "릴리즈 버전 메시지"
git push origin main --tags
git checkout develop
git merge release/버전
git push origin develop
```
## 긴급 수정 (Hotfix)
1. Hotfix 브랜치 생성:
```bash
Copy code
git checkout main
git pull origin main
git checkout -b hotfix/버그명
```
2. 버그 수정 및 커밋:
``` bash
Copy code
git add .
git commit -m "긴급 수정: 버그명에 대한 설명"
```
3.병합 및 배포:
```bash
Copy code
git checkout main
git merge hotfix/버그명
git tag -a v버전 -m "Hotfix 버전 메시지"
git push origin main --tags
git checkout develop
git merge hotfix/버그명
git push origin develop ```
