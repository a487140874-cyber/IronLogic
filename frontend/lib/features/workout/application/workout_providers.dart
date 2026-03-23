import 'dart:async';

import 'package:flutter_riverpod/flutter_riverpod.dart';

import '../data/api/workout_api.dart';
import '../data/repository/workout_repository_impl.dart';
import '../domain/model/workout_detail.dart';
import '../domain/model/workout_history_item.dart';
import '../domain/model/workout_set_input.dart';
import '../domain/repository/workout_repository.dart';

/// workout 仓储 Provider。
final workoutRepositoryProvider = Provider<WorkoutRepository>((ref) {
  return WorkoutRepositoryImpl(ref.watch(workoutApiProvider));
});

/// 训练详情 Provider。
final workoutDetailProvider =
    FutureProvider.family<WorkoutDetail, int>((ref, workoutId) async {
  return ref.watch(workoutRepositoryProvider).getWorkoutDetail(workoutId);
});

/// 训练历史 Provider。
final workoutHistoryProvider = FutureProvider<List<WorkoutHistoryItem>>((ref) async {
  return ref.watch(workoutRepositoryProvider).listWorkoutHistory();
});

/// 从模板开始训练控制器。
class StartWorkoutController extends AutoDisposeFamilyAsyncNotifier<void, int> {
  @override
  FutureOr<void> build(int arg) {}

  /// 发起从模板开始训练请求。
  Future<WorkoutDetail> start() async {
    state = const AsyncLoading();
    WorkoutDetail? result;
    state = await AsyncValue.guard(() async {
      final int templateId = arg;
      result = await ref
          .read(workoutRepositoryProvider)
          .startWorkoutFromTemplate(templateId);
      ref.invalidate(workoutHistoryProvider);
    });
    if (state.hasError) {
      throw state.error!;
    }
    return result!;
  }
}

/// 从模板开始训练控制器 Provider。
final startWorkoutControllerProvider =
    AutoDisposeAsyncNotifierProviderFamily<StartWorkoutController, void, int>(
  StartWorkoutController.new,
);

/// 保存训练组数据控制器。
class SaveWorkoutSetsController extends AutoDisposeFamilyAsyncNotifier<void, int> {
  @override
  FutureOr<void> build(int arg) {}

  /// 覆盖保存某个训练动作下的整组数据。
  Future<void> submit(List<WorkoutSetInput> sets) async {
    state = const AsyncLoading();
    state = await AsyncValue.guard(() async {
      final int workoutExerciseId = arg;
      // TODO: 当前后端采用整列表覆盖保存，这是 MVP 约束。
      // 后续若要提升体验，可改为局部编辑和增量保存。
      await ref.read(workoutRepositoryProvider).saveWorkoutSets(
            workoutExerciseId,
            sets,
          );
    });
  }
}

/// 保存组数据控制器 Provider。
final saveWorkoutSetsControllerProvider =
    AutoDisposeAsyncNotifierProviderFamily<SaveWorkoutSetsController, void, int>(
  SaveWorkoutSetsController.new,
);

/// 完成训练控制器。
class FinishWorkoutController extends AutoDisposeFamilyAsyncNotifier<void, int> {
  @override
  FutureOr<void> build(int arg) {}

  /// 提交完成训练请求。
  Future<WorkoutDetail> finish() async {
    state = const AsyncLoading();
    WorkoutDetail? result;
    state = await AsyncValue.guard(() async {
      final int workoutId = arg;
      result =
          await ref.read(workoutRepositoryProvider).finishWorkout(workoutId);
      ref.invalidate(workoutDetailProvider(workoutId));
      ref.invalidate(workoutHistoryProvider);
    });
    if (state.hasError) {
      throw state.error!;
    }
    return result!;
  }
}

/// 完成训练控制器 Provider。
final finishWorkoutControllerProvider =
    AutoDisposeAsyncNotifierProviderFamily<FinishWorkoutController, void, int>(
  FinishWorkoutController.new,
);
