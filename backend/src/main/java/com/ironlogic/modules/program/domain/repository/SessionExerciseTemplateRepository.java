package com.ironlogic.modules.program.domain.repository;

import com.ironlogic.modules.program.domain.model.SessionExerciseTemplate;
import java.util.List;
import java.util.Optional;

/**
 * SessionExerciseTemplate 的仓储边界。
 */
public interface SessionExerciseTemplateRepository {

    /**
     * 持久化一个新建的 SessionExerciseTemplate。
     *
     * @param templateExercise 待持久化的模板动作
     * @return 持久化后的模板动作
     */
    SessionExerciseTemplate save(SessionExerciseTemplate templateExercise);

    /**
     * 持久化一个已有 SessionExerciseTemplate 的更新。
     *
     * @param templateExercise 更新后的模板动作
     * @return 更新后的模板动作
     */
    SessionExerciseTemplate update(SessionExerciseTemplate templateExercise);

    /**
     * 按 id 查询 SessionExerciseTemplate。
     *
     * @param id 模板动作 id
     * @return 查询结果
     */
    Optional<SessionExerciseTemplate> findById(Long id);

    /**
     * 列出某个 SessionTemplate 下的 SessionExerciseTemplate。
     *
     * @param sessionTemplateId 所属 SessionTemplate id
     * @return 排序后的模板动作列表
     */
    List<SessionExerciseTemplate> findBySessionTemplateId(Long sessionTemplateId);

    /**
     * 判断某个 SessionTemplate 下是否已存在指定 orderNo。
     *
     * @param sessionTemplateId 所属 SessionTemplate id
     * @param orderNo 顺序号
     * @return 已存在时返回 true
     */
    boolean existsBySessionTemplateIdAndOrderNo(Long sessionTemplateId, Integer orderNo);

    /**
     * 判断某个 SessionTemplate 下是否已存在指定 orderNo，但排除当前记录自身。
     *
     * @param sessionTemplateId 所属 SessionTemplate id
     * @param orderNo 顺序号
     * @param excludeId 更新时需要排除的当前模板动作 id
     * @return 已存在时返回 true
     */
    boolean existsBySessionTemplateIdAndOrderNoAndIdNot(Long sessionTemplateId, Integer orderNo, Long excludeId);
}
