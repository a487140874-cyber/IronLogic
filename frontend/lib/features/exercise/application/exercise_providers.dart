import 'dart:async';

import 'package:flutter_riverpod/flutter_riverpod.dart';

import '../data/api/exercise_api.dart';
import '../data/repository/exercise_repository_impl.dart';
import '../domain/model/create_exercise_input.dart';
import '../domain/model/exercise.dart';
import '../domain/repository/exercise_repository.dart';

/// Exercise 仓储 Provider。
final exerciseRepositoryProvider = Provider<ExerciseRepository>((ref) {
  return ExerciseRepositoryImpl(ref.watch(exerciseApiProvider));
});

/// 动作列表查询 Provider。
///
/// 当前列表状态直接用 `FutureProvider` 承载，足够支撑 MVP 的加载、错误、成功三态。
final exerciseListProvider = FutureProvider<List<Exercise>>((ref) async {
  return ref.watch(exerciseRepositoryProvider).listExercises();
});

/// 创建动作的状态控制器。
///
/// 该控制器属于 `exercise/application`，负责处理创建请求的异步状态，
/// 并在成功后刷新动作列表。这样页面无需同时关心提交逻辑与列表刷新逻辑。
class ExerciseCreateController extends AutoDisposeAsyncNotifier<void> {
  @override
  FutureOr<void> build() {}

  /// 提交创建动作请求。
  Future<void> submit(CreateExerciseInput input) async {
    state = const AsyncLoading();
    state = await AsyncValue.guard(() async {
      await ref.read(exerciseRepositoryProvider).createExercise(input);
      ref.invalidate(exerciseListProvider);
    });
  }
}

/// 创建动作控制器 Provider。
final exerciseCreateControllerProvider =
    AutoDisposeAsyncNotifierProvider<ExerciseCreateController, void>(
  ExerciseCreateController.new,
);
