# Agent 工作流

## 总原则
1. 先设计，后编码
2. 一次只做一个模块
3. 小步提交，小步验证
4. 严格遵守 docs 与 AGENTS.md

## 开发顺序
1. backend 工程骨架
2. frontend 工程骨架
3. exercise 模块
4. program 模块
5. workout 模块
6. progression v1
7. stats v1

## 每轮 Agent 任务要求
每次只给 Agent 一个明确目标，例如：
- 只创建 exercise 模块的实体、DTO、Mapper、Service、Controller
- 不修改其他模块
- 生成 Flyway migration
- 保持符合 docs 设计

## 每轮完成后检查
- 能否编译
- DTO 是否合理
- 模块边界是否清晰
- 是否修改了不该改的文件