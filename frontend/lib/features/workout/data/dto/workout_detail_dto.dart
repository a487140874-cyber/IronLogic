import '../../domain/model/workout_detail.dart';
import 'workout_exercise_dto.dart';

/// WorkoutDetail DTO。
///
/// 该类属于 `workout/data`，用于和后端 `WorkoutDetailResponse` 对齐。
class WorkoutDetailDto {
  /// 创建 DTO。
  const WorkoutDetailDto({
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

  /// 从 JSON 构建 DTO。
  factory WorkoutDetailDto.fromJson(Map<String, dynamic> json) {
    final List<dynamic> rawExercises =
        json['exercises'] as List<dynamic>? ?? <dynamic>[];
    return WorkoutDetailDto(
      id: (json['id'] as num?)?.toInt() ?? 0,
      userId: (json['userId'] as num?)?.toInt() ?? 0,
      sourceType: json['sourceType']?.toString() ?? '',
      sourceProgramId: (json['sourceProgramId'] as num?)?.toInt(),
      sourceBlockId: (json['sourceBlockId'] as num?)?.toInt(),
      sourceTemplateId: (json['sourceTemplateId'] as num?)?.toInt(),
      status: json['status']?.toString() ?? '',
      startedAt: DateTime.tryParse(json['startedAt']?.toString() ?? ''),
      endedAt: DateTime.tryParse(json['endedAt']?.toString() ?? ''),
      notes: json['notes']?.toString(),
      createdAt: DateTime.tryParse(json['createdAt']?.toString() ?? ''),
      updatedAt: DateTime.tryParse(json['updatedAt']?.toString() ?? ''),
      exercises: rawExercises
          .map((dynamic item) => WorkoutExerciseDto.fromJson(item as Map<String, dynamic>))
          .toList(),
    );
  }

  final int id;
  final int userId;
  final String sourceType;
  final int? sourceProgramId;
  final int? sourceBlockId;
  final int? sourceTemplateId;
  final String status;
  final DateTime? startedAt;
  final DateTime? endedAt;
  final String? notes;
  final DateTime? createdAt;
  final DateTime? updatedAt;
  final List<WorkoutExerciseDto> exercises;

  /// 转为领域模型。
  WorkoutDetail toDomain() {
    return WorkoutDetail(
      id: id,
      userId: userId,
      sourceType: sourceType,
      sourceProgramId: sourceProgramId,
      sourceBlockId: sourceBlockId,
      sourceTemplateId: sourceTemplateId,
      status: status,
      startedAt: startedAt,
      endedAt: endedAt,
      notes: notes,
      createdAt: createdAt,
      updatedAt: updatedAt,
      exercises: exercises.map((WorkoutExerciseDto item) => item.toDomain()).toList(),
    );
  }
}
