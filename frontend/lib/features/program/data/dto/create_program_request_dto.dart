import '../../domain/model/create_program_input.dart';

/// 创建 Program 的请求 DTO。
///
/// 该类属于 `program/data`，负责把页面表单输入转换成后端 `POST /api/programs` 所需结构。
class CreateProgramRequestDto {
  /// 创建 Program 请求 DTO。
  const CreateProgramRequestDto({
    required this.name,
    required this.goalType,
    required this.status,
    this.description,
    this.startDate,
    this.endDate,
  });

  /// 从领域输入构建请求 DTO。
  factory CreateProgramRequestDto.fromInput(CreateProgramInput input) {
    return CreateProgramRequestDto(
      name: input.name,
      goalType: input.goalType,
      status: input.status,
      description: input.description,
      startDate: input.startDate,
      endDate: input.endDate,
    );
  }

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

  /// 转为 JSON。
  Map<String, dynamic> toJson() {
    return <String, dynamic>{
      'name': name,
      'goalType': goalType,
      'status': status,
      'description': description,
      'startDate': startDate,
      'endDate': endDate,
    };
  }
}
