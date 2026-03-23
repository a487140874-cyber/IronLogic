import '../../domain/model/program_block.dart';

/// ProgramBlock DTO。
///
/// 该类属于 `program/data`，用于和后端 `ProgramBlockResponse` 对齐。
class ProgramBlockDto {
  /// 创建 ProgramBlock DTO。
  const ProgramBlockDto({
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

  /// 从 JSON 构建 DTO。
  factory ProgramBlockDto.fromJson(Map<String, dynamic> json) {
    return ProgramBlockDto(
      id: (json['id'] as num?)?.toInt() ?? 0,
      programId: (json['programId'] as num?)?.toInt() ?? 0,
      name: json['name']?.toString() ?? '',
      blockType: json['blockType']?.toString() ?? '',
      sequenceNo: (json['sequenceNo'] as num?)?.toInt() ?? 0,
      durationMode: json['durationMode']?.toString(),
      durationValue: (json['durationValue'] as num?)?.toInt(),
      deloadEnabled: json['deloadEnabled'] as bool?,
      metadataJson: json['metadataJson']?.toString(),
      createdAt: DateTime.tryParse(json['createdAt']?.toString() ?? ''),
      updatedAt: DateTime.tryParse(json['updatedAt']?.toString() ?? ''),
    );
  }

  final int id;
  final int programId;
  final String name;
  final String blockType;
  final int sequenceNo;
  final String? durationMode;
  final int? durationValue;
  final bool? deloadEnabled;
  final String? metadataJson;
  final DateTime? createdAt;
  final DateTime? updatedAt;

  /// 转为领域模型。
  ProgramBlock toDomain() {
    return ProgramBlock(
      id: id,
      programId: programId,
      name: name,
      blockType: blockType,
      sequenceNo: sequenceNo,
      durationMode: durationMode,
      durationValue: durationValue,
      deloadEnabled: deloadEnabled,
      metadataJson: metadataJson,
      createdAt: createdAt,
      updatedAt: updatedAt,
    );
  }
}
