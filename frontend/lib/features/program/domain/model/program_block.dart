/// ProgramBlock 领域模型。
///
/// 该类属于 `program/domain`，表示某个 Program 下的训练阶段。
/// 前端把它单独建模，是为了把 Program、Block、SessionTemplate 这三层模板结构明确拆开，
/// 避免和 workout 执行记录模型混在一起。
class ProgramBlock {
  /// 创建 ProgramBlock 模型。
  const ProgramBlock({
    required this.id,
    required this.programId,
    required this.name,
    required this.blockType,
    required this.sequenceNo,
    this.durationMode,
    this.durationValue,
    this.deloadEnabled,
    this.metadataJson,
    this.createdAt,
    this.updatedAt,
  });

  /// Block id。
  final int id;

  /// 所属 Program id。
  final int programId;

  /// Block 名称。
  final String name;

  /// Block 类型。
  final String blockType;

  /// 在 Program 内的顺序号。
  final int sequenceNo;

  /// 持续方式。
  final String? durationMode;

  /// 持续值。
  final int? durationValue;

  /// 是否启用 deload。
  final bool? deloadEnabled;

  /// 预留元数据 JSON。
  final String? metadataJson;

  /// 创建时间。
  final DateTime? createdAt;

  /// 更新时间。
  final DateTime? updatedAt;
}
