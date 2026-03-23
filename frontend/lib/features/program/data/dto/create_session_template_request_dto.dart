import '../../domain/model/create_session_template_input.dart';

/// 创建 SessionTemplate 的请求 DTO。
///
/// 该类属于 `program/data`，负责把领域输入转换成后端创建模板接口的 JSON。
class CreateSessionTemplateRequestDto {
  /// 创建请求 DTO。
  const CreateSessionTemplateRequestDto({
    required this.name,
    required this.sequenceNo,
    required this.triggerMode,
    this.notes,
    required this.metadataJson,
  });

  /// 从领域输入构建请求 DTO。
  factory CreateSessionTemplateRequestDto.fromInput(
    CreateSessionTemplateInput input,
  ) {
    return CreateSessionTemplateRequestDto(
      name: input.name,
      sequenceNo: input.sequenceNo,
      triggerMode: input.triggerMode,
      notes: input.notes,
      metadataJson: input.metadataJson,
    );
  }

  final String name;
  final int sequenceNo;
  final String triggerMode;
  final String? notes;
  final String metadataJson;

  /// 转为 JSON。
  Map<String, dynamic> toJson() {
    return <String, dynamic>{
      'name': name,
      'sequenceNo': sequenceNo,
      'triggerMode': triggerMode,
      'notes': notes,
      'metadataJson': metadataJson,
    };
  }
}
