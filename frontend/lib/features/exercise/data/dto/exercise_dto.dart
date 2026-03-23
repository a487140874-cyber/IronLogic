import '../../domain/model/exercise.dart';

/// Exercise 的数据传输对象。
///
/// 该类属于 `exercise/data`，负责和后端 JSON 结构对齐。
/// DTO 与 domain model 分离后，前端可以在这里做字段兼容，而不污染页面层。
class ExerciseDto {
  /// 创建 DTO。
  const ExerciseDto({
    required this.id,
    required this.name,
    this.ownerUserId,
    this.category,
    this.primaryMuscle,
    this.secondaryMusclesJson,
    this.equipmentType,
    this.movementPattern,
    this.isCustom,
    this.metadataJson,
    this.createdAt,
    this.updatedAt,
  });

  /// 从 JSON 构建 DTO。
  factory ExerciseDto.fromJson(Map<String, dynamic> json) {
    return ExerciseDto(
      id: (json['id'] as num?)?.toInt() ?? 0,
      ownerUserId: (json['ownerUserId'] as num?)?.toInt(),
      name: json['name']?.toString() ?? '',
      category: json['category']?.toString(),
      primaryMuscle: json['primaryMuscle']?.toString(),
      secondaryMusclesJson: json['secondaryMusclesJson']?.toString(),
      equipmentType: json['equipmentType']?.toString(),
      movementPattern: json['movementPattern']?.toString(),
      isCustom: json['isCustom'] as bool?,
      metadataJson: json['metadataJson']?.toString(),
      createdAt: DateTime.tryParse(json['createdAt']?.toString() ?? ''),
      updatedAt: DateTime.tryParse(json['updatedAt']?.toString() ?? ''),
    );
  }

  /// 动作 id。
  final int id;

  /// 拥有者用户 id。
  final int? ownerUserId;

  /// 动作名称。
  final String name;

  /// 动作分类。
  final String? category;

  /// 主要训练肌群。
  final String? primaryMuscle;

  /// 次要肌群 JSON。
  final String? secondaryMusclesJson;

  /// 器械类型。
  final String? equipmentType;

  /// 动作模式。
  final String? movementPattern;

  /// 是否为自定义动作。
  final bool? isCustom;

  /// 扩展元数据。
  final String? metadataJson;

  /// 创建时间。
  final DateTime? createdAt;

  /// 更新时间。
  final DateTime? updatedAt;

  /// 转为 domain model。
  Exercise toDomain() {
    return Exercise(
      id: id,
      ownerUserId: ownerUserId,
      name: name,
      category: category,
      primaryMuscle: primaryMuscle,
      secondaryMusclesJson: secondaryMusclesJson,
      equipmentType: equipmentType,
      movementPattern: movementPattern,
      isCustom: isCustom,
      metadataJson: metadataJson,
      createdAt: createdAt,
      updatedAt: updatedAt,
    );
  }
}
