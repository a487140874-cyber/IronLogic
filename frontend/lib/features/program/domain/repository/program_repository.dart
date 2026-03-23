import '../model/create_program_input.dart';
import '../model/program.dart';

/// Program 仓储抽象。
///
/// 该接口属于 `program/domain`，定义 Program 列表与创建能力。
abstract class ProgramRepository {
  /// 查询当前用户 Program 列表。
  Future<List<Program>> listPrograms();

  /// 创建 Program。
  Future<Program> createProgram(CreateProgramInput input);
}
