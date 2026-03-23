import 'dart:async';

import 'package:flutter_riverpod/flutter_riverpod.dart';

import '../data/api/program_api.dart';
import '../data/repository/program_repository_impl.dart';
import '../domain/model/create_program_input.dart';
import '../domain/model/program.dart';
import '../domain/repository/program_repository.dart';

/// Program 仓储 Provider。
final programRepositoryProvider = Provider<ProgramRepository>((ref) {
  return ProgramRepositoryImpl(ref.watch(programApiProvider));
});

/// Program 列表 Provider。
final programListProvider = FutureProvider<List<Program>>((ref) async {
  return ref.watch(programRepositoryProvider).listPrograms();
});

/// 创建 Program 的状态控制器。
///
/// 该控制器属于 `program/application`，负责处理创建提交与列表刷新。
class ProgramCreateController extends AutoDisposeAsyncNotifier<void> {
  @override
  FutureOr<void> build() {}

  /// 提交创建 Program 请求。
  Future<void> submit(CreateProgramInput input) async {
    state = const AsyncLoading();
    state = await AsyncValue.guard(() async {
      await ref.read(programRepositoryProvider).createProgram(input);
      ref.invalidate(programListProvider);
    });
  }
}

/// 创建 Program 控制器 Provider。
final programCreateControllerProvider =
    AutoDisposeAsyncNotifierProvider<ProgramCreateController, void>(
  ProgramCreateController.new,
);
