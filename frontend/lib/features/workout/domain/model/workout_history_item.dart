/// Workout 历史项模型。
///
/// 该类属于 `workout/domain`，用于历史列表页。
class WorkoutHistoryItem {
  /// 创建历史项模型。
  const WorkoutHistoryItem({
    required this.id,
    required this.sourceType,
    required this.status,
    this.startedAt,
    this.endedAt,
    this.notes,
  });

  /// 训练 id。
  final int id;

  /// 来源类型。
  final String sourceType;

  /// 状态。
  final String status;

  /// 开始时间。
  final DateTime? startedAt;

  /// 结束时间。
  final DateTime? endedAt;

  /// 备注。
  final String? notes;
}
