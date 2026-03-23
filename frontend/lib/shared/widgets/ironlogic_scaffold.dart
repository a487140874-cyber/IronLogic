import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';

import '../../app/router.dart';

/// 应用统一页面骨架。
///
/// 该组件属于 `shared/widgets`，负责提供一致的 AppBar 和导航入口。
/// MVP 第一轮先使用 Drawer，避免在页面还很少时过早设计复杂导航容器。
class IronLogicScaffold extends StatelessWidget {
  /// 创建统一页面骨架。
  const IronLogicScaffold({
    required this.title,
    required this.body,
    super.key,
    this.floatingActionButton,
    this.actions,
  });

  /// 页面标题。
  final String title;

  /// 页面主体内容。
  final Widget body;

  /// 可选悬浮按钮。
  final Widget? floatingActionButton;

  /// AppBar 右侧操作区。
  final List<Widget>? actions;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(title),
        actions: actions,
      ),
      drawer: const _IronLogicDrawer(),
      body: SafeArea(child: body),
      floatingActionButton: floatingActionButton,
    );
  }
}

class _IronLogicDrawer extends StatelessWidget {
  const _IronLogicDrawer();

  @override
  Widget build(BuildContext context) {
    return Drawer(
      child: ListView(
        children: <Widget>[
          const DrawerHeader(
            child: Align(
              alignment: Alignment.bottomLeft,
              child: Text(
                'IronLogic',
                style: TextStyle(fontSize: 24, fontWeight: FontWeight.bold),
              ),
            ),
          ),
          ListTile(
            leading: const Icon(Icons.fitness_center),
            title: const Text('当前推荐训练'),
            onTap: () {
              context.go(AppRoutes.home);
            },
          ),
          ListTile(
            leading: const Icon(Icons.list_alt),
            title: const Text('动作列表'),
            onTap: () {
              context.go(AppRoutes.exercises);
            },
          ),
          ListTile(
            leading: const Icon(Icons.event_note),
            title: const Text('Program 列表'),
            onTap: () {
              context.go(AppRoutes.programs);
            },
          ),
          ListTile(
            leading: const Icon(Icons.history),
            title: const Text('训练历史'),
            onTap: () {
              context.go(AppRoutes.workoutHistory);
            },
          ),
        ],
      ),
    );
  }
}
