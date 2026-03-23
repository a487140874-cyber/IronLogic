/// 创建 Program 的输入模型。
///
/// 该类属于 `program/domain`，用于承载创建计划页面的表单数据。
class CreateProgramInput {
  /// 创建 Program 输入对象。
  const CreateProgramInput({
    required this.name,
    required this.goalType,
    required this.status,
    this.description,
    this.startDate,
    this.endDate,
  });

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
}
