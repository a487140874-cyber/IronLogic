import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:go_router/go_router.dart';

import '../features/exercise/presentation/pages/create_exercise_page.dart';
import '../features/exercise/presentation/pages/exercise_list_page.dart';
import '../features/program/domain/model/program_block.dart';
import '../features/program/domain/model/session_template.dart';
import '../features/program/presentation/pages/block_detail_page.dart';
import '../features/program/presentation/pages/create_program_block_page.dart';
import '../features/program/presentation/pages/create_program_page.dart';
import '../features/program/presentation/pages/create_session_exercise_template_page.dart';
import '../features/program/presentation/pages/create_session_template_page.dart';
import '../features/program/presentation/pages/program_detail_page.dart';
import '../features/program/presentation/pages/program_list_page.dart';
import '../features/program/presentation/pages/session_template_detail_page.dart';
import '../features/progression/presentation/pages/current_recommendation_page.dart';
import '../features/workout/presentation/pages/workout_detail_page.dart';
import '../features/workout/presentation/pages/workout_history_page.dart';

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

  /// 训练历史页。
  static const String workoutHistory = '/workouts/history';

  /// 生成 Program 详情页路由。
  static String programDetail(int programId) => '/programs/$programId';

  /// 生成 Block 创建页路由。
  static String programBlockCreate(int programId) =>
      '/programs/$programId/blocks/create';

  /// 生成 Block 详情页路由。
  static String blockDetail(int blockId) => '/blocks/$blockId';

  /// 生成 SessionTemplate 创建页路由。
  static String sessionTemplateCreate(int blockId) =>
      '/blocks/$blockId/session-templates/create';

  /// 生成 SessionTemplate 详情页路由。
  static String sessionTemplateDetail(int templateId) =>
      '/session-templates/$templateId';

  /// 生成模板动作创建页路由。
  static String sessionTemplateExerciseCreate(int templateId) =>
      '/session-templates/$templateId/exercises/create';

  /// 生成训练详情页路由。
  static String workoutDetail(int workoutId) => '/workouts/$workoutId';
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
      GoRoute(
        path: '/programs/:programId',
        builder: (context, state) {
          final int programId = int.parse(state.pathParameters['programId']!);
          return ProgramDetailPage(programId: programId);
        },
      ),
      GoRoute(
        path: '/programs/:programId/blocks/create',
        builder: (context, state) {
          final int programId = int.parse(state.pathParameters['programId']!);
          return CreateProgramBlockPage(programId: programId);
        },
      ),
      GoRoute(
        path: '/blocks/:blockId',
        builder: (context, state) {
          final int blockId = int.parse(state.pathParameters['blockId']!);
          final ProgramBlock? block = state.extra is ProgramBlock
              ? state.extra as ProgramBlock
              : null;
          return BlockDetailPage(blockId: blockId, block: block);
        },
      ),
      GoRoute(
        path: '/blocks/:blockId/session-templates/create',
        builder: (context, state) {
          final int blockId = int.parse(state.pathParameters['blockId']!);
          return CreateSessionTemplatePage(blockId: blockId);
        },
      ),
      GoRoute(
        path: '/session-templates/:templateId',
        builder: (context, state) {
          final int templateId = int.parse(state.pathParameters['templateId']!);
          final SessionTemplate? template = state.extra is SessionTemplate
              ? state.extra as SessionTemplate
              : null;
          return SessionTemplateDetailPage(
            templateId: templateId,
            template: template,
          );
        },
      ),
      GoRoute(
        path: '/session-templates/:templateId/exercises/create',
        builder: (context, state) {
          final int templateId = int.parse(state.pathParameters['templateId']!);
          return CreateSessionExerciseTemplatePage(templateId: templateId);
        },
      ),
      GoRoute(
        path: AppRoutes.workoutHistory,
        builder: (context, state) => const WorkoutHistoryPage(),
      ),
      GoRoute(
        path: '/workouts/:workoutId',
        builder: (context, state) {
          final int workoutId = int.parse(state.pathParameters['workoutId']!);
          return WorkoutDetailPage(workoutId: workoutId);
        },
      ),
    ],
  );
});
