/// 创建 Exercise 的输入模型。
///
/// 该类属于 `exercise/domain`，承载页面表单收集后的业务输入。
/// 这样页面不直接依赖网络请求 DTO，可以让领域层接口更清晰。
class CreateExerciseInput {
  /// 创建表单输入对象。
  const CreateExerciseInput({
    required this.name,
    required this.category,
    required this.primaryMuscle,
    required this.equipmentType,
    required this.movementPattern,
  });

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
}
