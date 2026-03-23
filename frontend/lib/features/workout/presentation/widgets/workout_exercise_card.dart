import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

import '../../../../shared/widgets/form_section.dart';
import '../../application/workout_providers.dart';
import '../../domain/model/workout_exercise.dart';
import '../../domain/model/workout_set.dart';
import '../../domain/model/workout_set_input.dart';

/// 训练动作编辑卡片。
///
/// 该组件属于 `workout/presentation`，用于展示并编辑某个 WorkoutExercise 下的 sets。
/// 单独拆出这个组件，是为了把训练详情页的页面编排与单动作编辑逻辑分开。
class WorkoutExerciseCard extends ConsumerStatefulWidget {
  /// 创建训练动作编辑卡片。
  const WorkoutExerciseCard({
    required this.exercise,
    required this.exerciseName,
    required this.onSaved,
    super.key,
  });

  /// 当前训练动作。
  final WorkoutExercise exercise;

  /// 动作名称。
  final String exerciseName;

  /// 保存成功后的回调。
  final VoidCallback onSaved;

  @override
  ConsumerState<WorkoutExerciseCard> createState() => _WorkoutExerciseCardState();
}

class _WorkoutExerciseCardState extends ConsumerState<WorkoutExerciseCard> {
  late List<_WorkoutSetDraft> _drafts;

  @override
  void initState() {
    super.initState();
    _drafts = widget.exercise.sets.isEmpty
        ? <_WorkoutSetDraft>[_WorkoutSetDraft.fromSetNo(1)]
        : widget.exercise.sets
            .map((WorkoutSet item) => _WorkoutSetDraft.fromWorkoutSet(item))
            .toList();
  }

  @override
  void didUpdateWidget(covariant WorkoutExerciseCard oldWidget) {
    super.didUpdateWidget(oldWidget);
    if (oldWidget.exercise.id != widget.exercise.id ||
        oldWidget.exercise.updatedAt != widget.exercise.updatedAt) {
      for (final _WorkoutSetDraft draft in _drafts) {
        draft.dispose();
      }
      _drafts = widget.exercise.sets.isEmpty
          ? <_WorkoutSetDraft>[_WorkoutSetDraft.fromSetNo(1)]
          : widget.exercise.sets
              .map((WorkoutSet item) => _WorkoutSetDraft.fromWorkoutSet(item))
              .toList();
    }
  }

  @override
  void dispose() {
    for (final _WorkoutSetDraft draft in _drafts) {
      draft.dispose();
    }
    super.dispose();
  }

  /// 追加一行新的组编辑草稿。
  void _addSetRow() {
    setState(() {
      _drafts.add(_WorkoutSetDraft.fromSetNo(_drafts.length + 1));
    });
  }

  /// 保存当前动作的整组数据。
  Future<void> _save() async {
    final SaveWorkoutSetsController controller = ref.read(
      saveWorkoutSetsControllerProvider(widget.exercise.id).notifier,
    );

    final List<WorkoutSetInput> inputs = _drafts.map((draft) => draft.toInput()).toList();
    await controller.submit(inputs);
    final AsyncValue<void> state = ref.read(
      saveWorkoutSetsControllerProvider(widget.exercise.id),
    );

    if (!mounted) {
      return;
    }

    state.whenOrNull(
      data: (_) {
        widget.onSaved();
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(content: Text('当前动作组数据保存成功')),
        );
      },
      error: (Object error, StackTrace stackTrace) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text(error.toString())),
        );
      },
    );
  }

  @override
  Widget build(BuildContext context) {
    final AsyncValue<void> saveState = ref.watch(
      saveWorkoutSetsControllerProvider(widget.exercise.id),
    );
    final bool isSaving = saveState.isLoading;

    return FormSection(
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: <Widget>[
          Text(
            widget.exerciseName,
            style: Theme.of(context).textTheme.titleMedium,
          ),
          const SizedBox(height: 8),
          Text('顺序：${widget.exercise.actualOrderNo}'),
          const SizedBox(height: 12),
          const Text(
            '当前保存逻辑为整列表覆盖保存。这是后端 MVP 约束，后续可优化为更细粒度的增量编辑体验。',
          ),
          const SizedBox(height: 12),
          ..._drafts.asMap().entries.map((entry) {
            final int index = entry.key;
            final _WorkoutSetDraft draft = entry.value;
            return _WorkoutSetDraftEditor(
              key: ValueKey('set-${widget.exercise.id}-${draft.setNo}'),
              index: index,
              draft: draft,
              onChanged: (updated) {
                _drafts[index] = updated;
              },
            );
          }),
          const SizedBox(height: 12),
          Wrap(
            spacing: 12,
            runSpacing: 12,
            children: <Widget>[
              OutlinedButton.icon(
                onPressed: _addSetRow,
                icon: const Icon(Icons.add),
                label: const Text('新增一组'),
              ),
              FilledButton(
                onPressed: isSaving ? null : _save,
                child: Text(isSaving ? '保存中...' : '保存当前动作组数据'),
              ),
            ],
          ),
        ],
      ),
    );
  }
}

class _WorkoutSetDraft {
  _WorkoutSetDraft({
    required int setNo,
    String? weight,
    String? reps,
    String? durationSeconds,
    String? restSeconds,
    String? rpe,
    String? rir,
    bool? isWarmup,
    bool? isCompleted,
  })  : setNoController = TextEditingController(text: setNo.toString()),
        weightController = TextEditingController(text: weight),
        repsController = TextEditingController(text: reps),
        durationSecondsController = TextEditingController(text: durationSeconds),
        restSecondsController = TextEditingController(text: restSeconds),
        rpeController = TextEditingController(text: rpe),
        rirController = TextEditingController(text: rir),
        isWarmup = isWarmup ?? false,
        isCompleted = isCompleted ?? true;

  factory _WorkoutSetDraft.fromSetNo(int setNo) {
    return _WorkoutSetDraft(setNo: setNo);
  }

  factory _WorkoutSetDraft.fromWorkoutSet(WorkoutSet set) {
    return _WorkoutSetDraft(
      setNo: set.setNo,
      weight: set.weight?.toString(),
      reps: set.reps?.toString(),
      durationSeconds: set.durationSeconds?.toString(),
      restSeconds: set.restSeconds?.toString(),
      rpe: set.rpe?.toString(),
      rir: set.rir?.toString(),
      isWarmup: set.isWarmup,
      isCompleted: set.isCompleted,
    );
  }

  final TextEditingController setNoController;
  final TextEditingController weightController;
  final TextEditingController repsController;
  final TextEditingController durationSecondsController;
  final TextEditingController restSecondsController;
  final TextEditingController rpeController;
  final TextEditingController rirController;
  bool isWarmup;
  bool isCompleted;

  int get setNo => int.tryParse(setNoController.text.trim()) ?? 1;

  WorkoutSetInput toInput() {
    return WorkoutSetInput(
      setNo: setNo,
      weight: double.tryParse(weightController.text.trim()),
      reps: int.tryParse(repsController.text.trim()),
      durationSeconds: int.tryParse(durationSecondsController.text.trim()),
      restSeconds: int.tryParse(restSecondsController.text.trim()),
      rpe: double.tryParse(rpeController.text.trim()),
      rir: int.tryParse(rirController.text.trim()),
      isWarmup: isWarmup,
      isCompleted: isCompleted,
    );
  }

  void dispose() {
    setNoController.dispose();
    weightController.dispose();
    repsController.dispose();
    durationSecondsController.dispose();
    restSecondsController.dispose();
    rpeController.dispose();
    rirController.dispose();
  }
}

class _WorkoutSetDraftEditor extends StatefulWidget {
  const _WorkoutSetDraftEditor({
    required this.index,
    required this.draft,
    required this.onChanged,
    super.key,
  });

  final int index;
  final _WorkoutSetDraft draft;
  final ValueChanged<_WorkoutSetDraft> onChanged;

  @override
  State<_WorkoutSetDraftEditor> createState() => _WorkoutSetDraftEditorState();
}

class _WorkoutSetDraftEditorState extends State<_WorkoutSetDraftEditor> {
  void _notify() {
    widget.onChanged(widget.draft);
  }

  @override
  Widget build(BuildContext context) {
    return Card(
      margin: const EdgeInsets.only(bottom: 12),
      child: Padding(
        padding: const EdgeInsets.all(12),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: <Widget>[
            Text('第 ${widget.draft.setNo} 组'),
            const SizedBox(height: 12),
            TextField(
              controller: widget.draft.setNoController,
              keyboardType: TextInputType.number,
              decoration: const InputDecoration(labelText: '组号'),
              onChanged: (_) => _notify(),
            ),
            const SizedBox(height: 12),
            TextField(
              controller: widget.draft.weightController,
              keyboardType: const TextInputType.numberWithOptions(decimal: true),
              decoration: const InputDecoration(labelText: '重量'),
              onChanged: (_) => _notify(),
            ),
            const SizedBox(height: 12),
            TextField(
              controller: widget.draft.repsController,
              keyboardType: TextInputType.number,
              decoration: const InputDecoration(labelText: '次数'),
              onChanged: (_) => _notify(),
            ),
            const SizedBox(height: 12),
            TextField(
              controller: widget.draft.restSecondsController,
              keyboardType: TextInputType.number,
              decoration: const InputDecoration(labelText: '休息秒数'),
              onChanged: (_) => _notify(),
            ),
            const SizedBox(height: 12),
            TextField(
              controller: widget.draft.rpeController,
              keyboardType: const TextInputType.numberWithOptions(decimal: true),
              decoration: const InputDecoration(labelText: 'RPE'),
              onChanged: (_) => _notify(),
            ),
            const SizedBox(height: 12),
            TextField(
              controller: widget.draft.rirController,
              keyboardType: TextInputType.number,
              decoration: const InputDecoration(labelText: 'RIR'),
              onChanged: (_) => _notify(),
            ),
            const SizedBox(height: 12),
            SwitchListTile(
              contentPadding: EdgeInsets.zero,
              value: widget.draft.isWarmup,
              title: const Text('热身组'),
              onChanged: (bool value) {
                setState(() => widget.draft.isWarmup = value);
                _notify();
              },
            ),
            SwitchListTile(
              contentPadding: EdgeInsets.zero,
              value: widget.draft.isCompleted,
              title: const Text('已完成'),
              onChanged: (bool value) {
                setState(() => widget.draft.isCompleted = value);
                _notify();
              },
            ),
          ],
        ),
      ),
    );
  }
}
