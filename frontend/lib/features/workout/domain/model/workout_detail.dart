import 'workout_exercise.dart';

/// WorkoutSession 详情模型。
///
/// 该类属于 `workout/domain`，用于训练详情页展示。
/// 这里聚合训练基本信息与动作列表，方便页面一次消费。
class WorkoutDetail {
  /// 创建训练详情模型。
  const WorkoutDetail({
    required this.id,
    required this.userId,
    required this.sourceType,
    required this.status,
    required this.exercises,
    this.sourceProgramId,
    this.sourceBlockId,
    this.sourceTemplateId,
    this.startedAt,
    this.endedAt,
    this.notes,
    this.createdAt,
    this.updatedAt,
  });

  /// 训练 id。
  final int id;

  /// 用户 id。
  final int userId;

  /// 来源类型。
  final String sourceType;

  /// 来源 Program id。
  final int? sourceProgramId;

  /// 来源 Block id。
  final int? sourceBlockId;

  /// 来源模板 id。
  final int? sourceTemplateId;

  /// 状态。
  final String status;

  /// 开始时间。
  final DateTime? startedAt;

  /// 结束时间。
  final DateTime? endedAt;

  /// 备注。
  final String? notes;

  /// 创建时间。
  final DateTime? createdAt;

  /// 更新时间。
  final DateTime? updatedAt;

  /// 动作列表。
  final List<WorkoutExercise> exercises;
}
