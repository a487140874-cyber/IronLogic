/// Program 领域模型。
///
/// 该类属于 `program/domain`，表示训练计划的前端业务对象。
/// 它和 DTO 分离，是为了让页面只依赖稳定字段，而不是直接绑定后端响应结构。
class Program {
  /// 创建 Program 模型。
  const Program({
    required this.id,
    required this.name,
    required this.goalType,
    required this.status,
    this.userId,
    this.description,
    this.startDate,
    this.endDate,
    this.createdAt,
    this.updatedAt,
  });

  /// Program id。
  final int id;

  /// 用户 id。
  final int? userId;

  /// 名称。
  final String name;

  /// 目标类型。
  final String goalType;

  /// 状态。
  final String status;

  /// 描述。
  final String? description;

  /// 开始日期。
  final String? startDate;

  /// 结束日期。
  final String? endDate;

  /// 创建时间。
  final DateTime? createdAt;

  /// 更新时间。
  final DateTime? updatedAt;
}
