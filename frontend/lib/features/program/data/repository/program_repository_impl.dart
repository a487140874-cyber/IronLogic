import '../dto/program_dto.dart';
import '../../domain/model/create_program_input.dart';
import '../../domain/model/program.dart';
import '../../domain/repository/program_repository.dart';
import '../api/program_api.dart';
import '../dto/create_program_request_dto.dart';

/// Program 仓储实现。
///
/// 该类属于 `program/data/repository`，负责承接 Program API 并转换成领域对象。
class ProgramRepositoryImpl implements ProgramRepository {
  /// 创建仓储实现。
  const ProgramRepositoryImpl(this._api);

  final ProgramApi _api;

  @override
  Future<List<Program>> listPrograms() async {
    final List<ProgramDto> items = await _api.listPrograms();
    return items.map((ProgramDto item) => item.toDomain()).toList();
  }

  @override
  Future<Program> createProgram(CreateProgramInput input) async {
    final CreateProgramRequestDto request =
        CreateProgramRequestDto.fromInput(input);
    final ProgramDto dto = await _api.createProgram(request);
    return dto.toDomain();
  }
}
