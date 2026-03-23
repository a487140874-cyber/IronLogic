import '../../domain/model/workout_history_item.dart';

/// WorkoutHistoryItem DTO。
///
/// 该类属于 `workout/data`，用于和后端训练历史接口响应对齐。
class WorkoutHistoryItemDto {
  /// 创建 DTO。
  const WorkoutHistoryItemDto({
    required this.id,
    required this.sourceType,
    required this.status,
    this.startedAt,
    this.endedAt,
    this.notes,
  });

  /// 从 JSON 构建 DTO。
  factory WorkoutHistoryItemDto.fromJson(Map<String, dynamic> json) {
    return WorkoutHistoryItemDto(
      id: (json['id'] as num?)?.toInt() ?? 0,
      sourceType: json['sourceType']?.toString() ?? '',
      status: json['status']?.toString() ?? '',
      startedAt: DateTime.tryParse(json['startedAt']?.toString() ?? ''),
      endedAt: DateTime.tryParse(json['endedAt']?.toString() ?? ''),
      notes: json['notes']?.toString(),
    );
  }

  final int id;
  final String sourceType;
  final String status;
  final DateTime? startedAt;
  final DateTime? endedAt;
  final String? notes;

  /// 转为领域模型。
  WorkoutHistoryItem toDomain() {
    return WorkoutHistoryItem(
      id: id,
      sourceType: sourceType,
      status: status,
      startedAt: startedAt,
      endedAt: endedAt,
      notes: notes,
    );
  }
}
