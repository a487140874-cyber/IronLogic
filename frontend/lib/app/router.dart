import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:go_router/go_router.dart';

import '../features/exercise/presentation/pages/create_exercise_page.dart';
import '../features/exercise/presentation/pages/exercise_list_page.dart';
import '../features/program/presentation/pages/create_program_page.dart';
import '../features/program/presentation/pages/program_list_page.dart';
import '../features/progression/presentation/pages/current_recommendation_page.dart';

/// 应用路由常量。
///
/// 该类属于 `app` 层，集中维护路径字符串，
/// 避免页面间直接散落硬编码路由，便于后续扩展详情页与嵌套路由。
abstract final class AppRoutes {
  /// 首页，展示当前推荐训练。
  static const String home = '/';

  /// 动作列表页。
  static const String exercises = '/exercises';

  /// 创建动作页。
  static const String exerciseCreate = '/exercises/create';

  /// Program 列表页。
  static const String programs = '/programs';

  /// 创建 Program 页。
  static const String programCreate = '/programs/create';
}

/// 全局路由 Provider。
///
/// 该 Provider 属于 `app` 层，用于把路由配置交给 Riverpod 管理。
/// 这样做的好处是后续若要接入鉴权、埋点或动态重定向，可以继续沿用同一入口。
final goRouterProvider = Provider<GoRouter>((ref) {
  return GoRouter(
    initialLocation: AppRoutes.home,
    routes: <RouteBase>[
      GoRoute(
        path: AppRoutes.home,
        builder: (context, state) => const CurrentRecommendationPage(),
      ),
      GoRoute(
        path: AppRoutes.exercises,
        builder: (context, state) => const ExerciseListPage(),
      ),
      GoRoute(
        path: AppRoutes.exerciseCreate,
        builder: (context, state) => const CreateExercisePage(),
      ),
      GoRoute(
        path: AppRoutes.programs,
        builder: (context, state) => const ProgramListPage(),
      ),
      GoRoute(
        path: AppRoutes.programCreate,
        builder: (context, state) => const CreateProgramPage(),
      ),
    ],
  );
});
