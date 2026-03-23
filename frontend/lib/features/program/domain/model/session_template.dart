/// SessionTemplate 领域模型。
///
/// 该类属于 `program/domain`，表示某个 Block 下的训练日模板。
/// 它仍然属于模板定义层，不包含实际训练结果。
class SessionTemplate {
  /// 创建 SessionTemplate 模型。
  const SessionTemplate({
    required this.id,
    required this.blockId,
    required this.name,
    required this.sequenceNo,
    this.triggerMode,
    this.notes,
    this.metadataJson,
    this.createdAt,
    this.updatedAt,
  });

  /// 模板 id。
  final int id;

  /// 所属 Block id。
  final int blockId;

  /// 模板名称。
  final String name;

  /// 顺序号。
  final int sequenceNo;

  /// 触发模式。
  final String? triggerMode;

  /// 备注。
  final String? notes;

  /// 元数据 JSON。
  final String? metadataJson;

  /// 创建时间。
  final DateTime? createdAt;

  /// 更新时间。
  final DateTime? updatedAt;
}
