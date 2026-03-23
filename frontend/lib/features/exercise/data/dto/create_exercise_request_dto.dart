import '../../domain/model/create_exercise_input.dart';

/// 创建 Exercise 的请求 DTO。
///
/// 该类属于 `exercise/data`，负责把领域输入转换成后端接口要求的 JSON。
/// 当前只提交 MVP 需要的最小字段，其余可选字段先传空值，保持与后端兼容。
class CreateExerciseRequestDto {
  /// 创建请求 DTO。
  const CreateExerciseRequestDto({
    required this.name,
    required this.category,
    required this.primaryMuscle,
    required this.equipmentType,
    required this.movementPattern,
  });

  /// 从领域输入构建请求 DTO。
  factory CreateExerciseRequestDto.fromInput(CreateExerciseInput input) {
    return CreateExerciseRequestDto(
      name: input.name,
      category: input.category,
      primaryMuscle: input.primaryMuscle,
      equipmentType: input.equipmentType,
      movementPattern: input.movementPattern,
    );
  }

  /// 动作名称。
  final String name;

  /// 动作分类。
  final String category;

  /// 主要训练肌群。
  final String primaryMuscle;

  /// 器械类型。
  final String equipmentType;

  /// 动作模式。
  final String movementPattern;

  /// 转为请求 JSON。
  Map<String, dynamic> toJson() {
    return <String, dynamic>{
      'name': name,
      'category': category,
      'primaryMuscle': primaryMuscle,
      'secondaryMusclesJson': null,
      'equipmentType': equipmentType,
      'movementPattern': movementPattern,
      'metadataJson': null,
    };
  }
}
