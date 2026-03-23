import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:go_router/go_router.dart';

import '../../../../app/router.dart';
import '../../../../shared/widgets/form_section.dart';
import '../../../../shared/widgets/ironlogic_scaffold.dart';
import '../../application/program_providers.dart';
import '../../domain/model/create_program_block_input.dart';

/// 创建 Block 页。
///
/// 该页面属于 `program/presentation`，承担 ProgramBlock 的最小创建流程。
class CreateProgramBlockPage extends ConsumerStatefulWidget {
  /// 创建 Block 页。
  const CreateProgramBlockPage({
    required this.programId,
    super.key,
  });

  /// 所属 Program id。
  final int programId;

  @override
  ConsumerState<CreateProgramBlockPage> createState() =>
      _CreateProgramBlockPageState();
}

class _CreateProgramBlockPageState extends ConsumerState<CreateProgramBlockPage> {
  static const List<String> _blockTypes = <String>[
    'ACCUMULATION',
    'INTENSIFICATION',
    'REALIZATION',
  ];

  static const List<String> _durationModes = <String>[
    'WEEKS',
    'SESSIONS',
  ];

  final GlobalKey<FormState> _formKey = GlobalKey<FormState>();
  final TextEditingController _nameController = TextEditingController();
  final TextEditingController _sequenceNoController = TextEditingController(text: '1');
  final TextEditingController _durationValueController = TextEditingController();

  String _blockType = _blockTypes.first;
  String _durationMode = _durationModes.first;
  bool _deloadEnabled = false;

  @override
  void dispose() {
    _nameController.dispose();
    _sequenceNoController.dispose();
    _durationValueController.dispose();
    super.dispose();
  }

  /// 提交创建 Block 请求。
  Future<void> _submit() async {
    if (!_formKey.currentState!.validate()) {
      return;
    }

    final ProgramBlockCreateController controller = ref.read(
      programBlockCreateControllerProvider(widget.programId).notifier,
    );
    await controller.submit(
      CreateProgramBlockInput(
        name: _nameController.text.trim(),
        blockType: _blockType,
        sequenceNo: int.parse(_sequenceNoController.text.trim()),
        durationMode: _durationMode,
        durationValue: _durationValueController.text.trim().isEmpty
            ? null
            : int.tryParse(_durationValueController.text.trim()),
        deloadEnabled: _deloadEnabled,
      ),
    );

    final AsyncValue<void> state = ref.read(
      programBlockCreateControllerProvider(widget.programId),
    );
    if (!mounted) {
      return;
    }

    state.whenOrNull(
      data: (_) {
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(content: Text('Block 创建成功')),
        );
        context.go(AppRoutes.programDetail(widget.programId));
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
    final AsyncValue<void> submitState = ref.watch(
      programBlockCreateControllerProvider(widget.programId),
    );
    final bool isSubmitting = submitState.isLoading;

    return IronLogicScaffold(
      title: '创建 Block',
      body: ListView(
        padding: const EdgeInsets.all(16),
        children: <Widget>[
          const Text('本页先支持 Block 的基础字段创建，先把 Program 的模板结构链路跑通。'),
          const SizedBox(height: 16),
          FormSection(
            child: Form(
              key: _formKey,
              child: Column(
                children: <Widget>[
                  TextFormField(
                    controller: _nameController,
                    decoration: const InputDecoration(labelText: 'Block 名称'),
                    validator: (String? value) {
                      if (value == null || value.trim().isEmpty) {
                        return '请输入 Block 名称';
                      }
                      return null;
                    },
                  ),
                  const SizedBox(height: 16),
                  DropdownButtonFormField<String>(
                    value: _blockType,
                    decoration: const InputDecoration(labelText: 'Block 类型'),
                    items: _blockTypes
                        .map((String value) => DropdownMenuItem<String>(
                              value: value,
                              child: Text(value),
                            ))
                        .toList(),
                    onChanged: (String? value) {
                      if (value != null) {
                        setState(() => _blockType = value);
                      }
                    },
                  ),
                  const SizedBox(height: 16),
                  TextFormField(
                    controller: _sequenceNoController,
                    keyboardType: TextInputType.number,
                    decoration: const InputDecoration(labelText: '顺序号'),
                    validator: (String? value) =>
                        int.tryParse(value ?? '') == null ? '请输入有效顺序号' : null,
                  ),
                  const SizedBox(height: 16),
                  DropdownButtonFormField<String>(
                    value: _durationMode,
                    decoration: const InputDecoration(labelText: '持续方式'),
                    items: _durationModes
                        .map((String value) => DropdownMenuItem<String>(
                              value: value,
                              child: Text(value),
                            ))
                        .toList(),
                    onChanged: (String? value) {
                      if (value != null) {
                        setState(() => _durationMode = value);
                      }
                    },
                  ),
                  const SizedBox(height: 16),
                  TextFormField(
                    controller: _durationValueController,
                    keyboardType: TextInputType.number,
                    decoration: const InputDecoration(labelText: '持续值'),
                  ),
                  const SizedBox(height: 16),
                  SwitchListTile(
                    value: _deloadEnabled,
                    title: const Text('启用 Deload'),
                    onChanged: (bool value) {
                      setState(() => _deloadEnabled = value);
                    },
                  ),
                  const SizedBox(height: 24),
                  SizedBox(
                    width: double.infinity,
                    child: FilledButton(
                      onPressed: isSubmitting ? null : _submit,
                      child: Text(isSubmitting ? '提交中...' : '提交创建'),
                    ),
                  ),
                ],
              ),
            ),
          ),
        ],
      ),
    );
  }
}
