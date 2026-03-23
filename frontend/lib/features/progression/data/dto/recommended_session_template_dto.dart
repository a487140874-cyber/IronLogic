import '../../domain/model/recommended_session_template.dart';
import 'recommended_exercise_dto.dart';

/// 推荐 SessionTemplate DTO。
///
/// 该类属于 `progression/data`，负责解析后端推荐模板对象。
class RecommendedSessionTemplateDto {
  /// 创建推荐模板 DTO。
  const RecommendedSessionTemplateDto({
    required this.id,
    required this.name,
    required this.exercises,
    this.blockId,
    this.sequenceNo,
    this.triggerMode,
    this.notes,
    this.metadataJson,
    this.createdAt,
    this.updatedAt,
  });

  /// 从 JSON 构建 DTO。
  factory RecommendedSessionTemplateDto.fromJson(Map<String, dynamic> json) {
    final List<dynamic> rawExercises =
        json['exercises'] as List<dynamic>? ?? <dynamic>[];
    return RecommendedSessionTemplateDto(
      id: (json['id'] as num?)?.toInt() ?? 0,
      blockId: (json['blockId'] as num?)?.toInt(),
      name: json['name']?.toString() ?? '',
      sequenceNo: (json['sequenceNo'] as num?)?.toInt(),
      triggerMode: json['triggerMode']?.toString(),
      notes: json['notes']?.toString(),
      metadataJson: json['metadataJson']?.toString(),
      createdAt: DateTime.tryParse(json['createdAt']?.toString() ?? ''),
      updatedAt: DateTime.tryParse(json['updatedAt']?.toString() ?? ''),
      exercises: rawExercises
          .map(
            (dynamic item) =>
                RecommendedExerciseDto.fromJson(item as Map<String, dynamic>),
          )
          .toList(),
    );
  }

  final int id;
  final int? blockId;
  final String name;
  final int? sequenceNo;
  final String? triggerMode;
  final String? notes;
  final String? metadataJson;
  final DateTime? createdAt;
  final DateTime? updatedAt;
  final List<RecommendedExerciseDto> exercises;

  /// 转为领域模型。
  RecommendedSessionTemplate toDomain() {
    return RecommendedSessionTemplate(
      id: id,
      blockId: blockId,
      name: name,
      sequenceNo: sequenceNo,
      triggerMode: triggerMode,
      notes: notes,
      metadataJson: metadataJson,
      createdAt: createdAt,
      updatedAt: updatedAt,
      exercises:
          exercises.map((RecommendedExerciseDto item) => item.toDomain()).toList(),
    );
  }
}
