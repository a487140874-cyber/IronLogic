import '../../domain/model/create_program_block_input.dart';

/// 创建 ProgramBlock 的请求 DTO。
///
/// 该类属于 `program/data`，负责把领域输入转换成后端 `POST /api/programs/{id}/blocks` 所需 JSON。
class CreateProgramBlockRequestDto {
  /// 创建请求 DTO。
  const CreateProgramBlockRequestDto({
    required this.name,
    required this.blockType,
    required this.sequenceNo,
    required this.durationMode,
    this.durationValue,
    required this.deloadEnabled,
    required this.metadataJson,
  });

  /// 从领域输入构建请求 DTO。
  factory CreateProgramBlockRequestDto.fromInput(CreateProgramBlockInput input) {
    return CreateProgramBlockRequestDto(
      name: input.name,
      blockType: input.blockType,
      sequenceNo: input.sequenceNo,
      durationMode: input.durationMode,
      durationValue: input.durationValue,
      deloadEnabled: input.deloadEnabled,
      metadataJson: input.metadataJson,
    );
  }

  final String name;
  final String blockType;
  final int sequenceNo;
  final String durationMode;
  final int? durationValue;
  final bool deloadEnabled;
  final String metadataJson;

  /// 转为 JSON。
  Map<String, dynamic> toJson() {
    return <String, dynamic>{
      'name': name,
      'blockType': blockType,
      'sequenceNo': sequenceNo,
      'durationMode': durationMode,
      'durationValue': durationValue,
      'deloadEnabled': deloadEnabled,
      'metadataJson': metadataJson,
    };
  }
}
