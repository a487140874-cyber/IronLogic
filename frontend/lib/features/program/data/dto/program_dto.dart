import '../../domain/model/program.dart';

/// Program 的数据传输对象。
///
/// 该类属于 `program/data`，用于和后端 `ProgramResponse` 对齐。
class ProgramDto {
  /// 创建 Program DTO。
  const ProgramDto({
    required this.id,
    required this.name,
    required this.goalType,
    required this.status,
    this.userId,
    this.description,
    this.startDate,
    this.endDate,
    this.createdAt,
    this.updatedAt,
  });

  /// 从 JSON 构建 DTO。
  factory ProgramDto.fromJson(Map<String, dynamic> json) {
    return ProgramDto(
      id: (json['id'] as num?)?.toInt() ?? 0,
      userId: (json['userId'] as num?)?.toInt(),
      name: json['name']?.toString() ?? '',
      goalType: json['goalType']?.toString() ?? '',
      status: json['status']?.toString() ?? '',
      description: json['description']?.toString(),
      startDate: json['startDate']?.toString(),
      endDate: json['endDate']?.toString(),
      createdAt: DateTime.tryParse(json['createdAt']?.toString() ?? ''),
      updatedAt: DateTime.tryParse(json['updatedAt']?.toString() ?? ''),
    );
  }

  /// Program id。
  final int id;

  /// 用户 id。
  final int? userId;

  /// 名称。
  final String name;

  /// 目标类型。
  final String goalType;

  /// 状态。
  final String status;

  /// 描述。
  final String? description;

  /// 开始日期。
  final String? startDate;

  /// 结束日期。
  final String? endDate;

  /// 创建时间。
  final DateTime? createdAt;

  /// 更新时间。
  final DateTime? updatedAt;

  /// 转为领域模型。
  Program toDomain() {
    return Program(
      id: id,
      userId: userId,
      name: name,
      goalType: goalType,
      status: status,
      description: description,
      startDate: startDate,
      endDate: endDate,
      createdAt: createdAt,
      updatedAt: updatedAt,
    );
  }
}
