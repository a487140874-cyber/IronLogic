import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

import 'router.dart';

/// IronLogic 前端应用根组件。
///
/// 该类属于 `app` 层，职责是组装全局主题与路由。
/// 本轮先保持 Material 默认风格，把重点放在功能链路打通，
/// 避免在 MVP 第一轮就引入复杂主题系统。
class IronLogicApp extends ConsumerWidget {
  /// 创建应用根组件。
  const IronLogicApp({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return MaterialApp.router(
      title: 'IronLogic',
      debugShowCheckedModeBanner: false,
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(seedColor: Colors.blueGrey),
        useMaterial3: true,
      ),
      routerConfig: ref.watch(goRouterProvider),
    );
  }
}
