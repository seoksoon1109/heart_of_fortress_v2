# 🏰 Heart of Fortress (요새의 심장)

**Heart of Fortress**는 두 팀이 각자의 “요새의 심장(Heart)”을 보호하고  
상대 팀의 심장을 파괴하면 승리하는 **Minecraft PvP 미니게임 플러그인**입니다.

이 버전은 [Heart_of_Fortress](https://github.com/seoksoon1109/Heart_of_Fortress)의 리팩토링 프로젝트이며,  
우아한테크코스(우테코) 클린 코드 기준을 준수합니다.

---

## ⚙️ 개발 환경

| 항목 | 버전 |
|------|------|
| **Minecraft** | Paper 1.21.4 |
| **Language** | Kotlin 2.0.21 |
| **JDK** | Java 21 |
| **Build System** | Gradle Kotlin DSL |
| **IDE** | IntelliJ IDEA 2024.x |

---

## 🎮 게임 개요

| 항목 | 설명 |
|------|------|
| **게임 방식** | 팀전 (RED vs BLUE) |
| **게임 목표** | 상대 팀의 타워 하트 2개 파괴 후, 요새의 심장을 모두 파괴하면 승리 |
| **진행 흐름** | 준비 → 파밍 → 전투 및 전직 → 상대 하트 파괴 → 승리 판정 |

---

## ⚔️ 직업 시스템

| 직업 | 설명 | 특수 능력 |
|------|------|-----------|
| 🪓 **광부 (Miner)** | 광산에서 자원을 효율적으로 채굴 | 빠른 **채굴** |
| 🛡️ **전사 (Warrior)** | 근접 전투에 특화, 높은 체력과 방어력 | 일정시간 **재생, 힘 효과** |
| 🏹 **궁수 (Archer)** | 원거리 공격 전문, 빠른 이동 속도 | **정밀 사격**, **넉백 효과** |
| 🗡️ **암살자 (Assassin)** | 은신과 기습에 특화된 전사 | **은신**, **순간이동** |

---

## 💎 포인트 & 전직 시스템

- 광산에서 **광석을 채굴하여 판매**하면 포인트를 획득합니다.  
- 포인트는 전직 및 아이템 구매에 사용되며, **최대 4차 전직**까지 가능합니다.  
- 전직 시 추가 능력치 상승 또는 스킬 해금이 이루어집니다.  

---

## 🛒 상점 시스템

- `/hof shop` 명령어 또는 **NPC 상호작용**으로 GUI 상점을 엽니다.  
- 판매 시 `PointService`를 통해 포인트가 자동 정산됩니다.

| 구분 | 예시 아이템 | 효과 |
|------|--------------|------|
| **판매** | 석탄, 철, 금, 다이아몬드 | 포인트 획득 |
| **구매** | 무기, 방어구, 버프 포션 | 전투력 강화 |

---

## 🌳 맵 요소

### 🍎 사과나무 (중앙 3그루)
- 사과를 수확하면 **랜덤 효과 1개** 획득:
  - 💰 포인트 보상  
  - ☠️ 독(Poison) 효과  
  - 💖 재생(Regeneration) 효과  

### 🪙 금 블록 (랜덤 버프 장치)
- **우클릭** 시 **랜덤 효과 1개** 발동:
  - 💪 힘(Strength): 공격력 증가  
  - ⚡ 신속(Speed): 이동속도 증가  
  - 🛡️ 저항(Resistance): 받는 피해 감소  

---

## 🧾 스코어보드 시스템

- 실시간으로 **킬 수**, **팀 하트 체력**, **남은 시간**을 표시합니다.  
- `PlayerDeathEvent`에서 킬 추적, `KillCountListener`가 관리합니다.  
- `ScoreboardService`가 주기적으로 갱신합니다.

| 항목 | 설명 |
|------|------|
| **킬 수** | 플레이어별 전투 성과 |
| **팀 하트 상태** | 각 팀 타워 및 요새 하트 체력 표시 |
| **남은 시간** | 경기 타이머 표시 |
| **표시 위치** | 화면 우측 스코어보드 |

---

## 🗂️ 프로젝트 구조

<details>
  <summary><b>📦 io.github.seoksoon.heartoffortress 패키지 구조 보기</b></summary>

  <pre>
io.github.seoksoon.heartoffortress
├── HeartOfFortressPlugin.kt   # 메인 플러그인 클래스
├── data/                      # PlayerData, SQLite 저장소
├── game/                      # 게임 상태, 로직, 매니저
├── listener/                  # 이벤트 리스너
└── command/                   # 명령어 처리
  </pre>
</details>


---

## 💾 저장 방식

- 플레이어 데이터는 **SQLite (hof.db)** 파일로 자동 저장됩니다.
- `/plugins/HeartOfFortress/hof.db` 경로에 생성됩니다.
- 내부에는 `uuid`, `name`, `team`, `points`, `kills`, `wins` 필드가 저장됩니다.

---

## 🚀 설치 및 실행

1. [PaperMC 1.21.4](https://papermc.io/downloads) 서버 다운로드  
2. `build/libs/HeartOfFortress-1.0.0.jar`를  
   `/plugins/` 폴더에 넣기  
3. 서버 실행:
   ```bash
   java -jar paper-1.21.4.jar
