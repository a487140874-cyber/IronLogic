/// 创建 SessionTemplate 的输入模型。
///
/// 该类属于 `program/domain`，用于承接 SessionTemplate 创建页表单输入。
class CreateSessionTemplateInput {
  /// 创建 SessionTemplate 输入对象。
  const CreateSessionTemplateInput({
    required this.name,
    required this.sequenceNo,
    required this.triggerMode,
    this.notes,
    this.metadataJson = '{}',
  });

  /// 名称。
  final String name;

  /// 顺序号。
  final int sequenceNo;

  /// 触发模式。
  final String triggerMode;

  /// 备注。
  final String? notes;

  /// 元数据 JSON。
  final String metadataJson;
}
