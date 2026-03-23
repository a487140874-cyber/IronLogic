import 'recommended_exercise.dart';

/// 推荐 SessionTemplate 模型。
///
/// 该类属于 `progression/domain`，用于承载首页展示需要的推荐训练模板信息。
class RecommendedSessionTemplate {
  /// 创建推荐 SessionTemplate 模型。
  const RecommendedSessionTemplate({
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

  /// SessionTemplate id。
  final int id;

  /// 所属 Block id。
  final int? blockId;

  /// 模板名称。
  final String name;

  /// Block 内顺序号。
  final int? sequenceNo;

  /// 触发模式。
  final String? triggerMode;

  /// 备注。
  final String? notes;

  /// 扩展元数据。
  final String? metadataJson;

  /// 创建时间。
  final DateTime? createdAt;

  /// 更新时间。
  final DateTime? updatedAt;

  /// 推荐动作列表。
  final List<RecommendedExercise> exercises;
}
