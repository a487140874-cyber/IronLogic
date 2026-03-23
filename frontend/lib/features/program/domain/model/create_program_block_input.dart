/// 创建 ProgramBlock 的输入模型。
///
/// 该类属于 `program/domain`，承接页面表单输入，
/// 让页面不直接依赖网络层 DTO。
class CreateProgramBlockInput {
  /// 创建 ProgramBlock 输入对象。
  const CreateProgramBlockInput({
    required this.name,
    required this.blockType,
    required this.sequenceNo,
    required this.durationMode,
    this.durationValue,
    required this.deloadEnabled,
    this.metadataJson = '{}',
  });

  /// 名称。
  final String name;

  /// Block 类型。
  final String blockType;

  /// 顺序号。
  final int sequenceNo;

  /// 持续方式。
  final String durationMode;

  /// 持续值。
  final int? durationValue;

  /// 是否启用 deload。
  final bool deloadEnabled;

  /// 元数据 JSON。
  final String metadataJson;
}
