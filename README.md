# KikoriTaikai Plugin

---

## English

**Game Title:** Kikori Taikai (Lumberjack Tournament)

**Subtitle:** Forest Extinction! "Human Bulldozer Championship"

### Overview
This plugin runs a lumberjack tournament on a Minecraft server. Players gain points when they break logs, and a ranking is displayed.

### Features
- Breaking a log counts as 1 point (stripped logs are ignored)
- Top 10 players are shown on a scoreboard by MCID
- Players below 10th place see their rank on the action bar
- Updates every 3 seconds
- Add additional log block IDs via `config.yml`

### Commands (OP only)
- `/kstart`: Start the game
- `/kstop`: Stop the game (results shown in chat)
- `/kclear`: Clear rankings (confirmation required)
- `/kreload`: Reload the config

### Data Storage
- Rankings are saved by UUID and MCID in the plugin folder
- Data persists across restarts
- Ongoing games continue after restart
- Rankings can be cleared with `/kclear`

### Installation
1. Place the JAR file into the `plugins` folder
2. Restart the server or reload the plugin
3. Edit the log list in `config.yml` if needed

### Configuration
Specify target log block IDs as a list in `config.yml`.


### Notes
- There is no protection against counting player-placed blocks (technical limitation).
- Compatible with Paper API 1.21 or newer.

Note: This project was initially developed with AI. I have performed basic checks and have not found obvious bugs, but issues may still exist. The author does not generally handle issue reports—please fork the repository and submit a pull request if you need fixes. Enjoy!

This is just a novelty plugin — for fun.

---

## 日本語

**ゲームタイトル:** 木こり大会（Kikori Taikai）

**サブタイトル:** 森林消滅！「人間ブルドーザー決定戦」

### 概要
このプラグインは、Minecraftサーバーで木こり大会を実施するためのものです。プレイヤーが原木を採掘するとスコアが加算され、ランキングが表示されます。

### 機能
- 原木を採掘すると1カウント（樹皮を剥いだ原木は対象外）
- スコアボードにMCIDのランキングトップ10を表示
- 10位より下のプレイヤーはアクションバーに自分の順位を表示
- 更新は3秒おき
- `config.yml`で原木のブロックIDを追加可能

### コマンド（OPのみ）
- `/kstart` : ゲームを開始
- `/kstop` : ゲームをストップ（結果をチャットに表示）
- `/kclear` : ランキングの削除（確認あり）
- `/kreload` : コンフィグのリロード

### データ保存
- プラグインフォルダにUUIDとMCIDでランキングを保存
- 再起動してもデータが消えない
- ゲーム実行中は再起動後も継続
- `/kclear`でランキング削除可能

### インストール
1. JARファイルを`plugins`フォルダに配置
2. サーバーを再起動またはプラグインをリロード
3. 必要に応じて`config.yml`の原木リストを編集

### 設定
`config.yml`で対象となる原木のブロックIDをリストで指定してください。

### 注意
- プレイヤーが自分で置いたブロックはカウント対象外にする機能は実装されていません（技術的制約のため）。
- PaperAPI 1.21以上対応。

注意: このプロジェクトは AI によって開発されました。簡単な確認は行い、目立ったバグは確認してありますが、問題が残っている可能性があります。Issue での対応は基本的に行いません—修正したい場合はリポジトリをフォークしてプルリクエストを送ってください。まあ、たのしんで！
あくまでネタプラグインだよ！

---

## Localization
This plugin includes default message files for easy localization: `messages.yml` (English, default), `messages_ja.yml` (Japanese), `messages_zh.yml` (Simplified Chinese), `messages_ru.yml` (Russian), and `messages_fr.yml` (French). Edit the appropriate file in the plugin folder and run `/kreload` to apply changes.

## Localization
This plugin includes default message files for easy localization: `messages.yml` (English, default), `messages_ja.yml` (Japanese), `messages_zh.yml` (Simplified Chinese), `messages_ru.yml` (Russian), and `messages_fr.yml` (French). Edit the appropriate file in the plugin folder and run `/kreload` to apply changes.