/// Exercise 领域模型。
///
/// 该类属于 `exercise/domain`，表示前端真正使用的动作对象。
/// 这里刻意不直接把 DTO 传到页面，便于后续在 repository 层做兼容转换和字段裁剪。
class Exercise {
  /// 创建 Exercise 模型。
  const Exercise({
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

  /// 动作 id。
  final int id;

  /// 拥有者用户 id，系统动作时通常为空。
  final int? ownerUserId;

  /// 动作名称。
  final String name;

  /// 动作分类。
  final String? category;

  /// 主要训练肌群。
  final String? primaryMuscle;

  /// 次要肌群 JSON 字符串。
  final String? secondaryMusclesJson;

  /// 器械类型。
  final String? equipmentType;

  /// 动作模式。
  final String? movementPattern;

  /// 是否为自定义动作。
  final bool? isCustom;

  /// 预留元数据。
  final String? metadataJson;

  /// 创建时间。
  final DateTime? createdAt;

  /// 更新时间。
  final DateTime? updatedAt;
}
