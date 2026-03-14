下面我直接把它整理成一份更正式、偏真实项目可落地的 **《软件架构设计文档》目录与内容草案**。
你可以把这份内容继续交给 Agent，让它在此基础上逐章展开，也可以作为你自己的项目设计初稿。

------

# 健身记录 App 软件架构设计文档（目录与内容草案）

## 文档信息

**项目名称**：IronLogic（暂定）
**项目类型**：专业健身记录与周期化训练管理系统
**技术栈**：Flutter + Spring Boot
**文档版本**：V1.0 Draft
**文档目标**：指导项目从需求分析、领域建模、架构设计到 MVP 落地开发

------

# 1. 引言

## 1.1 编写目的

本文档用于对健身记录 App 进行正式的软件架构设计说明，明确系统的业务目标、核心概念、领域模型、模块划分、数据模型、核心流程、接口设计原则与开发路线，为后续开发、协作、迭代提供统一依据。

## 1.2 项目背景

当前市面上多数健身记录 App 更偏向“普通打卡”或“简单日志记录”，对专业训练者真正关注的以下问题支持不足：

- 渐进超负荷如何连续追踪
- 周期化训练如何管理
- 训练计划如何与真实执行偏差兼容
- 训练安排如何在不固定星期几的情况下持续推进
- 如何同时支持模板训练和自由训练

本项目旨在构建一款以 **渐进超负荷** 与 **周期化训练** 为核心的专业训练记录系统。

## 1.3 项目目标

本项目目标包括：

- 提供专业训练记录能力
- 支持用户自定义训练模板
- 支持模板驱动的计划生成
- 支持自由训练记录
- 支持训练序列推进
- 支持渐进超负荷规则
- 为后续统计分析、饮食记录、自适应训练功能预留扩展能力

## 1.4 目标用户

目标用户包括：

- 有规律力量训练习惯的用户
- 关注增肌、增力、减脂周期安排的进阶训练者
- 希望长期追踪动作表现和训练进度的用户
- 不希望训练计划被自然周强绑定的用户

## 1.5 术语说明

建议统一术语如下：

- **Program**：训练计划
- **Block**：训练阶段 / 周期块
- **Session Template**：训练日模板
- **Exercise**：动作
- **Workout Session**：一次实际训练
- **Workout Set**：一次训练中的单组记录
- **Progression Rule**：渐进规则
- **Sequence Progression**：按训练序列推进，而非按自然周推进

> 说明：原始需求中的“动作组”命名不够清晰，本文档统一替换为“训练日模板”或“Session Template”。

------

# 2. 需求分析

## 2.1 业务愿景

构建一款兼具专业性、可扩展性与可用性的训练系统，使用户能够：

- 定义自己的训练计划结构
- 根据计划自动推进训练
- 在现实训练中灵活调整动作与安排
- 长期追踪渐进超负荷效果
- 通过系统记录支持训练决策

## 2.2 核心业务需求

### 2.2.1 训练模板管理

用户可创建和维护训练模板，模板中定义：

- 一次训练包含哪些动作
- 每个动作的组数、次数、重量、休息时间
- 训练日模板的顺序
- 模板所属训练阶段

### 2.2.2 周期化训练管理

用户可定义多个训练阶段，例如：

- 增肌期
- 增力期
- 减脂期
- 减载期

多个阶段可组合为完整训练计划。

### 2.2.3 自动生成训练计划

系统可根据当前计划进度，为用户推荐下一次应执行的训练模板，并给出参考重量、次数、休息时间等建议值。

### 2.2.4 自由训练支持

用户可不依赖模板，直接创建一次自由训练，并记录动作、组数、重量、次数等数据。

### 2.2.5 渐进超负荷支持

系统支持多种渐进规则，例如：

- 固定增重
- 百分比增重
- 双重渐进
- 失败回退
- 减载逻辑
- 容量推进

### 2.2.6 训练执行与历史记录

用户可查看每次训练的详细记录，包括：

- 执行动作
- 每组重量与次数
- 实际完成情况
- 与模板的偏差

### 2.2.7 后续扩展方向

后续可扩展：

- 训练统计分析
- PR 记录
- 趋势分析
- 饮食记录
- 恢复管理
- 自适应推荐

## 2.3 非功能性需求

### 2.3.1 可扩展性

系统需具备良好的模块化设计，以支持后续新增功能。

### 2.3.2 可维护性

系统应具备清晰的模块边界、统一命名与稳定的数据结构。

### 2.3.3 可用性

专业能力强，但默认交互不能复杂，需降低用户理解和操作成本。

### 2.3.4 一致性

模板、记录、进度推进逻辑必须具备一致性，避免数据语义混乱。

### 2.3.5 性能要求

移动端常见训练记录操作应快速响应，训练中核心交互不应依赖高延迟操作。

------

# 3. 产品抽象与设计原则

## 3.1 产品核心定位

本产品不是普通健身打卡工具，而是：

> 以“渐进超负荷 + 周期化训练 + 训练序列推进”为核心的专业训练记录系统。

## 3.2 产品核心差异化

核心差异化建议定义为：

### 3.2.1 模板与实际执行分离

系统清晰区分“计划中的理想训练”和“用户真实完成的训练”。

### 3.2.2 按训练序列推进

用户不需要严格在固定星期几完成某次训练，而是按完成顺序自动推进。

### 3.2.3 渐进规则内建

系统不只是记录，而是对下一次训练提供建议。

### 3.2.4 模板训练与自由训练并存

既适合结构化训练用户，也适合临时自由训练用户。

## 3.3 产品复杂度控制原则

为避免产品过于复杂，需遵循：

- 默认只暴露必要能力
- 高级能力逐层开放
- 专业逻辑尽量由系统承担
- 用户只在必要时进行高级配置

## 3.4 用户模式分层

建议产品交互分为三层：

### 3.4.1 快速开始模式

用户选择目标、频率、经验等级，系统自动生成初始模板。

### 3.4.2 自定义模板模式

用户可编辑训练日模板和动作处方。

### 3.4.3 高级周期化模式

高阶用户可配置阶段、循环、减载和复杂渐进规则。

------

# 4. 训练科学性与业务合理性分析

## 4.1 方案科学性分析

整体设计符合训练科学中的以下原则：

- 渐进超负荷
- 周期化训练
- 训练量与强度管理
- 训练频率与恢复平衡
- 实际执行与计划偏差管理

## 4.2 合理性分析

当前方案合理，但需要在产品表达与架构实现上做抽象优化：

- 不建议直接将“动作组”作为核心概念暴露给用户
- 应以“训练日模板”和“训练阶段”作为主要概念
- 渐进逻辑应设计为策略化规则，而不是写死在流程中

## 4.3 主要风险点

主要风险包括：

- 领域概念过多导致学习成本高
- 渐进逻辑设计过于死板
- 计划层和执行层混用导致数据混乱
- 产品首版功能过多导致开发失控

## 4.4 优化建议

建议：

- MVP 仅做最小核心闭环
- 先支持少量核心渐进规则
- 所有高级规则通过策略接口扩展
- 所有训练记录必须能回溯到模板来源或自由训练来源

------

# 5. 总体架构设计

## 5.1 架构选型结论

本项目建议采用：

> **模块化单体架构**，而非微服务架构。

## 5.2 选型理由

### 5.2.1 为什么不建议一开始做微服务

当前项目：

- 用户规模未知
- 业务尚在快速演化
- 核心问题在产品与领域建模，而非分布式拆分
- 微服务会显著增加开发和维护成本

### 5.2.2 为什么选择模块化单体

模块化单体具有以下优势：

- 开发效率高
- 部署简单
- 便于快速迭代
- 领域边界仍可清晰划分
- 后续有机会平滑演进

## 5.3 系统总体结构

建议总体结构分为：

- 移动端客户端（Flutter）
- 后端服务（Spring Boot）
- 数据存储层（MySQL/PostgreSQL）
- 缓存与扩展层（后续可加 Redis）
- 文件与媒体层（后续可加对象存储）

## 5.4 后端分层建议

建议后端采用以下层次：

- Interface 层：Controller、DTO、参数校验
- Application 层：应用服务、用例编排
- Domain 层：领域模型、领域服务、策略接口
- Infrastructure 层：Repository、ORM、外部集成

## 5.5 前端分层建议

Flutter 建议分层为：

- presentation：页面、组件、状态展示
- application：状态管理、交互流程
- domain：核心实体与业务抽象
- data：接口请求、本地缓存、DTO 映射

------

# 6. 领域模型设计

## 6.1 领域建模目标

目标是清晰区分：

- 训练计划定义
- 实际训练执行
- 进度推进逻辑
- 用户动作配置
- 渐进规则配置

## 6.2 核心领域对象

### 6.2.1 User

表示系统用户，负责个人偏好、账户信息、单位配置等。

### 6.2.2 Exercise

表示动作库中的动作，包括系统动作和用户自定义动作。

### 6.2.3 Program

表示完整训练计划。

### 6.2.4 ProgramBlock

表示训练计划中的阶段，如增肌期、增力期等。

### 6.2.5 SessionTemplate

表示某一阶段中的训练日模板。

### 6.2.6 SessionExerciseTemplate

表示训练日模板中的动作定义。

### 6.2.7 WorkoutSession

表示用户某次实际训练。

### 6.2.8 WorkoutExercise

表示实际训练中的某个动作。

### 6.2.9 WorkoutSet

表示动作中的单组完成情况。

### 6.2.10 ProgressionRule

表示渐进超负荷规则。

### 6.2.11 ProgramProgress

表示用户当前训练计划推进状态。

## 6.3 实体关系

建议在文档中以 ER 图方式表达，文字草案如下：

- 一个 User 可拥有多个 Program
- 一个 Program 包含多个 ProgramBlock
- 一个 ProgramBlock 包含多个 SessionTemplate
- 一个 SessionTemplate 包含多个 SessionExerciseTemplate
- 一个 WorkoutSession 可来源于某个 SessionTemplate 或自由训练
- 一个 WorkoutSession 包含多个 WorkoutExercise
- 一个 WorkoutExercise 包含多个 WorkoutSet
- 一个 SessionExerciseTemplate 可关联一个 ProgressionRule

## 6.4 关键建模原则

建模时遵循：

- 模板层与执行层分离
- 配置与结果分离
- 推进状态独立存储
- 复杂规则参数化
- 用户自定义动作可独立管理

------

# 7. 模块划分设计

## 7.1 模块划分原则

模块划分基于领域职责，而不是技术细节。

## 7.2 建议模块

### 7.2.1 用户模块

负责：

- 注册登录
- 用户设置
- 偏好管理
- 单位与默认值配置

### 7.2.2 动作库模块

负责：

- 系统动作库
- 用户自定义动作
- 动作分类
- 肌群标签
- 器械类型

### 7.2.3 训练计划模块

负责：

- 创建 Program
- 创建 Block
- 创建 SessionTemplate
- 编辑模板
- 模板排序与组织

### 7.2.4 训练执行模块

负责：

- 开始训练
- 从模板生成训练实例
- 自由训练创建
- 记录动作与组
- 保存训练结果

### 7.2.5 进度推进模块

负责：

- 当前计划状态维护
- 下一次训练推荐
- 渐进规则计算
- 减载判断
- 序列推进

### 7.2.6 统计分析模块

负责：

- 历史训练统计
- PR 统计
- 趋势分析
- 总量与频率分析

### 7.2.7 饮食模块（预留）

负责：

- 餐次记录
- 营养素统计
- 体重与饮食关联分析

------

# 8. 数据模型与数据库设计草案

## 8.1 数据库选型建议

优先推荐 PostgreSQL，其次 MySQL。
理由：

- PostgreSQL 对 JSON 字段支持更好
- 后续复杂查询与扩展能力更强
- 适合结构化 + 半结构化混合场景

## 8.2 核心表设计

### 8.2.1 users

字段建议：

- id
- email / mobile
- nickname
- gender
- weight_unit
- default_rest_seconds
- created_at
- updated_at

### 8.2.2 exercises

字段建议：

- id
- owner_user_id（为空表示系统动作）
- name
- category
- primary_muscle
- secondary_muscles
- equipment_type
- movement_pattern
- is_custom
- metadata_json

### 8.2.3 programs

字段建议：

- id
- user_id
- name
- goal_type
- status
- description
- created_at
- updated_at

### 8.2.4 program_blocks

字段建议：

- id
- program_id
- name
- block_type
- sequence_no
- duration_mode
- duration_value
- deload_enabled
- metadata_json

### 8.2.5 session_templates

字段建议：

- id
- block_id
- name
- sequence_no
- trigger_mode
- notes
- metadata_json

### 8.2.6 session_exercise_templates

字段建议：

- id
- session_template_id
- exercise_id
- order_no
- target_sets
- target_reps
- target_weight
- rest_seconds
- intensity_mode
- progression_rule_id
- prescription_json

### 8.2.7 workout_sessions

字段建议：

- id
- user_id
- source_type
- program_id
- block_id
- session_template_id
- started_at
- ended_at
- status
- notes

### 8.2.8 workout_exercises

字段建议：

- id
- workout_session_id
- exercise_id
- source_template_exercise_id
- actual_order_no
- notes

### 8.2.9 workout_sets

字段建议：

- id
- workout_exercise_id
- set_no
- weight
- reps
- duration_seconds
- rest_seconds
- rpe
- rir
- is_warmup
- is_completed

### 8.2.10 progression_rules

字段建议：

- id
- user_id / system_default
- name
- rule_type
- params_json
- is_active

### 8.2.11 program_progress

字段建议：

- id
- user_id
- program_id
- current_block_id
- next_session_template_id
- last_completed_session_id
- sequence_cursor
- progress_snapshot_json

## 8.3 JSON 字段使用建议

适合使用 JSON 扩展的字段：

- exercises.metadata_json
- program_blocks.metadata_json
- session_templates.metadata_json
- session_exercise_templates.prescription_json
- progression_rules.params_json
- program_progress.progress_snapshot_json

原则是：

- 核心查询字段结构化
- 高频筛选字段不放 JSON
- 仅扩展参数或未来变化较大的内容放 JSON

------

# 9. 核心业务流程设计

## 9.1 创建训练计划流程

流程概述：

1. 用户创建 Program
2. 为 Program 添加一个或多个 Block
3. 在每个 Block 下创建多个 Session Template
4. 为每个 Session Template 添加动作处方
5. 设置顺序与默认渐进规则
6. 激活计划

## 9.2 从模板生成训练实例流程

流程概述：

1. 用户点击“开始训练”
2. 系统读取当前推荐 Session Template
3. 复制模板生成 WorkoutSession
4. 将模板动作复制为 WorkoutExercise
5. 根据历史记录和 progression engine 生成建议值
6. 返回训练页面供用户开始记录

## 9.3 自由训练流程

流程概述：

1. 用户点击“自由训练”
2. 创建空的 WorkoutSession
3. 用户手动添加动作
4. 用户记录每组数据
5. 训练完成后保存
6. 训练数据纳入历史统计

## 9.4 完成训练后更新进度流程

流程概述：

1. 用户结束训练
2. 系统校验训练数据完整性
3. 保存 WorkoutSession / Exercise / Set
4. 计算本次训练是否完成模板要求
5. 更新 ProgramProgress
6. 生成下一次训练推荐

## 9.5 替换动作流程

流程概述：

1. 用户在训练中替换动作
2. 新动作与原动作保留关联信息
3. 实际执行动作记录到 WorkoutExercise
4. 模板不自动修改
5. 统计层可将其视为偏离计划或等效替代动作

## 9.6 跳过训练流程

流程概述：

1. 用户未严格执行推荐模板
2. 可手动选择其他模板或自由训练
3. 系统记录偏离情况
4. 进度推进不强制锁死
5. 后续推荐以最近完成状态为依据重新计算

------

# 10. 训练推进引擎设计

## 10.1 设计目标

progression engine 用于支持：

- 推荐下一次训练模板
- 推荐动作参数
- 渐进超负荷逻辑
- 减载与失败回退
- 灵活扩展不同规则

## 10.2 设计原则

推进引擎应遵循：

- 规则配置化
- 算法策略化
- 与模板层解耦
- 支持回退与异常处理
- 支持按序列推进

## 10.3 支持的基础规则

### 10.3.1 固定增重

达到要求后按固定重量增加。

### 10.3.2 百分比增重

达到要求后按百分比增加。

### 10.3.3 双重渐进

优先提升次数，达到上限后再增加重量。

### 10.3.4 失败回退

连续未完成时降低目标强度。

### 10.3.5 减载逻辑

在达到一定失败阈值或阶段切换时进入减载。

## 10.4 序列推进逻辑

系统应支持：

- A → B → C 的训练序列
- 用户上次做了 A，则下次默认推荐 B
- 如果用户跳到 C，系统不报错，而是重新记录当前状态
- Block 内所有 Session 可按 sequence_no 管理

## 10.5 可扩展策略设计

建议使用策略模式设计规则接口，例如：

- ProgressionStrategy
- FixedIncrementStrategy
- DoubleProgressionStrategy
- DeloadStrategy

------

# 11. 接口设计原则与 API 草案

## 11.1 API 设计原则

接口设计应遵循：

- 面向资源
- 尽量 RESTful
- 前后端职责清晰
- 核心流程优先落地
- DTO 与领域实体分离

## 11.2 主要 API 草案

### 11.2.1 用户相关

- POST /api/users/register
- POST /api/users/login
- GET /api/users/me
- PUT /api/users/preferences

### 11.2.2 动作库相关

- GET /api/exercises
- POST /api/exercises
- PUT /api/exercises/{id}
- GET /api/exercises/{id}

### 11.2.3 训练计划相关

- POST /api/programs
- GET /api/programs
- GET /api/programs/{id}
- PUT /api/programs/{id}
- POST /api/programs/{id}/blocks
- POST /api/blocks/{id}/session-templates

### 11.2.4 训练执行相关

- POST /api/workouts/from-template/{templateId}
- POST /api/workouts/manual
- GET /api/workouts/current-recommendation
- POST /api/workouts/{id}/exercises
- POST /api/workouts/{id}/finish
- GET /api/workouts/history

### 11.2.5 统计相关

- GET /api/stats/overview
- GET /api/stats/exercises/{exerciseId}/trend
- GET /api/stats/prs

------

# 12. 前端架构设计草案

## 12.1 页面规划

MVP 页面建议包括：

- 登录 / 注册页
- 首页 / 今日推荐训练页
- 训练计划列表页
- 训练计划编辑页
- 训练执行页
- 历史训练页
- 动作详情趋势页
- 个人设置页

## 12.2 状态管理建议

推荐使用：

- Riverpod 或 Bloc

要求：

- 页面状态与业务状态分离
- 网络请求状态标准化
- 训练执行过程支持本地临时状态

## 12.3 本地缓存建议

训练执行过程建议支持本地缓存，避免训练中断导致数据丢失。

------

# 13. 安全性与稳定性设计

## 13.1 认证与授权

建议使用：

- JWT
- Spring Security

## 13.2 数据完整性

关键训练记录提交时需确保：

- WorkoutSession 与其下属 Exercise、Set 原子保存
- 重要流程使用事务控制

## 13.3 容错设计

训练中断时应支持：

- 本地草稿恢复
- 未完成训练续写
- 网络异常时临时缓存

------

# 14. MVP 范围定义

## 14.1 第一版必须实现的功能

建议 MVP 包含：

- 用户注册登录
- 动作库管理
- 训练计划管理
- 训练模板管理
- 从模板开始训练
- 自由训练
- 训练记录保存
- 基础历史查看
- 简单渐进推荐
- 首页推荐下一次训练

## 14.2 第一版不做的功能

以下内容建议延后：

- 饮食记录
- 社交功能
- AI 聊天教练
- 高复杂度统计大屏
- 可穿戴设备接入
- 复杂恢复模型
- 复杂多层循环嵌套

## 14.3 MVP 成功标准

MVP 的成功标准是：

- 用户能完整创建一个训练计划
- 用户能持续记录训练
- 系统能自动推荐下一次训练
- 用户能查看自己的进步趋势
- 核心模型不会因后续扩展被推翻

------

# 15. 开发路线图建议

## 15.1 第一阶段：核心数据闭环

目标：

- 建立动作、模板、训练记录完整闭环
- 打通前后端基础交互
- 验证核心领域模型是否成立

内容：

- 动作库
- 训练计划
- 训练模板
- 训练记录
- 历史记录

## 15.2 第二阶段：加入推进引擎

目标：

- 支持下一次推荐训练
- 支持基础渐进超负荷规则

内容：

- ProgramProgress
- ProgressionRule
- 当前推荐逻辑
- 简单规则引擎

## 15.3 第三阶段：统计与体验优化

目标：

- 增强用户留存和数据可视化体验

内容：

- PR 统计
- 趋势图
- 训练频率与容量分析
- 训练中断恢复

## 15.4 第四阶段：高级功能扩展

目标：

- 提升系统专业深度和长期价值

内容：

- 饮食记录
- 自适应训练
- 恢复指标
- 更复杂周期逻辑

------

# 16. 主要技术风险与应对策略

## 16.1 风险一：过度设计

表现：

- 一开始设计过多规则
- 引入过多高级概念
- 工程复杂度超过实际收益

应对：

- 严格按 MVP 边界推进
- 高级规则统一后置
- 所有复杂点先留扩展口

## 16.2 风险二：模板层与执行层混淆

表现：

- 修改模板后影响历史记录解释
- 实际训练数据无法回溯来源

应对：

- 模板与执行数据必须分表
- 执行时拷贝快照
- 历史记录不得依赖当前模板实时解释

## 16.3 风险三：渐进规则写死

表现：

- 规则难扩展
- 特殊动作或训练方式难支持

应对：

- 使用策略模式
- 规则参数 JSON 化
- 引擎独立模块化

## 16.4 风险四：用户理解成本过高

表现：

- 上手难
- 配置复杂
- 留存差

应对：

- 提供默认模板
- 隐藏高级配置
- 引入“快速开始”路径

------

# 17. 后续文档拆分建议

这份架构文档确定后，建议继续拆成以下子文档：

1. 《领域模型详细设计文档》
2. 《数据库设计文档》
3. 《API 设计文档》
4. 《Flutter 前端页面与状态流设计文档》
5. 《训练推进引擎设计文档》
6. 《MVP 开发任务拆解文档》

------

# 18. 附录

## 18.1 建议 ER 关系图

后续补充。

## 18.2 建议系统模块图

后续补充。

## 18.3 建议核心流程时序图

后续补充。

------

# 一个更适合你现在使用的“精简版目录”

如果你准备马上让 Agent 继续展开，我建议你优先用下面这个精简主目录：

1. 引言
2. 需求分析
3. 产品抽象与设计原则
4. 总体架构设计
5. 领域模型设计
6. 模块划分设计
7. 数据库设计草案
8. 核心业务流程设计
9. 训练推进引擎设计
10. API 设计草案
11. MVP 范围定义
12. 开发路线图
13. 风险与应对

下面我直接给你一版可落地的正式内容。

------

# 第 4 章 总体架构设计（正式展开）

## 4.1 架构目标

本系统的总体架构目标如下：

1. 以“训练模板 + 实际训练记录 + 渐进推进引擎”为核心，形成完整业务闭环。
2. 在保证 MVP 可快速落地的前提下，为后续统计分析、饮食记录、自适应训练等功能预留扩展能力。
3. 通过明确的模块边界，降低后续需求变化对整体系统的冲击。
4. 兼顾移动端训练场景下的高频交互需求，保证训练过程中操作顺畅、低延迟、低认知负担。
5. 避免过早微服务化，优先保证领域模型清晰、代码结构稳定、开发效率可控。

------

## 4.2 架构选型结论

本项目建议采用：

> **Monorepo + 模块化单体（Modular Monolith） + 前后端分离**

推荐技术栈：

- 前端：Flutter
- 后端：Spring Boot
- 数据库：PostgreSQL
- 缓存：Redis（第二阶段再引入）
- 对象存储：预留，不作为 MVP 必需

### 选择模块化单体的原因

当前项目的核心挑战不是分布式扩展，而是：

- 领域模型是否稳定
- 训练规则抽象是否合理
- 模板、执行、推进三层是否清晰
- MVP 是否能快速闭环

如果一开始上微服务，会带来：

- 服务拆分成本
- 接口协作成本
- 本地联调复杂度
- 部署复杂度
- 日志与追踪复杂度

这些问题在当前阶段都不是收益点。

因此，最合理的方案是：

- **单服务部署**
- **模块内高内聚**
- **模块间通过应用服务和领域接口隔离**
- 未来若出现明显边界和流量瓶颈，再逐步演进

------

## 4.3 总体系统结构

系统分为四层：

### 4.3.1 客户端层

由 Flutter 实现，负责：

- 用户登录与设置
- 训练计划管理
- 模板编辑
- 训练执行记录
- 历史数据浏览
- 基础统计展示

### 4.3.2 应用服务层

由 Spring Boot 提供 REST API，负责：

- 业务流程编排
- 权限校验
- DTO 转换
- 调用领域服务
- 事务管理

### 4.3.3 领域核心层

负责真正的业务规则，包括：

- 训练计划模型
- 模板与执行分离
- 训练序列推进
- 渐进超负荷规则
- 训练完成后的状态更新

这是系统最重要的部分。

### 4.3.4 基础设施层

负责：

- 数据持久化
- 缓存
- 第三方集成
- 日志
- 监控
- 文件与对象存储扩展接口

------

## 4.4 后端架构设计

## 4.4.1 分层结构

后端建议采用 DDD-lite 风格，而不是纯教科书式 DDD。
具体分为：

### Interface 层

负责：

- Controller
- Request/Response DTO
- 参数校验
- 统一异常响应

### Application 层

负责：

- 用例编排
- 调用多个领域对象完成业务
- 事务边界控制
- 权限与流程控制

### Domain 层

负责：

- 实体
- 聚合根
- 值对象
- 领域服务
- 规则策略接口

### Infrastructure 层

负责：

- Repository 实现
- 数据库 Mapper
- ORM / SQL
- Redis / 文件存储 / 外部服务

------

## 4.4.2 模块划分

建议按领域划分为以下模块：

### user

用户、偏好、基础设置

### exercise

动作库、用户自定义动作、动作分类

### program

训练计划、阶段、训练日模板、模板动作

### workout

实际训练、动作执行、组记录

### progression

推荐逻辑、序列推进、渐进规则、减载逻辑

### stats

训练统计、PR、趋势分析

### common

通用能力，例如：

- 基础枚举
- 异常体系
- 返回体
- 时间工具
- ID 生成
- 审计字段

------

## 4.4.3 模块依赖原则

必须遵守以下依赖约束：

- `interface -> application -> domain`
- `infrastructure -> domain/application`
- `program` 不直接依赖 `workout` 的具体实现细节
- `progression` 可以依赖 `program` 与 `workout` 的领域接口
- `stats` 尽量做读取型模块，不反向影响核心业务写路径

核心原则是：

> **训练计划定义、实际训练执行、训练推进推荐三者分离，但通过明确接口协作。**

------

## 4.4.4 持久化方案建议

结合你的背景，我更建议：

> **Spring Boot + MyBatis-Plus + 手写关键 SQL**

原因：

1. 你本身是 Java 后端开发，MyBatis-Plus 上手更快。
2. 当前项目表关系不算极其复杂，但查询会逐渐变多。
3. 训练历史、统计、趋势分析类查询后期很可能需要手写 SQL。
4. 与你的经验栈一致，Agent 也更容易配合生成 CRUD 与 Mapper。

建议做法：

- 简单 CRUD：MyBatis-Plus
- 复杂查询：Mapper XML / 注解 SQL
- 领域规则：不要写进 Mapper，放在 application/domain 层

------

## 4.4.5 事务设计

建议以下操作作为事务边界：

1. 创建 Program + Block + SessionTemplate + SessionExerciseTemplate
2. 从模板生成训练实例
3. 完成训练并保存 WorkoutSession/WorkoutExercise/WorkoutSet
4. 更新 ProgramProgress 并生成下一次推荐

原则：

- 单次训练提交必须保证原子性
- 不要让训练记录保存一半成功一半失败
- 推进状态更新必须与训练完成状态绑定

------

## 4.5 前端架构设计

## 4.5.1 前端分层

Flutter 推荐分层如下：

### presentation

页面、组件、视图状态展示

### application

状态管理、页面用例编排、交互流程控制

### domain

客户端业务对象、核心模型定义、轻量规则

### data

接口请求、本地缓存、DTO 映射、Repository 实现

------

## 4.5.2 前端模块划分

建议按 feature 拆分：

- auth
- exercise
- program
- workout
- stats
- settings

每个 feature 内部分为：

- page
- widget
- state
- model
- repository

------

## 4.5.3 状态管理建议

推荐 Riverpod。

原因：

- 对模块化结构友好
- 适合中大型 Flutter 项目
- 可测试性较好
- 易于拆分 provider，避免后期状态混乱

------

## 4.5.4 本地缓存与离线草稿

训练执行场景非常适合本地草稿机制。

建议：

- 正在进行的训练先保存在本地
- 结束训练时再统一提交后端
- 网络异常时允许恢复草稿
- 避免用户训练到一半数据丢失

本地存储建议：

- MVP：Hive / Isar 二选一
- 若需要更强查询能力：Drift

------

## 4.6 关键架构原则

### 4.6.1 模板层与执行层分离

模板定义理想训练，执行层记录真实完成情况。
历史训练不可依赖“当前模板”动态解释。

### 4.6.2 推荐逻辑独立

“下一次练什么、推荐多少重量”属于 progression 模块职责，不应散落在 program 或 workout 模块中。

### 4.6.3 规则配置化

渐进规则必须参数化，而不是硬编码。

### 4.6.4 高级能力后置

周期化、减载、复杂规则先预留扩展点，MVP 不全部做完。

### 4.6.5 读写分离思维

写路径关注正确性与一致性，读路径关注展示和统计。
后期复杂统计可以单独优化，不影响主流程。

------

## 4.7 部署与环境建议

建议分为：

- local：本地开发环境
- dev：联调环境
- prod：正式环境

MVP 阶段可采用：

- 后端：单实例 Spring Boot
- 数据库：PostgreSQL
- 前端：Flutter 本地运行 / 测试包
- CI：先不复杂化，至少保证格式检查、单元测试、构建通过

------

# 第 5 章 领域模型设计（正式展开）

## 5.1 领域设计目标

领域模型的核心目标是解决以下问题：

1. 如何表达训练计划的结构化设计
2. 如何表达一次实际训练
3. 如何将计划与实际分离
4. 如何根据历史训练生成下一次推荐
5. 如何保证模型对未来功能扩展保持稳定

------

## 5.2 核心领域划分

系统可划分为四个核心子域：

### 5.2.1 训练计划子域

关注“应该怎么练”。

包括：

- Program
- ProgramBlock
- SessionTemplate
- SessionExerciseTemplate

### 5.2.2 训练执行子域

关注“实际上练了什么”。

包括：

- WorkoutSession
- WorkoutExercise
- WorkoutSet

### 5.2.3 训练推进子域

关注“接下来怎么练”。

包括：

- ProgramProgress
- ProgressionRule
- ProgressionContext
- ProgressionResult

### 5.2.4 动作与基础资料子域

关注动作定义、分类、标签等基础信息。

包括：

- Exercise
- ExerciseCategory
- MuscleGroup
- EquipmentType

------

## 5.3 聚合设计

## 5.3.1 Program 聚合

### 聚合根

```
Program
```

### 包含对象

- ProgramBlock
- SessionTemplate
- SessionExerciseTemplate

### 职责

- 表达一个完整训练计划
- 管理阶段与训练模板结构
- 保证模板结构合法性
- 控制模板顺序和所属关系

### 不变量

- Program 必须属于一个用户
- 一个 Block 必须属于一个 Program
- 一个 SessionTemplate 必须属于一个 Block
- 一个 SessionExerciseTemplate 必须属于一个 SessionTemplate
- sequence_no 在同一层级中必须唯一

------

## 5.3.2 Workout 聚合

### 聚合根

```
WorkoutSession
```

### 包含对象

- WorkoutExercise
- WorkoutSet

### 职责

- 表达一次完整训练
- 记录动作执行结果
- 区分模板训练与自由训练
- 保存训练的历史快照

### 不变量

- 一个 WorkoutSession 必须属于一个用户
- WorkoutExercise 必须从属于某个 WorkoutSession
- WorkoutSet 必须从属于某个 WorkoutExercise
- 已完成的 WorkoutSession 不允许被任意覆盖式修改

------

## 5.3.3 Exercise 聚合

### 聚合根

```
Exercise
```

### 职责

- 描述训练动作
- 支持系统预置动作和用户自定义动作
- 提供动作分类、肌群、器械等基础信息

### 不变量

- 系统动作不可被普通用户直接修改
- 用户自定义动作仅归属于创建者

------

## 5.3.4 Progression 聚合

这个部分不必做成超重实体聚合，更适合：

- `ProgramProgress` 作为状态实体
- `ProgressionRule` 作为规则配置实体
- `ProgressionService` 作为领域服务
- `ProgressionStrategy` 作为规则策略接口

职责：

- 判断下一次推荐训练模板
- 计算推荐重量 / 次数 / 组数 / 休息时间
- 处理失败回退与减载

------

## 5.4 核心实体说明

## 5.4.1 Program

表示一份完整训练计划。

关键字段建议：

- id
- userId
- name
- goalType
- status
- description
- startDate
- endDate
- createdAt
- updatedAt

说明：

- goalType 表示计划目标，如增肌、增力、减脂
- status 表示草稿、激活、归档等状态
- 一个用户可拥有多份 Program，但通常只有一份 active Program

------

## 5.4.2 ProgramBlock

表示训练计划中的阶段。

关键字段建议：

- id
- programId
- name
- blockType
- sequenceNo
- durationMode
- durationValue
- deloadEnabled
- metadataJson

说明：

- blockType 可表示 hypertrophy / strength / cut / deload
- durationMode 可表示按周、按轮次、按完成次数
- durationValue 表示具体持续量

------

## 5.4.3 SessionTemplate

表示某个阶段内的一次训练日模板。

关键字段建议：

- id
- blockId
- name
- sequenceNo
- triggerMode
- notes
- metadataJson

说明：

- 例如 Push A、Pull B、Leg Day
- triggerMode 推荐支持 sequence 为主
- 系统应优先按 sequenceNo 推进，而非自然周日期

------

## 5.4.4 SessionExerciseTemplate

表示训练日模板中的动作处方。

关键字段建议：

- id
- sessionTemplateId
- exerciseId
- orderNo
- targetSets
- targetReps
- targetWeight
- targetWeightUnit
- restSeconds
- intensityMode
- progressionRuleId
- prescriptionJson

说明：

- targetReps 可先按简单整数设计
- 后续若支持范围，如 6-8，可放入 prescriptionJson
- intensityMode 可预留：weight / rpe / rir

------

## 5.4.5 WorkoutSession

表示一次实际训练。

关键字段建议：

- id
- userId
- sourceType
- sourceProgramId
- sourceBlockId
- sourceTemplateId
- startedAt
- endedAt
- status
- notes

说明：

- sourceType: template / manual
- 模板训练要记录来源 templateId
- 自由训练则 sourceTemplateId 可为空

------

## 5.4.6 WorkoutExercise

表示一次实际训练中的某个动作。

关键字段建议：

- id
- workoutSessionId
- exerciseId
- sourceTemplateExerciseId
- actualOrderNo
- notes
- replacementOfExerciseId

说明：

- replacementOfExerciseId 用于记录替代动作关系
- 便于后续统计“偏离模板但具有等效替代”

------

## 5.4.7 WorkoutSet

表示动作中的单组记录。

关键字段建议：

- id
- workoutExerciseId
- setNo
- weight
- reps
- durationSeconds
- restSeconds
- rpe
- rir
- isWarmup
- isCompleted

说明：

- 这是训练数据最原子的一层
- 后续统计、PR、总训练量都基于它

------

## 5.4.8 Exercise

表示动作定义。

关键字段建议：

- id
- ownerUserId
- name
- category
- primaryMuscle
- secondaryMusclesJson
- equipmentType
- movementPattern
- isCustom
- metadataJson

说明：

- ownerUserId 为空表示系统动作
- isCustom=true 表示用户私有动作

------

## 5.4.9 ProgressionRule

表示渐进规则配置。

关键字段建议：

- id
- userId
- name
- ruleType
- paramsJson
- enabled

说明：

ruleType 初期建议支持：

- FIXED_INCREMENT
- PERCENT_INCREMENT
- DOUBLE_PROGRESSION
- FAIL_RESET
- DELOAD

paramsJson 示例：

- 增重 2.5kg
- 达到 8 次后加重
- 连续失败 2 次回退 7.5%

------

## 5.4.10 ProgramProgress

表示计划推进状态。

关键字段建议：

- id
- userId
- programId
- currentBlockId
- nextSessionTemplateId
- lastCompletedWorkoutId
- sequenceCursor
- progressSnapshotJson
- updatedAt

说明：

这是系统“推荐下一次训练”的关键状态表。

------

## 5.5 值对象建议

建议引入以下值对象，而不必都做成数据库实体：

- WeightValue
- RepRange
- RestDuration
- ExercisePrescription
- RecommendedLoad
- ProgressionDecision

这样可以避免核心规则里大量出现裸字段拼装。

------

## 5.6 领域服务建议

## 5.6.1 ProgramDomainService

负责：

- 校验模板结构合法性
- 组装 Program 聚合
- 复制模板快照

## 5.6.2 WorkoutDomainService

负责：

- 从模板生成训练实例
- 结束训练时校验完整性
- 处理动作替换与自由训练

## 5.6.3 ProgressionDomainService

负责：

- 获取下一次推荐模板
- 计算推荐重量与次数
- 执行失败回退与减载判断

------

## 5.7 关键领域规则

### 规则一：模板与记录必须解耦

创建 WorkoutSession 时应复制模板快照，而不是运行时总去读当前模板。

### 规则二：训练推进默认按序列推进

用户完成 A 后推荐 B，而不是强制绑定周几。

### 规则三：自由训练可纳入统计，但不一定推进模板

是否推进 ProgramProgress，应由规则明确控制。

### 规则四：渐进规则只产生“推荐”，不强制改写用户结果

系统推荐下一次练多少，但用户实际训练记录永远优先。

------

# Spring Boot + Flutter 项目目录结构

我建议你直接用 Monorepo。

```text
ironlogic/
├─ README.md
├─ AGENTS.md
├─ .gitignore
├─ docs/
│  ├─ 00-project-overview.md
│  ├─ 01-requirements.md
│  ├─ 02-architecture.md
│  ├─ 03-domain-model.md
│  ├─ 04-database-design.md
│  ├─ 05-api-design.md
│  ├─ 06-mvp-roadmap.md
│  ├─ 07-agent-workflow.md
│  └─ adr/
│     ├─ ADR-001-modular-monolith.md
│     ├─ ADR-002-postgresql.md
│     └─ ADR-003-sequence-progression.md
├─ backend/
│  ├─ pom.xml
│  ├─ src/
│  │  ├─ main/
│  │  │  ├─ java/com/ironlogic/
│  │  │  │  ├─ IronLogicApplication.java
│  │  │  │  ├─ common/
│  │  │  │  │  ├─ api/
│  │  │  │  │  ├─ exception/
│  │  │  │  │  ├─ enums/
│  │  │  │  │  ├─ model/
│  │  │  │  │  └─ util/
│  │  │  │  ├─ config/
│  │  │  │  ├─ modules/
│  │  │  │  │  ├─ auth/
│  │  │  │  │  │  ├─ controller/
│  │  │  │  │  │  ├─ application/
│  │  │  │  │  │  ├─ domain/
│  │  │  │  │  │  ├─ infrastructure/
│  │  │  │  │  │  └─ dto/
│  │  │  │  │  ├─ user/
│  │  │  │  │  ├─ exercise/
│  │  │  │  │  ├─ program/
│  │  │  │  │  ├─ workout/
│  │  │  │  │  ├─ progression/
│  │  │  │  │  └─ stats/
│  │  │  ├─ resources/
│  │  │  │  ├─ application.yml
│  │  │  │  ├─ mapper/
│  │  │  │  └─ db/migration/
│  │  └─ test/
│  │     └─ java/com/ironlogic/
│  └─ Dockerfile
├─ frontend/
│  ├─ pubspec.yaml
│  ├─ lib/
│  │  ├─ main.dart
│  │  ├─ app/
│  │  │  ├─ app.dart
│  │  │  ├─ router.dart
│  │  │  └─ theme.dart
│  │  ├─ core/
│  │  │  ├─ network/
│  │  │  ├─ storage/
│  │  │  ├─ constants/
│  │  │  ├─ error/
│  │  │  └─ utils/
│  │  ├─ shared/
│  │  │  ├─ widgets/
│  │  │  ├─ models/
│  │  │  └─ providers/
│  │  └─ features/
│  │     ├─ auth/
│  │     │  ├─ data/
│  │     │  ├─ domain/
│  │     │  ├─ application/
│  │     │  └─ presentation/
│  │     ├─ exercise/
│  │     ├─ program/
│  │     ├─ workout/
│  │     ├─ stats/
│  │     └─ settings/
│  └─ test/
└─ scripts/
   ├─ dev-up.sh
   ├─ dev-down.sh
   └─ format.sh
```

------

# 核心实体类设计（Java 初稿）

下面我给你的是“核心业务实体骨架”，不是最终可直接运行的 MyBatis PO。
建议先按这个抽象建立 domain/model，再由 Agent 生成 persistence 层对象。

```java
public class Program {
    private Long id;
    private Long userId;
    private String name;
    private ProgramGoalType goalType;
    private ProgramStatus status;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<ProgramBlock> blocks;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
public class ProgramBlock {
    private Long id;
    private Long programId;
    private String name;
    private BlockType blockType;
    private Integer sequenceNo;
    private DurationMode durationMode;
    private Integer durationValue;
    private Boolean deloadEnabled;
    private String metadataJson;
    private List<SessionTemplate> sessionTemplates;
}
public class SessionTemplate {
    private Long id;
    private Long blockId;
    private String name;
    private Integer sequenceNo;
    private TriggerMode triggerMode;
    private String notes;
    private String metadataJson;
    private List<SessionExerciseTemplate> exercises;
}
public class SessionExerciseTemplate {
    private Long id;
    private Long sessionTemplateId;
    private Long exerciseId;
    private Integer orderNo;
    private Integer targetSets;
    private Integer targetReps;
    private BigDecimal targetWeight;
    private String targetWeightUnit;
    private Integer restSeconds;
    private IntensityMode intensityMode;
    private Long progressionRuleId;
    private String prescriptionJson;
}
public class WorkoutSession {
    private Long id;
    private Long userId;
    private WorkoutSourceType sourceType;
    private Long sourceProgramId;
    private Long sourceBlockId;
    private Long sourceTemplateId;
    private WorkoutStatus status;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private String notes;
    private List<WorkoutExercise> exercises;
}
public class WorkoutExercise {
    private Long id;
    private Long workoutSessionId;
    private Long exerciseId;
    private Long sourceTemplateExerciseId;
    private Integer actualOrderNo;
    private Long replacementOfExerciseId;
    private String notes;
    private List<WorkoutSet> sets;
}
public class WorkoutSet {
    private Long id;
    private Long workoutExerciseId;
    private Integer setNo;
    private BigDecimal weight;
    private Integer reps;
    private Integer durationSeconds;
    private Integer restSeconds;
    private BigDecimal rpe;
    private Integer rir;
    private Boolean warmup;
    private Boolean completed;
}
public class Exercise {
    private Long id;
    private Long ownerUserId;
    private String name;
    private ExerciseCategory category;
    private String primaryMuscle;
    private String secondaryMusclesJson;
    private EquipmentType equipmentType;
    private String movementPattern;
    private Boolean custom;
    private String metadataJson;
}
public class ProgressionRule {
    private Long id;
    private Long userId;
    private String name;
    private ProgressionRuleType ruleType;
    private String paramsJson;
    private Boolean enabled;
}
public class ProgramProgress {
    private Long id;
    private Long userId;
    private Long programId;
    private Long currentBlockId;
    private Long nextSessionTemplateId;
    private Long lastCompletedWorkoutId;
    private Integer sequenceCursor;
    private String progressSnapshotJson;
    private LocalDateTime updatedAt;
}
```

------

# 数据库表设计初稿（PostgreSQL）

## 1. users

```sql
create table users (
    id bigserial primary key,
    email varchar(128) unique,
    mobile varchar(32) unique,
    password_hash varchar(255) not null,
    nickname varchar(64) not null,
    gender varchar(16),
    weight_unit varchar(8) not null default 'kg',
    default_rest_seconds integer,
    created_at timestamp not null default now(),
    updated_at timestamp not null default now()
);
```

## 2. exercises

```sql
create table exercises (
    id bigserial primary key,
    owner_user_id bigint null,
    name varchar(128) not null,
    category varchar(32),
    primary_muscle varchar(64),
    secondary_muscles_json jsonb,
    equipment_type varchar(32),
    movement_pattern varchar(64),
    is_custom boolean not null default false,
    metadata_json jsonb,
    created_at timestamp not null default now(),
    updated_at timestamp not null default now()
);

create index idx_exercises_owner_user_id on exercises(owner_user_id);
create index idx_exercises_name on exercises(name);
```

## 3. programs

```sql
create table programs (
    id bigserial primary key,
    user_id bigint not null,
    name varchar(128) not null,
    goal_type varchar(32) not null,
    status varchar(32) not null,
    description text,
    start_date date,
    end_date date,
    created_at timestamp not null default now(),
    updated_at timestamp not null default now()
);

create index idx_programs_user_id on programs(user_id);
create index idx_programs_user_status on programs(user_id, status);
```

## 4. program_blocks

```sql
create table program_blocks (
    id bigserial primary key,
    program_id bigint not null,
    name varchar(128) not null,
    block_type varchar(32) not null,
    sequence_no integer not null,
    duration_mode varchar(32) not null,
    duration_value integer,
    deload_enabled boolean not null default false,
    metadata_json jsonb,
    created_at timestamp not null default now(),
    updated_at timestamp not null default now(),
    unique(program_id, sequence_no)
);

create index idx_program_blocks_program_id on program_blocks(program_id);
```

## 5. session_templates

```sql
create table session_templates (
    id bigserial primary key,
    block_id bigint not null,
    name varchar(128) not null,
    sequence_no integer not null,
    trigger_mode varchar(32) not null default 'SEQUENCE',
    notes text,
    metadata_json jsonb,
    created_at timestamp not null default now(),
    updated_at timestamp not null default now(),
    unique(block_id, sequence_no)
);

create index idx_session_templates_block_id on session_templates(block_id);
```

## 6. progression_rules

```sql
create table progression_rules (
    id bigserial primary key,
    user_id bigint null,
    name varchar(128) not null,
    rule_type varchar(32) not null,
    params_json jsonb not null,
    enabled boolean not null default true,
    created_at timestamp not null default now(),
    updated_at timestamp not null default now()
);

create index idx_progression_rules_user_id on progression_rules(user_id);
```

## 7. session_exercise_templates

```sql
create table session_exercise_templates (
    id bigserial primary key,
    session_template_id bigint not null,
    exercise_id bigint not null,
    order_no integer not null,
    target_sets integer not null,
    target_reps integer,
    target_weight numeric(10,2),
    target_weight_unit varchar(8) default 'kg',
    rest_seconds integer,
    intensity_mode varchar(32) default 'WEIGHT',
    progression_rule_id bigint,
    prescription_json jsonb,
    created_at timestamp not null default now(),
    updated_at timestamp not null default now(),
    unique(session_template_id, order_no)
);

create index idx_set_templates_session_template_id on session_exercise_templates(session_template_id);
```

## 8. workout_sessions

```sql
create table workout_sessions (
    id bigserial primary key,
    user_id bigint not null,
    source_type varchar(32) not null,
    source_program_id bigint,
    source_block_id bigint,
    source_template_id bigint,
    status varchar(32) not null,
    started_at timestamp not null,
    ended_at timestamp,
    notes text,
    created_at timestamp not null default now(),
    updated_at timestamp not null default now()
);

create index idx_workout_sessions_user_id on workout_sessions(user_id);
create index idx_workout_sessions_template_id on workout_sessions(source_template_id);
create index idx_workout_sessions_started_at on workout_sessions(started_at);
```

## 9. workout_exercises

```sql
create table workout_exercises (
    id bigserial primary key,
    workout_session_id bigint not null,
    exercise_id bigint not null,
    source_template_exercise_id bigint,
    actual_order_no integer not null,
    replacement_of_exercise_id bigint,
    notes text,
    created_at timestamp not null default now(),
    updated_at timestamp not null default now(),
    unique(workout_session_id, actual_order_no)
);

create index idx_workout_exercises_session_id on workout_exercises(workout_session_id);
create index idx_workout_exercises_exercise_id on workout_exercises(exercise_id);
```

## 10. workout_sets

```sql
create table workout_sets (
    id bigserial primary key,
    workout_exercise_id bigint not null,
    set_no integer not null,
    weight numeric(10,2),
    reps integer,
    duration_seconds integer,
    rest_seconds integer,
    rpe numeric(4,1),
    rir integer,
    is_warmup boolean not null default false,
    is_completed boolean not null default true,
    created_at timestamp not null default now(),
    updated_at timestamp not null default now(),
    unique(workout_exercise_id, set_no)
);

create index idx_workout_sets_exercise_id on workout_sets(workout_exercise_id);
```

## 11. program_progress

```sql
create table program_progress (
    id bigserial primary key,
    user_id bigint not null,
    program_id bigint not null,
    current_block_id bigint,
    next_session_template_id bigint,
    last_completed_workout_id bigint,
    sequence_cursor integer default 0,
    progress_snapshot_json jsonb,
    updated_at timestamp not null default now(),
    unique(user_id, program_id)
);

create index idx_program_progress_program_id on program_progress(program_id);
```

------





