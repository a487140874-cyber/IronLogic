import '../../domain/model/session_template.dart';

/// SessionTemplate DTO。
///
/// 该类属于 `program/data`，用于和后端 `SessionTemplateResponse` 对齐。
class SessionTemplateDto {
  /// 创建 SessionTemplate DTO。
  const SessionTemplateDto({
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

  /// 从 JSON 构建 DTO。
  factory SessionTemplateDto.fromJson(Map<String, dynamic> json) {
    return SessionTemplateDto(
      id: (json['id'] as num?)?.toInt() ?? 0,
      blockId: (json['blockId'] as num?)?.toInt() ?? 0,
      name: json['name']?.toString() ?? '',
      sequenceNo: (json['sequenceNo'] as num?)?.toInt() ?? 0,
      triggerMode: json['triggerMode']?.toString(),
      notes: json['notes']?.toString(),
      metadataJson: json['metadataJson']?.toString(),
      createdAt: DateTime.tryParse(json['createdAt']?.toString() ?? ''),
      updatedAt: DateTime.tryParse(json['updatedAt']?.toString() ?? ''),
    );
  }

  final int id;
  final int blockId;
  final String name;
  final int sequenceNo;
  final String? triggerMode;
  final String? notes;
  final String? metadataJson;
  final DateTime? createdAt;
  final DateTime? updatedAt;

  /// 转为领域模型。
  SessionTemplate toDomain() {
    return SessionTemplate(
      id: id,
      blockId: blockId,
      name: name,
      sequenceNo: sequenceNo,
      triggerMode: triggerMode,
      notes: notes,
      metadataJson: metadataJson,
      createdAt: createdAt,
      updatedAt: updatedAt,
    );
  }
}
