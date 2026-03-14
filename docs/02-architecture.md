# 总体架构设计

## 架构结论
本项目采用：

- Monorepo
- 前后端分离
- 模块化单体（Modular Monolith）

## 技术栈
- Frontend: Flutter
- Backend: Spring Boot
- Database: PostgreSQL
- ORM: MyBatis-Plus
- Migration: Flyway

## 架构目标
1. 保持领域模型清晰
2. 支持模板、执行、推进三层分离
3. 支持后续统计分析和饮食模块扩展
4. 先满足 MVP，避免过度设计

## 后端模块
- auth
- user
- exercise
- program
- workout
- progression
- stats

## 前端模块
- auth
- exercise
- program
- workout
- stats
- settings

## 核心原则
1. 模板层与执行层分离
2. 训练推进逻辑独立
3. 规则配置化
4. 复杂能力后置