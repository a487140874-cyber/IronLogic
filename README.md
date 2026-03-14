# IronLogic

IronLogic 是一款专业健身记录 App，核心围绕：

- 渐进超负荷（Progressive Overload）
- 周期化训练（Periodization）
- 模板训练 + 自由训练
- 按训练序列推进，而非强绑定自然周
- 后续支持统计分析、饮食记录与自适应训练

## 项目结构

- `backend/`：Spring Boot 后端
- `frontend/`：Flutter 前端
- `docs/`：需求、架构、领域模型、数据库、API 等设计文档
- `scripts/`：辅助脚本
- `AGENTS.md`：AI Agent 协作规则

## 当前阶段

当前处于 MVP 设计与工程骨架搭建阶段。

## MVP 范围（第一版）

- 用户登录注册
- 动作库
- 训练计划 / Block / SessionTemplate 管理
- 从模板开始训练
- 自由训练
- 历史训练记录
- 简单渐进推荐
- 基础统计

## 核心设计原则

1. 模板层与执行层严格分离
2. 训练默认按序列推进
3. 渐进规则归 progression 模块管理
4. 保持 MVP 边界清晰，避免过度设计