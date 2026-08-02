# BiliTVNative 开发进度

最后更新：2026-07-13

## 更新规则

- 每完成一个可验证任务，都要更新本文件。
- 状态只使用：`Done`、`In Progress`、`Pending`、`Blocked`。
- 新任务必须插入到合理阶段，不要只追加到末尾。
- 完成任务时补充验收结果，例如编译、安装、接口验证或用户确认。
- UI/UX 交互体验由用户手动测试，开发侧只负责编译、安装和必要的日志验证。

## 当前状态

当前阶段：真实二维码登录、首页、搜索、动态、历史、设置、点播播放器、字节跳动弹幕叠加层、空降助手、发布构建、TV 图标/横幅、主页主题、液态玻璃、设置重分组、搜索返回缓存、迷你进度条开关和关于展示面板均已接入；直播播放暂缓，后续单独评估。

推荐下一项：围绕当前 UI 收尾做真机视觉/性能抽样，重点检查液态玻璃开启/关闭两条路径、设置/About 右侧面板、搜索播放返回、播放器侧栏列表和高弹幕播放。不要恢复常驻播放器 HUD，性能排查优先使用 `gfxinfo`、`meminfo`、日志和可删除的临时 instrumentation。

## P0 项目决策与规则

| ID | 任务 | 状态 | 验收/备注 |
| --- | --- | --- | --- |
| P0-01 | 确定原生重写方案：Kotlin + Jetpack Compose + Android TV | Done | 已写入 `DEVELOPMENT_PLAN.md` |
| P0-02 | 确定包名 `com.kirin.bilitv` | Done | Gradle `applicationId` 已使用 |
| P0-03 | 确定首发 ABI 策略：主发 `armeabi-v7a`，保留 `arm64-v8a` 能力 | Done | `targetAbi` Gradle 参数已支持 |
| P0-04 | 确定不做应用内更新检查 | Done | 计划中已明确 |
| P0-05 | 确定不复刻完整插件系统，只保留内置空降助手开关 | Done | 计划中已明确 |
| P0-06 | 建立 `AGENTS.md` 开发约束 | Done | 包含 tokens、焦点、播放器、图片、低配置模式等规则 |
| P0-07 | 建立进度跟踪文件 | Done | 本文件 |

## P1 基础工程与首页/搜索

| ID | 任务 | 状态 | 验收/备注 |
| --- | --- | --- | --- |
| P1-01 | 创建原生 Android 项目骨架 | Done | 单 app 模块，Gradle 可编译 |
| P1-02 | 接入 Gradle 版本目录 | Done | `gradle/libs.versions.toml` 已使用 |
| P1-03 | 接入 Compose、Material3、TV Material、DataStore、OkHttp、Coil、Brotli | Done | 依赖集中管理 |
| P1-04 | 建立 `AppContainer` | Done | Network、Repository、Storage 等核心对象集中创建 |
| P1-05 | 建立设计令牌 | Done | `BiliTokens.kt` 包含颜色、字号、间距、圆角、焦点、骨架屏常量 |
| P1-06 | 建立 D-pad 焦点基础组件 | Done | `BiliFocusableSurface` 支持粉色焦点框、低配置关闭动画 |
| P1-07 | 接入推荐、热门、分区接口 | Done | 首页可加载卡片 |
| P1-08 | 接入 WBI 签名与 Brotli | Done | 推荐和搜索 API 可用 |
| P1-09 | 实现首页分区标签 | Done | 支持分区切换和确认刷新 |
| P1-10 | 实现首页卡片网格 | Done | 16:9 封面、UP 主头像、固定底部作者行 |
| P1-11 | 修复首页焦点滚动与卡片完整显示 | Done | 选中卡片保持完整可见 |
| P1-12 | 实现首页分页加载 | Done | 接近末尾自动加载更多 |
| P1-13 | 实现搜索键盘 | Done | 参考 Flutter 版布局 |
| P1-14 | 实现搜索建议与搜索历史 | Done | 支持历史记录和清除历史 |
| P1-15 | 实现搜索结果列表 | Done | 支持卡片、分页、焦点回退 |
| P1-16 | 实现搜索排序 | Done | 综合排序、最多播放、最新发布、最多弹幕 |
| P1-17 | 修复搜索结果头像加载 | Done | 头像 URL 解析已处理 |
| P1-18 | 修复启动初始焦点 | Done | 启动后焦点进入首页首个卡片 |
| P1-19 | 调整侧边栏导航顺序 | Done | 搜索、主页、动态、历史、设置 |
| P1-20 | 添加动态入口图标 | Done | 使用风车样式图标 |
| P1-21 | 修复首页标签左键回导航栏焦点错误 | Done | 第一个标签按左回当前激活导航项 |

## P1.5 设置、低配置模式与图片治理

| ID | 任务 | 状态 | 验收/备注 |
| --- | --- | --- | --- |
| P1.5-01 | 实现设置页基础布局 | Done | 性能、交互、首页分区 |
| P1.5-02 | 实现低配置模式开关 | Done | 开关使用 BiliPink，操作时不放大 |
| P1.5-03 | 实现切换时自动确认开关 | Done | 设置页交互区显示“切换时自动确认”；默认关闭；关闭后焦点移到导航栏或首页分区标签不会自动进入，首次访问或无内容时仍会自动进入加载 |
| P1.5-04 | 实现切换时自动刷新开关 | Done | 设置页交互区新增独立“切换时自动刷新”；依赖“切换时自动确认”，自动确认关闭时不可打开；关闭后自动切回已有内容会保留原内容，按确认键切换/确认时刷新；主页加载请求完成后清空触发器，避免重复消费旧请求，同时保留冷启动首次加载；`assembleDebug` 通过 |
| P1.5-05 | 实现首页分区显示开关 | Done | 至少保留一个分区 |
| P1.5-06 | 低配置模式关闭焦点动画和平滑滚动 | Done | 已接入 `AppPerformancePolicy` |
| P1.5-07 | 低配置模式降低封面和头像请求尺寸 | Done | 图片请求按策略传入尺寸 |
| P1.5-08 | 低配置模式减少骨架屏数量 | Done | 标准 12，低配置 8 |
| P1.5-09 | 低配置模式预留强制 H.264 | Done | 字段已存在，播放器阶段接入 |
| P1.5-10 | 封面使用 CDN 尺寸参数 | Done | `@widthw_heighth_1c.webp` |
| P1.5-11 | 头像使用 CDN 尺寸参数 | Done | 小头像不拉原图 |
| P1.5-12 | Coil 全局缓存限制 | Done | 全局内存上限 20%，磁盘 128MB；低配置模式通过请求级策略禁用图片内存缓存并清空已热缓存 |
| P1.5-13 | `RGB_565` 不再全局强制 | Done | 低配置策略控制 |
| P1.5-14 | 清理局部视觉硬编码 | Done | 搜索输入字号、设置列数、骨架屏参数移入 tokens |

## P2 账号与登录

| ID | 任务 | 状态 | 验收/备注 |
| --- | --- | --- | --- |
| P2-00 | 独立登录入口到侧边栏顶部 | Done | 未登录显示账号图标，设置页账号卡片已移除 |
| P2-00A | 建立用户登录态结构 | Done | `UserSession` 支持 `sessData`、`biliJct`、`mid`、`face`、`uname`、`isVip` |
| P2-00B | 侧边栏头像和大会员角标结构 | Done | 登录后显示头像，大会员显示右下角“大”；登录后头像只展示不参与焦点 |
| P2-01 | 实现 TV 二维码生成 | Done | 使用 TV 登录接口生成 `auth_code`，ZXing 本地渲染二维码，`assembleDebug` 通过 |
| P2-02 | 实现二维码状态轮询 | Done | 每 2 秒轮询，使用 `repeatOnLifecycle(RESUMED)` 暂停后台轮询 |
| P2-03 | 处理二维码过期和刷新 | Done | 过期/失败显示刷新二维码按钮 |
| P2-04 | 登录成功保存 Cookie | Done | 从 `cookie_info.cookies` 保存 `SESSDATA`、`bili_jct` |
| P2-05 | 登录成功拉取用户信息 | Done | 调用 `/x/web-interface/nav` 保存头像、昵称、UID、VIP 状态；已补完整 Cookie 和登录后资料补刷 |
| P2-06 | 登录成功刷新侧边栏头像 | Done | `SessionStore.session` 驱动 Compose 自动刷新；Shell 全局补刷用户资料，头像使用限定尺寸请求、HTTPS 规范化和 B 站图片请求头 |
| P2-07 | 账号页显示登录态 | Done | 退出登录入口暂时移除，后续整体完成后再决定位置；`assembleDebug` 通过 |

## P3 登录态页面

| ID | 任务 | 状态 | 验收/备注 |
| --- | --- | --- | --- |
| P3-00 | 补齐动态/历史接入前的视频卡片数据与显示规则 | Done | `VideoSummary` 已加入历史进度、观看时间、多 P、角标、直播标记等字段；`VideoCard` 支持普通、动态、历史模式；发布时间格式为分钟/小时/昨天/2-6 天前/同年 MM-DD/跨年 YY-MM；`assembleDebug` 通过，并使用外部 build 目录 APK 安装到 `127.0.0.1:16384` |
| P3-01 | 实现动态页接口 | Done | 使用 `/x/polymer/web-dynamic/v1/feed/all`，解析视频动态、offset 和 has_more；依赖登录 Cookie |
| P3-02 | 实现动态页卡片列表 | Done | 复用视频卡片与分页焦点逻辑，使用 `VideoCardMode.Dynamic`；接近末尾自动加载更多 |
| P3-03 | 实现历史页接口 | Done | 使用 `/x/web-interface/history/cursor`，解析 view_at/max 游标、观看进度、多 P、直播标记和角标 |
| P3-04 | 实现历史页卡片列表 | Done | 使用 `VideoCardMode.History` 显示已看时长/总时长、进度条、最后观看时间和多 P 提示 |
| P3-05 | 未登录页面提示与跳转 | Done | 动态/历史未登录时显示居中登录提示；暂不自动跳转账号入口，避免焦点链路不确定 |
| P3-06 | 视频卡片播放量图标 | Done | 封面左下角播放数前新增播放 icon；`assembleDebug` 通过并安装到 `127.0.0.1:16384` |

## P4 播放器

| ID | 任务 | 状态 | 验收/备注 |
| --- | --- | --- | --- |
| P4-01 | 接入 Media3 ExoPlayer | Done | `PlayerScreen` 使用 Media3 `ExoPlayer` + `PlayerView`，保持默认 SurfaceView；`assembleDebug` 通过；`127.0.0.1:16384` 烟测进入播放器 |
| P4-02 | 实现播放地址获取 | Done | `PlaybackRepository` 接入 `/x/player/wbi/playurl`，支持 BVID/CID/quality/fnval；CID 缺失时通过 `/x/web-interface/view` 解析；烟测已创建音视频解码器 |
| P4-03 | 播放请求头/Cookie 封装 | Done | `BiliMediaDataSourceFactory` 使用 Media3 OkHttp DataSource 统一注入 User-Agent/Referer/Origin/Cookie；`assembleDebug` 通过，烟测未见 403/source error |
| P4-04 | 编码探测与 H.264 回退 | Done | `CodecCapabilityProbe` 探测 H.264/H.265/AV1；低配置模式强制 H.264 并调整 `fnval`；`127.0.0.1:16384` 烟测选择 HEVC 解码器 |
| P4-05 | 点击卡片直接播放 | Done | 首页、搜索、动态、历史卡片确认键直接进入播放器，未引入详情页前置 |
| P4-06 | 播放控制栏 | Done | 遥控器确认键播放/暂停，左右键 10 秒 seek，控制层自动隐藏；画质切换面板待后续细化 |
| P4-07 | 播放进度保存 | Done | `PlaybackProgressStore` 使用 DataStore 保存进度，播放器 `onPause` 和返回时保存 |
| P4-08 | 返回后恢复原卡片焦点 | Done | 退出播放器后按当前页面 FocusRequester 重试恢复：首页/搜索/动态/历史共用入口；真机焦点待手测 |
| P4-09 | 早期临时开发诊断面板 | Done | 曾用于显示编码、分辨率、画质、缓冲、掉帧、host、time 且不显示 Cookie/token；该常驻叠加层已在 P4-16 移除，当前性能排查不保留播放器 HUD |
| P4-10 | Flutter 播放器 UI 对照细化 | Done | 播放器改为顶部标题/UP/发布时间/播放量/时钟、底部大进度条/icon 控制行/画质弹幕状态、自动隐藏小进度条、右侧设置面板、画质/弹幕/倍速子面板和 seek 预览；`assembleDebug` 通过，已安装 `127.0.0.1:16384` 并烟测无 `FATAL EXCEPTION`/`ParserException`/source error |
| P4-11 | 播放器内容面板和状态数据 | Done | 剧集、UP 主视频和相关推荐面板已接入真实 API；移除点赞控制；在线人数使用 `/x/player/online/total`；画质状态追加 H.264/H.265/AV1；`assembleDebug` 通过 |
| P4-12 | UP 主面板排序与关注操作 | Done | UP 主面板默认最新投稿，新增最新/热门排序和关注/已关注操作；取消关注显示确认弹窗；打开前先解析元数据 owner mid，并忽略过期面板请求，降低首次空列表风险；画质状态现在格式化为 `高清 1080P(H.265)`；`assembleDebug` 通过 |
| P4-13 | UP 主面板加载和滚动打磨 | Done | UP 主面板现在匹配 Flutter 布局，头部使用一个排序切换按钮加关注按钮；重复打开时保留已缓存视频并刷新，WBI 失败时回退到未签名空间接口；列表只在焦点视频离开可见范围时滚动；`assembleDebug` 通过 |
| P4-14 | UP 主面板行元数据与 Cookie 补充 | Done | UP 主视频行隐藏重复 owner 名称，使用首页卡片播放图标显示播放数，头部焦点可在排序和关注按钮之间移动，空间投稿请求同时携带 `SESSDATA` 和 `bili_jct` Cookie；`assembleDebug` 通过 |
| P4-15 | 播放器面板焦点和行样式打磨 | Done | 播放默认隐藏控制层，控制栏默认进入剧集；UP 主/相关推荐视频面板在数据加载后聚焦第一个视频；UP 主列表过滤当前视频；视频列表滚动会露出被遮挡的焦点行；行播放数移到缩略图左下角；`assembleDebug` 通过 |
| P4-16 | 播放器按键处理和调试清理 | Done | 移除画质/倍速待确认提示，控制层激活改为 Menu 键，OK/Enter 切换播放/暂停，新增居中暂停指示，并移除播放诊断叠加层；`assembleDebug` 通过 |
| P4-17 | 播放器按键修正和 UP 主重试诊断 | Done | OK/Enter 仅在控制层隐藏时切换播放/暂停，控制层可见时激活当前聚焦控件/面板；Menu 只显示控制层；隐藏状态 seek 不再显示控制层；UP 主投稿请求在签名失败后刷新 WBI key 并重试，同时记录脱敏失败码；`assembleDebug` 通过 |
| P4-18 | 快进预览雪碧图和选中设置颜色 | Done | 画质/倍速当前行使用 BiliPink 显示选中文本；设置页新增快进预览雪碧图开关；播放器加载 `/x/player/videoshot` 和 pvdata，并在 seek 预览时裁剪雪碧图帧，关闭或不可用时回退到纯时间预览；`assembleDebug` 通过 |
| P4-19 | 快进预览雪碧图确认模式 | Done | 快进预览雪碧图默认开启，雪碧图模式等待 OK/Enter 后再 seek，Back 取消预览；雪碧图渲染改用 Canvas source-rect 裁剪，不再用超大偏移图片渲染，避免黑色预览帧；`assembleDebug` 通过 |
| P4-20 | 快进预览雪碧图加载路径 | Done | 雪碧图预览图片现在通过播放仓库下载并携带 Bilibili 请求头，在 `PlayerScreen` 解码为 `ImageBitmap` 后传入叠加层直接进行 Canvas 裁剪；缺失图片时回退到纯时间预览，不再无限加载；`assembleDebug` 通过 |
| P4-21 | 快进预览时间戳对齐 | Done | 原生快进预览模式现在对齐 Flutter 的 `getClosestTimestamp` 行为：左右预览目标在渲染前和 OK 确认 seek 前都会吸附到 pvdata 时间戳，减少预览图、显示时间和最终跳转目标不一致；`assembleDebug` 通过 |
| P4-22 | 播放和应用退出确认 | Done | 设置页新增默认开启的播放退出确认开关；开启后播放器 Back 显示 `再按一次返回键退出播放`，并且只在 3 秒内第二次 Back 时退出；应用级 Back 始终使用 `再按一次返回键退出APP` 双确认；`assembleDebug` 通过 |
| P4-23 | 播放页防息屏 | Done | 播放页通过多层兼容方式防止 TV 息屏：`FLAG_KEEP_SCREEN_ON`、根 View 和 `PlayerView.keepScreenOn`，以及在 pause/dispose 时释放的 `SCREEN_BRIGHT_WAKE_LOCK` 回退方案；manifest 声明 `WAKE_LOCK`；`assembleDebug` 通过 |
| P4-24 | 设置页溢出修复 | Done | 设置行为列改为由焦点驱动的 `LazyColumn`，新增播放开关后底部交互设置会滚入视图，不再被裁剪；`assembleDebug` 通过 |
| P4-25 | 设置页双列焦点返回 | Done | 设置页记住上次聚焦的行为开关，并给每个 lazy row 提供稳定 `FocusRequester`；从首页分区网格向左移动时会滚回并聚焦之前的设置行，不再落到被复用的顶部项；`assembleDebug` 通过 |
| P4-26 | 播放心跳和历史续播打磨 | Done | 在暂停、完成、退出、后台和切换当前视频时上报 `/x/click-interface/web/heartbeat`；完成时上报 `played_time=-1`；历史播放强制使用卡片 cid/progress，不再被本地缓存覆盖；历史卡片用粉色 `P1/P2` 角标显示多 P，用粉色 `已看完` 角标显示已完成；`assembleDebug` 通过 |
| P4-27 | 播放器顶部元数据图标 | Done | 播放器顶部元数据现在使用图标显示 owner、发布时间和播放数，不再使用纯分隔符字符串；`assembleDebug` 通过 |
| P4-28 | 播放编码偏好设置 | Done | 设置页新增解码器选项，默认 Auto，优先级为 AV1 > H.265 > H.264；手动选项只显示 `MediaCodecList` 报告的硬件加速编码；不支持的已保存选项回退到 Auto；播放 URL 请求和 DASH 轨道选择尊重 Auto/H.264/H.265/AV1 偏好，同时保留对受支持轨道的自动回退；低配置模式不再强制覆盖编码偏好；`assembleDebug` 通过，`192.168.1.131:5555` 探测结果为支持 H.264/H.265，不支持 AV1 |
| P4-29 | 默认播放画质设置 | Done | 设置页新增默认画质选项，包含最高/1080P/720P/480P，默认最高。播放 URL 请求优先使用播放器已选画质，仅在初始播放时回退到全局默认；播放器画质面板仍显示 Bilibili 返回的全部画质；`assembleDebug` 通过 |
| P4-30 | 播放完成动作 | Done | 设置页新增默认关闭的完成后自动播下一集、自动播相关推荐和自动退出播放。播放完成时会上报进度，显示可取消 toast 并标出下一个目标，然后执行已启用动作中优先级最高的一项；自动退出复用手动退出播放器路径，确保焦点回到原视频；AirJump 直接跳到结尾附近时会抑制已跳过 toast，避免与完成提示冲突；`assembleDebug` 通过 |
| P4-31 | 缓存清理操作 | Done | 设置页新增清理缓存操作，当前归入 `系统设置`，并在行内显示当前磁盘/临时缓存大小。当前缓存策略为 Coil 内存缓存占可用内存 20%，图片磁盘缓存位于 `cacheDir/image_cache` 且上限 128MB；OkHttp 不使用磁盘缓存，推荐/播放器侧缓存仅在内存中。清理缓存会删除 Coil 图片缓存和 app cache 临时文件，同时保留登录、设置、搜索历史、WBI key 和播放进度；`assembleDebug` 通过 |
| P4-32 | 繁体中文语言支持 | Done | 新增 OpenCC4J 和简体/香港繁体/台湾繁体语言设置循环。静态字符串已有 `values-zh-rHK` 和 `values-zh-rTW`；动态标题、角标、弹幕文本、UP 主名称、分集标题、播放器侧边面板、账号名称和完成提示目标名称在显示时转换，请求和缓存 key 保留原文；`assembleDebug` 通过，并已在 `192.168.1.131:5555` 安装/启动 |

## P5 弹幕与空降助手

| ID | 任务 | 状态 | 验收/备注 |
| --- | --- | --- | --- |
| P5-00 | 弹幕获取/渲染首版 | Done | 新增 `x/v1/dm/list.so?type=1` 和 `comment.bilibili.com/{cid}.xml` 回退路径，在 UI 线程外完成 gzip/zlib/raw-deflate XML 解码，补充播放器弹幕加载日志，打磨暂停/播放控制层可见性，并为弹幕数值设置加入适合 TV 的左右键调节；`assembleDebug` 通过 |
| P5-01 | 弹幕 XML 获取与解析 | Done | `PlaybackRepository.getDanmaku()` 使用 `x/v1/dm/list.so?type=1` 和 `comment.bilibili.com/{cid}.xml` 回退路径，gzip/zlib/raw-deflate 解码和 XML 解析放在 `Dispatchers.IO` |
| P5-02 | 弹幕排布与碰撞处理 | Done | 当前决策为使用字节跳动 `danmaku-render-engine`，轨道分配和碰撞由 `DanmakuView` 引擎处理；不再要求首版自研 Kotlin 轨道预计算 |
| P5-03 | 弹幕原生叠加层渲染 | Done | `PlayerDanmakuLayer` 通过 Compose `AndroidView` 承载字节跳动 `DanmakuView`，不把每条弹幕渲染为 Compose 节点；应用层不使用 `delay()` 驱动弹幕重绘 |
| P5-04 | 弹幕显示开关与样式设置 | Done | 弹幕开关、透明度、字号、占屏比、速度、顶部/底部悬停全部接入独立 DataStore 持久化；透明度按 0.1 调节，字号最小 16 且按 2 调节；`assembleDebug` 通过 |
| P5-05 | 空降助手内置开关 | Done | 设置页新增默认开启的空降助手开关，使用 AppSettings/DataStore 持久化；不做插件标签页；`assembleDebug` 通过 |
| P5-06 | 空降助手播放跳转逻辑 | Done | 播放器按 BVID 请求 `bsbsb.top/api/skipSegments` 的 sponsor/intro/outro/interaction/selfpromo 片段，进度条和迷你进度条用绿色标出跳过范围；跳过前 3.5 秒显示 `Toast.LENGTH_LONG` 的即将跳过提示，跳过后保持 `Toast.LENGTH_SHORT` 的已跳过提示；回退到片段前会重置触发状态；`assembleDebug` 通过，已安装并启动到 `192.168.1.131:5555` |

## P6 收尾与发布

| ID | 任务 | 状态 | 验收/备注 |
| --- | --- | --- | --- |
| P6-00 | 卡片焦点移动流畅度优化 | Done | 视频网格覆盖默认 TV pivot bring-into-view 策略，左右切换焦点时不再触发纵向支点滚动；上下切换焦点改为目标行稳定定位，避免第三行后焦点继续移动但画面不滚；视频卡片关闭焦点阴影动画，保留边框/底色反馈以降低重绘成本；视频行声明稳定 `contentType` 便于 LazyColumn 复用；`assembleDebug` 通过 |
| P6-00A | 设置页焦点和导航反馈修正 | Done | 设置页上下键改为显式计算目标设置项，目标项已完整可见时不滚动，贴边/半遮挡时只做最小像素补偿，并覆盖默认 TV pivot bring-into-view，避免每移动一项整列大幅跳动；侧边栏图标获得焦点立即变粉，降低导航切换慢一拍的体感；`assembleDebug` 通过，已安装 `192.168.1.195:5555` 并验证设置项上下移动 |
| P6-00B | 画质轨道与主页焦点恢复修正 | Done | playurl 返回后只向 ExoPlayer 暴露接口实际 `quality` 对应的视频轨道，避免 480P 设置被高分辨率轨道覆盖；播放日志输出 requested/returned qn 和实际轨道分辨率；侧栏右键进入首页/动态/历史时读取 `requestFocus()` 真实返回值，失败则触发网格滚动恢复焦点，修复低配置模式切回主页后必须确认刷新才能进内容；封面预取降为标准 12/低配 6，降低导航切页时的解码压力；`assembleDebug` 通过，已安装 `192.168.1.195:5555` 并验证侧栏右键进首页内容 |
| P6-00C | 卡片聚焦特效恢复 | Done | 视频卡片标准模式恢复聚焦 `scale` 放大；低配置模式通过 `motionEnabled=false` 关闭卡片放大和标题跑马灯，保留无动画边框/底色焦点反馈；焦点阴影仍关闭以避免额外合成负担；`assembleRelease -PtargetAbi=armeabi-v7a` 通过，已安装 `192.168.1.195:5555` |
| P6-00D | 卡片轻量焦点视觉增强 | Done | 标准模式下新增封面轻微提亮、标题颜色过渡、UP 主/日期元信息颜色过渡；边框颜色/粗细继续复用 `BiliFocusableSurface` 的轻量动画；低配置模式关闭这些动画效果，仅保留静态焦点可见性；`assembleRelease -PtargetAbi=armeabi-v7a` 通过，已安装 `192.168.1.195:5555` |
| P6-00E | 首页标签栏与时钟避让 | Done | 首页分类栏改为轻量文字标签/选中小胶囊样式，移除整条深色胶囊背景；右侧预留 `176dp` 时钟避让区，保持时钟位置不动；`assembleRelease -PtargetAbi=armeabi-v7a` 通过，已安装 `192.168.1.195:5555` 并截图确认顶部不冲突 |
| P6-00F | 启动图标与繁体切换崩溃修复 | Done | 复用 Flutter 版 `BiliTV` 的启动器图标和 TV 横幅资源；manifest 声明 `android:icon` 和 application/activity `android:banner`，`aapt dump badging` 确认手机图标与 leanback 横幅均存在；发布构建 R8 保留 OpenCC4J 反射构造器，修复切换香港/台湾繁体后 `FastForwardSegment` 初始化崩溃；`assembleRelease -PtargetAbi=armeabi-v7a` 通过，已保留数据安装到 `192.168.1.195:5555` 并启动无 BiliTV 崩溃 |
| P6-01 | Baseline Profile 配置 | Done | 新增保守 baseline profile；发布构建已产出 `assets/dexopt/baseline.prof` 和 `assets/dexopt/baseline.profm` |
| P6-02 | R8/资源裁剪检查 | Done | `assembleRelease -PtargetAbi=armeabi-v7a` 通过；发布构建已执行 R8 代码压缩和资源裁剪，输出 `mapping.txt`、`usage.txt`、`resources.txt`、`seeds.txt`、`configuration.txt` |
| P6-03 | v7a 包体检查 | Done | 电视 `192.168.1.195:5555` ABI 为 `armeabi-v7a,armeabi`；当前 v7a 发布 APK `5,719,171` bytes，仅包含 `lib/armeabi-v7a/*` native 库 |
| P6-04 | 内存检查 | Done | 电视首页空闲 PSS 约 40.7 MB；导航/焦点操作后 PSS 约 55.0 MB，Native heap 约 20.1 MB，Java heap 约 12.7 MB |
| P6-05 | 帧耗时检查 | Done | 电视端焦点导航后 `dumpsys gfxinfo`：20721 frames，jank 123（0.59%），P50 9ms，P90 10ms，P95 11ms，P99 14ms，GPU memory 约 26 MB |
| P6-06 | 真机/模拟器安装验证 | Done | v7a 发布包已安装并启动到电视 `192.168.1.195:5555` 和模拟器 `192.168.1.131:5555`；本轮文档/字符串清理后重新通过 `assembleRelease -PtargetAbi=armeabi-v7a`，如需重新真机手测再安装最新 APK |
| P6-07 | 清理临时代码和调试页面 | Done | 删除未使用的 `NetworkProbeScreen` 和 `network_probe_*`、`home_shell_title` 字符串；`rg` 确认无 `NetworkProbeScreen`、`network_probe`、`home_shell_title`、`ui.debug` 残留引用 |
| P6-08 | 文档一致性与视频卡片本地化清理 | Done | `AGENTS.md`/`DEVELOPMENT_PLAN.md`/本文已记录字节跳动弹幕引擎、直播暂缓和 DataStore/Room 取舍；视频卡片相对时间、历史“看过”和播放/弹幕数量单位已移入 strings 资源；`assembleRelease -PtargetAbi=armeabi-v7a` 通过 |
| P6-09 | 内存和缓存收口复查 | Done | 推荐、动态、历史和搜索结果恢复按接口无限分页，不再用高低配置条数上限提前停止；封面预取记录随列表和图片尺寸重置；UP 主投稿缓存限制为最近 4 个 key、每个 50 条；`assembleRelease -PtargetAbi=armeabi-v7a` 通过，已安装 `192.168.1.195:5555`，图片列表实测 PSS 约 44 MB、CPU 0%、jank 0.73%、P99 13ms |
| P6-10 | 高低配置策略分层 | Done | 标准模式启用卡片放大、阴影、封面提亮、焦点封面模糊、标题/元信息颜色动画、平滑滚动、16 张封面预取和图片内存缓存；低配置模式关闭这些动画/模糊/阴影/平滑滚动/封面预取，封面 320x180 RGB_565，头像低尺寸 RGB_565，请求级禁用图片内存缓存并切换时清空 Coil 内存缓存；列表分页不按模式限条，避免影响连续浏览体验；`assembleRelease -PtargetAbi=armeabi-v7a` 通过，已安装 `192.168.1.195:5555` |
| P6-11 | 导航栏进入网格焦点一致性 | Done | `TvVideoGrid` 将导航入口焦点和播放返回恢复焦点拆分：侧边栏右键进入首页/动态/历史时请求第一个卡片；播放返回或显式内容恢复仍请求上次记录的卡片；`assembleRelease -PtargetAbi=armeabi-v7a` 通过，已安装 `192.168.1.195:5555` |
| P6-12 | 首页/搜索标签栏紧凑化 | Done | 首页分类和搜索排序同步改为轻量文字标签，选中项改为粉色文字且无背景色；遥控焦点落到标签时使用与导航栏/卡片一致的粉色边框反馈；视频卡片未改动；`assembleRelease -PtargetAbi=armeabi-v7a` 通过，已安装 `192.168.1.195:5555` 并启动无崩溃 |
| P6-13 | 低配置播放策略与首次启动内存检测 | Done | 低配置模式下播放器有效解码偏好强制为 H.264，设置页解码器显示同步为 H.264，并新增 playurl codec 日志输出 requested/effective/fnval；首次读取设置时若设备总内存低于 1GB 且用户未手动设置过低配置开关，则默认启用低配置模式；`assembleRelease -PtargetAbi=armeabi-v7a` 通过，已安装 `192.168.1.195:5555` 并启动无崩溃，电视 `MemTotal` 约 2.26GB 因此不会自动默认低配 |
| P6-14 | 网络权限兼容性补齐 | Done | Manifest 原本已声明 `android.permission.INTERNET`；为兼容国产系统和连通性判断补充 `android.permission.ACCESS_NETWORK_STATE`；`aapt2 dump permissions` 和电视 `dumpsys package` 均确认 `INTERNET`、`ACCESS_NETWORK_STATE`、`WAKE_LOCK` 存在且已授予；`assembleRelease -PtargetAbi=armeabi-v7a` 通过，已安装 `192.168.1.195:5555` 并启动无崩溃 |
| P6-15 | 发布包体编译压缩 | Done | 发布构建增加 `androidResources.localeFilters`，仅保留默认、简中、香港繁中和台湾繁中资源，并排除依赖嵌套 LICENSE 文本；APK 从 `6,186,139` bytes 降至 `5,719,171` bytes，主要减少 `resources.arsc`；`assembleRelease -PtargetAbi=armeabi-v7a` 通过，已安装 `192.168.1.195:5555` 并启动无崩溃 |
| P6-16 | 根目录文档英文残留中文化 | Done | 已将 `AGENTS.md`、`DEVELOPMENT_PLAN.md`、`DEVELOPMENT_PROGRESS.md` 中面向读者的英文说明翻译为中文；保留命令、API、类名、构建类型和状态值；`rg` 检查后剩余英文主要为技术标识 |
| P6-17 | GitHub 上传前无用文件清理 | Done | 删除本机生成目录 `.gradle/`、`.kotlin/`、`build/`、`app/build/` 和本机 SDK 配置 `local.properties`；补充 `.gitignore` 忽略 `.kotlin/`、NDK/外部构建目录和 APK/AAB 产物；删除 0 引用资源 `ic_player_like.xml`、`ic_banner.png`；`rg --files --hidden` 确认剩余文件为源码、资源、Gradle wrapper 和文档 |

## P7 主页主题与视觉效果

| ID | 任务 | 状态 | 验收/备注 |
| --- | --- | --- | --- |
| P7-01 | 将视觉性能模式扩展为流畅/均衡/精致三档 | Done | 已新增流畅/均衡/精致三档策略并通过 DataStore 持久化；低于 1GB 首次启动默认流畅，其余默认均衡；精致档必须用户手动开启；`assembleDebug` 通过 |
| P7-02 | 新增 4 种主页主题设置和持久化 | Done | 已新增默认粉、深黑、高级灰、蓝灰 4 种主题，设置页可切换并通过 DataStore 持久化；播放器暂不跟随主页主题 |
| P7-03 | 建立主页专用主题色系统 | Done | 已新增 `HomeColorScheme` 和 `LocalHomeColors`；首页、搜索、动态、历史、设置、侧边栏和标签栏读取主页主题 |
| P7-04 | 实现主页玻璃背景 | Done | 已接入主题渐变背景；精致模式额外启用环境高光，流畅模式不启用额外动态视觉 |
| P7-05 | 重做侧边导航玻璃样式 | Done | 侧边栏改为半透明玻璃竖栏、轻边框和主题色焦点反馈，头像、图标、选中态跟随主题色 |
| P7-06 | 重做主页/搜索标签栏玻璃样式 | Done | 首页分类和搜索排序标签使用主题色文字与焦点边框，继续保留无实心背景并避让顶部时钟 |
| P7-07 | 重做视频卡片玻璃材质 | Done | 卡片信息区使用半透明玻璃层；获焦保留细边框、轻提亮、文字颜色过渡和克制缩放；流畅模式关闭动画和阴影 |
| P7-08 | 增加精致模式主题色斜向流光效果 | Done | 精致模式下焦点卡片使用单个跟随焦点的小尺寸 overlay 绘制斜向流光；切到卡片后先等 2 秒再扫，后续约每 5 秒扫一次，颜色跟随当前主页主题 |
| P7-09 | 主页视觉性能回归测试 | Done | `assembleRelease -PtargetAbi=armeabi-v7a` 通过并已安装 `192.168.1.131:5555`；基础 D-pad smoke test 无 `FATAL EXCEPTION`；`gfxinfo` 52 帧 jank 0.00%，P50 11ms、P90 18ms、P95 31ms、P99 42ms；PSS 约 85.6MB |
| P7-10 | 静态规则复查修正 | Done | 播放器打开时不再组合主页层，主页背景动画、卡片阴影/流光和封面预取随主页 Composable 一起释放；均衡档关闭封面实时模糊；主页主题颜色收口到 `BiliTokens.kt`；`assembleDebug` 和 `assembleRelease -PtargetAbi=armeabi-v7a` 通过 |
| P7-11 | 卡片跨行滚动裁切回退 | Done | 撤回安全区触发、目标舒适区、额外视觉余量、行留白和 `zIndex` 试验，恢复 `TvVideoGrid` 稳定顶齐滚动与换行前等帧，避免上下焦点卡片被裁切；`assembleDebug` 和 `assembleRelease -PtargetAbi=armeabi-v7a` 通过，已安装 `192.168.1.131:5555` |
| P7-12 | 对齐 Flutter 版跨行滚动手感 | Done | 参考 Flutter 版 `ScrollController.animateTo` 的 `500ms + easeOutCubic`，Native 仅调整 `animateScrollBy` 的滚动时长和滚动专用曲线，不改可视边界、行留白、缩放或目标行定位；`assembleDebug` 和 `assembleRelease -PtargetAbi=armeabi-v7a` 通过，已安装 `192.168.1.131:5555` |
| P7-13 | MT9655 电视 UI 性能限制策略 | Done | 针对 MT9655 或 `MiTV-MFFU1` 小米电视启用受限 TV UI 策略：保留平滑滚动和基础焦点动效，但关闭焦点阴影、精致/电影视觉、焦点封面模糊和大规模封面预取，封面降为 480x270 RGB_565、预取降为 8；`assembleDebug` 和 `assembleRelease -PtargetAbi=armeabi-v7a` 通过；已安装 `192.168.1.195:5555`，Sony Android 9 约 2.26GB 内存，模拟方向键后 P50 53ms/P95 69ms，仍比 210 旧数据 P50 93ms/P95 150ms 轻；210 设备在线后再安装实测 |
| P7-14 | 全局焦点特效层试验 | Done | `TvVideoGrid` 将方向键网格内移动的父级焦点状态写回改为离开网格/点击播放时提交，降低父级重组；卡片本体关闭真实焦点阴影，精致模式流光改为单个跟随焦点卡片的小尺寸 overlay 绘制，避免每张卡片各自跑流光；`assembleDebug` 和 `assembleRelease -PtargetAbi=armeabi-v7a` 通过，已安装 `192.168.1.195:5555`；195 实测 P50 53ms/P95 约 77-81ms，较 69ms 基线未明显改善，下一步应准备 RecyclerView/DpadRecyclerView 网格对照方案 |
| P7-15 | 播放器进出场黑屏过渡与静态约束复查 | Done | 黑屏遮罩转场改为 `BiliMotion` token，时长收短为进入 90ms、保持 10ms、退出 90ms；静态检查确认未恢复 `AnimatedVisibility` 缩放/淡入淡出，未对视频 `SurfaceView` 本体做变换，主页与播放器按 `visiblePlaybackRequest` 互斥组合，播放器显示后主页背景动画、卡片流光/阴影和封面预取会随主页 Composable 释放；本轮按要求未编译安装 |
| P7-16 | 精致模式卡片悬浮感增强 | Done | 精致模式视频卡片获焦时启用 `1.072` 轻微放大、`8dp` 上浮、斜向流光和液态玻璃感边缘；玻璃边缘使用整圈外层折射边和内层细高光模拟，不启用真实背景模糊；已移除卡片背景发光、左上额外高光线、右下暗边与非焦点卡片黑色遮罩，避免焦点移动时闪烁或边角不对称；`TvVideoGrid` 同步使用精致档缩放参与滚动可视区计算，均衡/流畅档保持原策略；`assembleRelease -PtargetAbi=armeabi-v7a` 通过，已安装并启动 `192.168.1.131:5555` |
| P7-17 | AndroidLiquidGlass 实验开关 | Done | 变更前已备份到 `C:\Users\Kirin\OneDrive\Code\BiliTVNative_backup_before_liquidglass_20260509_212240`；升级 Gradle Wrapper `9.4.1`、AGP `9.2.1`、Kotlin `2.3.21`、Compose BOM `2026.05.00`，并接入 `io.github.kyant0:backdrop:2.0.0-alpha03`；设置页新增“液态玻璃控件”独立开关，仅在 Android 13+ 且精致档可开启，Android 13 以下持久化读取和写入都会强制关闭；开启后主页背景作为 `LayerBackdrop` 采样层，卡片、导航按钮、设置/搜索等共享焦点控件以及播放器覆盖层控件统一切换为单层 AndroidLiquidGlass 表面；播放器视频 `SurfaceView` 本体不参与变换或采样，避免影响播放兼容性；`assembleDebug`、`assembleRelease -PtargetAbi=armeabi-v7a` 和 `git diff --check` 通过，release APK SHA256 `5D321AEC299CCA34E0A928E9B547E4AE5A2675F37A7AD2147C0F999B660BE6B2`，已安装并启动 `192.168.1.210:5555`，该设备版本 `1.0.0` / `versionCode=100` |
| P7-18 | 液态玻璃视觉微调 | Done | 单层 LiquidGlass 启用库内置 `Highlight.Ambient`、`Shadow.Default` 和 `InnerShadow.Default`，将 blur/refraction 提高到 `6dp / 14dp / 24dp`；主页视频卡片在 LiquidGlass 开启时关闭前景描边，避免获焦后底部出现一条硬横线；共享焦点控件和播放器小控件继续保留轻边缘以维持可读性；`assembleDebug`、`assembleRelease -PtargetAbi=armeabi-v7a` 和 `git diff --check` 通过，release APK SHA256 `19E7CCCE984769AD702A089D69C5A0E3DA7BB90E20695D41F77609C638F3B75D`，已安装并启动 `192.168.1.210:5555`，未见 `AndroidRuntime` / `FATAL EXCEPTION` |
| P7-19 | 液态玻璃方向性与仿玻璃边框修复 | Done | 去掉 AndroidLiquidGlass 默认 `Highlight/Shadow/InnerShadow` 方向光，保留无方向 `vibrancy + blur + lens`，避免左上角和右下角质感不一致；液态玻璃关闭时，卡片仿玻璃边框从内容子层改为卡片根层前景绘制，并向内安全缩进 `5dp`，同时 `TvVideoGrid` 提升焦点所在行 `zIndex`，避免卡片放大后被相邻行覆盖到只剩底部一条线；`assembleDebug`、`assembleRelease -PtargetAbi=armeabi-v7a` 和 `git diff --check` 通过，release APK SHA256 `C4C37B0077FD6AD521CA4D57D2CA9473A4735FF81B417622F8232C9CC450C5AB`，已安装并启动 `192.168.1.210:5555`，未见 `AndroidRuntime` / `FATAL EXCEPTION` |
| P7-20 | 玻璃焦点框去重与轻光源恢复 | Done | `BiliFocusableSurface` 新增单一前景绘制入口，主页卡片关闭 LiquidGlass 时的仿玻璃边框改为同一个焦点 Surface 内绘制，避免根层和内容层多套边框叠加；高级卡片在非 LiquidGlass 状态下同步关闭 Surface 自带描边，只保留一套仿玻璃边；LiquidGlass 恢复 `Highlight.Plain` 和 `InnerShadow.Default`，保留外部 `Shadow` 关闭以降低方向光过重；`assembleDebug`、`assembleRelease -PtargetAbi=armeabi-v7a` 和 `git diff --check` 通过，release APK SHA256 `EB324BB443278345ACAC541C5136109D16163384975D319558A8C9055B2EDDA7`，已安装并启动 `192.168.1.210:5555`，未见 `AndroidRuntime` / `FATAL EXCEPTION` |
| P7-21 | 卡片底线与液态玻璃强度调整 | Done | 根据电视实拍，关闭 LiquidGlass 时仍有多边框/底线不一致，已撤掉主页卡片仿玻璃多层边框，避免上下左右不一致；LiquidGlass 底线来自 `InnerShadow.Default`，已关闭内阴影，改为 `Highlight.Ambient + Shadow.Default`，并将 blur/refraction 提高到 `8dp / 20dp / 34dp` 增强液态感；`assembleDebug`、`assembleRelease -PtargetAbi=armeabi-v7a` 和 `git diff --check` 通过，release APK SHA256 `C9E08F38090AB7B9F807C198FC26B92B62595324B589DA50961CA6E77DAA4281`，已安装并启动 `192.168.1.210:5555`，未见 `AndroidRuntime` / `FATAL EXCEPTION` |
| P7-22 | 卡片焦点色与侧栏玻璃面板修复 | Done | 主页视频卡片获焦背景从 `cardFocusedSurface` 改回 `cardSurface`，封面 polish 的中段高光从主题 accent 改为中性 `textPrimary`，避免液态玻璃开关开/关时卡片整体都泛粉；关闭 LiquidGlass 时恢复备份前的白色仿玻璃焦点边框，并只在 LiquidGlass 不可用时绘制，避免和真实玻璃叠层；侧边导航栏 panel 本体接入 `biliLiquidGlassSurface`，开启液态玻璃后不再只有按钮是玻璃；`assembleDebug`、`assembleRelease -PtargetAbi=armeabi-v7a` 和 `git diff --check` 通过，release APK SHA256 `5115C0BF8CD7620ECD9C3B47A7C5A0CE68D9C7146E28DEDB3C1C231A6C06282B`，`192.168.1.210:5555` 上版本 `1.0.0` / `versionCode=100`，未见 `AndroidRuntime` / `FATAL EXCEPTION` |
| P7-23 | 液态玻璃边缘裁剪与侧栏圆角调整 | Done | `BiliFocusableSurface` 改为只裁剪内容层，不再裁剪外层 LiquidGlass/前景边框，避免获焦缩放后玻璃折射边缘被自身 clip 吃掉；主页卡片在 LiquidGlass 开启时增加 `4dp` 内缩留白，给 `1.072` 焦点缩放预留边缘显示空间；关闭 LiquidGlass 时，仿玻璃卡片边框改回焦点 Surface 前景绘制，避免内容层覆盖后只剩底线；侧边导航栏四角统一改为 `30dp` 圆角；`assembleDebug` 和 `git diff --check` 通过，debug APK SHA256 `DACED13656D4A064325A97759B4A87163A9153CC091EA6D5AF411E9DEC595975`，已安装并启动 `192.168.1.210:5555`，未见 `AndroidRuntime` / `FATAL EXCEPTION` |
| P7-24 | 主页网格与侧栏尺寸微调 | Done | 针对关闭 LiquidGlass 时第一行卡片获焦顶部被裁剪的问题，将网格顶部安全区 `ScrollInset` 从 `20dp` 提高到 `32dp`；侧边导航栏从 `88dp` 收窄到 `76dp`，导航按钮高度 `56dp -> 48dp`、图标 `28dp -> 24dp`、头像容器和 VIP 标记同步缩小，侧栏内部横向 padding 和按钮间距从 `Lg` 收紧到 `Md`；卡片网格间距 `12dp -> 10dp`，配合侧栏释放的宽度让 4 列卡片略微放大并缩短间隔；`assembleDebug` 和 `git diff --check` 通过，debug APK SHA256 `C2C7D170FCA2FAC0A9BA602A98D3415A7F72AE24FD537134AD7426D7A03092D1`，已安装并启动 `192.168.1.131:5555`，未见 `FATAL EXCEPTION` |
| P7-25 | 首页分区居中胶囊导航 | Done | 首页分区栏从左对齐 `LazyRow` 改为内容自适应的居中大胶囊，支持设置页动态启用的 `1-12` 个分区；胶囊内分区按钮保持原有焦点、确认和向左回侧栏逻辑，选中/焦点文字仍跟随主题色；LiquidGlass 开启时胶囊外层走 `biliLiquidGlassSurface` 真实玻璃材质，关闭时自动回落到主题半透明玻璃底和细边框；`assembleDebug` 和 `git diff --check` 通过，debug APK SHA256 `3FBBF2C80AA0EE193586D839F4F41B43DE179EDC25C7DA08BC556C5B3DBEF45A`，已安装并启动 `192.168.1.131:5555`，未见 `FATAL EXCEPTION` |
| P7-26 | 首页分区胶囊定位微调 | Done | `RecommendHeader` 改为 `BoxWithConstraints`，胶囊按启用分区内容宽度自适应并以可用宽度为上限居中，避免 1-12 个分区切换时位置被内容长度顶偏；胶囊上移量先试 `10dp` 后按实机截图回收为 `2dp`；`assembleDebug` 和 `git diff --check` 通过，debug APK SHA256 `F1B12FCC1D82F4B5A9DDC527BFE3EB7DA0E6A8EB7934120515D523A3C4AED846`，已安装并启动 `192.168.1.131:5555`，未见 `FATAL EXCEPTION` |
| P7-27 | 首页分区文字视觉居中校正 | Done | `HomeSectionTab` 原本通过 `Box(contentAlignment = Alignment.Center)` 做布局居中，但中文字体在 `includeFontPadding=false` 下视觉重心略偏上；新增 `HomeSectionTabTextVerticalOffset = 1dp` 只下移文字本身，不改变胶囊、按钮和焦点边框尺寸；`assembleDebug` 和 `git diff --check` 通过，debug APK SHA256 `119FBB584273A42CB17824058186CBB3CF81B1F187F29C33798F0660111128CE`，已安装并启动 `192.168.1.131:5555`，未见 `FATAL EXCEPTION` |
| P7-28 | 首页分区胶囊字号放大 | Done | 面向 12 个分区全开状态，将分区文字从 `15sp/18sp` 放大到 `17sp/20sp`，tab 高度从 `32dp` 增至 `36dp`，胶囊高度从 `48dp` 增至 `52dp`，垂直 padding 从 `5dp` 增至 `6dp`，让文字和选中态边框比例更接近电视端远距离观看；`assembleDebug` 和 `git diff --check` 通过，debug APK SHA256 `83BAAB631AA72AD1D11D6AA51E13D28F5A464D80ADF0067CCF8982FBC1BA49F6`，已安装并启动 `192.168.1.131:5555`，未见 `FATAL EXCEPTION` |
| P7-29 | 首页分区胶囊字号二次放大 | Done | 根据 12 个分区全开截图继续利用横向余量，将分区文字从 `17sp/20sp` 放大到 `19sp/23sp`，tab 高度从 `36dp` 增至 `40dp`，胶囊高度从 `52dp` 增至 `58dp`，垂直 padding 从 `6dp` 增至 `7dp`，横向间距保持不变以避免全开时过宽；`assembleDebug` 和 `git diff --check` 通过，debug APK SHA256 `252796EA165F92264D6B692DAB83477B87311A8ED027183C2E3EB3A016280555`，已安装并启动 `192.168.1.131:5555`，未见 `FATAL EXCEPTION` |
| P7-30 | 撤回分区文字下移校正 | Done | 按实机观感验证需求，删除 `HomeSectionTabTextVerticalOffset` 和 `Text` 上的 `Modifier.offset`，分区文字重新完全依赖 `Box(contentAlignment = Alignment.Center)` 与字体原始 metrics 居中；`assembleDebug` 和 `git diff --check` 通过，debug APK SHA256 `1A53B6C3EF6797EF1B206CA4099E099932731B64108C3994299DB95AE8F63A82`，已安装并启动 `192.168.1.131:5555`，未见 `FATAL EXCEPTION` |
| P7-31 | 分区少时胶囊自适应拉宽与首页卡片上移 | Done | 首页分区胶囊新增按启用数量计算的最小宽度：`1-6` 个分区逐级拉宽并使用 `Arrangement.SpaceEvenly` 均匀分布，`7-12` 个维持内容自适应与固定间距；`TvVideoGrid` 新增 `topPadding` 参数，首页单独使用 `HomeVideoGridTopPadding = 24dp`，让第一行卡片较原 `32dp` 顶部留白上移 `8dp`，不影响搜索/动态/历史网格默认安全留白；`assembleDebug` 和 `git diff --check` 通过，debug APK SHA256 `58BF1448C2BADA3DB313BC9E5F009537B21DF66BA8AD8D2FC60315B7176BF0A0`，已安装并启动 `192.168.1.131:5555`，未见 `FATAL EXCEPTION` |
| P7-32 | 首页卡片顶部放大裁剪修复 | Done | `TvVideoGrid` 新增 `topBleed` 视口扩展参数，首页使用 `HomeVideoGridTopBleed = 16dp` 将 LazyColumn 实际测量/绘制区域向上扩出，同时把首页内容 top padding 增加同等 bleed，保持卡片平时位置不变但允许焦点放大和上浮时盖过上方空白区域；默认 `topBleed = 0dp`，搜索/动态/历史不受影响；`assembleDebug` 和 `git diff --check` 通过，debug APK SHA256 `74B6569B8394DD94902D0D7D26FC1F22DE967A20D3473C26461961BB63693C71`，已安装并启动 `192.168.1.131:5555`，未见 `FATAL EXCEPTION` |
| P7-33 | 液态玻璃卡片选中外扩边缘 | Done | 液态玻璃卡片保持原有 `4dp` 安全内缩，但选中态新增 `drawLiquidGlassCardFocusedOutline()`，在焦点前景层向外扩出同等 `4dp` 并绘制 `2dp` 半透明玻璃高光边缘，使选中外沿尺寸接近非液态玻璃卡片；普通/关闭液态玻璃时仍使用原有仿玻璃边框逻辑；`assembleRelease -PtargetAbi=armeabi-v7a` 和 `git diff --check` 通过，release APK SHA256 `793987AAD8DC3E27BE1D5C62D41F0893611BBC7BCD4FCE4F4470B0299754E53C`，已安装并启动 `192.168.1.210:5555`，未见 `AndroidRuntime` / `FATAL EXCEPTION` |
| P7-34 | 液态玻璃卡片选中边框强化 | Done | 针对实拍中液态玻璃边框不明显的问题，将液态玻璃选中外扩轮廓从 `2dp` 增强到 `3dp`，新增内侧 `1dp` 高光线，并把卡片专用白色玻璃高光透明度提升到 `0.76/0.36/0.52`，比 P7-33 更接近非液态玻璃卡片的可见边框；`assembleRelease -PtargetAbi=armeabi-v7a` 和 `git diff --check` 通过，release APK SHA256 `129602DD8CB8D3743D03CF3B8D09202340C019ACA1DA74EEB29B097F00FBC01D`，已安装并启动 `192.168.1.210:5555`，未见 `AndroidRuntime` / `FATAL EXCEPTION` |
| P7-35 | 液态玻璃清透度调整 | Done | 按实机观感将液态玻璃从磨砂感转向更清透：全局 LiquidGlass blur 从 `8dp` 降到 `3dp`，卡片主体在液态玻璃开启时单独使用 `0.18` 透明度，底部信息区使用 `0.46` 透明度以保留文字可读性，折射参数保持 `20dp/34dp` 以保留液态边缘；`assembleRelease -PtargetAbi=armeabi-v7a` 通过，release APK SHA256 `087A0EC6AA0119459D1398ED206BD14721DC326E9AD9BD67094E0E681D397159`，已安装并启动 `192.168.1.210:5555`，未见 `AndroidRuntime` / `FATAL EXCEPTION` |
| P7-36 | 液态玻璃焦点边框去磨砂化 | Done | 根据实拍中焦点边框仍显厚重磨砂的问题，移除液态玻璃卡片选中态的中间 `3dp` 厚内描边，仅保留外扩折射轮廓和 `1dp` 内侧淡高光；外扩轮廓从 `3dp` 降到 `2dp`，高光透明度从 `0.76/0.36/0.52` 降到 `0.58/0.12/0.22`，让边缘更像透明玻璃折射而不是乳白磨砂边；`assembleRelease -PtargetAbi=armeabi-v7a` 和 `git diff --check` 通过，release APK SHA256 `CBF59A1431C2403723081F3627FA9D87FE455C5713BB43E08BA73A8EF30FF1C4`，已安装并启动 `192.168.1.210:5555`，未见 `AndroidRuntime` / `FATAL EXCEPTION` |
| P7-37 | 液态玻璃焦点边框连续环带 | Done | 针对 P7-36 实机出现外圈与卡片本体边缘之间透明空隙、视觉变成双层的问题，将液态玻璃卡片选中态从单条外扩描边改为覆盖 `4dp` 外扩安全区的连续渐变玻璃环带，环带内边贴合卡片本体、外边保留外扩尺寸；同时清理已不生效的内侧描边 token，并将环带透明度调整为 `0.38/0.08`，避免填满后重新变成厚重磨砂白边；`assembleRelease -PtargetAbi=armeabi-v7a` 和 `git diff --check` 通过，release APK SHA256 `E2F9434699A5C83139F7839824C19D72A8CE44DE29C75E997EDE36E546E0D2E3`，已安装并启动 `192.168.1.210:5555`，未见 `AndroidRuntime` / `FATAL EXCEPTION` |

## P8 UI/设置/文档收尾

| ID | 任务 | 状态 | 验收/备注 |
| --- | --- | --- | --- |
| P8-01 | 搜索播放返回状态修复 | Done | `AppShell` 持有 `SearchUiState`，从搜索结果进入播放器再返回时保留结果和封面加载状态；从搜索切换到其他主页面时清空搜索态，避免回到搜索仍显示旧结果 |
| P8-02 | 播放器侧栏列表视觉修正 | Done | 选集当前播放项移除粉色竖线；UP 主更多视频和相关推荐卡片放大并收紧间距，封面上的播放数、弹幕数和时长改为接近主页卡片的覆盖样式，不再挤在面板内 |
| P8-03 | 播放器设置列表滚动 | Done | 弹幕设置、分辨率/画质等播放器侧栏列表支持滚动，保留标题可读性，不再通过压缩标题字号解决底部选项被裁剪 |
| P8-04 | 迷你进度条设置开关 | Done | 设置页新增默认开启的“显示迷你进度条”，归入 `播放设置`；播放器按该设置决定是否显示自动隐藏的小进度条 |
| P8-05 | 播放器周期任务收口 | Done | 播放器保留一个 `BiliMotion.PlayerProgressUpdateMs = 500ms` 的生命周期绑定状态循环，用于播放进度、缓冲、时钟分钟变化、在线人数节流和迷你进度条状态；未保留常驻性能 HUD |
| P8-06 | 设置分类重排 | Done | 设置页分为 `播放设置`、`UI/UX`、`系统设置`：播放画质/编码、快进预览、空降助手、退出确认、播放完成动作、时钟和迷你进度条归入播放；视觉性能、液态玻璃、主页主题、切换自动确认和切换自动刷新归入 UI/UX；缓存清理、语言和关于归入系统 |
| P8-07 | 关于展示面板 | Done | 系统设置末尾新增“关于”；焦点移到该行时右侧自动展示项目简介、项目地址二维码、MIT License 和第三方开源库名称/URL/说明，右侧内容纯展示不可聚焦；面板支持液态玻璃开启/关闭两种状态 |
| P8-08 | README 重写 | Done | `README.md` 已重写为当前项目介绍、功能说明、构建方式和 UI/液态玻璃说明；截图位置保留给用户后续补充 |
| P8-09 | 根目录 3 个 MD 一致性修正 | Done | 本轮只更新 `AGENTS.md`、`DEVELOPMENT_PLAN.md` 和 `DEVELOPMENT_PROGRESS.md`，删除或改写过期的 `feature/*`、Compose Navigation、Coil 3、Room/Koin 默认推荐和常驻播放器诊断 HUD 说法；`README.md` 未改动 |
| P8-10 | UP 主面板头部按钮焦点修正 | Done | “最新发布/最热门”和“关注/已关注”不再使用整块粉色实心底作为选中态，改为粉色文字/细边表达选中；焦点态使用白色高亮边框和播放器面板焦点底，避免粉底下焦点不可见；`assembleRelease -PtargetAbi=armeabi-v7a` 通过 |
| P8-11 | UP 主更多视频间歇加载失败诊断 | Done | 在 `PlayerScreen` 和 `VideoRepository` 增加脱敏日志，记录打开 UP 主面板、mid 解析、缓存命中、space 接口签名/刷新/回退、空 vlist、网络失败和过期 token 丢弃；不打印 Cookie/token；`assembleRelease -PtargetAbi=armeabi-v7a` 通过 |
| P8-13 | UP 主更多视频 412 加载体验优化 | Done | 将空间投稿加载拆成 `Interactive` 和 `Recovery` 两种重试模式：前台面板只做一次 600ms 短重试，失败后立即结束 loading 并保留缓存/空态；后台 1.2s 后再做恢复重试，成功且仍停留在同一 UP 面板时再刷新列表，避免 412 退避把侧栏卡住数秒；`assembleRelease -PtargetAbi=armeabi-v7a` 通过，已安装并启动 `192.168.1.131:5555` |
| P8-14 | UP 投稿接口对齐网页抓包 | Done | 根据网页端 `x/space/wbi/arc/search` 抓包同步最新发布/最多播放请求：参数改为 `ps=25,index=1,order_avoided=true,platform=web,web_location=333.1387`，请求头使用 `space.bilibili.com/{mid}` Referer/Origin、Chrome 147 UA、sec-ch-ua/priority/fetch 头，并在 Cookie 中补充登录 mid 对应的 `DedeUserID`；`assembleRelease -PtargetAbi=armeabi-v7a` 通过，已安装并启动 `192.168.1.131:5555` |

## P9 重构拆分收尾

| ID | 任务 | 状态 | 验收/备注 |
| --- | --- | --- | --- |
| P9-01 | 播放仓库职责拆分 | Done | 将弹幕 XML 获取/解压/解析拆到 `DanmakuRepository`，雪碧图元数据和图片字节获取拆到 `VideoshotRepository`，空降助手 SponsorBlock 请求拆到 `AirJumpRepository`；`PlaybackRepository` 保留播放地址、元数据、在线人数和播放进度职责；`assembleDebug` 通过 |
| P9-02 | 播放器页面逻辑拆分 | Done | 将播放器侧栏视频加载、UP 主缓存和恢复重试拆到 `PlayerSidePanelLoader`；将播放完成后的下一集/相关视频选择规则拆到 `PlayerCompletionPlanner`；将快进预览雪碧图时间对齐、预加载 URL、解码和缓存裁剪拆到 `PlayerVideoshotPreview`；保留 ExoPlayer 生命周期、D-pad 主循环和弹幕/Surface 叠层不动；`assembleDebug` 通过 |
| P9-03 | 视频数据仓库按业务域拆分 | Done | `VideoRepository` 保留对外门面和关注/取消关注，首页/热门/分区/相关推荐拆到 `HomeVideoRepository`，搜索和搜索建议拆到 `SearchVideoRepository`，UP 空间投稿/WBI/buvid/412 恢复策略拆到 `SpaceVideoRepository`，动态和历史拆到 `UserFeedRepository`，JSON 到 `VideoSummary` 的映射集中到 `VideoSummaryMappers`；`assembleDebug` 通过 |
| P9-04 | 设置页和应用壳 UI 拆分 | Done | 设置页右侧主页栏目/About 面板拆到 `SettingsRightPanels`，设置行和显示文案拆到 `SettingsRows`，设置焦点入口/方向键边界/滚动辅助拆到 `SettingsFocus`；应用左侧导航栏、账号头像和导航按钮拆到 `AppSidebar`；`SettingsScreen` 降到约 646 行，`AppShell` 保留应用级状态、页面切换、播放入口和播放器叠层；`assembleDebug` 通过 |
| P9-05 | MVVM/平板规划与 CodeGraph 初始化 | Done | 新增 `.trellis` 最小规划结构，记录 `MVVM and TV/tablet foundation` 父级任务、PRD、设计和实施清单，明确 ViewModel 与 TV 焦点/播放器 UI-runtime 边界，并补充禁止用 `part`/`partial`/编号碎片文件伪装 MVVM 拆分的架构约束；初始化 `.codegraph/`，索引 112 个文件、2181 个节点、5715 条边，`codegraph status .` 显示索引已最新；本轮未改 Android 业务代码 |
| P9-06 | 首页推荐 ViewModel 首个 MVVM 切片 | Done | 新增 lifecycle ViewModel 依赖和 `RecommendViewModel`，首页分区、加载状态、刷新和分页逻辑从 `RecommendScreen` 迁出；`RecommendScreen` 只保留渲染、D-pad 焦点和网格滚动相关状态，`AppShell` 通过显式 factory 创建 ViewModel；静态扫描未发现 `Part1`/`Partial`/编号碎片式拆分命名；`assembleDebug` 通过 |
| P9-07 | 搜索页 ViewModel 第二个 MVVM 切片 | Done | 新增 `SearchViewModel`，搜索文本、建议、历史写入、排序、结果加载、重试和分页逻辑从 `SearchScreen` 迁出；`SearchScreen` 只保留 TV 键盘焦点、结果焦点恢复和渲染状态，`AppShell` 通过显式 factory 注入 `VideoRepository` 与 `SearchHistoryStore`；静态扫描未发现 `Part1`/`Partial`/编号碎片式拆分命名；`assembleDebug` 通过 |
| P9-08 | 动态/历史 Feed ViewModel 切片 | Done | 新增 `DynamicFeedViewModel`、`HistoryFeedViewModel` 和共享 `UserFeedState`，动态/历史首屏加载、刷新消费、分页游标、重试和加载更多错误状态从 `UserVideoFeedScreen` 迁出；屏幕侧只保留未登录提示、TV 焦点恢复、D-pad 边界和渲染，未引入基类式 Feed ViewModel；静态扫描未发现 `Part1`/`Partial`/编号碎片式拆分命名；`assembleDebug` 通过 |
| P9-09 | AppShell 焦点状态拆分 | Done | 新增 `AppShellFocusState` 承载账号/侧栏/内容页 `FocusRequester`、播放返回焦点和内容返回焦点 request key；`AppShell` 保留路由选择、播放入口、手动刷新计数、设置写入和缓存操作，没有引入根 `AppViewModel`；静态扫描未发现 `Part1`/`Partial`/编号碎片式拆分命名；`assembleDebug` 通过 |
| P9-10 | 平板 InteractionMode 基础 | Done | 新增 `InteractionMode.Tv/Touch`、`LocalInteractionMode` 和运行时检测，普通 launcher/leanback launcher 共存的 manifest 保持不变；Touch 模式禁用焦点自动确认、焦点切换自动刷新、首页初始聚焦和共享焦点 Surface 的 TV 缩放/阴影/上浮/高亮效果，但保留点击操作；静态扫描未发现 `Part1`/`Partial`/编号碎片式拆分命名；`assembleDebug` 通过 |
| P9-11 | 播放器状态边界后续规划 | Done | 根据 `PlayerScreen` 当前状态分组新增 4 个待实施 Trellis 子任务：`player-load-state-boundary`、`player-side-panel-boundary`、`player-completion-boundary`、`player-progress-boundary`；明确先做元数据/加载态，再做侧栏，再做完成动作，最后才评估高频进度，且禁止把 `ExoPlayer`、`PlayerView`、`SurfaceView`、`WakeLock` 或焦点状态移入 ViewModel；本轮只更新规划文档，未改播放器实现 |
| P9-12 | 播放器加载状态边界 | Done | 新增 `PlayerLoadStateHolder` 承载播放器 active/display request、元数据、加载态、已选清晰度和重试 token；`PlayerScreen` 改为消费加载状态并继续在 UI 侧创建 Media3 `DashMediaSource`、管理 `ExoPlayer`、`PlayerView`、`SurfaceView`、`WakeLock`、焦点和高频进度循环；保留历史下一集、最近播放进度、编码/画质偏好和失败重试行为；静态扫描未发现 `Part1`/`Partial`/编号碎片式拆分命名；`assembleDebug` 通过 |
| P9-13 | 播放器侧栏状态边界 | Done | 新增 `PlayerSidePanelStateHolder` 承载侧栏视频列表、loading、加载 token、UP 投稿排序、关注状态和 UP 投稿缓存；`PlayerScreen` 只保留 `PlayerPanel`、焦点下标、方向键导航和取消关注确认弹窗焦点；继续复用 `PlayerSidePanelLoader` 的 stale-token 检查、缓存回退和 412 后台恢复重试；静态扫描未发现 `Part1`/`Partial`/编号碎片式拆分命名；`assembleDebug` 通过 |
| P9-14 | 播放器完成动作边界 | Done | 新增 `PlayerCompletionCoordinator` 承载播放完成 reported 状态、一次性动作 token 和 pending job；`PlayerScreen` 继续负责 Toast 文案、进度保存/上报、下一集/相关推荐起播和返回首页执行；完成动作仍按下一集、相关推荐、返回首页优先级执行，并通过 coordinator action scope 保持延迟期间可取消；静态扫描未发现 `Part1`/`Partial`/编号碎片式拆分命名；`assembleDebug` 通过 |
| P9-15 | 播放器高频进度边界复核 | Done | 复核 `BiliMotion.PlayerProgressUpdateMs` 生命周期绑定循环后决定暂不移动高频进度状态：`playbackPositionState`、duration、buffer、clock minute、在线人数节流、AirJump bookkeeping 和 `ExoPlayer` 读取继续留在 `PlayerScreen` UI-runtime 侧，避免引入宽 `StateFlow` 或额外 timer 扩大重组；后续只有在同一播放路径的 `gfxinfo`/`meminfo` 证明收益后再实施移动；静态扫描未发现 `Part1`/`Partial`/编号碎片式拆分命名；`assembleDebug` 通过 |
| P9-16 | MVVM/平板基础实现切片收尾 | Done | `.trellis` 父任务 `mvvm-tablet-foundation` 的实现类子任务已完成：推荐、搜索、动态/历史、AppShell 焦点、平板 Touch/TV 交互模式、播放器加载/侧栏/完成动作边界和高频进度复核；未引入 Room、Koin、Compose Navigation、root `AppViewModel` 或 `part`/`partial`/编号碎片拆分；`codegraph status .` 显示索引已最新，`assembleDebug` 通过 |
| P9-17 | MVVM/平板基础运行态验证 | Done | 新增 opt-in `emulatorValidationAbi` 构建开关，仅在显式传 `-PemulatorValidationAbi=true -PtargetAbi=x86/x86_64` 时允许模拟器 ABI，默认 debug/release 仍只打 `armeabi-v7a/arm64-v8a`；中途确认历史无线设备 `192.168.1.131:5555`、`192.168.1.195:5555`、`192.168.1.210:5555` 不可达；平板路径使用 `medium_tablet` x86_64 AVD 安装启动成功，`meminfo` PSS `98,992KB`，`gfxinfo` 可采集，未见 `AndroidRuntime` 崩溃；TV 路径创建 `bilitv_tv_api28` Android TV x86 AVD，安装启动成功，`meminfo` PSS `103,367KB`，交互后 `gfxinfo` 101 帧、P50 `23ms`、P90 `150ms`，未见 `AndroidRuntime` 崩溃；本机 `assembleDebug`、`assembleRelease -PtargetAbi=armeabi-v7a` 和模拟器 ABI debug 构建均通过 |
| P9-18 | 平板播放器 Touch 手势与触底分页 | Done | 对照 `D:\share\BiliPad\VideoPlayer\Layers\GestureLayer.swift` 和 `VideoPlayer\Components\PlayerSlider.swift`，平板 Touch 模式播放器新增单击显隐控制、双击播放/暂停、长按临时 2 倍速、横滑 90 秒范围 seek、左右边缘横滑直接退出、左半屏竖滑亮度、右半屏竖滑音量；Touch 模式使用独立于 TV D-pad 控制栏的 `PlayerTouchOverlay`，包含左上角返回、浮动底栏、可拖动 Slider 和选集/UP/相关/弹幕/设置图标按钮，打开右侧面板后自动隐藏 Touch 顶部标题和底部进度/控制条，底层 ExoPlayer、弹幕、加载状态和侧栏数据仍与 TV 共享；Touch 退出播放不再走二次确认；亮度、音量和临时 2 倍速反馈改为图标显示，亮度/音量使用环形进度，反馈尺寸、透明度、描边和液态玻璃路径跟随视觉效果档位；共享 `TvVideoGrid` 在 Touch 模式根据 `LazyListState` 触底触发 `onLoadMore`，覆盖首页、搜索、动态、历史；TV 模式遥控器路径保持不变；`git diff --check`、`assembleDebug` 和 `assembleDebug -PemulatorValidationAbi=true -PtargetAbi=x86_64` 通过；MuMu 12 实例 `192.168.1.131:5555` 为 SDK 32/x86_64/Touch 普通 UI 模式，默认 ARM 包会经 `libhoudini` 转译并出现白屏，使用 x86_64 验证包后首页正常显示，播放器可进入，Touch Slider 拖动可从 `00:14` 预览/提交到 `01:08`，左上角返回直接回首页，设置面板打开时不再被标题和底栏遮挡，最近日志未见 `FATAL EXCEPTION` |
| P9-19 | 平板播放器 Touch 控制修正 | Done | Touch 交互模式优先识别真实 touchscreen，避免带 Leanback 的平板/模拟器误走 TV 逻辑；右侧设置/选集/UP/相关推荐面板行和 UP 排序/关注、取消关注确认弹窗支持触摸点击；Touch 底部进度栏移除播放/暂停按钮，依赖双击播放/暂停，时间文本改为内容自适应并收紧控件间距，避免短视频时在进度条和时长两侧留下大块空白，同时继续支持 4 小时以上视频总时长；播放区域横滑 seek 参考 iPad 版改为起始位置加总位移直算目标时间，拖动中不拉起底部控制条，复用通用 seek 预览层显示视频帧、目标时间和 `+/-Ns` 增量，雪碧图未加载前退回纯时间预览，松手后一次性提交；底部进度条拖动继续使用原有预览路径；边缘退出只保留右边缘左滑，左边缘右滑不再退出；临时 2 倍速反馈改为固定 `116x40dp` 顶部胶囊并显示 `2X`，顶部偏移调整为 `16dp`，去除液态玻璃小尺寸高光层并将图标/文字整体居中；亮度/音量仍保留中间环形反馈；播放器完成监听改用最新 `PlayerScreenState.Ready` 和完成回调，修复结束后不执行下一集/相关推荐/退出设置的问题；`git diff --check`、`assembleDebug` 和 `assembleDebug -PemulatorValidationAbi=true -PtargetAbi=x86_64` 通过 |
| P9-20 | 搜索页 Touch 界面拆分 | Done | `SearchScreen` 按 `LocalInteractionMode` 分流，TV 模式继续使用原 D-pad 左侧字母键盘和焦点结果页；Touch 模式新增系统输入框、清空/搜索按钮、触摸历史/建议列表、触摸排序栏和结果网格，复用既有 `SearchViewModel`、搜索历史、建议、排序、分页和 `TvVideoGrid` 触底加载能力；新增搜索 Touch 尺寸 token，未引入第二套搜索数据逻辑；`assembleDebug` 和 `assembleDebug -PemulatorValidationAbi=true -PtargetAbi=x86_64` 通过 |
| P9-21 | 播放器失败态返回修正 | Done | 播放器加载/解码失败态改为专用 `PlayerFailedOverlay`：Touch 模式显示左上角返回按钮，系统返回键在失败态直接退出播放器，不再停留在只有“重试”的黑屏；长错误文案在通用 `FeedStatusScreen` 内增加水平 padding 和居中换行，避免 MediaCodec 错误信息横向溢出屏幕；`assembleDebug` 和 `assembleDebug -PemulatorValidationAbi=true -PtargetAbi=x86_64` 通过 |
| P9-22 | Touch 顶端下拉刷新 | Done | `TvVideoGrid` 新增 Touch 专用顶端下拉刷新触发器，只有传入 `onRefresh` 且列表位于首行顶部时响应，不改变 TV 模式 D-pad 焦点和返回路径；首页推荐、搜索结果、动态、历史接入各自现有刷新/重试逻辑，底部触底分页保持不变；新增下拉刷新距离 token；`assembleDebug` 通过 |
| P9-23 | Touch 下拉刷新动画 | Done | 下拉刷新改为截图风格：拉动时网格内容整体下移，顶部居中显示小型 `CircularProgressIndicator`，达到阈值后指示器颜色切到主题粉；刷新动作从“拉够立即触发”改为“松手后触发”，未达到阈值松手只收起指示器；移除上一版胶囊按钮和刷新图标资源，新增 spinner/内容下移距离 token；`assembleDebug` 和 `assembleDebug -PemulatorValidationAbi=true -PtargetAbi=x86_64` 通过 |
| P9-24 | Touch 播放器退出动画 | Done | Touch 模式退出播放器改为先播放 180ms 过渡：禁用手势、关闭面板/预览/反馈，Compose 覆盖层轻微缩小下沉并淡出，同时叠加黑色淡入层，动画结束后再走原有保存进度和返回首页流程；未对底层 `SurfaceView`/`PlayerView` 做缩放、裁切或透明动画，TV 模式退出确认路径保持不变；新增退出动画时长、下沉、覆盖层缩放和黑色层 alpha token；`assembleDebug` 和 `assembleDebug -PemulatorValidationAbi=true -PtargetAbi=x86_64` 通过 |
| P9-25 | Touch 下拉刷新位置修正 | Done | `TvVideoGrid` 的下拉刷新 spinner 固定在分区胶囊下方、首行卡片上方的顶部空档，不再随下拉距离继续下漂；spinner 改为 Canvas 弧线加无限旋转动画，保留卡片列表整体下移和松手刷新逻辑，移除不再需要的 spinner travel token；`assembleDebug` 和 `assembleDebug -PemulatorValidationAbi=true -PtargetAbi=x86_64` 通过 |
| P9-26 | 平板沉浸全屏和退出过渡修正 | Done | `MainActivity` 增加全局 immersive system bars 控制，启动、恢复和重新获得焦点时隐藏状态栏/导航栏，并将系统栏设为透明、关闭导航栏对比强制；主题增加全屏和透明系统栏兜底，避免返回首页时露出顶部状态栏或底部白色手势栏；Touch 播放器退出过渡改为 110ms 黑色遮罩，不再缩放/下沉覆盖层；`assembleDebug` 和 `assembleDebug -PemulatorValidationAbi=true -PtargetAbi=x86_64` 通过 |
| P9-27 | 播放共享封面转场 | Done | `VideoCard` 上报封面根坐标，`TvVideoGrid` 将封面 bounds 随视频点击一路传到 `AppShell`；进入播放时用封面从卡片位置放大到全屏后再挂载播放器，退出播放时先恢复首页再用同一封面从全屏缩回原卡片位置；播放器 `SurfaceView` 不参与缩放，缺失封面坐标或关闭动效时回退为直接切换；新增 hero 转场时长和遮罩 alpha token；`assembleDebug` 和 `assembleDebug -PemulatorValidationAbi=true -PtargetAbi=x86_64` 通过 |
| P9-28 | 参考 BiliPai 调整共享封面策略 | Done | 参考 `jay3-yy/BiliPai` 的共享元素策略方向（仅参考结构，不复制非商业授权代码）：播放转场改用更接近 iOS-like 的 cubic easing 和 360ms 时长，过渡期间将源卡片封面隐藏，让顶层共享封面独占画面，减少封面双份叠加和硬浮层感；隐藏键从 `AppShell` 贯通到推荐、搜索、动态、历史和 `TvVideoGrid`；`assembleDebug` 和 `assembleDebug -PemulatorValidationAbi=true -PtargetAbi=x86_64` 通过 |
| P9-29 | 官方 SharedTransition 播放转场 | Done | 删除手写 `PlaybackHeroTransitionOverlay`、封面 Rect 上报、`hiddenHeroVideoKey` 和源卡片隐藏链路；新增 `PlaybackSharedTransitionLayout` / `playbackSharedBounds`，让卡片封面与播放页全屏封面通过 Compose 官方 `SharedTransitionLayout` / `sharedBounds` 连接，进入播放时封面放大全屏后挂载 `PlayerScreen`，退出时全屏封面缩回原卡片；播放器 `SurfaceView` 仍不参与缩放/裁切/透明动画；`assembleDebug`、`assembleDebug -PemulatorValidationAbi=true -PtargetAbi=x86_64` 和 `git diff --check` 通过，已安装并启动 MuMu `192.168.1.131:5555`，最近日志未见 `FATAL EXCEPTION` / `AndroidRuntime` |
| P9-30 | Touch 退出播放缩回动画修正 | Done | 参考用户提供的 `video_2026-07-05_16-42-47.mp4`，退出播放不再先走播放器内部 110ms 黑色遮罩/淡出动画，Touch 退出保存完成后直接交给 AppShell 的 sharedBounds 反向转场；转场期间主页背景增加暗化层，前景全屏封面缩回原卡片，更接近“列表在背后、卡片缩小回去”的参考效果；删除旧 `PlayerTouchExit*` token；`assembleDebug`、`assembleDebug -PemulatorValidationAbi=true -PtargetAbi=x86_64` 和 `git diff --check` 通过，已安装并启动 MuMu `192.168.1.131:5555`，最近日志未见 `FATAL EXCEPTION` / `AndroidRuntime` |
| P9-31 | 共享封面转场节奏校准 | Done | 使用 OpenCV 抽帧分析用户提供的 `video_2026-07-05_16-42-47.mp4`，参考动效的卡片放大/缩小阶段约 `0.6-0.8s`，将 `PlaybackHeroTransitionMs` 从 `360ms` 调整到 `700ms`，缓动从快速起势曲线改为 `CubicBezierEasing(0.2, 0, 0, 1)`，让进入和退出都更接近连续流畅的卡片到全屏/全屏到卡片运动；`assembleDebug`、`assembleDebug -PemulatorValidationAbi=true -PtargetAbi=x86_64` 通过，已安装并启动 MuMu `192.168.1.131:5555`，最近日志未见 `FATAL EXCEPTION` / `AndroidRuntime` |
| P9-32 | 退出播放当前帧缩回 | Done | 退出播放器前由 `PlayerScreen` 优先通过 `PixelCopy` 抓取 Media3 `PlayerView` 内部 `SurfaceView` 当前帧，失败时退回窗口区域截图；`AppShell` 的 sharedBounds 退出目标优先绘制该当前帧快照，进入播放仍使用卡片封面放大全屏，避免退出时从视频画面硬切到封面图或黑屏；播放器 `SurfaceView` 本体仍不参与缩放、裁切或透明动画；`assembleDebug`、`assembleDebug -PemulatorValidationAbi=true -PtargetAbi=x86_64` 和 `git diff --check` 通过，已安装并启动 MuMu `192.168.1.131:5555`，最近日志未见 `FATAL EXCEPTION` / `AndroidRuntime` |
| P9-33 | 退出转场生命周期对齐 | Done | 修正 `AppShell` 播放路由 `AnimatedContent` 退出分支只有 `1ms` 导致 sharedBounds 反向动画被提前截断的问题：退出分支现在按 `PlaybackHeroTransitionMs=700ms` 保活且 `targetAlpha=1f` 不做淡出；过渡中的播放容器背景改为透明，只保留当前帧快照缩回卡片，避免 700ms 黑底挡住首页；进入和退出共享同一时长 token；`assembleDebug`、`assembleDebug -PemulatorValidationAbi=true -PtargetAbi=x86_64` 和 `git diff --check` 通过，已安装并启动 MuMu `192.168.1.131:5555`，最近日志未见 `FATAL EXCEPTION` / `AndroidRuntime` |
| P9-34 | 退出当前帧手写缩回兜底 | Done | 针对退出时官方 shared transition 仍可能因旧分支层级或目标卡片首帧未匹配而没有缩小动画的问题，新增播放转场目标 bounds 上报通道：`VideoCard` 封面在匹配播放 key 时通过 `onGloballyPositioned` 上报真实卡片 bounds；`AppShell` 退出时隐藏旧播放分支的全屏 shared target，改用独立 `PlaybackExitSnapshotOverlay` 将当前帧快照从全屏按 `PlaybackHeroTransitionMs=700ms` 插值缩到卡片位置，bounds 未到位前保持全屏，正常完成后清理转场状态，兜底清理延后到两倍时长避免提前截断；进入播放继续使用官方 shared element 放大；`assembleDebug`、`assembleDebug -PemulatorValidationAbi=true -PtargetAbi=x86_64` 和 `git diff --check` 通过，已安装并启动 MuMu `192.168.1.131:5555`，最近日志未见 `FATAL EXCEPTION` / `AndroidRuntime` |
| P9-35 | 播放转场时长调试到 500ms | Done | 按用户反馈将统一播放转场 token `BiliMotion.PlaybackHeroTransitionMs` 从 `700ms` 下调到 `500ms`，同时影响进入 shared element、退出当前帧 overlay、播放路由退出分支保活和兜底清理节奏；`assembleDebug`、`assembleDebug -PemulatorValidationAbi=true -PtargetAbi=x86_64` 和 `git diff --check` 通过，已安装并启动 MuMu `192.168.1.131:5555`，最近日志未见 `FATAL EXCEPTION` / `AndroidRuntime` |
| P9-36 | Touch 底部控制栏全宽 | Done | 平板 Touch 播放器底部控制栏新增专用外侧横向 padding token `PlayerTouchBottomOverlayHorizontalPadding=0.dp`，底部胶囊不再复用顶部覆盖层 `24.dp` 横向 padding，控制栏可铺满屏幕宽度；顶部标题/返回区域仍保留原横向安全边距；`assembleDebug`、`assembleDebug -PemulatorValidationAbi=true -PtargetAbi=x86_64` 和 `git diff --check` 通过；安装验证未执行，原因是当前 Windows 上 `adb start-server` 持续失败，报 `could not read ok from ADB Server` / `failed to start daemon` |
| P9-37 | TV/平板拆分第一切片 | Done | 新增 `InteractionProfile`、`DeviceClass` 和 `InputMode`，把设备形态与输入方式从旧 `InteractionMode.Tv/Touch` 中拆开，旧接口保留为兼容层；新增 `AdaptiveVideoGrid`，页面调用方不再直接依赖 `TvVideoGrid`，Remote 路径继续使用原 TV D-pad 网格，Touch 路径改走独立 `TouchVideoGrid`，承载触摸点击、触底分页、下拉刷新和 2/3/4 列宽度自适应；推荐、搜索、动态、历史均切到自适应网格；`git diff --check` 和 `assembleDebug` 通过 |
| P9-38 | TV/平板应用壳拆分 | Done | 新增 `AdaptiveAppScaffold`，把应用级导航外壳从 `AppShell` 中拆出；Remote/TV 路径继续使用原 `AppSidebar`、焦点 requester 和右移进入内容区逻辑，Touch 路径改用独立顶部导航栏与内容区纵向布局，不再渲染 TV 左侧栏；`AppShell` 只传入路由、账号选择、目的地选择和内容 padding 策略，页面内容和 ViewModel 不变；`git diff --check` 和 `assembleDebug` 通过 |
| P9-39 | 播放器 TV/Touch overlay 拆分 | Done | 将原 `PlayerOverlay` 拆成 `PlayerTvChrome`、`PlayerPassiveStatusChrome`、`PlayerSharedOverlay` 和既有 `PlayerTouchOverlay` 四层：TV/Remote 控制栏只在 TV 路径挂载，Touch 控制栏继续独立挂载，时钟/迷你进度作为 passive chrome 复用，seek 预览、暂停提示、右侧设置/选集/UP/相关面板和取消关注确认移到共享 overlay；`PlayerScreen` 仍持有 `ExoPlayer`、`PlayerView`、`SurfaceView`、WakeLock、高频进度和焦点状态，没有移动播放器 runtime；`git diff --check` 和 `assembleDebug` 通过 |
| P9-40 | 播放器交互状态 holder 拆分 | Done | 新增 `PlayerInteractionStateHolders`，将 TV/overlay 焦点状态 `progressFocused`、`focusedControl`、`focusedPanelIndex` 的 backing state 移入 `PlayerOverlayFocusStateHolder`，将 Touch seek/手势 seek/反馈状态移入 `PlayerTouchGestureStateHolder`，并把 `PlayerTouchFeedback`/`PlayerTouchFeedbackType` 提升为播放器包内类型；`PlayerScreen` 继续负责 ExoPlayer runtime、seek 提交、音量/亮度执行和业务动作，只把重复 reset、焦点移动和 Touch 手势状态变更收敛到 holder 方法；`git diff --check` 和 `assembleDebug` 通过 |
| P9-41 | 播放性能/缓存复查 | Done | 复查播放器周期任务、弹幕层、图片请求、缓存和释放路径：播放器仍只保留 `BiliMotion.PlayerProgressUpdateMs = 500ms` 生命周期绑定状态循环，自动隐藏/seek 确认为一次性延迟；弹幕层关闭 `pauseInvalidateWhenBlank`，避免控制栏隐藏时引擎空白暂停 invalidation 导致滚动速度偏慢；播放共享封面 fallback 改为统一 `buildVideoThumbnailRequest` CDN 裁切请求；播放进度 DataStore 增加最近 200 条索引和超限清理，避免长期无上限增长；`assembleDebug` 通过 |
| P9-42 | 播放会话配置变化恢复 | Done | 新增轻量 `PlaybackSessionViewModel`，通过 `SavedStateHandle` 保存当前 `PlaybackRequest`；`AppShell` 在 Activity 重建后直接恢复播放器分支且不重播 shared transition，正常退出同步清除会话；`PlayerScreen` 在切集、相关推荐、换画质时低频更新请求，并在 `ON_PAUSE` 保存 resolved request、当前位置和实际画质，`ExoPlayer`、`PlayerView`、WakeLock、焦点与高频进度仍留在 UI runtime；新增 4 个会话写入/恢复/清除/序列化单测并全部通过 |
| P9-43 | TV/Touch UI 边界收紧 | Done | 将 `AdaptiveVideoGrid`、`TvVideoGrid`、`TouchVideoGrid` 拆为独立职责文件，TV 网格彻底移除 Touch 分页、下拉刷新、nested scroll 和指示器残留；`AdaptiveAppScaffold` 只保留 InputMode selector，TV 侧边栏壳和 Touch 顶部导航壳分别进入 `TvAppScaffold` / `TouchAppScaffold`；搜索与播放器既有分层保持不动，未扩大高风险播放器改写；`git diff --check` 和 Debug 构建通过 |
| P9-44 | TV/Touch 自动化回归基础 | Done | 将设备判定提炼为纯 `resolveInteractionProfile`，明确 TV `uiMode` 优先于错误 touchscreen 声明，同时保留 Leanback 触屏平板的 Tablet/Touch 路径；新增 7 个设备判定 JVM 单测和 2 个 `AdaptiveAppScaffold` Remote/Touch Compose 冒烟测试；完整 `testDebugUnitTest` 共 11 个测试通过，Compose 测试分别在 `bilitv_tv_api28` x86 TV AVD 与 `sony` medium-tablet x86_64 AVD 上均 2/2 通过；`compileDebugKotlin --rerun-tasks`、`assembleDebug`、`assembleRelease -PtargetAbi=armeabi-v7a` 和 `git diff --check` 全部通过，平板 AVD 安装启动后未见 `AndroidRuntime` / `FATAL EXCEPTION` |
| P9-45 | 双 ABI Release 构建脚本 | Done | `build-release.bat` 默认顺序构建 `armeabi-v7a` 与 `arm64-v8a`，每次构建后立即复制并保留为独立命名 APK，避免 Gradle 的 `app-release.apk` 被下一 ABI 覆盖；产物统一写入 `~/.gradle/bilitv-native-build/release-apks/`，仍支持传入单个 ABI 只构建一个包，并移除不必要的仓库本地构建目录递归清理；脚本实跑成功，`aapt dump badging` 分别确认仅包含 `armeabi-v7a` / `arm64-v8a` native code |
| P9-46 | TV 启动与播放返回回归修复 | Done | 冷启动路由改由不跨进程恢复的轻量 Shell ViewModel 默认进入推荐页，并增加首帧焦点自动确认保护；播放返回按 request key 冻结来源卡片目标并保留播放来源 key/index，移除固定 30 帧过期清理；退出转场预先缓存目标 bounds，缺失目标时直接返回，SurfaceView 抓帧失败不再生成伪黑帧；流畅档关闭播放进出场动画、旧分支保活和退出抓帧；新增导航、转场策略和焦点目标冻结单测；`testDebugUnitTest`、Debug 与 `armeabi-v7a` Release 构建通过，Release 已安装 `192.168.1.195:5555` |
| P9-47 | 退出播放反向共享元素修正 | Done | 删除退出路径的 PixelCopy 当前帧、手写 Rect 缩小 overlay、目标 bounds 上报和黑色快照兜底；退出时先在仍显示播放器的分支挂载全屏封面，再切回列表，让与进入播放相同的 Compose `sharedElement`、shared key、时长和 easing 原路反向缩回来源卡片；流畅档继续直接切换且不挂载转场；`compileDebugKotlin` 与 `armeabi-v7a` Release 构建通过，Release 已覆盖安装 `192.168.1.195:5555` |
| P9-48 | 退出播放当前帧无缝缩回 | Done | 退出动画不再先显示全屏封面；在进度保存/上报完成后仅通过 `SurfaceView PixelCopy` 获取退出瞬间的当前视频帧，将该帧挂载一帧后直接沿同一 shared key 缩回来源卡片；不再使用窗口截图或 View.draw 黑帧兜底，并对纯黑采样帧做拒绝，抓帧失败时直接返回列表；流畅档不抓帧、不播放动画；`armeabi-v7a` Release 构建通过并已覆盖安装 `192.168.1.195:5555` |
| P9-49 | README MVVM 与平板支持更新 | Done | README 项目定位从仅 Android TV 更新为 Android TV/平板双端；新增 InteractionProfile、Remote/Touch 隔离、平板网格/搜索/播放器手势、渐进式 MVVM 边界和双 ABI Release 构建说明；同步修正视觉档位名称与播放进出场描述；文档差异检查通过 |
| P9-50 | TV 网格导航返回跳过残缺行 | Done | `TvVideoGrid` 的侧边栏进入焦点改为绑定首个完整可见行的第一张卡片；顶部行只剩少量像素时会跳过该行，没有完整可见行时选择可见面积最大的行，播放返回的原卡片恢复逻辑保持不变；新增 3 个入口行选择单测，针对性单测、`assembleDebug` 与 `assembleRelease -PtargetAbi=armeabi-v7a` 通过 |
| P9-51 | 首页普通分区接口 404 修复 | Done | 普通分区从已统一返回业务码 `-404` 的 `/x/web-interface/dynamic/region` 切换到支持现有 `rid/pn/ps` 分页和 `archives` 映射的 `/x/web-interface/newlist`；推荐与热门接口保持不变；番剧、电影、游戏、知识、科技、音乐、舞蹈、生活、美食、动画 10 个现有 TID 实时抽查均返回 `code=0` 和 20 条数据，完整 JVM 单测与 `assembleDebug` 通过 |
| P9-52 | 应用补丁版本递增 | Done | `versionName` 从 `1.0.0` 更新为 `1.0.1`，`versionCode` 从 `100` 更新为 `101`；`assembleDebug` 通过 |
| P9-53 | 平板统一使用左侧导航布局 | Done | `AdaptiveAppScaffold` 不再按 Remote/Touch 切换应用壳，电视、平板和手机横屏统一复用图二所示的 `TvAppScaffold` 左侧导航；删除顶部导航专用 `TouchAppScaffold`，平板仍保留 Touch 网格分页、触摸搜索和播放器手势，不改成遥控器输入模式；Remote/Touch Compose 冒烟测试均改为验证左侧导航，完整 JVM 单测、Android 测试编译、默认 Debug 与 x86_64 Debug 构建通过，已安装到 `127.0.0.1:16384` 并截图确认左侧栏生效，最近日志未见 `AndroidRuntime` / `FATAL EXCEPTION` |
| P9-54 | 搜索番剧和 PGC 选集播放 | Done | 搜索页新增“视频/番剧”内容分区；番剧搜索使用 `media_bangumi` 并解析 `season_id`、`ep_id`、封面和集数角标，失败时按既有搜索仓库风格回退到非 WBI 搜索接口；播放器支持通过 `pgc/view/web/season` 解析番剧季详情和剧集 `aid/bvid/cid/ep_id`，通过 `pgc/player/web/playurl` 获取 DASH 播放地址并复用现有画质/编码选择、选集面板和自动下一集逻辑；`ANDROID_HOME=/Users/kevin/Library/Android/sdk bash gradlew :app:assembleDebug -Dorg.gradle.java.home=/Library/Java/JavaVirtualMachines/zulu-17.jdk/Contents/Home` 通过 |
| P9-55 | 播放器 TV 控制栏点击修复 | Done | TV/Remote 播放器控制栏的选集、UP、相关推荐和设置图标补充点击回调，点击时同步焦点并复用原确认键打开面板逻辑；遥控器 D-pad 路径保持不变；`git diff --check` 和 `ANDROID_HOME=/Users/kevin/Library/Android/sdk bash gradlew :app:assembleDebug -Dorg.gradle.java.home=/Library/Java/JavaVirtualMachines/zulu-17.jdk/Contents/Home` 通过 |
| P9-56 | PGC 选集面板元数据兜底 | Done | 针对番剧播放首集可播但播放器选集面板打不开的问题，选集入口在 `metadata.pages` 缺失或为空时会先保持面板打开并补拉当前 PGC 季详情，PGC 当前集焦点定位优先使用 `ep_id` 再回退 `cid`；普通多 P 视频原有同步打开路径不变；`git diff --check` 和 `ANDROID_HOME=/Users/kevin/Library/Android/sdk bash gradlew :app:assembleDebug -Dorg.gradle.java.home=/Library/Java/JavaVirtualMachines/zulu-17.jdk/Contents/Home` 通过 |
| P9-57 | 番剧播放器控制栏日志 | Done | 针对番剧播放时选集、UP、相关推荐、设置四个控制按钮仍无响应的问题，新增 `BiliTVNative:PlayerControl` 脱敏日志，覆盖 Overlay 按钮点击、PlayerScreen 控制分发、面板打开、PGC 选集元数据补取前后和焦点定位；不打印 Cookie/token；`git diff --check` 和 `ANDROID_HOME=/Users/kevin/Library/Android/sdk bash gradlew :app:assembleDebug -Dorg.gradle.java.home=/Library/Java/JavaVirtualMachines/zulu-17.jdk/Contents/Home` 通过 |
| P9-58 | 番剧播放器根焦点兜底 | Done | 针对实机番剧播放后控制栏确认键没有进入控制分发的问题，Media3 `PlayerView` 设置为不可获取焦点并阻止子视图抢焦点，播放器 Ready 后按当前播放项重新请求 Compose 根焦点；同时将 `PlayerControl` 写入日志正文，覆盖根按键、TV 控制按钮和 Touch 控制按钮入口，便于 logcat 正则抓取；`git diff --check` 和 `ANDROID_HOME=/Users/kevin/Library/Android/sdk bash gradlew :app:assembleDebug -Dorg.gradle.java.home=/Library/Java/JavaVirtualMachines/zulu-17.jdk/Contents/Home` 通过 |
| P9-59 | 番剧切集/切画质元数据复用 | Done | 实机日志显示番剧切画质/切集后 `Loading` 到 `playurl` 请求前等待 27-59 秒，瓶颈是重复拉取 `pgc/view/web/season` 季详情而不是播放地址接口；`PlayerLoadStateHolder` 现在在同一 PGC 季或同一普通视频内复用已有 metadata，避免每次切集/切画质重拉 185 集季信息，并保留脱敏复用日志；`git diff --check` 和 `ANDROID_HOME=/Users/kevin/Library/Android/sdk bash gradlew :app:assembleDebug -Dorg.gradle.java.home=/Library/Java/JavaVirtualMachines/zulu-17.jdk/Contents/Home` 通过 |
| P9-60 | 番剧首次开播跳过季详情阻塞 | Done | 首次从搜索结果进入 PGC 番剧时，如果已有 `ep_id`，播放器不再先同步拉取完整 `pgc/view/web/season`，而是直接使用 `pgc/player/web/playurl` 开播；季详情改为 Ready 后后台补齐，用于弹幕、选集、进度和后续切集复用，避免 185 集季详情阻塞首屏播放；`git diff --check` 和 `ANDROID_HOME=/Users/kevin/Library/Android/sdk bash gradlew :app:assembleDebug -Dorg.gradle.java.home=/Library/Java/JavaVirtualMachines/zulu-17.jdk/Contents/Home` 通过 |
| P9-61 | 番剧搜索结果展示信息补全 | Done | `media_bangumi` 分类搜索本身不返回普通视频的 `play/duration/upic` 字段；番剧搜索结果现在按 `season_id/ep_id` 限制并发补拉 PGC 季详情，补齐播放数、弹幕数、当前集时长、UP/版权方头像与名称，并顺带带上首集 `bvid/cid/ep_id`；补全失败时保留原搜索结果；`git diff --check` 和 `ANDROID_HOME=/Users/kevin/Library/Android/sdk bash gradlew :app:assembleDebug -Dorg.gradle.java.home=/Library/Java/JavaVirtualMachines/zulu-17.jdk/Contents/Home` 通过 |
| P9-62 | 番剧搜索季级统计修正 | Done | PGC 季播放量可超过 `Int` 上限，例如 `凡人修仙传` `views=6982419343`，因此将视频播放量和播放器播放量字段改为 `Long` 并补充 `Long` 紧凑格式化；番剧搜索卡片保留季级开播时间，不再用第一集发布时间覆盖，季条目不再显示第一集时长，播放量为 0 时不显示播放图标；`git diff --check` 和 `ANDROID_HOME=/Users/kevin/Library/Android/sdk bash gradlew :app:assembleDebug -Dorg.gradle.java.home=/Library/Java/JavaVirtualMachines/zulu-17.jdk/Contents/Home` 通过 |
| P9-63 | 番剧搜索统计兜底解析 | Done | PGC 季详情在不同路径下播放量字段可能不稳定；番剧搜索补全现在优先读 `stat.views/view/play/plays`，再从季 `subtitle` 或当前集 `subtitle` 的“已观看 69.8 亿次”文本解析播放量，弹幕数兼容 `danmakus/danmaku/danmaku_count/dm` 字段；时长继续不显示；`git diff --check` 和 `ANDROID_HOME=/Users/kevin/Library/Android/sdk bash gradlew :app:assembleDebug -Dorg.gradle.java.home=/Library/Java/JavaVirtualMachines/zulu-17.jdk/Contents/Home` 通过 |
| P9-64 | 番剧搜索统计诊断与原始兜底 | Done | 番剧原始搜索结果现在从 `subtitle/desc/evaluate` 直接解析播放量，并用封面作为头像兜底；PGC 搜索链路新增原始解析、季详情补全成功和失败日志；通用 JSON 字符串/数值读取与 `BiliNumberParser` 对非 primitive 字段安全返回默认值，避免季详情里对象字段导致整条补全回退；`git diff --check` 和 `ANDROID_HOME=/Users/kevin/Library/Android/sdk bash gradlew :app:assembleDebug -Dorg.gradle.java.home=/Library/Java/JavaVirtualMachines/zulu-17.jdk/Contents/Home` 通过 |
| P9-65 | 番剧搜索首屏快速显示 | Done | 番剧搜索不再等待每个条目的 PGC 季详情补全后才展示列表；第一页和翻页都先返回原始搜索结果，随后在当前查询、排序和分区仍匹配时后台补齐播放量、弹幕数、头像、BVID/CID/EPID 并按搜索唯一键替换卡片数据，避免旧请求覆盖新搜索；普通视频搜索保持同步返回完整结果；`git diff --check` 和 `ANDROID_HOME=/Users/kevin/Library/Android/sdk bash gradlew :app:assembleDebug -Dorg.gradle.java.home=/Library/Java/JavaVirtualMachines/zulu-17.jdk/Contents/Home` 通过 |
| P9-66 | 番剧后台补全回填匹配修正 | Done | 实机日志确认后台补全已拿到 `凡人修仙传` 的播放量和弹幕数，但补全后条目新增 BVID，原来的替换 key 从 `pgc-season-*` 变成 `bvid-*`，导致 UI 没有替换原卡片；搜索唯一键现在对 PGC 条目优先使用 `season_id/ep_id`，普通视频仍使用 BVID；`git diff --check` 和 `ANDROID_HOME=/Users/kevin/Library/Android/sdk bash gradlew :app:assembleDebug -Dorg.gradle.java.home=/Library/Java/JavaVirtualMachines/zulu-17.jdk/Contents/Home` 通过 |
| P9-67 | 番剧搜索补全内存缓存 | Done | PGC 搜索补全结果按 `season_id/ep_id` 缓存在进程内，最多保留 128 条；番剧搜索原始结果会先套用缓存，后台补全也会跳过缓存命中的条目，切换排序或翻页遇到同一季/同一集时可直接复用播放量、弹幕数、头像和播放入口信息；新增 `pgc enrich cache hit` 日志用于实机确认缓存命中；`git diff --check` 和 `ANDROID_HOME=/Users/kevin/Library/Android/sdk bash gradlew :app:assembleDebug -Dorg.gradle.java.home=/Library/Java/JavaVirtualMachines/zulu-17.jdk/Contents/Home` 通过 |
| P9-68 | 搜索影视分区扩展 | Done | 搜索分区新增电视剧、电影和纪录片；番剧继续使用 `media_bangumi`，影视分区使用 `media_ft` 并按 B 站 PGC `season_type` 区分电视剧 `5`、电影 `2`、纪录片 `3`，同时保留本地过滤兜底；新增分区复用 PGC 首屏快速显示、后台补全、补全缓存和播放入口逻辑；`git diff --check` 和 `ANDROID_HOME=/Users/kevin/Library/Android/sdk bash gradlew :app:assembleDebug -Dorg.gradle.java.home=/Library/Java/JavaVirtualMachines/zulu-17.jdk/Contents/Home` 通过 |
| P9-69 | 搜索结果默认聚焦分区标签 | Done | TV 搜索结果页不再在结果加载完成后默认聚焦第一个视频卡片，初次搜索、切换分区或切换排序后会请求当前分区标签焦点，方便 D-pad 左右切换视频、番剧、电视剧、电影和纪录片；向下仍先到排序标签，再向下进入结果列表；触屏搜索布局不变；`git diff --check` 和 `ANDROID_HOME=/Users/kevin/Library/Android/sdk bash gradlew :app:assembleDebug -Dorg.gradle.java.home=/Library/Java/JavaVirtualMachines/zulu-17.jdk/Contents/Home` 通过 |
| P9-70 | 搜索排序焦点保持修正 | Done | 搜索结果页拆分分类切换和排序切换的焦点重置逻辑：切换分类仍回到当前分类标签，切换排序只清理结果列表恢复位置，不再触发焦点跳回分类标签，保证排序行内左右切换时光标留在排序标签上；`git diff --check` 和 `ANDROID_HOME=/Users/kevin/Library/Android/sdk bash gradlew :app:assembleDebug -Dorg.gradle.java.home=/Library/Java/JavaVirtualMachines/zulu-17.jdk/Contents/Home` 通过 |
| P9-71 | 搜索排序向上焦点固定 | Done | 排序标签行的 D-pad 向上不再依赖默认最近邻焦点搜索，而是显式请求当前选中的分类标签焦点，避免从靠右排序按钮上移时落到空间位置最近但非当前的分类标签；`git diff --check` 和 `ANDROID_HOME=/Users/kevin/Library/Android/sdk bash gradlew :app:assembleDebug -Dorg.gradle.java.home=/Library/Java/JavaVirtualMachines/zulu-17.jdk/Contents/Home` 通过 |
| P9-72 | 首页 PGC 剧集分区 | Done | 首页“番剧”不再使用普通投稿 `rid=13`，改为 PGC 索引 `season_type=1`；保留原“影视”投稿分区用于影视解说内容，并新增电视剧 `season_type=5`、电影 `season_type=2`、纪录片 `season_type=3` 三个首页分区；PGC 首页条目带首集 `ep_id` 可直接进入播放，同时修正 PGC 条目的网格 key、翻页去重、焦点恢复和共享转场 key，避免空 BVID 导致卡片复用或去重异常；设置页首页分区网格高度扩到 4 行；`git diff --check` 和 `ANDROID_HOME=/Users/kevin/Library/Android/sdk bash gradlew :app:assembleDebug -Dorg.gradle.java.home=/Library/Java/JavaVirtualMachines/zulu-17.jdk/Contents/Home` 通过 |
| P9-73 | 首页影视分区顺序调整 | Done | 首页默认分区顺序调整为番剧、电视剧、电影、纪录片之后再显示“影视”，保留“影视”作为普通投稿影视解说分区；`git diff --check` 和 `ANDROID_HOME=/Users/kevin/Library/Android/sdk bash gradlew :app:assembleDebug -Dorg.gradle.java.home=/Library/Java/JavaVirtualMachines/zulu-17.jdk/Contents/Home` 通过 |
| P9-74 | 播放 Source error 诊断与重试续播 | Done | 实机日志显示本次 `Source error` 底层为 Media3 分片加载 `HttpDataSource.InvalidResponseCodeException: 503`，属于 CDN/播放分片临时不可用而非解码失败；播放器错误回调新增 `BiliTVNative:PlayerError` 脱敏日志，记录 bvid/cid/PGC id、错误前位置、Media3 错误码和底层 cause；进入失败态前保存重试位置，点击重试会从错误前位置续播，不再默认从头开始；`git diff --check` 和 `ANDROID_HOME=/Users/kevin/Library/Android/sdk bash gradlew :app:assembleDebug -Dorg.gradle.java.home=/Library/Java/JavaVirtualMachines/zulu-17.jdk/Contents/Home` 通过 |
| P9-75 | 焦点边界保持修正 | Done | 主页分区标签和搜索结果分类标签在 TV D-pad 向上时显式消费按键，避免顶部标签把焦点交给默认最近邻搜索导致丢焦或跳到侧边栏/搜索；左侧账号入口和导航菜单项在向左时显式消费按键，避免处在屏幕最左边界时焦点消失，向右仍保持进入当前页面内容区；设置页、视频网格和播放器已有显式顶部或边界焦点处理，未发现同类明显问题；`git diff --check` 和 `ANDROID_HOME=/Users/kevin/Library/Android/sdk bash gradlew :app:assembleDebug -Dorg.gradle.java.home=/Library/Java/JavaVirtualMachines/zulu-17.jdk/Contents/Home` 通过 |
| P9-76 | PGC 剧集按季恢复最近观看 | Done | 播放进度存储新增按 `pgcSeasonId` 保存最近观看的 `ep_id/bvid/cid/page/position`；播放器保存进度时同步写入 season 最近记录，首页或搜索再次打开同一番剧、电视剧、电影或纪录片时，在非强制播放和非清晰度切换场景优先恢复到上次观看集和位置，手动选集、重试续播和清晰度切换不被覆盖；`git diff --check` 和 `ANDROID_HOME=/Users/kevin/Library/Android/sdk bash gradlew :app:assembleDebug -Dorg.gradle.java.home=/Library/Java/JavaVirtualMachines/zulu-17.jdk/Contents/Home` 通过 |
| P9-77 | 播放器暂停焦点提示优化 | Done | TV 播放器用 OK 键暂停时仍显示控制层，但隐藏底部播放列表等按钮的焦点高亮，避免误导用户以为再次 OK 会打开播放列表；暂停态未进入菜单焦点时再次 OK 直接恢复播放，按下键或左右键进入底部按钮焦点后，OK 会执行当前高亮按钮，例如打开播放列表；从播放列表返回后若焦点在进度条，OK 仍按暂停态恢复播放处理，不会误触发上一次高亮的播放列表按钮；`git diff --check` 和 `ANDROID_HOME=/Users/kevin/Library/Android/sdk bash gradlew :app:assembleDebug -Dorg.gradle.java.home=/Library/Java/JavaVirtualMachines/zulu-17.jdk/Contents/Home` 通过 |
| P9-78 | 播放器弹幕快捷开关 | Done | TV 播放器底部控制菜单新增弹幕快捷按钮并放在第二位；按钮使用透明背景的“弹/彈”字形图标而非字幕图标，字形外绘制白色圆圈，关闭状态在圆圈内叠加斜线，开启状态不显示斜线；选中后按 OK 可直接切换 `danmakuSettings.enabled` 并持久化，播放中自动收起控制菜单以便观察画面反馈，暂停状态下保留菜单和当前操作上下文；底部状态区始终显示弹幕数量或“弹幕”，不再因弹幕关闭显示“弹幕关”；`git diff --check` 和 `ANDROID_HOME=/Users/kevin/Library/Android/sdk bash gradlew :app:assembleDebug -Dorg.gradle.java.home=/Library/Java/JavaVirtualMachines/zulu-17.jdk/Contents/Home` 通过 |
| P9-79 | 播放器控制层自动隐藏时长 | Done | TV 播放器控制层无操作自动隐藏时间从 4 秒调整为 6 秒，并增加控制层交互 token，左右移动菜单焦点、上下切换进度条和底部菜单、打开控制层等操作都会刷新计时，确保最后一次遥控器操作后再开始 6 秒隐藏；暂停状态仍保持不自动隐藏；`git diff --check` 和 `ANDROID_HOME=/Users/kevin/Library/Android/sdk bash gradlew :app:assembleDebug -Dorg.gradle.java.home=/Library/Java/JavaVirtualMachines/zulu-17.jdk/Contents/Home` 通过 |
| P9-80 | 播放中向上键切换弹幕 | Done | TV 播放器在控制层未显示、没有打开面板或 seek 预览时，D-pad 向上键可直接切换弹幕开关并持久化，切换后用 Toast 提示“弹幕已开启/弹幕已关闭”，不弹出控制菜单；设置页播放设置新增“向上键切换弹幕”开关，默认开启，可控制该快捷键是否生效；控制层已显示时向上仍保持原有焦点移动到进度条的行为，焦点已在进度条或不在底部菜单上时也不会触发弹幕快捷开关，避免破坏菜单导航；`git diff --check` 和 `ANDROID_HOME=/Users/kevin/Library/Android/sdk bash gradlew :app:assembleDebug -Dorg.gradle.java.home=/Library/Java/JavaVirtualMachines/zulu-17.jdk/Contents/Home` 通过 |
| P9-81 | 播放历史显示 PGC 剧集 | Done | 播放历史来自 B 站服务端 `/x/web-interface/history/cursor`；解析新增 PGC 字段兜底，支持从历史条目的 `history`、顶层字段和 `uri` 中提取 `ep_id/season_id`，允许没有 BVID 但带 PGC id 的番剧、电视剧、电影、纪录片条目进入列表；历史卡片角标优先组合显示 PGC 集序号和集标题；PGC 元数据补全兼容 `episodes/main_section/section/sections` 多种剧集列表，并在 `ep_id` 只返回单集时用响应里的 `season_id` 二次补拉完整季，选集面板复用完整列表；`git diff --check` 和 `ANDROID_HOME=/Users/kevin/Library/Android/sdk bash gradlew :app:assembleDebug -Dorg.gradle.java.home=/Library/Java/JavaVirtualMachines/zulu-17.jdk/Contents/Home` 通过 |
| P9-82 | Source error 自动降级恢复 | Done | 播放器错误回调会递归识别 Media3 `HttpDataSource.InvalidResponseCodeException` 的 HTTP 状态码，并只对分片 `403/404/503` 触发自动恢复；恢复策略优先从高画质直接降到不高于 1080P 的可用档位并从错误前位置续播，若 1080P 或更低仍失败则临时回退 H.264，避免逐档重载导致多次“正在加载播放地址”；H.264 仍失败或非目标 HTTP 错误时进入原失败态，失败重试位置单独保存，只有用户点击重试才再次加载，避免失败态因更新 `activeRequest` 自动重载；自动降级、HTTP 状态码和目标画质/编码写入 `BiliTVNative:PlayerError` 脱敏日志；`git diff --check` 和 `ANDROID_HOME=/Users/kevin/Library/Android/sdk bash gradlew :app:assembleDebug -Dorg.gradle.java.home=/Library/Java/JavaVirtualMachines/zulu-17.jdk/Contents/Home` 通过 |
| P9-83 | Source error 同画质备用 CDN 恢复 | Done | 播放器保留 B 站 DASH track 的 `backupUrl/backup_url`，生成 MPD 时为同一 Representation 写入多个 `BaseURL`；分片 `403/404/503` 自动恢复顺序调整为先在同画质同编码 track 内切换备用 CDN 并从错误前位置重建 MediaSource，备用 CDN 不可用时才继续走降到 1080P 或更低、再回退 H.264 的策略；CDN fallback 日志只记录状态码、画质、编码、脱敏 host、bvid/cid 和位置，不打印 Cookie/token；`git diff --check` 和 `ANDROID_HOME=/Users/kevin/Library/Android/sdk bash gradlew :app:assembleDebug -Dorg.gradle.java.home=/Library/Java/JavaVirtualMachines/zulu-17.jdk/Contents/Home` 通过 |
